import java.net.*;
import java.io.*;

public class MiniHTTPD extends Thread {

	private Socket socket;

	private static String root;

	public static void main(String[] args) {
		try {
			ServerSocket socket = new ServerSocket(8080);
			root = args.length > 0 ? args[0] : "/http-root";
			System.out.println("MiniHTTPD started at http://localhost:8080/");
			System.out.println("Put your .html files in " + root);
			while (true) {
				MiniHTTPD miniHTTPD = new MiniHTTPD();
				miniHTTPD.socket = socket.accept();
				miniHTTPD.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			OutputStream out = socket.getOutputStream();
			String request = in.readLine();
			System.out.println(request);
			out.write(new String(
					"HTTP/1.0 200 OK\r\nContent-type: text/html\r\n\r\n")
					.getBytes());
			int i = request.indexOf("GET") + 4;
			String filename = request.substring(i, request.indexOf(" ", i));
			FileInputStream file = new FileInputStream(root
					+ (filename.equals("/") ? "/index.html" : filename));
			byte[] buffer = new byte[4096];
			while ((i = file.read(buffer)) > 0)
				out.write(buffer, 0, i);
			file.close();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
