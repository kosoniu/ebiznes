package models.origin

import models.proficiency.Proficiency
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{Reads, Writes, __}

case class Origin(id: Long, name: String, description: String)
case class OriginWithProficiencies(origin: Origin, proficiencies: Seq[Proficiency])
case class OriginProficiency(id: Long, originId: Long, proficiencyId: Long)

object Origin {
  implicit val writes: Writes[Origin] = (
    (__ \ "id").write[Long] and
      (__ \ "name").write[String] and
      (__ \ "description").write[String]
    )(unlift(Origin.unapply))

  implicit val reads: Reads[Origin] = (
    (__ \ "id").read[Long] and
      (__ \ "name").read[String] and
      (__ \ "description").read[String]
    )(Origin.apply _)
}

object OriginWithProficiencies {
  implicit val writes: Writes[OriginWithProficiencies] = (
    (__ \ "origin").write[Origin] and
      (__ \ "proficiencies").write[Seq[Proficiency]]
    )(unlift(OriginWithProficiencies.unapply))

  implicit val reads: Reads[OriginWithProficiencies] = (
    (__ \ "origin").read[Origin] and
      (__ \ "proficiencies").read[Seq[Proficiency]]
    )(OriginWithProficiencies.apply _)
}

