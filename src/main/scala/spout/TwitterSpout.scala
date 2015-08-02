package spout

import java.util
import java.util.Map
import java.util.concurrent.LinkedBlockingQueue

import twitter4j._

import backtype.storm.Config
import backtype.storm.spout.SpoutOutputCollector
import backtype.storm.task.TopologyContext
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichSpout
import backtype.storm.tuple.Fields
import backtype.storm.tuple.Values
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

import spout.TwitterAPIUtils._

class TwitterSampleSpout(keyWords: Array[String]) extends BaseRichSpout {

  var _collector: SpoutOutputCollector = null
  var queue: LinkedBlockingQueue[Status] = null
  var _twitterStream: TwitterStream = null


  override def open(conf: Map[_,_], context: TopologyContext, collector: SpoutOutputCollector) {
    queue = new LinkedBlockingQueue[Status](1000)
    _collector = collector



    val twitterStream = new TwitterStreamFactory(
      new ConfigurationBuilder().setJSONStoreEnabled(true).build())
      .getInstance()

    twitterStream.addListener(new StatusListener(queue))
    twitterStream.setOAuthConsumer(Keys.APIKey, Keys.APISecret)
    twitterStream.setOAuthAccessToken(new AccessToken(Keys.Token, Keys.TokenSecret))

    if (keyWords.length == 0)
      twitterStream.sample()
    else
      twitterStream.filter(new FilterQuery().track(keyWords))

  }

  override def nextTuple = {
    val ret = queue.poll()
    if (ret != null)
      _collector.emit(new Values(ret))
  }

  override def close = {
    _twitterStream.shutdown()
  }

  override def getComponentConfiguration: util.Map[String, Object] = {
    val ret = new Config
    ret.setMaxTaskParallelism(1)
    ret
  }

  override def ack(id: Object) { }

  override def fail(id: Object) { }

  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
    declarer.declare(new Fields("tweet"))
  }

}
