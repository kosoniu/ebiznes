package models.origin

import models.proficiency.ProficiencyTable
import slick.jdbc.SQLiteProfile.api._

class OriginProficiencyTable(tag: Tag) extends Table[OriginProficiency](tag, "origin_proficiency") {

  val proficiencies = TableQuery[ProficiencyTable]
  val origins = TableQuery[OriginTable]

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def originId = column[Long]("origin_id")

  def proficiencyId = column[Long]("proficiency_id")

  def fk1 = foreignKey("origin_fk", originId ,origins)(origin => origin.id)

  def fk2 = foreignKey("proficiency_fk", proficiencyId, proficiencies)(proficiency => proficiency.id)

  def * = (id, originId, proficiencyId) <> (OriginProficiency.tupled, OriginProficiency.unapply)

}
