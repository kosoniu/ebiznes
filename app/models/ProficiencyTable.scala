package models

import slick.jdbc.SQLiteProfile.api._

class ProficiencyTable(tag: Tag) extends Table[Proficiency](tag, "proficiency") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def proficiencyType = column[Int]("type")

  def * = (id, name, proficiencyType) <> ((Proficiency.apply _).tupled, Proficiency.unapply)

}
