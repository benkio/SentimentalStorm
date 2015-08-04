package spout.TwitterAPIUtils

import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

import twitter4j._

import java.util.concurrent.LinkedBlockingQueue
import commonDataStructures._

object TwitterStreamBuilder {
  def buildTwitterStream(filterKeywordsList: List[String], queue: LinkedBlockingQueue[Tweet]): TwitterStream = {
    //Setup Twitter4j object for request and response
    val twitterStream = new TwitterStreamFactory(
      new ConfigurationBuilder().setJSONStoreEnabled(true).build())
      .getInstance()

    twitterStream.addListener(new MyStatusListener(queue))
    twitterStream.setOAuthConsumer(Keys.APIKey, Keys.APISecret)
    twitterStream.setOAuthAccessToken(new AccessToken(Keys.Token, Keys.TokenSecret))

    if (!filterKeywordsList.isEmpty)
      twitterStream.filter(new FilterQuery().track(filterKeywordsList.toArray))
    else
      twitterStream.sample()

    twitterStream
  }

}
