import javafx.scene.text.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;
    int playerNumber = 1;

    String playerNumberString;


    private Consumer<Serializable> callback;

    Client(Consumer<Serializable> call){
        callback = call;
    }

    public void run() {
        try {
            socketClient= new Socket(ConnectFourClient.ipAddress,ConnectFourClient.portNumber);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {}

        while(true) {
            try {
                CFourInfo cFourInfo = (CFourInfo) in.readObject();
                if (cFourInfo.p1Played.equals("set")) { // get assigned player number
                    playerNumber = 1;
                    playerNumberString = "Player 1";

                }
                else if (cFourInfo.p2Played.equals("set")) {
                    playerNumber = 2;
                    playerNumberString = "Player 2";
                }
                else {
                    callback.accept(cFourInfo); // gives info to accept() - defined in lambda in ConnectFourClient
                }
            }
            catch(Exception e) {}
        }
    }

    public void send(CFourInfo info) {
        try {
            out.flush();
            out.writeObject(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
