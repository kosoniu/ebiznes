package models.hero

import models.clazz.ClassWithProficiencies
import models.origin.OriginWithProficiencies
import models.race.RaceWithFeatures
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{Reads, Writes, __}

case class Hero(id: Long, name: String, level: Int, raceId: Long, classId: Long, originId: Long)
case class HeroWithProperties(id:Long, name: String, level: Int, race: RaceWithFeatures, clazz: ClassWithProficiencies, origin: OriginWithProficiencies)

object Hero {
  implicit val writes: Writes[Hero] = (
    (__ \ "id").write[Long] and
      (__ \ "name").write[String] and
      (__ \ "level").write[Int] and
      (__ \ "raceId").write[Long] and
      (__ \ "classId").write[Long] and
      (__ \ "originId").write[Long]
    )(unlift(Hero.unapply))

  implicit val reads: Reads[Hero] = (
    (__ \ "id").read[Long] and
      (__ \ "name").read[String] and
      (__ \ "level").read[Int] and
      (__ \ "raceId").read[Long] and
      (__ \ "classId").read[Long] and
      (__ \ "originId").read[Long]
    )(Hero.apply _)
}

object HeroWithProperties {
  implicit val writes: Writes[HeroWithProperties] = (
    (__ \ "id").write[Long] and
      (__ \ "name").write[String] and
      (__ \ "level").write[Int] and
      (__ \ "race").write[RaceWithFeatures] and
      (__ \ "clazz").write[ClassWithProficiencies] and
      (__ \ "origin").write[OriginWithProficiencies]
    )(unlift(HeroWithProperties.unapply))

  implicit val reads: Reads[HeroWithProperties] = (
    (__ \ "id").read[Long] and
      (__ \ "name").read[String] and
      (__ \ "level").read[Int] and
      (__ \ "race").read[RaceWithFeatures] and
      (__ \ "clazz").read[ClassWithProficiencies] and
      (__ \ "origin").read[OriginWithProficiencies]
    )(HeroWithProperties.apply _)
}



