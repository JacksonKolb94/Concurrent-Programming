import java.net._
import java.io._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object EchoServer {
  def read_and_write(in: BufferedReader, out:BufferedWriter): Unit = {
    out.write(in.readLine())
    out.flush()
  }

  def serve(s: Socket): Unit = {
      val in = new BufferedReader(new InputStreamReader(s.getInputStream))
      val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))

      read_and_write(in, out)

      in.close()
      out.close()
      s.close()
  }

  def main(args: Array[String]) {
    val server = new ServerSocket(9999)
    while(true) {
      val s = server.accept()
      Future { serve(s) }
    }
  }
}



