package models

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import models._
import play.api.data._
import play.api.data.Forms._
import scala.concurrent.Future
import java.sql.Timestamp

object Queries {
	def validLogin(user: User, db: Database):Future[Int] = {
		val Users = new TableQuery(tag => new Users(tag))
		val matches = db.run(Users.filter(u => u.username === user.username && u.password === user.password).result)
		matches.map(us => if (us.isEmpty) -1 else 1)
	}

	def existingUser(username: String, db: Database):Future[Int] = {
		val Users = new TableQuery(tag => new Users(tag))
		val matches = db.run(Users.filter(u => u.username === username).result)
		matches.map(us => if (us.isEmpty) -1 else 1)
	}
}
