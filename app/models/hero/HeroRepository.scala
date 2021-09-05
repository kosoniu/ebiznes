package models.hero

import models.clazz.{ClassProficiencyTable, ClassTable, ClassWithProficiencies}
import models.origin.{OriginProficiencyTable, OriginTable, OriginWithProficiencies}
import models.proficiency.{Proficiency, ProficiencyTable}
import models.race.{RaceFeatureTable, RaceTable, RaceWithFeatures}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HeroRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private val hero = TableQuery[HeroTable]
  private val origin = TableQuery[OriginTable]
  private val originProficiency = TableQuery[OriginProficiencyTable]
  private val proficiency = TableQuery[ProficiencyTable]
  private val clazz = TableQuery[ClassTable]
  private val classProficiency = TableQuery[ClassProficiencyTable]
  private val race = TableQuery[RaceTable]
  private val raceFeature = TableQuery[RaceFeatureTable]

  val originQuery = hero
    .joinLeft(origin)
    .on(_.originId === _.id)
    .joinLeft(originProficiency)
    .on(_._1.originId === _.originId)
    .joinLeft(proficiency)
    .on(_._2.flatMap(_.proficiencyId) === _.id)
    .result

  val classQuery = hero
    .joinLeft(clazz)
    .on(_.classId === _.id)
    .joinLeft(classProficiency)
    .on(_._1.classId === _.classId)
    .joinLeft(proficiency)
    .on(_._2.flatMap(_.proficiencyId) === _.id)
    .result

  val raceQuery =  hero
    .joinLeft(race)
    .on(_.raceId === _.id)
    .joinLeft(raceFeature)
    .on(_._2.flatMap(_.id) === _.raceId)
    .result

  val heroAction = for {
    classResult <- classQuery
    originResult <- originQuery
    raceResult <- raceQuery
  } yield {
    classResult.groupBy(_._1._1._1.id).map {
      case (_, classTuples) =>
        val (((h, c), p), _) = classTuples.head
        val cp = classTuples.flatMap(_._2)

        originResult.groupBy(_._1._1._1.id).map {
          case (_, originTuples) =>
            val (((h, o), p), _) = originTuples.head
            val op = originTuples.flatMap(_._2)

            raceResult.groupBy(_._1._1.id).map {
              case (_, raceTuples) =>
                val ((h, r), _) = raceTuples.head
                val rf = raceTuples.flatMap(_._2)

                HeroWithProperties(
                  h.id,
                  h.name,
                  h.level,
                  RaceWithFeatures(r.head, rf),
                  ClassWithProficiencies(c.head, cp),
                  OriginWithProficiencies(o.head, op)
                )
            }.head
        }.head
    }.toSeq
  }

  def getAll(): Future[Seq[HeroWithProperties]] = db.run {
    heroAction
  }

  def add(newHero: Hero): Future[Hero] = {
    dbConfig.db
      .run(hero += newHero)
      .map(_ => newHero)
  }

  def delete(id: Long): Future[Unit] = db.run(hero.filter(_.id === id).delete).map(_ => ())


}



