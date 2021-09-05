package controllers

import models.race.{Race, RaceRepository, RaceWithFeatures}
import play.api.libs.json.{JsError, JsValue, Json}

import javax.inject._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

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

  def add(): Action[JsValue] = Action.async(parse.json) { request =>
    val result = request.body.validate[RaceWithFeatures]
    var future: Future[RaceWithFeatures] = null

    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      race => future = raceRepository.add(race)
    )

    future.map(result => Ok(Json.toJson(result)))
  }

  def update(id: Long): Action[JsValue] = Action.async(parse.json) { request =>
    val result = request.body.validate[Race]
    var future: Future[Race] = null

    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      race => {
        raceRepository.update(id, race)
        Ok(Json.toJson(race))
      }
    )

    future.map(result => Ok(Json.toJson(result)))
  }
}
