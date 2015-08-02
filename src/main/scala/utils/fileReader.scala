package utils
import java.io.InputStream
class fileReader(filePath: String) {
  val stream : InputStream = getClass.getResourceAsStream(filePath)
  val lines: List[String] = try scala.io.Source.fromInputStream( stream ).mkString split("\n") toList finally stream.close()
  val words: List[String] = lines.filter(l => !l.startsWith(";") && !l.isEmpty)
}
