import com.sun.corba.se.spi.activation.Server;
import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import jdk.internal.util.xml.impl.Input;

import javax.sound.sampled.Port;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.AbstractMap;

public class PortMapper extends Thread {
    TCPServer responsible;
    private Socket socket;
    public PortMapper(Socket socket,TCPServer r)throws IOException{
        super();
        this.socket = socket;
        this.responsible=r;
    }
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String line = in.readLine();
            System.out.println(line);
            String[] commands = line.split("\\s");

            switch (commands[0]){
                case "register":
                    out.println("doing the thing");
                    String name = commands[1];
                    String ip = commands[3];
                    String port = commands[2];
                    responsible.hashMap.put(name, port +  " " + ip);
                    System.out.println("made it");
                    break;
                case "get":
                    out.println(responsible.hashMap.get(commands[1]));
                    out.println("ran");
                    break;
                case "call":

                    String some[] = responsible.hashMap.get(commands[1]).split("\\s");
                    String x = some[0];
                    String y = some[1];
                    String message = commands[2];
                    Socket socket = new Socket(y.toString(), Integer.parseInt(x));
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    pr.println(message);
                    out.println(br.readLine());
                    break;
                case "test":
                    out.println("something");
                    break;

            }
        } catch (IOException e1) {
            // do nothing
        }

        try {
            socket.close();
        } catch (IOException e) {
            // do nothing
        }
    }
    public static void main(String[] args) throws IOException {


    }
}