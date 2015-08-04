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
      val stormTweet = tuple.getValue(0) match {
        case t : StormTweet => t
        case _ => throw new ClassCastException
      }
      println("BY: " +stormTweet.tweet.author.handle +" @: "+ stormTweet.tweet.timestamp.toString +" CONTENT: " + stormTweet.tweet.body)
    }

  override def declareOutputFields(outputFieldsDeclarer: OutputFieldsDeclarer): Unit = {}
}

