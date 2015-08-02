/**
 * Created by benkio on 02/08/15.
  */
import utils._
object Main {
  def main(args: Array[String]) {
    val positivefile = new fileReader(files.positiveWordsFile)
    println(positivefile.words)
    val negativefile = new fileReader(files.negativeWordsFile)
    println(negativefile.words)
  }
}
