package models.origin

import slick.jdbc.SQLiteProfile.api._

class OriginTable(tag: Tag) extends Table[Origin](tag, "origin") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def description = column[String]("description")

  def * = (id, name, description) <> ((Origin.apply _).tupled, Origin.unapply)

}
