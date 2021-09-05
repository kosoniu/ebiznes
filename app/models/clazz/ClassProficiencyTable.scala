package models.clazz

import models.proficiency.ProficiencyTable
import slick.jdbc.SQLiteProfile.api._

class ClassProficiencyTable(tag: Tag) extends Table[ClassProficiency](tag, "class_proficiency") {

  val proficiencies = TableQuery[ProficiencyTable]
  val classes = TableQuery[ClassTable]

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def classId = column[Long]("class_id")

  def proficiencyId = column[Long]("proficiency_id")

  def fk1 = foreignKey("class_fk", classId, classes)(clazz => clazz.id)

  def fk2 = foreignKey("proficiency_fk", proficiencyId, proficiencies)(proficiency => proficiency.id)

  def * = (id, classId, proficiencyId) <> (ClassProficiency.tupled, ClassProficiency.unapply)

}
