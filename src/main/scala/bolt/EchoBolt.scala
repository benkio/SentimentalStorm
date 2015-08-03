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
      val tweet = tuple.getValue(0) match {
        case t : Tweet => t
        case _ => throw new ClassCastException
      }
      println("BY: " +tweet.author.handle +" @: "+tweet.timestamp.toString +" CONTENT: " + tweet.body)
    }

  override def declareOutputFields(outputFieldsDeclarer: OutputFieldsDeclarer): Unit = {}
}

