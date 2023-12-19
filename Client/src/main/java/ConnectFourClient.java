import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.ListView;


public class ConnectFourClient extends Application {
    static int portNumber;
    static String ipAddress;

    public static void main(String[] args) {
        launch(args);
    }
    ListView<String> playerMoves;
    StackPane gamePane;
    Text yourMoveText;
    Text winnerOrLoserText;
    Client player;
    GameBoard gameBoard;
    boolean myTurn = false;
    boolean iWon = false;
    Button resultsButton;

    void changeScreens() {
        gamePane.getChildren().remove(gamePane.getChildren().size()-1); // remove play screen
    }

    EventHandler<ActionEvent> playerMove = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (myTurn) {
                GameButton button = (GameButton) actionEvent.getSource();
                if(button.getStatus() == 0) { // This spot is available
                    int[] coor = gameBoard.registerMove(button.getX(), button.getY(), player.playerNumber);
                    iWon = gameBoard.didIWin(coor[0],coor[1],player.playerNumber); // did I win
                    CFourInfo infoOut = new CFourInfo();
                    infoOut.has2Players = true;
                    if(player.playerNumber == 1){
                        infoOut.p1Played = coor[0] + " " + coor[1];
                        infoOut.p2Played = "";
                        infoOut.playerTurn = "1";
                        infoOut.playerWon = "0";
                        playerMoves.getItems().add("  Player 1 Played: " + infoOut.p1Played); // print my move
                        if(iWon) {
                            infoOut.playerWon = "1";
                            playerMoves.getItems().add("\n  PLAYER 1 WON THE GAME!"); // Player 1 won
                        }
                    }
                    else if(player.playerNumber == 2) {
                        infoOut.p1Played = "";
                        infoOut.playerTurn = "2";
                        infoOut.p2Played = coor[0] + " " + coor[1];
                        infoOut.playerWon = "0";
                        playerMoves.getItems().add("  Player 2 Played: " + infoOut.p2Played); // print my move
                        if(iWon) {
                            infoOut.playerWon = "2";
                            playerMoves.getItems().add("\n  PLAYER 2 WON THE GAME!"); // Player 2 won
                        }
                    }
                    player.send(infoOut);
                }

            }
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Player");
        gameBoard = new GameBoard(playerMove);

        player = new Client(data -> Platform.runLater(() -> {
            CFourInfo InfoIn = (CFourInfo) data; // cFourInfo will contain info from in.readObject()

            if(InfoIn.p1Played.equals("again") || InfoIn.p2Played.equals("again")) {
                return;
            }

            if(player.playerNumber == 1 && !InfoIn.p2Played.isEmpty()) {
                int x = Integer.parseInt(InfoIn.p2Played.substring(0, 1));
                int y = Integer.parseInt(InfoIn.p2Played.substring(2, 3));
                gameBoard.registerMove(x, y, 2);
                playerMoves.getItems().add("  Player 2 Played: " + InfoIn.p2Played); // print opposite players move

                if(InfoIn.playerWon.equals("2")) {
                    gameBoard.didIWin(x,y,2); // register win
                    playerMoves.getItems().add("\n  PLAYER 2 WON THE GAME!"); // Player 2 won
                    winnerOrLoserText.setText("UNFORTUNATELY, YOU LOST");
                }
            }

            if(player.playerNumber == 2 && !InfoIn.p1Played.isEmpty()) {
                int x = Integer.parseInt(InfoIn.p1Played.substring(0, 1));
                int y = Integer.parseInt(InfoIn.p1Played.substring(2, 3));
                gameBoard.registerMove(x, y, 1);
                playerMoves.getItems().add("  Player 1 Played: " + InfoIn.p1Played); // print opposite players move

                if(InfoIn.playerWon.equals("1")){
                    gameBoard.didIWin(x,y,1); // register win
                    playerMoves.getItems().add("\n  PLAYER 1 WON THE GAME!"); // Player 2 won
                    winnerOrLoserText.setText("UNFORTUNATELY, YOU LOST");
                }
            }

            int playerTurn = Integer.parseInt(InfoIn.playerTurn);
            myTurn = (playerTurn == player.playerNumber); // if the server called my number then it's my turn
            yourMoveText.setText(player.playerNumberString + ": NOT YOUR TURN");
            if (myTurn){
                yourMoveText.setText(player.playerNumberString + ": IT'S YOUR TURN");
            }

            if(!InfoIn.playerWon.equals("0")) { // we have a winner !
                resultsButton.setVisible(true);
                yourMoveText.setVisible(false);
                gameBoard.setDisable(true);
            }
        }));



        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });


