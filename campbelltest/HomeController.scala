package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
   
    val ConsumerKey = ""
    val ConsumerSecret = ""
	
  def index = Action {
    val consumer = new CommonsHttpOAuthConsumer(ConsumerKey,ConsumerSecret);
 
     val request = new HttpGet("https://api.twitter.com/1.1/statuses/user_timeline.json?count=2&screen_name=DrMarkCLewis");
     consumer.sign(request);
     val client = new DefaultHttpClient();
     val response = client.execute(request);
 
    val json = Json.parse(IOUtils.toString(response.getEntity().getContent()))
    
    println(json.toString())
    println(response.getStatusLine().getStatusCode());
     
      
    Ok(views.html.index("Yo dawg check this shit out"))
  }

}
