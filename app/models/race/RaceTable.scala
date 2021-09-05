package models.race

import slick.jdbc.SQLiteProfile.api._

class RaceTable(tag: Tag) extends Table[Race](tag, "race") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def description = column[String]("description")

  def * = (id, name, description) <> ((Race.apply _).tupled, Race.unapply)

}
