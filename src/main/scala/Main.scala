/**
 * Created by benkio on 02/08/15.
  */
import utils._
object Main {
  def main(args: Array[String]) {
    val positivefile = new FileReader(Files.positiveWordsFile)
    println(positivefile.words)
    val negativefile = new FileReader(Files.negativeWordsFile)
    println(negativefile.words)
  }
}
