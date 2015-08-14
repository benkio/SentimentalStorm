package topology

import backtype.storm.generated.StormTopology
import backtype.storm.topology._
import bolt._
import spout.TwitterSampleSpout
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import backtype.storm.tuple.Fields

import utils._
/**
 * Created by benkio on 02/08/15.
 */
object TopologyBuilder {
  val builder = new TopologyBuilder()
  val keywordsFuture = new FileReader(Files.keywordsFile).words
  val negativeWordsFuture = new FileReader(Files.negativeWordsFile).words
  val positiveWordsFuture = new FileReader(Files.positiveWordsFile).words
  def buildOSSentimentalTopology(spoutHintParallelismExecutors: Int, boltHintParallelismExecutors: Int, spoutTasks: Int, boltTasks: Int): StormTopology = {
    val keywords = Await.result(keywordsFuture,Duration(5,SECONDS))
    val positiveWords = Await.result(positiveWordsFuture,Duration(5,SECONDS))
    val negativeWords = Await.result(negativeWordsFuture,Duration(5,SECONDS))
    builder.setSpout("OS",new TwitterSampleSpout(keywords) ,spoutHintParallelismExecutors).setNumTasks(spoutTasks)
    builder.setBolt("KeywordSplitter", new KeywordStreamBolt(keywords),boltHintParallelismExecutors).setNumTasks(boltTasks).shuffleGrouping("OS")
    builder.setBolt("Judge",new JudgeBolt(positiveWords,negativeWords), boltHintParallelismExecutors).setNumTasks(boltTasks).fieldsGrouping("KeywordSplitter", new Fields("keyword"))
    //builder.setBolt("OS Echo", new EchoBolt, boltHintParallelismExecutors)fieldsGrouping("KeywordSplitter", new Fields("keyword"))
    builder.setBolt("positiveCounter", new SentimentalCounter,boltHintParallelismExecutors).setNumTasks(boltTasks).fieldsGrouping("Judge","PositiveTweetStream", new Fields("keyword", "sentimentalWord"))
    builder.setBolt("negativeCounter", new SentimentalCounter,boltHintParallelismExecutors).setNumTasks(boltTasks).fieldsGrouping("Judge","NegativeTweetStream", new Fields("keyword", "sentimentalWord"))
    builder.setBolt("positiveStream Echo", new EchoBolt, boltHintParallelismExecutors).setNumTasks(boltTasks).shuffleGrouping("positiveCounter")
    builder.setBolt("negativeStream Echo", new EchoBolt, boltHintParallelismExecutors).setNumTasks(boltTasks).shuffleGrouping("negativeCounter")
    builder.createTopology()
  }
}
