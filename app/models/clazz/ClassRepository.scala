package models.clazz

import models.proficiency.ProficiencyTable
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ClassRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val clazz = TableQuery[ClassTable]
  private val classProficiency = TableQuery[ClassProficiencyTable]
  private val proficiency = TableQuery[ProficiencyTable]

  val query = clazz
    .joinLeft(classProficiency)
    .on(_.id === _.classId)
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
        ClassWithProficiencies(o, p)
    }.toSeq
  })

  def add(newClass: ClassWithProficiencies): Future[ClassWithProficiencies] = {
    val writeClass = (clazz
      returning clazz.map(_.id)
      into { case (clazz, id) => Class(id, clazz.name, clazz.description, clazz.healthPoints, clazz.healthPointsOnHigherLevels, clazz.hitDice) }
      )

    val writeTuples = (classProficiency
      returning classProficiency.map(_.id)
      into { case (tuple, id) => ClassProficiency(id, tuple.classId, tuple.proficiencyId) }
      )


    val action = for {
      addedClass <- writeClass += newClass.clazz
      tuples <- {
        var tuples = newClass.proficiencies.map(proficiency => ClassProficiency(0, addedClass.id, proficiency.id))
        writeTuples ++= tuples
      }
      proficiencies <- proficiency.filter(_.id.inSet(tuples.map(tuple => tuple.proficiencyId))).result
    } yield ClassWithProficiencies(addedClass, proficiencies)

    db.run(action.transactionally)
  }

  def getAll: Future[Seq[ClassWithProficiencies]] = db.run {
    action
  }

  def getById(id: Long): Future[Class] = db.run {
    clazz.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Class]] = db.run {
    clazz.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(clazz.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, new_product: Class): Future[Unit] = {
    val update: Class = new_product.copy(id)
    db.run(clazz.filter(_.id === id).update(update)).map(_ => ())
  }

}
