defmodule EchoServer do
  require Logger

  def accept(port) do
    {:ok, socket} = :gen_tcp.listen(port,
      [:binary, packet: :line, active: false, reuseaddr: true])
    Logger.info "Accepting connections on port #{port}"
    loop_acceptor(socket)
  end

  defp loop_acceptor(socket) do
    {:ok, client} = :gen_tcp.accept(socket)
    Task.start_link(fn -> serve(client) end)
    loop_acceptor(socket)
  end

  defp serve(socket) do
    line = read_line(socket)
    take_prefix = fn full, prefix ->
    base = String.length(prefix)
    String.slice(String.trim(line), base..-1) |>  String.replace_trailing(" HTTP/1.1", "")
    end
    file_path = take_prefix.(line, "GET ")

    write_line(get_file(file_path), socket)
    :ok = :gen_tcp.close(socket)
  end

  defp read_line(socket) do
    {:ok, data} = :gen_tcp.recv(socket, 0)
    data
  end

  defp write_line(line, socket) do
    Logger.info line
    :gen_tcp.send(socket, line)
  end

  defp get_file(file_path) do
    try do
      File.read! "." <> file_path <> ".html"
    rescue
       _ in File.Error -> File.read! "./404.html"
    end
  end

  def main(args \\ []) do
    accept(9999)
  end

end
