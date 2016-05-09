package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
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
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io._

import play.api.mvc.Session

class Application @Inject()(implicit environment: Environment, dbConfigProvider: DatabaseConfigProvider) extends Controller {


	val ConsumerKey = "XXXXXXXXXXXXXXXXXX"
	val ConsumerSecret = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
	val dbConfig = dbConfigProvider.get[JdbcProfile]

	var session = new Session()

	lazy val Users = new TableQuery(tag => new Users(tag))
	lazy val Historys = new TableQuery(tag => new Historys(tag))

	val userForm = Form(
		mapping(
			"username" -> nonEmptyText,
			"password" -> nonEmptyText)(User.apply)(User.unapply))

	val searchForm = Form(
		mapping(
			"twitterUsername" -> nonEmptyText,
			"numTweets" -> number)(Search.apply)(Search.unapply))

  def countWords(allWords : String) : List[(String, Int)] = {
      val wordMap = scala.collection.mutable.Map[String, Int]().withDefaultValue(0)
      for (word <- allWords.split(" ")) {
          // TODO filter words
          wordMap.update(word, wordMap(word) + 1)
      }
      wordMap -= ""
      wordMap.toList.sortWith(_._2 > _._2)
  }

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
		val consumer = new CommonsHttpOAuthConsumer(ConsumerKey,ConsumerSecret);
		val stringy = StringBuilder.newBuilder
		var count = Math.min(3200, search.numTweets)
		var maxID : Long = -1
		val iters = if (count % 200 == 0) count / 200 else count / 200 + 1
		for (i <- 0 until iters) {
			val thisCount = Math.min(200, count)
			count -= thisCount
			println("Consuming " + thisCount + ", " + count + " remaining")
			val requestBuilder = StringBuilder.newBuilder
			requestBuilder.append("https://api.twitter.com/1.1/statuses/user_timeline.json?count=" + thisCount + "&screen_name=" + search.twitterUsername)
			if (maxID != -1) requestBuilder.append("&max_id=" + maxID)
			println(requestBuilder.toString)
			val request = new HttpGet(requestBuilder.toString);
			consumer.sign(request);
     			val client = new DefaultHttpClient();
     			val response = client.execute(request);
    			val json = Json.parse(IOUtils.toString(response.getEntity().getContent()))
    			stringy.append((json \\ "text").map(e => e.toString.replace("\"", "").replaceAll("[^a-zA-Z ]", "")).mkString(" ").toLowerCase)
			val jsid = json.last \ "id"
			jsid match {
  				case JsDefined(v) => maxID = v.toString.toLong
  				case undefined: JsUndefined => println(undefined.validationError)
			}
			println(maxID)
		}
		var user:String = session.get("username").get
		session = session + (s"$user - tweetUsername" -> search.twitterUsername)
		session = session + (s"$user - numTweets" -> search.numTweets.toString)
		session = session + (s"$user - json" -> (countWords(stringy.toString).take(250).mkString("-")))
        	Future { Ok(views.html.results(user, countWords(stringy.toString).take(250).mkString("-"))) }
      })
  	})

  def getHistory(id:Int) = Action {
	val futureHistory:Future[Seq[models.History]] = dbConfig.db.run(Historys.filter(_.id === id).result)
	val lst:Future[List[String]] = futureHistory.map(x => x.map(y => (y.json)).toList)
	val result = Await.result(lst, 2e+9 nanos)

	var user:String = session.get("username").get
	Ok(views.html.results(user, result.mkString("-")))
  }

  def history = Action {
	var user:String = session.get("username").get

	val futureHistory:Future[Seq[models.History]] = dbConfig.db.run(Historys.filter(_.owner === user).result)
	val lst:Future[List[(Int, String, Int, String)]] = futureHistory.map(x => x.map(y => (y.id, y.tweetUsername, y.numTweets, y.json)).toList)
	val result = Await.result(lst, 2e+9 nanos)

	Ok(views.html.history(user, result))
  }

  def results(user:String, json:String) = Action {
	Ok(views.html.results(user, json))
  }

  def saveWordCloud(user:String) = Action {
	var tweetUsername:String = session.get(s"$user - tweetUsername").get
	var numTweets:Int = session.get(s"$user - numTweets").get.toInt
	var json:String = session.get(s"$user - json").get

	val db = dbConfig.db

	db.run(Historys += History(0, user, tweetUsername, numTweets, json))
	
	Ok(views.html.home(user,searchForm))
  }

}
