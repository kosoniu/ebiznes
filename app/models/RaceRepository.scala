package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{JsValue, Json, OFormat}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RaceRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class RaceTable(tag: Tag) extends Table[Race](tag, "race") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[String]("description")

    def * = (id, name, description) <> ((Race.apply _).tupled, Race.unapply)

  }

  private val race = TableQuery[RaceTable]

  def add(newRace: Race): Future[String] = {
  dbConfig.db
    .run(race += newRace)
    .map(res => "Race added!")
    .recover {
      case ex: Exception => {
        printf(ex.getMessage())
        ex.getMessage
      }
    }
  }

  def getAll(): Future[Seq[Race]] = db.run {
    race.result
  }

  def getById(id: Long): Future[Race] = db.run {
    race.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Race]] = db.run {
    race.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = db.run(race.filter(_.id === id).delete).map(_ => ())

  def update(newRace: Race): Future[Unit] = {
    db.run(race.filter(_.id === newRace.id).update(newRace)).map(_ => ())
  }

}



