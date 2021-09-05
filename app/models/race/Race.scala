package models.race

import play.api.libs.json.{Json, OFormat}

case class Race(id: Long, name: String, description: String)

object Race {
  implicit val raceFormat: OFormat[Race] = Json.format[Race]
}
