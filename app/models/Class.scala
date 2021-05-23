package models

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}
import play.api.libs.json.{Json, OFormat}

case class Class(
                  id: Long,
                  name: String,
                  description: String,
                  healthPoints: Int,
                  hitDice: String
                )

object Class {
  implicit val productFormat: OFormat[Class] = Json.format[Class]
}


