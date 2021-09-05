package controllers

import models.clazz.{Class, ClassRepository, ClassWithProficiencies}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ClassController @Inject()(classRepository: ClassRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getAll: Action[AnyContent] = Action.async { implicit request =>
    classRepository.getAll.map(classes => Ok(Json.toJson(classes)))
  }

  def get(id: Long): Action[AnyContent] = Action.async { implicit request =>
    classRepository.getByIdOption(id).map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NoContent
    }
  }

  def delete(id: Long): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    classRepository.delete(id).map( _ => Redirect("/classes"))
  }

  def add(): Action[JsValue] = Action(parse.json) { request =>
    val result = request.body.validate[ClassWithProficiencies]

    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      clazz => {
        classRepository.add(clazz)
        Ok(Json.toJson(clazz))
      }
    )
  }

  def update(id: Long): Action[JsValue] = Action(parse.json) { request =>
    val result = request.body.validate[Class]

    result.fold(
      errors => {
        BadRequest(Json.obj("message" -> JsError.toJson(errors)))
      },
      clazz => {
        classRepository.update(id, clazz)
        Ok(Json.toJson(clazz))
      }
    )
  }
}
