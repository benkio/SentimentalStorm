package topology

import backtype.storm.generated.StormTopology
import backtype.storm.topology.TopologyBuilder
import bolt.EchoBolt
import spout.TwitterSampleSpout
import utils._
/**
 * Created by benkio on 02/08/15.
 */
object TopologyBuilder {
  val builder = new TopologyBuilder()
  val keywords = new FileReader(Files.keywordsFile)
  def buildOSSentimentalTopology(spoutHintParallelism: Int, boltHintParallelism: Int): StormTopology = {
    builder.setSpout("OS",new TwitterSampleSpout(keywords.words.toArray) ,spoutHintParallelism)
    builder.setBolt("split", new EchoBolt, 2).shuffleGrouping("OS")
    builder.createTopology()
  }
}
