package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

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

    var tuples = newOrigin.proficiencies.map(proficiency => OriginProficiency(0, newOrigin.origin.id, proficiency.id))

    val action = for {
      addedOrigin <- writeOrigin += newOrigin.origin
      tuples <- writeTuples ++= tuples
      proficiencies <- proficiency.filter(_.id.inSet(tuples.map(tuple => tuple.proficiencyId))).result
    } yield OriginWithProficiencies(addedOrigin, proficiencies)

    db.run(action.transactionally)
  }

  def delete(id: Long): Future[Unit] = db.run(origin.filter(_.id === id).delete).map(_ => ())

//  def update(id: Long, updatedOrigin: OriginWithProficiencies): Future[OriginWithProficiencies] = {
//
//    val action = for {
//      editedOrigin <- origin.filter(_.id === id).update(updatedOrigin.origin)
//    } yield OriginWithProficiencies(editedOrigin, proficiencies)
//
//    db.run(action.transactionally)
//  }

}



