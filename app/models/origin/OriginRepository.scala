package models.origin

import models.proficiency.ProficiencyTable
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OriginRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val origin = TableQuery[OriginTable]
  private val originProficiency = TableQuery[OriginProficiencyTable]
  private val proficiency = TableQuery[ProficiencyTable]

  val query = origin
    .joinLeft(originProficiency)
    .on(_.id === _.originId)
    .joinLeft(proficiency)
    .on(_._2.flatMap(_.proficiencyId) === _.id)
    .result

  val action = (for {
    result <- query
  } yield {
    result.groupBy(_._1._1.id).map {
        case (_, tuples) =>
            val ((o, op), _) = tuples.head
            val p = tuples.flatMap(_._2)
            OriginWithProficiencies(o, p)
    }.toSeq
  })

  def getAll: Future[Seq[OriginWithProficiencies]] = db.run {
    action
  }

  def getById(id: Long): Future[Origin] = db.run {
    origin.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Origin]] = db.run {
    origin.filter(_.id === id).result.headOption
  }

  def add(newOrigin: OriginWithProficiencies): Future[OriginWithProficiencies] = {
    val writeOrigin = (origin
      returning origin.map(_.id)
      into { case (origin, id) => Origin(id, origin.name, origin.description) }
    )

    val writeTuples = (originProficiency
      returning originProficiency.map(_.id)
      into { case (tuple, id) => OriginProficiency(id, tuple.originId, tuple.proficiencyId) }
    )

    val action = for {
      addedOrigin <- writeOrigin += newOrigin.origin
      tuples <- {
        var tuples = newOrigin.proficiencies.map(proficiency => OriginProficiency(0, addedOrigin.id, proficiency.id))
        writeTuples ++= tuples
      }
      proficiencies <- proficiency.filter(_.id.inSet(tuples.map(tuple => tuple.proficiencyId))).result
    } yield OriginWithProficiencies(addedOrigin, proficiencies)

    db.run(action.transactionally)
  }

  def delete(id: Long): Future[Unit] = db.run(origin.filter(_.id === id).delete).map(_ => ())

}



