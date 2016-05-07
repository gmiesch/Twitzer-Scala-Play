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

	canvas.width = 1200
	canvas.height = 450

	renderer.fillStyle = "rgb(0, 122, 183)"
	renderer.fillRect(0, 0, canvas.width, canvas.height)

	renderer.fillStyle = "black"

	/*var sample:List[(String, Int)] = List(("saying",1),("What",1),("United",1),("just",3),("being",1),("over",4),("all",1),("bring",61),("kill",16),("its",1),("before",1),("Internet?",21),("better",1),("to",6),("only",2),("dead,",2),("Not",1),("has",1),("Marine",1),("clever",1),("sniper",1),("it.",1),("paying",1),("get",1),("know",1),("now",2),("wipe",2),("shit.",1),("entire",2),("tongue.",1),("held",1),("did",1),("couldn’t,",1),("maggot.",1),("traced",1),("idiot.",1),("storm,",1),("anywhere,",1),("ass",18),("right",15),("trained",2),("fucking",5),("me,",1),("bare",19),("are",1),("miserable",1),("out",2),("what",1),("network",1),("for",1),("away",1),("extensively",1),("access",1),("You’re",2),("numerous",1),("across",1),("kid.",1),("target.",1),("be",1),("we",1),("full",1),("I’ll",1),("300",1),("never",1),("use",1),("spies",100),("But",1),("extent",1),("graduated",1),("Think",1),("Earth,",1),("retribution",1),("on",2),("about",2),("would",1),("of",5),("could",1),("US",1),("thing",1),("that’s",1),("Navy",1),("think",1),("comment",1),("price,",12),("bitch?",1),("arsenal",1),("down",1),("you’re",5),("Seals,",1),("likes",1),("another",1),("I’ve",1),("continent,",1),("armed",1),("your",5),("speak",1),("little",4),("prepare",1),("top",2),("ways,",1),("been",2),("mark",1),("secret",2),("call",1),("Al-Quaeda,",1),("raids",51),("goddamn",6),("forces.",1),("hundred",1),("was",1),("wipes",27),("you",90),("storm",1),("that",2),("precision",1),("but",2),("it",15),("drown",1),("known",1),("with",3),("hands.",1),("me",2),("warfare",1),("unarmed",1),("this",1),("combat,",1),("will",4),("fucker.",1),("can",3),("involved",1),("life.",1),("my",4),("and",9),("seven",1),("USA",1),("fury",1),("is",12),("am",3),("fuck",2),("didn’t,",1),("words.",1),("say",1),("have",5),("in",7),("seen",1),("You",2),("unholy",1),("shit",2),("kills.",1),("confirmed",1),("which",1),("contacting",1),("again,",1),("I",11),("IP",1),("upon",1),("pathetic",1),("kiddo.",1),("nothing",1),("The",1),("class",1),("anytime,",1),("I’m",1),("off",1),("Corps",1),("maybe",1),("face",1),("States",1),("gorilla",1),("As",1),("so",1),("the",15),("If",1))*/


