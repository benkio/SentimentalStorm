package main.scala.TwitterAPIUtils

import java.util.concurrent.LinkedBlockingQueue

import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient

class TwitterAPIConnector (){
  val blockingQueue = new LinkedBlockingQueue<String>()
  val endpoint = new StatusesSampleEndpoint()
  endpoint.stallWarnings(false)

  //Create the client for connection
  var client: BasicClient
  def TwitterAPIConnector(consumerKey: String, consumerSecret: String, token: String, secret: String){
    client = new ClientBuilder().name("client")
            .hosts(Constants.STREAM_HOST)
            .endpoint(endpoint)
            .authentication(Auth.OAuth1(consumerKey,consumerSecret,token,secret))
            .processor(new StringDelimitedProcessor(blockingQueue))
            .build()
  }
  def TwitterAPIConnector(username: String, password: String) {
    client = new ClientBuilder().name("client")
            .hosts(Constants.STREAM_HOST)
            .endpoint(endpoint)
            .authentication(Auth.BasicAuth(username,password))
            .processor(new StringDelimitedProcessor(blockingQueue))
            .build()
  }
  def connectClient = client.connect()
  def stopClient = client.stop()

}
