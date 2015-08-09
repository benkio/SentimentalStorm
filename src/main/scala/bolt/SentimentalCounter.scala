package bolt

import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.tuple.Tuple
import backtype.storm.topology.BasicOutputCollector
import backtype.storm.topology.OutputFieldsDeclarer
import scala.collection.mutable.HashMap
import scala.collection.Map
import backtype.storm.tuple.Fields
import backtype.storm.tuple.Values

/**
 * @author benkio
 */
class SentimentalCounter extends BaseBasicBolt {
  var sentimentalCount: Map[(String,String),Int] = new HashMap[(String,String),Int] 
  
  override def execute(tuple: Tuple, collector: BasicOutputCollector){
    val key: (String, String) = (tuple.getValue(1).toString(), tuple.getValue(2).toString())
    if (sentimentalCount.contains(key)){
      val count = sentimentalCount.get(key)
      sentimentalCount += key -> (1 + count.get)
    }
    else
      sentimentalCount += key -> 1
      
    collector.emit(new Values(key._1,key._2,sentimentalCount(key).asInstanceOf[Object]))
  }
  
  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
    declarer.declare(new Fields("keyword", "sentimentalWord", "Count"))
  }
}