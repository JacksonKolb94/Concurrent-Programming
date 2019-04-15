import java.net._
import java.io._

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

object EchoServer {
  def props: Props = Props[EchoServer]
  final case class MySocket(socket: Socket)
}

class EchoServer extends Actor {
  import EchoServer._

  def read_and_write(in: BufferedReader, out:BufferedWriter): Unit = {
    out.write(in.readLine())
    out.flush()
    in.close()
    out.close()
  }

  def receive: PartialFunction[Any, Unit] = {
    case MySocket(s) =>
      val in = new BufferedReader(new InputStreamReader(s.getInputStream))
      val out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream))

      read_and_write(in, out)

      s.close()
  }
}

object AkkaEchoServer {
  import EchoServer._

  val system: ActorSystem = ActorSystem("AkkaEchoServer")
  val my_server: ActorRef = system.actorOf(EchoServer.props)

  def main(args: Array[String]) {
    val server = new ServerSocket(9999)
    while(true) {
      val s = server.accept()
      my_server ! MySocket(s)
    }
  }
}



