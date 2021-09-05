package models.proficiency

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProficiencyRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val proficiency = TableQuery[ProficiencyTable]

  def add(newProficiency: Proficiency): Future[Proficiency] = {
  dbConfig.db
    .run(proficiency += newProficiency)
    .map(_ => newProficiency)
  }

  def getAll(): Future[Seq[Proficiency]] = db.run {
    proficiency.result
  }

  def getById(id: Long): Future[Proficiency] = db.run {
    proficiency.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Proficiency]] = db.run {
    proficiency.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(proficiency.filter(_.id === id).delete).map(_ => ())

  def update(id: Long, newProficiency: Proficiency): Future[Unit] = {
    val proficiencyToUpdate: Proficiency = newProficiency.copy(id)
    db.run(proficiency.filter(_.id === id).update(proficiencyToUpdate)).map(_ => ())
  }

}



