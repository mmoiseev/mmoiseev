import java.net.*;
import java.io.*;

public class HTTPServer {
    public static void main(String[] args) throws IOException {
        //Создаем новый серверный Socket на порту 2048
        ServerSocket s = new ServerSocket(2048);
        while (true)
        {
            Socket clientSocket = s.accept(); //принимаем соединение
            System.out.println("Recieved connect from:"+clientSocket.getInetAddress()
                    +":"+clientSocket.getPort());
            //Создаем и запускаем поток для обработки запроса
            Thread t = new Thread(new RequestProcessor(clientSocket));
            System.out.println("Starting processor...");
            t.start();
        }
    }
}

class RequestProcessor implements Runnable
{
    Socket s; //Точка установленного соединения
    RequestProcessor(Socket s)  {
        this.s = s;
    }
    public void run()  {
        try  {
            InputStream input = s.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = s.getOutputStream();
            //s.close();
            OutputStreamWriter writer = new OutputStreamWriter(output);
            String line = "";
            while ( true ) {
                System.out.println("thread: "+Thread.currentThread());
                System.out.println("Waiting for string...");
                line = reader.readLine();
                if (line == null) break;
                System.out.println("Got the string: "+line+" from "+s.getInetAddress()+" : "+s.getPort());
                writer.write("answer from:"+s.getLocalAddress()+":"+s.getLocalPort()
                        +" for:"+s.getInetAddress()+":"+s.getPort()+" : "+line+"\n");
                writer.flush();
            }
            writer.close();
            System.out.println("Processor finished.");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
