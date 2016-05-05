import java.security.MessageDigest
import java.net.URLEncoder
import javax.crypto._
import javax.crypto.spec.SecretKeySpec

def md5(s : String) = {
  val messageDigest = MessageDigest.getInstance("MD5")
	val bytes = s.getBytes("UTF-8")
  messageDigest.update(bytes, 0, bytes.length)
  new java.math.BigInteger(1, messageDigest.digest()).toString(16)
}

def sha1(s : String, secret:String) = {
	val mac = Mac.getInstance("HmacSHA1")
  mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA1"))
  val hashed : Array[Byte] =  mac.doFinal(s.getBytes("UTF-8"))
  //println(hashed.map{ b => String.format("%02X", java.lang.Byte.valueOf(b)) }.mkString)
  rawurlencode(java.util.Base64.getUrlEncoder.encodeToString(hashed))
}

def makeCurl(url:String, count:String, user:String, consKey:String, nonce:String, sig:String) = {
  val curl = StringBuilder.newBuilder
  curl.append("curl --get '" +url+"' --data ")
  curl.append("'count=" + count + user +"' --header 'Authorization: OAuth ")
  curl.append("oauth_consumer_key=\""+consKey+"\", ")
  curl.append("oauth_nonce=\""+nonce+"\", ")
  curl.append("oauth_signature=\""+sig+"\", oauth_signature_method=\"HMAC-SHA1\", ")
  curl.append("oauth_timestamp=\""+timestamp+"\", oauth_version=\"1.0\"' --verbose")
  curl.toString
}

def rand() = {
  val rand = util.Random
  rand.nextInt.toString
}

def rawurlencode(s : String) = {
  URLEncoder.encode(s, "UTF-8")
}

def buildBaseString(count:String, consKey:String, user:String, nonce:String, timestamp:String) = {
  val base = StringBuilder.newBuilder
  base.append("GET&" + rawurlencode("https://api.twitter.com/1.1/statuses/user_timeline.json"))
  base.append("&" + rawurlencode(count))
  val subSig = StringBuilder.newBuilder
  subSig.append("oauth_consumer_key=" + rawurlencode(consKey))
	subSig.append("&oauth_nonce=" + rawurlencode(nonce))
  subSig.append("&oauth_signature_method=" + rawurlencode("HMAC-SHA1"))
  subSig.append("&oauth_timestamp=" + timestamp)
  subSig.append("&oauth_version=" + "1.0" + user)
  base.append(rawurlencode(subSig.toString)).toString
}

// TODO CHANGE
val rawCount = 200

// TODO check for missing file

try {
	io.Source.fromFile("keys.txt").getLines.toList
}
catch {
  case e: Exception => println("Missing keys.txt!")
}
val lines = io.Source.fromFile("keys.txt").getLines.toList
val consumerKey = lines(0)
val secretKey = lines(1) + "&"
// TODO get username
val user = "&screen_name=" + "DrMarkCLewis"
val count = "count=" + rawCount.toString + "&"
val nonce = md5(rand)
val timestamp = (System.currentTimeMillis / 1000).toString
val base = buildBaseString(count, consumerKey, user, nonce, timestamp)
val encodedbase = sha1(base, secretKey)
// TODO get the URL for the API requested
val url = "https://api.twitter.com/1.1/statuses/user_timeline.json"
println(makeCurl(url, rawCount.toString, user, consumerKey, nonce, encodedbase))
