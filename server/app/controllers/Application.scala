package controllers

import play.api._
import play.api.mvc._
import shared.SharedMessages

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.Tag
import slick.lifted.Rep
import scala.concurrent.ExecutionContext.Implicits.global
import models._
import play.api.data._
import play.api.data.Forms._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._

import play.api.mvc.Session

class Application @Inject()(implicit environment: Environment, dbConfigProvider: DatabaseConfigProvider) extends Controller {

	val dbConfig = dbConfigProvider.get[JdbcProfile]

	var session = new Session()

	lazy val Users = new TableQuery(tag => new Users(tag))

	val userForm = Form(
		mapping(
			"username" -> nonEmptyText,
			"password" -> nonEmptyText)(User.apply)(User.unapply))

	val searchForm = Form(
		mapping(
			"twitterUsername" -> nonEmptyText,
			"numTweets" -> number)(Search.apply)(Search.unapply))

  def index = Action {
    Ok(views.html.login(userForm))
  }

	def verifyLogin = Action.async(implicit request => {
    userForm.bindFromRequest().fold(
      formWithErrors => {
        Future { Ok(views.html.login(formWithErrors)) }
      },
      user => {
        val db = dbConfig.db
        Queries.validLogin(user, db).flatMap(_ match {
        	case -1 => Future(Redirect(routes.Application.index))
        	case 1 => {
						session = session + ("username" -> user.username)
        		Future(Redirect(routes.Application.home))
        	}
        })
      })
  	})

	def createUser = Action {
		Ok(views.html.createUser(userForm))
	}

	def logout = Action {
		session = session - "username"
		Redirect(routes.Application.index)
	}

	def addNewUser = Action.async(implicit request => {
    	userForm.bindFromRequest().fold(
      	formWithErrors => {
        	Future{ Ok(views.html.createUser(formWithErrors)) }
      	},
      	user => {
			val db = dbConfig.db
			Queries.existingUser(user.username, db).flatMap(_ match {
        		case 1 => Future(Redirect(routes.Application.createUser))
        		case -1 => {
        			//Add the user to the database
    				db.run(Users += User(user.username, user.password))
    				Future(Redirect(routes.Application.index))
        		}
        	})
      	})
	})

	def home = Action {
		var user:String = session.get("username").get
		Ok(views.html.home(user, searchForm))
	}

  def searchResults = Action.async(implicit request => {
    searchForm.bindFromRequest().fold(
      formWithErrors => {
				var user:String = session.get("username").get
        Future { Ok(views.html.home(user, formWithErrors)) }
      },
      search => {
        	//HIT TWITTER API
					//CAMPBELL PLS
        	Future { Ok("Search results were: " + search.twitterUsername + ", " + search.numTweets) }
      })
  	})

  def history = Action {
		var user:String = session.get("username").get
		Ok(views.html.history(user))
  }

  def results = Action {
		//var user:String = session.get("username").get
		//Ok(views.html.results(user))
		Ok(views.html.results("testingUsername"))
  }

}
