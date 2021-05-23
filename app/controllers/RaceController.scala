package controllers

import models.{Origin, Race, RaceRepository}
import play.api.libs.json.{JsError, JsValue, Json}

import javax.inject._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class RaceController @Inject()(raceRepository: RaceRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getAll: Action[AnyContent] = Action.async { implicit request =>
    raceRepository.getAll().map(races => Ok(Json.toJson(races)))
  }

  def get(id: Long): Action[AnyContent] = Action.async { implicit request =>
    raceRepository.getByIdOption(id).map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NoContent
    }
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    raceRepository.delete(id).map { res =>
      Redirect("/races")
    }
  }

  def add(): Action[JsValue] = Action(parse.json) { request =>
    val result = request.body.validate[Race]

    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      race => {
        raceRepository.add(race)
        Ok(Json.toJson(race))
      }
    )
  }

  def update(): Action[JsValue] = Action(parse.json) { request =>
    val result = request.body.validate[Race]

    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      race => {
        raceRepository.update(race)
        Ok(Json.toJson(race))
      }
    )
  }
}
