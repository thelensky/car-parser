package models

import play.api.libs.json.Json

case class Car(id: Long, gosNumber: String, mark: String, color: String, year: Int)


object Car {
  implicit val carFormat = Json.format[Car]
}

case class CreateCar(gosNumber: String, mark: String, color: String, year: Int)

object CreateCar {
  implicit val carFormat = Json.format[CreateCar]
}

case class StaticCars(amount: Int, firstAdd: Car, lastAdd: Car)
