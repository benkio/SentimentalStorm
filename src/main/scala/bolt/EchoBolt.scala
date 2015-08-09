package bolt

import java.util

import backtype.storm.topology.{OutputFieldsDeclarer, BasicOutputCollector}
import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.tuple.Tuple
import commonDataStructures._

/**
 * Created by benkio on 02/08/15.
 */
class EchoBolt extends BaseBasicBolt{

    override def execute(tuple: Tuple, collector: BasicOutputCollector) {
      tuple.getValue(0) match {
        case t : StormTweet => //Common sentimental step 
          val steps: String = t.stormSteps map (st => st.compontentId + "+" + st.taskIndex) reduceLeft((x,y) => x + "=>" + y )
          println("BY: " +t.tweet.author.handle +" @: "+ t.tweet.timestamp.toString +" CONTENT: " + t.tweet.body + " Steps: " + steps )
        case t : String => //After counting
          println("keyword: " + t + " sentimentalWord: " + tuple.getValue(1) + " Count: " + tuple.getValue(2))
        case _ => throw new ClassCastException
      }

    }

  override def declareOutputFields(outputFieldsDeclarer: OutputFieldsDeclarer): Unit = {}
}

