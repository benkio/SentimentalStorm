/**
 * Created by benkio on 02/08/15.
 */

import backtype.storm.LocalCluster
import topology.TopologyBuilder
import backtype.storm.Config
object Main {
  def main(args: Array[String]) {

    val conf = new Config()
    conf.setDebug(false)

    val cluster: LocalCluster = new LocalCluster()
    cluster.submitTopology("OSEcho", conf, TopologyBuilder.buildOSSentimentalTopology(2,2,4,4))

    Thread.sleep(100000)
    cluster.shutdown()
  }
}
