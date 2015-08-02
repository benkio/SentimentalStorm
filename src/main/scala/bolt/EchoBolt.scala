package bolt

import java.util

import backtype.storm.topology.{OutputFieldsDeclarer, BasicOutputCollector}
import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.tuple.Tuple;

/**
 * Created by benkio on 02/08/15.
 */
class EchoBolt extends BaseBasicBolt{

    override def execute(tuple: Tuple, collector: BasicOutputCollector) {
      println(tuple.getString(0))
    }

  override def declareOutputFields(outputFieldsDeclarer: OutputFieldsDeclarer): Unit = {}
}

