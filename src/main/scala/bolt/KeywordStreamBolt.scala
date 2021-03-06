package bolt

import backtype.storm.topology.IRichBolt
import java.util
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.IRichBolt
import backtype.storm.tuple.Tuple
import backtype.storm.tuple.Fields
import backtype.storm.tuple.Values
import backtype.storm.task.{TopologyContext, OutputCollector}
import commonDataStructures._
import utils._

/**
 * @author benkio
 */
class KeywordStreamBolt(keyWords: List[String]) extends IRichBolt {
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
         val tweetWithStep = StormTweetStepManager.AddStormStep(t, _context.getThisComponentId, _context.getThisTaskId)
         val tweetBody = t.tweet.body
         keyWords foreach (k => if (tweetBody.contains(k)) _collector.emit(tuple, new Values(tweetWithStep,k)))
       case _ => throw new ClassCastException
     }
  }
  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
    declarer.declare(new Fields("StormTweet", "keyword"))
   }
}