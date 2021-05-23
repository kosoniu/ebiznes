package controllers

import models.{Origin, OriginRepository, OriginWithProficiencies}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class OriginController @Inject()(originRepository: OriginRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll: Action[AnyContent] = Action.async { implicit request =>
    originRepository.getAll.map(origins => Ok(Json.toJson(origins)))
  }

  def get(id: Long): Action[AnyContent] = Action.async { implicit request =>
    originRepository.getByIdOption(id).map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NoContent
    }
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    originRepository.delete(id).map( _ => Redirect("/origins"))
  }

  def add() = Action.async(parse.json) { request =>
    val result = request.body.validate[OriginWithProficiencies]

    var future: Future[OriginWithProficiencies] = null

    result.fold(
      errors => BadRequest(Json.obj("message" -> JsError.toJson(errors))),
      origin => future = originRepository.add(origin)
    )

    future.map(result => Ok(Json.toJson(result)))
  }

//  def update(id: Long): Action[JsValue] = Action(parse.json) { request =>
//    val result = request.body.validate[OriginWithProficiencies]
//
//    var future: Future[OriginWithProficiencies] = null
//
//    result.fold(
//      errors => BadRequest(Json.obj("message" -> JsError.toJson(errors))),
//      origin => future = originRepository.update(id, origin)
//    )
//  }
}
