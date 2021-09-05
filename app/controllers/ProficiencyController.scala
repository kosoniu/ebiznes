package controllers

import models.proficiency.{Proficiency, ProficiencyRepository}
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

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    proficiencyRepository.delete(id).map( _ => Redirect("/proficiencies"))
  }

  def add(): Action[JsValue] = Action.async(parse.json) { request =>
    val result = request.body.validate[Proficiency]

    var future: Future[Proficiency] = null

    result.fold(
      errors => BadRequest(Json.obj("message" -> JsError.toJson(errors))),
      proficiency => future = proficiencyRepository.add(proficiency)
    )

    future.map(result => Ok(Json.toJson(result)))

  }

  def update(id: Long): Action[JsValue] = Action.async(parse.json) { request =>
    val result = request.body.validate[Proficiency]

    var future: Future[Proficiency] = null

    result.fold(
      errors => BadRequest(Json.obj("message" -> JsError.toJson(errors))),
      proficiency => {
        proficiencyRepository.update(id, proficiency)
        Ok(Json.toJson(proficiency))
      }
    )

    future.map(result => Ok(Json.toJson(result)))
  }
}
