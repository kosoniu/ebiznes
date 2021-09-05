package models.race

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RaceRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val race = TableQuery[RaceTable]
  private val raceFeature = TableQuery[RaceFeatureTable]

  val query = race
    .joinLeft(raceFeature)
    .on(_.id === _.raceId)
    .result

  val action = (for {
    result <- query
  } yield {
    result.groupBy(_._1)
      .map {
      case (_, tuples) =>
        val ((o), _) = tuples.head
        val p = tuples.flatMap(_._2)
        RaceWithFeatures(o, p)
    }.toSeq
  })

  def add(newRace: RaceWithFeatures): Future[RaceWithFeatures] = {
    val writeRace = (race
      returning race.map(_.id)
      into { case (race, id) => Race(id, race.name, race.description) }
    )

    val writeRaceFeatures = (raceFeature
      returning raceFeature.map(_.id)
      into { case (feature, id) => RaceFeature(id, feature.name, feature.description, feature.raceId) }
    )

    val action = for {
      addedRace <- writeRace += newRace.race
      features <- {
        var newFeatures = newRace.raceFeatures.map(feature => RaceFeature(0, feature.name, feature.description, addedRace.id))
        writeRaceFeatures ++= newFeatures
      }
    } yield RaceWithFeatures(addedRace, features)

    db.run(action.transactionally)
  }

  def getAll(): Future[Seq[RaceWithFeatures]] = db.run {
    action
  }

  def getById(id: Long): Future[Race] = db.run {
    race.filter(_.id === id).result.head
  }

  def getByIdOption(id: Long): Future[Option[Race]] = db.run {
    race.filter(_.id === id).result.headOption
  }

  def delete(id: Long): Future[Unit] = {
    db.run(raceFeature.filter(_.raceId === id).delete).map(_ => ())
    db.run(race.filter(_.id === id).delete).map(_ => ())
  }

  def update(id: Long, newRace: Race): Future[Unit] = {
    val raceToUpdate: Race = newRace.copy(id)
    db.run(race.filter(_.id === id).update(raceToUpdate)).map(_ => ())
  }

}



