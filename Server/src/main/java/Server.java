import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server {
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;

	
	Server(Consumer<Serializable> call){
		callback = call;
		server = new TheServer();
		server.start();
	}

	public class TheServer extends Thread{
		public void run() {
			try {
				ServerSocket mySocket = new ServerSocket(Integer.parseInt(ConnectFourServer.portField.getText()));
				System.out.println("Server is waiting for a client!");
				while(true) {
					if (clients.size() < 2) {
						ClientThread c = new ClientThread(mySocket.accept(), clients.size() + 1);
						clients.add(c);
						ConnectFourServer.numClientsConnected.setText("Number of Clients: " + clients.size());
						c.start(); // calls ClientThread run()
					}
				}
			}
			catch(Exception e) {
				System.out.println("Server socket did not launch");
			}
		}
	}//end of TheServer
	

	class ClientThread extends Thread{
		Socket connection;
		int playerNumber;
		ObjectInputStream in;
		ObjectOutputStream out;
		CFourInfo cFourInfo = new CFourInfo();

		ClientThread(Socket s, int count){
			this.connection = s;
			this.playerNumber = count;
		}

		public void updateClients() {

			for(int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				try {
					this.out.flush();
				 	t.out.writeObject(cFourInfo);
				}
				catch(Exception e) {}
			}
		}

		public void run(){
			try { 		// open stream
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
			}

			if(playerNumber == 1) {		// assign player number
				cFourInfo.p1Played = "set";
				cFourInfo.p2Played = "";
			}
			if(playerNumber == 2) {
				cFourInfo.p1Played = "";
				cFourInfo.p2Played = "set";
			}
			cFourInfo.has2Players = (clients.size() > 1);
			try {
				this.out.flush();
				this.out.writeObject(cFourInfo);
			} catch(Exception e) {}



			if (clients.size() >= 2) {
				cFourInfo.p1Played = ""; // Begin game when second player joins
				cFourInfo.p2Played = "";
				cFourInfo.has2Players = true;
				cFourInfo.playerWon = "0";
				updateClients();
			}


			 while(true) {
					try {
						cFourInfo = (CFourInfo) in.readObject();
						callback.accept(cFourInfo);
						if(cFourInfo.playerTurn.equals("1")){
							cFourInfo.playerTurn = "2";
						}
						else {
							cFourInfo.playerTurn = "1";
						}
						updateClients();
						}
					catch(Exception e) {
						clients.remove(this);
						ConnectFourServer.numClientsConnected.setText("Number of Clients: " + clients.size());
						cFourInfo.has2Players = true;
						updateClients();
						break;
					}
				}
			}//end of run


	}//end of client thread
}


	
	

	
