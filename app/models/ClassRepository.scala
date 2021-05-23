package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ClassRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ClassTable(tag: Tag) extends Table[Class](tag, "class") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[String]("description")

    def hitPoints = column[Int]("hit_points")

    def hitDice = column[String]("hit_dice")

    def * = (id, name, description, hitPoints, hitDice) <> ((Class.apply _).tupled, Class.unapply)

  }

  private val clazz = TableQuery[ClassTable]

  def add(newClass: Class): Future[Class] = {
    dbConfig.db
      .run(clazz += newClass)
      .map(_ => newClass)
  }

  def getAll(): Future[Seq[Class]] = db.run {
    clazz.result
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
