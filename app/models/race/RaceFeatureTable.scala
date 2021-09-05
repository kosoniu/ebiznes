package models.race

import slick.jdbc.SQLiteProfile.api._

class RaceFeatureTable(tag: Tag) extends Table[RaceFeature](tag, "race_feature") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def description = column[String]("description")

  def raceId = column[Long]("race_id")

  def * = (id, name, description, raceId) <> ((RaceFeature.apply _).tupled, RaceFeature.unapply)

}