// GAME FRAME
        playerMoves = new ListView<String>();
            playerMoves.setBackground(new Background(new BackgroundFill(Color.rgb(1,1,1,0.2),null,null)));
        HBox hBox = new HBox(gameBoard, playerMoves);
            hBox.setSpacing(20);
        yourMoveText = new Text("Waiting for opponent to connect...");
            yourMoveText.setStyle(" -fx-font-weight: Bold; -fx-fill: Orange");
        resultsButton = new Button("SEE RESULTS");
            resultsButton.setVisible(false);
            resultsButton.setOnAction(actionEvent -> { /// TODO: what else goes here.
                resultsButton.setVisible(false);
                yourMoveText.setVisible(true);
                changeScreens();
            });


        StackPane resultsButtonStack = new StackPane(yourMoveText, resultsButton);

        VBox gamePlayScreen = new VBox(hBox, resultsButtonStack);
            gamePlayScreen.setSpacing(20);
            gamePlayScreen.setPadding(new Insets(10));
            gamePlayScreen.setAlignment(Pos.TOP_CENTER);
            gamePlayScreen.setBackground(new Background(new BackgroundImage(new Image("background.jpg"),null, null, null, null)));


// GAME SETUP SCREEN

        Text title = new Text("WELCOME TO CONNECT FOUR - ONLINE");
            title.setStyle(" -fx-font-weight: Bold; -fx-fill: Orange; -fx-font-size: 20");
        Text instructions  = new Text("     Please enter an IP address and port number, then press submit.");
            instructions.setStyle(" -fx-font-weight: Bold; -fx-fill: White; -fx-font-size: 16");

        VBox titleBox = new VBox(title,instructions);
            titleBox.setSpacing(10);

        Text ipAddressTitle = new Text("IP Address:");
            ipAddressTitle.setStyle(" -fx-font-weight: Bold; -fx-fill: White; -fx-font-size: 16");
        TextField ipAddressField = new TextField();
            ipAddressField.setPromptText("127.0.0.1");
        Button defaultIP = new Button("DEFAULT");
            EventHandler<ActionEvent> setDefaultIP = actionEvent -> ipAddressField.setText("127.0.0.1");
            defaultIP.setOnAction(setDefaultIP);
        HBox ipAddressHBox = new HBox(ipAddressField, defaultIP);
            ipAddressHBox.setSpacing(40);

        Text portNumberTitle = new Text("Port Number:");
            portNumberTitle.setStyle(" -fx-font-weight: Bold; -fx-fill: White; -fx-font-size: 16");
        TextField portNumberField = new TextField();
        portNumberField.setPromptText("5555");
        Button defaultPort = new Button("DEFAULT");
            EventHandler<ActionEvent> setDefaultPort = actionEvent -> portNumberField.setText("5555");
            defaultPort.setOnAction(setDefaultPort);
        HBox portHBox = new HBox(portNumberField, defaultPort);
         portHBox.setSpacing(40);


        Button submitButton = new Button("SUBMIT");
            EventHandler<ActionEvent> setupServerConnection = actionEvent -> {
                portNumber = Integer.parseInt(portNumberField.getText());
                ipAddress = ipAddressField.getText();
                player.start();
                gamePane.getChildren().remove(2);
            };
            submitButton.setOnAction(setupServerConnection);
        VBox formBox = new VBox(ipAddressTitle, ipAddressHBox, portNumberTitle, portHBox);
            formBox.setSpacing(10);

        VBox gameSetupScreen = new VBox(titleBox, formBox, submitButton);
            gameSetupScreen.setSpacing(30);
            gameSetupScreen.setPadding(new Insets(40));
            gameSetupScreen.setAlignment(Pos.TOP_LEFT);
        gameSetupScreen.setBackground(new Background(new BackgroundImage(new Image("background.jpg"),null, null, null, null)));


// GAME OVER SCREEN
        winnerOrLoserText = new Text("YOU WON!");
            winnerOrLoserText.setStyle(" -fx-font-weight: Bold; -fx-fill: Orange; -fx-font-size: 20");
        Button playAgainButton = new Button("PLAY AGAIN");
            EventHandler<ActionEvent> playAgain = actionEvent -> {
                gameBoard = new GameBoard(playerMove);
                hBox.getChildren().clear();
                hBox.getChildren().add(gameBoard);
                hBox.getChildren().add(playerMoves);
                playerMoves.getItems().clear();
                iWon = false;
                gamePane.getChildren().add(gamePlayScreen);
                winnerOrLoserText.setText("YOU WON!");
                gameBoard.setDisable(false);
                CFourInfo playAgainInfo = new CFourInfo();
                playAgainInfo.p1Played = ""; // format: x y
                playAgainInfo.p2Played = ""; // format: x y
                if(player.playerNumber == 1){
                    playAgainInfo.p1Played = "again"; // format: x y
                }
                if(player.playerNumber == 2){
                    playAgainInfo.p2Played = "again"; // format: x y
                }
                playAgainInfo.has2Players = true;
                player.send(playAgainInfo);
            };
            playAgainButton.setOnAction(playAgain);
        VBox gameOverScreen = new VBox(winnerOrLoserText, playAgainButton);
            gameOverScreen.setAlignment(Pos.CENTER);
            gameOverScreen.setBackground(new Background(new BackgroundImage(new Image("background.jpg"),null, null, null, null)));
            gameOverScreen.setSpacing(20);




        gamePane = new StackPane(gameOverScreen, gamePlayScreen, gameSetupScreen);
        Scene scene = new Scene(gamePane, 800,600);
        scene.getRoot().setStyle("-fx-font-family: 'serif'; -fx-font-size: 16px;");
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
