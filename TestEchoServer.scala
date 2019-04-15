import java.io._
import java.net._

import org.scalatest._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class TestEchoServer extends FlatSpec with Matchers with MockitoSugar {
  "Bytes in" should "be bytes out" in {
    val socket = mock[Socket]
    val bytearrayinputstream = new ByteArrayInputStream("This is a test".getBytes())
    val bytearrayoutputstream = new ByteArrayOutputStream()

    when(socket.getInputStream).thenReturn(bytearrayinputstream)
    when(socket.getOutputStream).thenReturn(bytearrayoutputstream)

    EchoServer.serve(socket)

    bytearrayoutputstream.toString() should be("This is a test")
    verify(socket).close()
  }

  "Read and write" should "echo" in {
    val in = mock[BufferedReader]
    val out = mock[BufferedWriter]

    when(in.readLine()).thenReturn("This is a test")

    EchoServer.read_and_write(in, out)

    verify(out).write("This is a test")
    verify(out).flush()

  }

  "Read and write" should "do something" in {
    val in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("This is a test".getBytes())))
    val BAOS = new ByteArrayOutputStream()
    val out = new BufferedWriter(new OutputStreamWriter(BAOS))

    EchoServer.read_and_write(in, out)

    BAOS.toString shouldBe("This is a test")
  }
}