var sample:List[(String,Int)] = List(("study",40),("motion",15),("forces",10),("electricity",15),("movement",10),("relation",5),("things",10),("force",5),("ad",5),("energy",85),("living",5),("nonliving",5),("laws",15),("speed",45),("velocity",30),("define",5),("constraints",5),("universe",10),("physics",120),("describing",5),("matter",90),("physics-the",5),("world",10),("works",10),("science",70),("interactions",30),("studies",5),("properties",45),("nature",40),("branch",30),("concerned",25),("source",40),("google",10),("defintions",5),("two",15),("grouped",15),("traditional",15),("fields",15),("acoustics",15),("optics",15),("mechanics",20),("thermodynamics",15),("electromagnetism",15),("modern",15),("extensions",15),("thefreedictionary",15),("interaction",15),("org",25),("answers",5),("natural",15),("objects",5),("treats",10),("acting",5),("department",5),("gravitation",5),("heat",10),("light",10),("magnetism",10),("modify",5),("general",10),("bodies",5),("philosophy",5),("brainyquote",5),("words",5),("ph",5),("html",5),("lrl",5),("zgzmeylfwuy",5),("subject",5),("distinguished",5),("chemistry",5),("biology",5),("includes",5),("radiation",5),("sound",5),("structure",5),("atoms",5),("including",10),("atomic",10),("nuclear",10),("cryogenics",10),("solid-state",10),("particle",10),("plasma",10),("deals",5),("merriam-webster",5),("dictionary",10),("analysis",5),("conducted",5),("order",5),("understand",5),("behaves",5),("en",5),("wikipedia",5),("wiki",5),("physics-",5),("physical",5),("behaviour",5),("collinsdictionary",5),("english",5),("time",35),("distance",35),("wheels",5),("revelations",5),("minute",5),("acceleration",20),("torque",5),("wheel",5),("rotations",5),("resistance",5),("momentum",5),("measure",10),("direction",10),("car",5),("add",5),("traveled",5),("weight",5),("electrical",5),("power",5))


	var common:List[String] = List("via","000","&amp","\n","&lt","&gt","rt","http://t","https://","i","me","my","myself","we","us","our","ours","ourselves","you","your","yours","yourself","yourselves","he","him","his","himself","she","her","hers","herself","it","its","itself","they","them","their","theirs","themselves","what","which","who","whom","whose","this","that","these","those","am","is","are","was","were","be","been","being","have","has","had","having","do","does","did","doing","will","would","should","can","could","ought","i'm","you're","he's","she's","it's","we're","they're","i've","you've","we've","they've","i'd","you'd","he'd","she'd","we'd","they'd","i'll","you'll","he'll","she'll","we'll","they'll","isn't","aren't","wasn't","weren't","hasn't","haven't","hadn't","doesn't","don't","didn't","won't","wouldn't","shan't","shouldn't","can't","cannot","couldn't","mustn't","let's","that's","who's","what's","here's","there's","when's","where's","why's","how's","a","an","the","and","but","if","or","because","as","until","while","of","at","by","for","with","about","against","between","into","through","during","before","after","above","below","to","from","up","upon","down","in","out","on","off","over","under","again","further","then","once","here","there","when","where","why","how","all","any","both","each","few","more","most","other","some","such","no","nor","not","only","own","same","so","than","too","very","say","says","said","shall")
	
	sample = sample.filter(s => !common.contains(s._1))

	sample = sample.sortWith(_._2 > _._2)

	var inMin:Int = sample(sample.length-1)._2
	var inMax:Int = sample(0)._2
	var outMin:Int = 0
	var outMax:Int = 255

	def lerp(x:Int):Int = {
		(x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}

	var inMinFont:Int = sample(sample.length-1)._2
	var inMaxFont:Int = sample(0)._2
	var outMinFont:Int = 5
	var outMaxFont:Int = 120

	def lerpFont(x:Int):Int = {
		(x - inMinFont) * (outMaxFont - outMinFont) / (inMaxFont - inMinFont) + outMinFont;
	}

	sample.foreach(s => {
		val r:Double = 2*Math.PI*Random.nextFloat()
		val lenX:Double = (canvas.width/4.0)*Random.nextFloat()
		val lenY:Double = (canvas.height/3.5)*Random.nextFloat()
		var x:Double = (canvas.width/4.0)*Math.cos(r)+(canvas.width/2.0)
		if(x >= canvas.width/2.0) x -= lenX
		else x += lenX		
		var y:Double = (canvas.height/3.5)*Math.sin(r)+(canvas.height/2.0)
		if(y >= canvas.height/2.0) y -= lenY
		else y += lenY

		var fontSize = lerpFont(s._2)
		
		var fontColor = lerp(s._2) 
		renderer.fillStyle = s"rgb($fontColor, $fontColor, $fontColor)"

		renderer.font = fontSize.toString + "px sans-serif"
		renderer.fillText(s._1, x, y)
	})
  }
}
