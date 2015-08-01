package main.scala.TwitterAPIUtils

import com.twitter.hbc.httpclient.auth.{OAuth1, Authentication, BasicAuth}

object Auth{
  def OAuth1(consumerKey: String, consumerSecret: String, token: String, secret: String): Authentication = {
    val auth = new OAuth1(consumerKey, consumerSecret, token, secret)
    return auth
  }

  def BasicAuth(username: String, password: String): Authentication = {
    val basic = new BasicAuth(username, password)
    return basic
  }
}