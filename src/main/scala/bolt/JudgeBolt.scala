package bolt

import java.util
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.IRichBolt
import backtype.storm.tuple.Tuple
import backtype.storm.tuple.Fields
import backtype.storm.tuple.Values
import backtype.storm.task.{TopologyContext, OutputCollector}
import commonDataStructures._
import utils._
import scala.concurrent._
import scala.concurrent.duration._

class JudgeBolt(positiveWords: List[String], negativeWords: List[String]) extends IRichBolt {
  var _collector: OutputCollector = null
  var _context: TopologyContext = null
 
  override def cleanup {}
  override def getComponentConfiguration: java.util.Map[String,Object] = { null }

  override def prepare(conf: java.util.Map[_,_],context: TopologyContext, collector: OutputCollector){
    _collector = collector
    _context = context
  }

  override def execute(tuple: Tuple) {
     tuple.getValue(0) match {
       case t : StormTweet =>
         val tweetWithStep = StormTweet(t.tweet,t.stormSteps :+ StormStep(_context.getThisComponentId(),_context.getThisTaskIndex()))
         val tweetBody = t.tweet.body
         if (positiveWords exists(pw => tweetBody.contains(pw)))
           _collector.emit("PositiveTweetStream",tuple, new Values(tweetWithStep))
         else if (negativeWords exists(nw => tweetBody.contains(nw)))
           _collector.emit("NegativeTweetStream",tuple, new Values(tweetWithStep))
         else
           println("Tweet discarted, no matching words")

       case _ => throw new ClassCastException
     }
  }
  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
     declarer.declareStream("PositiveTweetStream", new Fields("PositiveStormTweet"));
     declarer.declareStream("NegativeTweetStream", new Fields("NegativeStormTweet"));
   }
}
