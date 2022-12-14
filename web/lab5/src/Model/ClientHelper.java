package Model;

//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.logging.LogManager;
import java.util.logging.Logger;

class ClientHelper {
    private Socket socket;
    private BufferedReader in;

    private BufferedWriter out;
    private BufferedReader inputUser;

    private String name;
    static Logger logger = Logger.getLogger(ClientHelper.class.getName());

    public ClientHelper(String addr, int port) {
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.pressNickname();
            new ReadMsg().start();
        } catch (IOException e) {
            logger.info(e.getMessage());
            ClientHelper.this.downService();
        }
    }

    private void pressNickname() {
        System.out.print("Enter name: ");
        try {
            name = inputUser.readLine();
            out.write("Hi, " + name + "!\n");
            out.flush();
            logger.info("Entered name: " + name);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals("stop")) {
                        ClientHelper.this.downService();
                        break;
                    }
                    logger.info(name + " received: " + str);
                }
            } catch (IOException e) {
                logger.info(e.getMessage());
                ClientHelper.this.downService();
            }
        }
    }

}
