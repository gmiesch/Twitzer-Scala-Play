package example

import scala.scalajs.js
import org.scalajs.dom
import shared.SharedMessages
import scala.util.Random
import scala.collection.mutable.Buffer



object ScalaJSExample extends js.JSApp {
  def main(): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = "O shit waddup"

	var text = dom.document.getElementById("json").innerHTML
	text = text.drop(1)
	var sample2:List[String] = text.split("-").toList

	var temp:Buffer[(String,Int)] = Buffer[(String,Int)]()
	sample2 = sample2.take(sample2.length-1)
	sample2.foreach(s => {
		val ss = s.split(",")
		temp.append((ss(0).drop(1), ss(1).take(ss(1).length-1).toInt))
	})

	var sample:List[(String,Int)] = temp.toList

    	val canvas = dom.document.getElementById("canvas").asInstanceOf[dom.html.Canvas]

	val renderer = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

	canvas.width = 1200
	canvas.height = 450

	renderer.fillStyle = "rgb(0, 122, 183)"
	renderer.fillRect(0, 0, canvas.width, canvas.height)
 
	renderer.fillStyle = "black"

	var common:List[String] = List("via","000","&amp","\n","&lt","&gt","rt","http://t","https://","i","me","my","myself","we","us","our","ours","ourselves","you","your","yours","yourself","yourselves","he","him","his","himself","she","her","hers","herself","it","its","itself","they","them","their","theirs","themselves","what","which","who","whom","whose","this","that","these","those","am","is","are","was","were","be","been","being","have","has","had","having","do","does","did","doing","will","would","should","can","could","ought","i'm","you're","he's","she's","it's","we're","they're","i've","you've","we've","they've","i'd","you'd","he'd","she'd","we'd","they'd","i'll","you'll","he'll","she'll","we'll","they'll","isn't","aren't","wasn't","weren't","hasn't","haven't","hadn't","doesn't","don't","didn't","won't","wouldn't","shan't","shouldn't","can't","cannot","couldn't","mustn't","let's","that's","who's","what's","here's","there's","when's","where's","why's","how's","a","an","the","and","but","if","or","because","as","until","while","of","at","by","for","with","about","against","between","into","through","during","before","after","above","below","to","from","up","upon","down","in","out","on","off","over","under","again","further","then","once","here","there","when","where","why","how","all","any","both","each","few","more","most","other","some","such","no","nor","not","only","own","same","so","than","too","very","say","says","said","shall")
	
	sample = sample.filter(s => !common.contains(s._1))

	sample = sample.sortWith(_._2 > _._2)

	sample = sample.take(250)

	var inMin:Int = sample(sample.length-1)._2
	var inMax:Int = sample(0)._2
	var outMin:Int = 0
	var outMax:Int = 255


	case class Rect(var x:Double, var y:Double, w:Double, h:Double, text:String, fontColor:Int, fontSize:Int)

	def intersect(cur:Rect, otherRect:Rect):Boolean = {
		var intersect:Boolean = false
		if(math.abs(otherRect.x-cur.x) <= otherRect.w/2.0 + cur.w/2.0) {
			if(math.abs(otherRect.y-cur.y) <= otherRect.h/2.0 + cur.h/2.0) {
				intersect = true
			}
		}
		intersect
	}

	def display(rect:Rect):Unit = {
		//this is so fucked
		//deal with length
		//not to flacked
		val color:String = "rgb(" + rect.fontColor.toString + "," + rect.fontColor.toString + "," +  rect.fontColor.toString  + ")"
		//println(color)
        renderer.fillStyle = color
        renderer.font = rect.fontSize.toString + "px sans-serif"
        renderer.fillText(rect.text, rect.x-(rect.w/2.0), rect.y+(rect.h/2.0))
		/*renderer.beginPath()
		renderer.fillStyle = "red"
		renderer.arc(rect.x, rect.y, 5, 0, 2*Math.PI, false)
		renderer.arc(rect.x-rect.w/2.0, rect.y, 5, 0, 2*Math.PI, false)
		renderer.stroke()
		renderer.fill()
		renderer.closePath()
		renderer.strokeRect(rect.x-(rect.w/2.0), rect.y-(rect.h/2.0), rect.w, rect.h)*/

	}

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

	def intersectAll(cur:Rect, placed:Buffer[Rect]):Boolean = {
		var intersectRect:Boolean = false
		for(rect <- placed)  {
			if(intersect(cur,rect)) {
				intersectRect = true 
			}
		}
		intersectRect
	}

