package models.hero

import slick.jdbc.SQLiteProfile.api._

class HeroTable(tag: Tag) extends Table[Hero](tag, "hero") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def level = column[Int]("level")

  def name = column[String]("name")

  def raceId = column[Long]("race_id")

  def classId = column[Long]("class_id")

  def originId = column[Long]("origin_id")

  def * = (id, name, level, raceId, classId, originId) <> ((Hero.apply _).tupled, Hero.unapply)

}
