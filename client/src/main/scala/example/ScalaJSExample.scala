package example

import scala.scalajs.js
import org.scalajs.dom
import shared.SharedMessages
import scala.util.Random

object ScalaJSExample extends js.JSApp {
  def main(): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = "O shit waddup"

    val canvas = dom.document.getElementById("canvas").asInstanceOf[dom.html.Canvas]

	val renderer = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

	canvas.width = 600
	canvas.height = 400

	renderer.fillStyle = "#f8f8f8"
	renderer.fillRect(0, 0, canvas.width, canvas.height)

	renderer.fillStyle = "black"

	val sample:List[(String, Int)] = List(("saying",1),("What",1),("United",1),("just",3),("being",1),("over",4),("all",1),("bring",1),("kill",1),("its",1),("before",1),("Internet?",1),("better",1),("to",6),("only",2),("dead,",2),("Not",1),("has",1),("Marine",1),("clever",1),("sniper",1),("it.",1),("paying",1),("get",1),("know",1),("now",2),("wipe",2),("shit.",1),("entire",2),("tongue.",1),("held",1),("did",1),("couldn’t,",1),("maggot.",1),("traced",1),("idiot.",1),("storm,",1),("anywhere,",1),("ass",1),("right",1),("trained",2),("fucking",5),("me,",1),("bare",1),("are",1),("miserable",1),("out",2),("what",1),("network",1),("for",1),("away",1),("extensively",1),("access",1),("You’re",2),("numerous",1),("across",1),("kid.",1),("target.",1),("be",1),("we",1),("full",1),("I’ll",1),("300",1),("never",1),("use",1),("spies",1),("But",1),("extent",1),("graduated",1),("Think",1),("Earth,",1),("retribution",1),("on",2),("about",2),("would",1),("of",5),("could",1),("US",1),("thing",1),("that’s",1),("Navy",1),("think",1),("comment",1),("price,",1),("bitch?",1),("arsenal",1),("down",1),("you’re",5),("Seals,",1),("likes",1),("another",1),("I’ve",1),("continent,",1),("armed",1),("your",5),("speak",1),("little",4),("prepare",1),("top",2),("ways,",1),("been",2),("mark",1),("secret",2),("call",1),("Al-Quaeda,",1),("raids",1),("goddamn",6),("forces.",1),("hundred",1),("was",1),("wipes",1),("you,",1),("storm",1),("that",2),("precision",1),("but",2),("it",1),("drown",1),("known",1),("with",3),("hands.",1),("me",2),("warfare",1),("unarmed",1),("this",1),("combat,",1),("will",4),("fucker.",1),("can",3),("involved",1),("life.",1),("my",4),("and",9),("seven",1),("USA",1),("fury",1),("is",1),("am",3),("fuck",2),("didn’t,",1),("words.",1),("say",1),("have",5),("in",7),("seen",1),("You",2),("unholy",1),("shit",2),("kills.",1),("confirmed",1),("which",1),("you",16),("contacting",1),("again,",1),("I",11),("IP",1),("upon",1),("pathetic",1),("kiddo.",1),("nothing",1),("The",1),("class",1),("anytime,",1),("I’m",1),("off",1),("Corps",1),("maybe",1),("face",1),("States",1),("gorilla",1),("As",1),("so",1),("the",15),("If",1))

	sample.foreach(s => {
		val minX = canvas.width/8.0
		val maxX = canvas.width*(7.0/8.0)
		val minY = canvas.height/8.0
		val maxY = canvas.height*(7.0/8.0)

		val xPos = (maxX - minX) * Random.nextFloat() + minX
 		val yPos = (maxY - minY) * Random.nextFloat() + minY
		val fontSize = s._2 * 5

		renderer.font = fontSize.toString + "px sans-serif"
		renderer.fillText(s._1, xPos, yPos)
	})
  }
}
