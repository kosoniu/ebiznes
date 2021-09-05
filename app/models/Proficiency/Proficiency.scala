package models.proficiency

import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{Reads, Writes, __}

case class Proficiency(id: Long, name: String, proficiencyType: Int)

object Proficiency {
  implicit val writes: Writes[Proficiency] = (
    (__ \ "id").write[Long] and
    (__ \ "name").write[String] and
    (__ \ "proficiencyType").write[Int]
  )(unlift(Proficiency.unapply))

  implicit val reads: Reads[Proficiency] = (
    (__ \ "id").read[Long] and
    (__ \ "name").read[String] and
    (__ \ "proficiencyType").read[Int]
  )(Proficiency.apply _)

}


