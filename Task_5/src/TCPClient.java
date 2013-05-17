
import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("127.0.0.1",2048);
        OutputStreamWriter writer = new OutputStreamWriter(s.getOutputStream());
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        String line = "";
        do {
            out.print("Input  string:");
            out.flush();
            line = consoleReader.readLine();
            if (line.equalsIgnoreCase("exit")) break;
            writer.write(line+"\n");
            writer.flush();
            line = reader.readLine();
            out.println("Got answer:" + line);
            out.flush();
        } while (true);
        s.shutdownInput();
        s.shutdownOutput();


    }
}
