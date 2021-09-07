package controllers

import models.hero.{Hero, HeroRepository}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HeroController @Inject()(heroRepository: HeroRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll: Action[AnyContent] = Action.async { implicit request =>
    heroRepository.getAll().map(heroes => Ok(Json.toJson(heroes)))
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    heroRepository.delete(id).map( _ => Redirect("/heroes"))
  }

  def add(): Action[JsValue] = Action.async(parse.json) { request =>
    val result = request.body.validate[Hero]
    var future: Future[Hero] = null

    result.fold(
      errors => BadRequest(Json.obj("message" -> JsError.toJson(errors))),
      hero => future = heroRepository.add(hero)
    )

    future.map(result => Ok(Json.toJson(result)))
  }

}
