package models.race

import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{Reads, Writes, __}

case class RaceFeature(id: Long, name: String, description: String, raceId: Long)
case class RaceWithFeatures(race: Race, raceFeatures: Seq[RaceFeature])

object RaceFeature {
  implicit val writes: Writes[RaceFeature] = (
    (__ \ "id").write[Long] and
      (__ \ "name").write[String] and
      (__ \ "description").write[String] and
      (__ \ "raceId").write[Long]
    )(unlift(RaceFeature.unapply))

  implicit val reads: Reads[RaceFeature] = (
    (__ \ "id").read[Long] and
      (__ \ "name").read[String] and
      (__ \ "description").read[String] and
      (__ \ "raceId").read[Long]
    )(RaceFeature.apply _)

}

object RaceWithFeatures {
  implicit val writes: Writes[RaceWithFeatures] = (
    (__ \ "race").write[Race] and
      (__ \ "raceFeatures").write[Seq[RaceFeature]]
    )(unlift(RaceWithFeatures.unapply))

  implicit val reads: Reads[RaceWithFeatures] = (
    (__ \ "race").read[Race] and
      (__ \ "raceFeatures").read[Seq[RaceFeature]]
    )(RaceWithFeatures.apply _)

}


