package controllers

import models.{Proficiency, ProficiencyRepository}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProficiencyController @Inject()(proficiencyRepository: ProficiencyRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll: Action[AnyContent] = Action.async { implicit request =>
    proficiencyRepository.getAll().map(proficiencies => Ok(Json.toJson(proficiencies)))
  }

  def get(id: Long): Action[AnyContent] = Action.async { implicit request =>
    proficiencyRepository.getByIdOption(id).map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NoContent
    }
  }

  def delete(id: Long): Action[JsValue] = Action(parse.json) { request =>
    val result = request.body.validate[Proficiency]

    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      data => {
        proficiencyRepository.delete(id)
        Ok(Json.toJson(data))
      }
    )
  }

  def add(): Action[JsValue] = Action(parse.json) { request =>
    val result = request.body.validate[Proficiency]

    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      data => {
        proficiencyRepository.add(data)
        Ok(Json.toJson(data))
      }
    )
  }

  def update(id: Long): Action[JsValue] = Action(parse.json) { request =>
    val result = request.body.validate[Proficiency]

    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      data => {
        proficiencyRepository.update(id, data)
        Ok(Json.toJson(data))
      }
    )
  }
}
