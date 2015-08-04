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
import commonDataStructures._

import spout.TwitterAPIUtils._

class TwitterSampleSpout(keywords: List[String]) extends BaseRichSpout {

  var _collector: SpoutOutputCollector = null
  var queue: LinkedBlockingQueue[StormTweet] = null
  var _twitterStream: TwitterStream = null


  override def open(conf: Map[_,_], context: TopologyContext, collector: SpoutOutputCollector) {
    //Setup The structures used by storm
    queue = new LinkedBlockingQueue[StormTweet](1000)
    _collector = collector

    val spoutsSize: Int = context.getComponentTasks(context.getThisComponentId()).size()
    val myIdx: Int = context.getThisTaskIndex()
    
    _twitterStream = TwitterStreamBuilder.buildTwitterStream(KeywordListSplitter.getKeywordList(keywords,spoutsSize,myIdx),queue,context.getThisComponentId(),myIdx)
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
    declarer.declare(new Fields("StormTweet"))
  }

}
