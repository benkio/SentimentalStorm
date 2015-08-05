package utils

import java.io.InputStream
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.Failure

class FileReader(filePath: String) {
  val words: Future[List[String]] = Future {
    val stream : InputStream = getClass.getResourceAsStream(filePath)
    val lines: List[String] = try scala.io.Source.fromInputStream( stream ).mkString split("\n") toList finally stream.close()
    lines.filter(l => !l.startsWith(";") && !l.isEmpty)
  }

  words onFailure {
    case t => println("An error has occured in the reading of file"+ filePath +" with error: " + t.getMessage)
  }
}
