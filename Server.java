import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ramka r = new Ramka("Wątek do przerywania");
                r.setSize(new Dimension(750,200));
                r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                r.setVisible(true);
            }
        });
    }
}

class Ramka extends JFrame {
    public Ramka(String title) throws HeadlessException {
        super(title);
        this.add(new Panel());
    }
}

class Panel extends JComponent{
    private String ThreadsName = "";
    private String ThreadsState = "";
    private JLabel label = new JLabel("<html><h1><i>"+"Threads name: "+ThreadsName+"</i></h1><hr>"+"ThreadsState: "+ThreadsState+"</html>");
    private JButton startThread = new JButton("wystartuj watki");
    private JButton inetrruptedThread = new JButton("przerywa watek clienta");
    private JTextField poleCyferek = new JTextField(20);
    private Thread clientThread = null;
    private Thread serverThread = null;
    private Socket client = new Socket();
    private static Panel panel = null;

    public Panel() {
        panel = this;
        this.setLayout(new BorderLayout());
        this.add(label,BorderLayout.CENTER);
        label.setLayout(new FlowLayout(FlowLayout.LEFT));
        label.add(startThread); label.add(inetrruptedThread); label.add(poleCyferek);
        
        label.setText("<html><h1><i>"+"Threads name: "+ThreadsName+"</i></h1><hr>"+"ThreadsState: "+ThreadsState+"</html>");

        startThread.addActionListener((action)->{
            Runnable run = ()->{
                startClient(client);
            };

            clientThread = new Thread(run);
            clientThread.setName("clinet_Thread");
            clientThread.start();

            ThreadsName = clientThread.getName()+" - "+serverThread.getName();
            ThreadsState = clientThread.getState().name()+" - "+serverThread.getState().name();
            label.setText("<html><h1><i>"+"Threads name: "+ThreadsName+"</i></h1><hr>"+"ThreadsState: "+ThreadsState+"</html>");

        });

        Runnable runServer = ()-> {

            try {
                ServerSocket server = new ServerSocket();
                server.bind(new InetSocketAddress(InetAddress.getLocalHost(), 12345));
                System.out.println("waiting ...");
                Socket socket = server.accept();
                System.out.println("connecting ...");
                //IO server
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                //read and write server
                Scanner read = new Scanner(in);
                PrintWriter writer = new PrintWriter(out);
                //loop value send to client
                int x = 0;
                while (true) {
                    writer.println(String.valueOf(x));
                    writer.flush();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    x++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        serverThread = new Thread(runServer);
        serverThread.setName("server_Thread");
        serverThread.start();

        inetrruptedThread.addActionListener((action)->{
            try {
                client.shutdownOutput();
                client.shutdownInput();
                client.close();
                System.out.println("cl1 = "+clientThread.getState().name());
                clientThread.interrupt();
                System.out.println("clientThread is interrupted = "+Thread.interrupted());
                System.out.println("cl2 = "+clientThread.getState().name());

            } catch (IOException ioexc) {
                JOptionPane.showMessageDialog(null,"innterupted Exc: "+ioexc.getMessage());
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ThreadsName = clientThread.getName()+" - "+serverThread.getName();
            ThreadsState = clientThread.getState().name()+" - "+serverThread.getState().name();
            label.setText("<html><h1><i>"+"Threads name Client - Server: "+ThreadsName+"</i></h1><hr>"+"ThreadsState Client - Server: "+ThreadsState+"</html>");
        });
    }

    public void startClient(Socket client){
        try {
            client.connect(new InetSocketAddress(InetAddress.getLocalHost(),12345),2000);
            //IO client
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            //read and write client
            Scanner read = new Scanner(in);
            PrintWriter writer = new PrintWriter(out);

            while(read.hasNextLine()){
                String str = read.nextLine();
                poleCyferek.setText("");
                poleCyferek.setText(str);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"client message: "+e.getMessage());
        }
    }
}