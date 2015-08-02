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
    builder.setSpout("OS Win",new TwitterSampleSpout(keywords.words.filter(key => !key.startsWith("Win")).toArray) ,spoutHintParallelism)
    builder.setBolt("split", new EchoBolt, 1).shuffleGrouping("OS Win")
    builder.createTopology()
  }
}
