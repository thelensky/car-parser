package controllers

import javax.inject.{Inject, Singleton}
import models.{Car, CarRepository, CreateCar}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CarController @Inject()(cc: ControllerComponents, protected val repo: CarRepository)
                             (implicit ex: ExecutionContext) extends AbstractController(cc) {

  def createCar = Action.async(parse.json) { implicit request =>
    val result = request.body.validate[CreateCar]
    result.fold(
      err => Future.successful(Ok(err.toString())),
      car => repo.create(car).map(c => {
        val value = Json.toJson(c)
        Ok(value)
      })
    )
  }

  def deleteCar(id: Long) = Action.async { implicit request =>
    repo.delete(id).map(c => c match {
      case c if c == 1 => Ok(("ok"))
      case _ => InternalServerError("not found")
    })
  }

  def listCars(gosNumber: Option[String],
               mark: Option[String],
               color: Option[String],
               year: Option[Int]) = Action.async { implicit request =>
    repo.listCars(gosNumber,
      mark,
      color,
      year).map(cars => Ok(Json.toJson(cars)))
  }

  def statics = Action.async{ implicit request =>
    repo.static().map(s => {
      val data = s.head
      Ok(s"total: ${data._1}, oldest: ${data._2}, newest: ${data._3}")
    })
  }

  def default = TODO

}
