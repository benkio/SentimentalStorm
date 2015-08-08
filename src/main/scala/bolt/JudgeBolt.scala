package bolt

import java.util
y
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.IRichBolt
import backtype.storm.tuple.Tuple
import backtype.storm.tuple.Fields
import backtype.storm.task.{TopologyContext, OutputCollector}
import commonDataStructures._
import utils._
import scala.concurrent._

class JudgeBolt extends IRichBolt {
  var _collector: OutputCollector = null
  var _context: TopologyContext = null
  val negativeWordsFuture: Future[List[String]] =  new FileReader(Files.negativeWordsFile).words
  val positiveWordsFuture: Future[List[String]] =  new FileReader(Files.positiveWordsFile).words

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
         val positiveWords = Await.Result(positiveWordsFuture, Duration(5,SECONDS))
         val negativeWords = Await.Result(negativeWordsFuture, Duration(5,SECONDS))
         if (positiveWords exists(pw => tweetBody.contains(pw)))
           //emit
           println("")
         else if (negativeWords exists(nw => tweetBody.contains(nw)))
           //emit
           println("")
         else
           println("Tweet discarted, no matching words")


       case _ => throw new ClassCastException
     }
  }
  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
     declarer.declare(new Fields("StormTweet"))
   }
}
