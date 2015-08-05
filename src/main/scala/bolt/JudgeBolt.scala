package bolt

import java.util

import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.IRichBolt
import backtype.storm.tuple.Tuple
import backtype.storm.tuple.Fields
import backtype.storm.task.{TopologyContext, OutputCollector}
import commonDataStructures._

class JudgeBolt extends IRichBolt {
  var _collector: OutputCollector = null
  var _context: TopologyContext = null
  val BadWords = null

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


       case _ => throw new ClassCastException
     }
  }
  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
     declarer.declare(new Fields("StormTweet"))
   }
}
