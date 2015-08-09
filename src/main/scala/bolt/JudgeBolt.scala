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
         val tweetWithStep = StormTweetStepManager.AddStormStep(t, _context.getThisComponentId, _context.getThisTaskId)
         val tweetBody = t.tweet.body
         
         positiveWords foreach (pw => 
           if (tweetBody.contains(pw))
             _collector.emit("PositiveTweetStream",tuple, new Values(tweetWithStep, tuple.getValue(1),pw))
           )
                
         negativeWords foreach(nw => 
           if (tweetBody.contains(nw))
             _collector.emit("NegativeTweetStream",tuple, new Values(tweetWithStep, tuple.getValue(1),nw))
         )

       case _ => throw new ClassCastException
     }
  }
  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
     declarer.declareStream("PositiveTweetStream", new Fields("PositiveStormTweet","keyword", "sentimentalWord"));
     declarer.declareStream("NegativeTweetStream", new Fields("NegativeStormTweet","keyword", "sentimentalWord"));
   }
}
