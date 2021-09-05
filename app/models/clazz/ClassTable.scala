package models.clazz

import slick.jdbc.SQLiteProfile.api._

class ClassTable(tag: Tag) extends Table[Class](tag, "class") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def description = column[String]("description")

  def healthPoints = column[Int]("health_points")

  def healthPointsOnHigherLevel = column[Int]("health_points_on_higher_level")

  def hitDice = column[Int]("hit_dice")

  def * = (id, name, description, healthPoints, healthPointsOnHigherLevel, hitDice) <> ((Class.apply _).tupled, Class.unapply)

}
