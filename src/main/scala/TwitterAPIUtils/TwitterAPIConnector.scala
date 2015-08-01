package main.scala.TwitterAPIUtils

import java.util.concurrent.{TimeUnit, LinkedBlockingQueue}

import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient

class TwitterAPIConnector{
  val blockingQueue = new LinkedBlockingQueue[String]()
  val endpoint = new StatusesSampleEndpoint()
  endpoint.stallWarnings(false)

  var client: BasicClient = null
  def this(consumerKey: String, consumerSecret: String, token: String, secret: String) {
    this()
    this.client = new ClientBuilder().name("client")
            .hosts(Constants.STREAM_HOST)
            .endpoint(endpoint)
            .authentication(Auth.OAuth1(consumerKey,consumerSecret,token,secret))
            .processor(new StringDelimitedProcessor(blockingQueue))
            .build()
  }
  def this(username: String, password: String) {
    this()
    this.client = new ClientBuilder().name("client")
            .hosts(Constants.STREAM_HOST)
            .endpoint(endpoint)
            .authentication(Auth.BasicAuth(username,password))
            .processor(new StringDelimitedProcessor(blockingQueue))
            .build()
  }
  def connectClient = client.connect()
  def stopClient = client.stop()

  def popMessage : String = client.isDone() match {
    case true => {
      System.out.println("Client connection closed unexpectedly: " + client.getExitEvent().getMessage())
      ""
    }
    case false => blockingQueue poll(5, TimeUnit.SECONDS) match {
      case null => System.out.println("Did not receive a message in 5 seconds")
        ""
      case v =>  v
    }   
  }
}
