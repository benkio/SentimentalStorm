package spout.TwitterAPIUtils

import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder
import scala.util.Failure
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

import twitter4j._

import java.util.concurrent.LinkedBlockingQueue
import commonDataStructures._

object TwitterStreamBuilder {
  def buildTwitterStream(filterKeywordsListFuture: Future[List[String]], queue: LinkedBlockingQueue[StormTweet],componentId: String,taskId: Int): Future[TwitterStream] = {
    //Setup Twitter4j object for request and response
    val twitterStream = new TwitterStreamFactory(
      new ConfigurationBuilder().setJSONStoreEnabled(true).build())
      .getInstance()

    twitterStream.addListener(new MyStatusListener(queue,componentId,taskId))
    twitterStream.setOAuthConsumer(Keys.APIKey, Keys.APISecret)
    twitterStream.setOAuthAccessToken(new AccessToken(Keys.Token, Keys.TokenSecret))

    val twitterBuilderFuture = Future {
      val filterKeywordsList = Await.result(filterKeywordsListFuture,Duration(5, SECONDS))
      //println("FilterList: " + filterKeywordsList)
      if ( !filterKeywordsList.isEmpty)
        twitterStream.filter(new FilterQuery().track(filterKeywordsList.toArray))
      else
        twitterStream.sample()

      twitterStream
    }
    twitterBuilderFuture onFailure {
          case t => println("An error has occured in the building of twitterStream with error: " + t.getMessage)
    }
    twitterBuilderFuture
  }

}
