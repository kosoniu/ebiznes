package models.clazz

import models.proficiency.Proficiency
import play.api.libs.functional.syntax.{toFunctionalBuilderOps, unlift}
import play.api.libs.json.{Reads, Writes, __}

case class Class(
                  id: Long,
                  name: String,
                  description: String,
                  healthPoints: Int,
                  healthPointsOnHigherLevels: Int,
                  hitDice: Int
                )
case class ClassWithProficiencies(clazz: Class, proficiencies: Seq[Proficiency])
case class ClassProficiency(id: Long, classId: Long, proficiencyId: Long)

object Class {
  implicit val writes: Writes[Class] = (
    (__ \ "id").write[Long] and
      (__ \ "name").write[String] and
      (__ \ "description").write[String] and
      (__ \ "healthPoints").write[Int] and
      (__ \ "healthPointsOnHigherLevels").write[Int] and
      (__ \ "hitDice").write[Int]
    )(unlift(Class.unapply))

  implicit val reads: Reads[Class] = (
    (__ \ "id").read[Long] and
      (__ \ "name").read[String] and
      (__ \ "description").read[String] and
      (__ \ "healthPoints").read[Int] and
      (__ \ "healthPointsOnHigherLevels").read[Int] and
      (__ \ "hitDice").read[Int]
    )(Class.apply _)
}

object ClassWithProficiencies {
  implicit val writes: Writes[ClassWithProficiencies] = (
    (__ \ "clazz").write[Class] and
      (__ \ "proficiencies").write[Seq[Proficiency]]
    )(unlift(ClassWithProficiencies.unapply))

  implicit val reads: Reads[ClassWithProficiencies] = (
    (__ \ "clazz").read[Class] and
      (__ \ "proficiencies").read[Seq[Proficiency]]
    )(ClassWithProficiencies.apply _)
}


