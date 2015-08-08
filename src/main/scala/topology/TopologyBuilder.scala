package topology

import backtype.storm.generated.StormTopology
import backtype.storm.topology._
import bolt._
import spout.TwitterSampleSpout
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import utils._
/**
 * Created by benkio on 02/08/15.
 */
object TopologyBuilder {
  val builder = new TopologyBuilder()
  val keywords = new FileReader(Files.keywordsFile)
  val negativeWords = new FileReader(Files.negativeWordsFile).words
  val positiveWords = new FileReader(Files.positiveWordsFile).words
  def buildOSSentimentalTopology(spoutHintParallelism: Int, boltHintParallelism: Int): StormTopology = {
    builder.setSpout("OS",new TwitterSampleSpout(Await.result(keywords.words,Duration(5,SECONDS))) ,spoutHintParallelism)
    builder.setBolt("Judge",new JudgeBolt(Await.result(positiveWords, Duration(5,SECONDS)),Await.result(negativeWords, Duration(5,SECONDS))), boltHintParallelism)
    builder.setBolt("OS Echo", new EchoBolt, boltHintParallelism).shuffleGrouping("OS")
    builder.setBolt("positiveStream Echo", new EchoBolt, boltHintParallelism).shuffleGrouping("Judge","PositiveTweetStream")
    builder.setBolt("negativeStream Echo", new EchoBolt, boltHintParallelism).shuffleGrouping("Judge","NegativeTweetStream")
    builder.createTopology()
  }
}
