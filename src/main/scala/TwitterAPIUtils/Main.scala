import TwitterAPIUtils._
import main.scala.TwitterAPIUtils.TwitterAPIConnector
/**
 * Created by benkio on 01/08/15.
 */
object Main {
  def main (args: Array[String]) {
    val connector = new TwitterAPIConnector(keys.APIKey,keys.APISecret,keys.Token,keys.TokenSecret)
    connector.connectClient
    for ( i <- 1 to 5)
      println(connector.popMessage)
    connector.stopClient
  }
}
