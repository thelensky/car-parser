package models

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CarRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class CarTable(tag: Tag) extends Table[Car](tag, "CARS") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def gosNumber = column[String]("GOS_NUMBER", O.Unique)

    def mark = column[String]("MARK")

    def color = column[String]("COLOR")

    def year = column[Int]("YEAR")

    def * = (id, gosNumber, mark, color, year) <> ((Car.apply _).tupled, Car.unapply)
  }

  private val cars = TableQuery[CarTable]


  def create(createCar: CreateCar): Future[Car] = db.run {
    (cars.map(c => (c.gosNumber, c.mark, c.color, c.year))
      returning cars.map(_.id)
      into ((car, id) => Car(id, car._1, car._2, car._3, car._4))
      ) += (createCar.gosNumber, createCar.mark, createCar.color, createCar.year)
  }

  def delete(id: Long) = db.run {
    cars.filter(_.id === id).delete
  }

  def listCars(gosNumber: Option[String],
               mark: Option[String],
               color: Option[String],
               year: Option[Int]): Future[Seq[Car]] = db.run {

    cars.filter { car =>
      List(gosNumber.map(car.gosNumber === _),
        mark.map(car.mark === _),
        color.map(car.color === _),
        year.map(car.year === _))

        .collect({ case Some(criteria) => criteria }).reduceLeftOption(_ && _).getOrElse(true: Rep[Boolean])
    }.result
  }

  def updateCar(car: Car) = db.run {
    cars.filter(_.id === car.id).map(c => (c.gosNumber, c.color)).update(car.gosNumber, car.color)
  }

  def static() = db.run {
    sql"""select count(*), max(c.year), min(c.year) from cars c""".as[(Int,Int,Int)]
  }

}