	def outside(curRect:Rect,theta:Double):Boolean = {
		//println("theta: " + theta)
		val radius_x:Double = canvas.width/3.0
		val radius_y:Double = canvas.height/4.0
		var radius:Double = 1
		if(theta < Math.PI/2.0) {
			//println("Here1")
			radius = (canvas.width/2.0)/Math.cos(theta)
			//println(s"Rad: $radius")
		}
		else if(theta > Math.PI/2.0 && theta < Math.PI) {
			//println("Here2")
			radius = (canvas.width/2.0)/Math.cos((Math.PI/2.0)-theta)
		}
		else if(theta > Math.PI && theta < (3*Math.PI)/2.0) {
			//println("Here3")
			radius = (canvas.width/2.0)/Math.cos(((3*Math.PI)/2.0)-theta)
		} else {
			//println("Here4")
			radius = (canvas.width/2.0)/Math.cos(2*Math.PI-theta)
		}


		val dist_1 = ((curRect.x-curRect.w/2.0) - canvas.width/2.0)*((curRect.x-curRect.w/2.0) - canvas.width/2.0) + ((curRect.y+curRect.h/2.0) - canvas.height/2.0)*((curRect.y+curRect.h/2.0) - canvas.height/2.0)
		val dist_2 = ((curRect.x+curRect.w/2.0) - canvas.width/2.0)*((curRect.x+curRect.w/2.0) - canvas.width/2.0) + ((curRect.y+curRect.h/2.0) - canvas.height/2.0)*((curRect.y+curRect.h/2.0) - canvas.height/2.0)
		val dist_3 = ((curRect.x-curRect.w/2.0) - canvas.width/2.0)*((curRect.x-curRect.w/2.0) - canvas.width/2.0) + ((curRect.y-curRect.h/2.0) - canvas.height/2.0)*((curRect.y-curRect.h/2.0) - canvas.height/2.0)
		val dist_4 = ((curRect.x+curRect.w/2.0) - canvas.width/2.0)*((curRect.x+curRect.w/2.0) - canvas.width/2.0) + ((curRect.y-curRect.h/2.0) - canvas.height/2.0)*((curRect.y-curRect.h/2.0) - canvas.height/2.0)

		dist_1 > radius*radius || dist_2 > radius*radius || dist_3 > radius*radius || dist_4 > radius*radius
	}
	

	val rects:Buffer[Rect] = Buffer[Rect]()

	sample.foreach(s => {
		var fontSize:Int = lerpFont(s._2)
		var fontColor:Int = lerp(s._2) 
		val theta:Double = 2*Math.PI*Random.nextFloat()
		val x:Double = Math.cos(theta) + canvas.width/2.0
		val y:Double = Math.sin(theta) + canvas.height/2.0
		renderer.font = fontSize.toString + "px sans-serif"
		val measure = renderer.measureText(s._1)
		val w:Double = measure.width
		//get font width
		rects.append(new Rect(x, y, w, w/5.0, s._1, fontColor, fontSize))
	})

	val placed:Buffer[Rect] = Buffer[Rect]()
	
	while(rects.size != 0) {
		var curRect:Rect = rects(0)
		if(placed.isEmpty) {
			curRect.x = canvas.width/2.0
			curRect.y = canvas.height/2.0
		} else {
			var theta:Double = 2*Math.PI*Random.nextFloat()
			var rad:Double = 0.1
			curRect.x = Math.cos(theta) + canvas.width/2.0
			curRect.y = Math.sin(theta) + canvas.height/2.0
			var times = 1
			var keepGoing:Boolean = intersectAll(curRect, placed)
			while(keepGoing) {
				times += 1
				rad += 0.5
				curRect.x = rad*Math.cos(theta) + canvas.width/2.0
				curRect.y = rad*Math.sin(theta) + canvas.height/2.0
				if(outside(curRect,theta)) {
					theta = 2*Math.PI*Random.nextFloat()
					rad = 0.1
					curRect.x = rad*Math.cos(theta) + canvas.width/2.0
					curRect.y = rad*Math.sin(theta) + canvas.height/2.0
				}	
				keepGoing = intersectAll(curRect, placed)
				if(times > 25000) {
					keepGoing = false
				}
			}
		}
		display(curRect)
		placed.append(curRect)
		rects.remove(0)
	}
	
	//renderer.beginPath()
	//renderer.arc(canvas.width/2.0, canvas.height/2.0, 10, 0, 2*Math.PI, false)
	//renderer.stroke()
	//renderer.fill()
	//renderer.closePath()







  }
}
