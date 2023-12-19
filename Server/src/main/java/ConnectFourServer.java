import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ConnectFourServer extends Application {
    static Text numClientsConnected = new Text("Number of Clients: 0");
    static TextField portField;

    public static void main(String[] args) {
        launch(args);
    }
    Server serverConnection;
    ListView<String> serverLog;
    CFourInfo cFourInfo;

    // Start menu
    Button startButton;
    VBox startMenu;
    StackPane stackPane;

    EventHandler<ActionEvent> startServer = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            stackPane.getChildren().remove(1);
            serverConnection = new Server(info -> {
                Platform.runLater(() -> {
                    cFourInfo = (CFourInfo) info;
                    if(cFourInfo.p1Played.equals("again")){ // player 1 is playing again!
                        serverLog.getItems().add(" ** Player 1 is playing again!");
                    }
                     else if(cFourInfo.p2Played.equals("again")){ // player 1 is playing again!
                        serverLog.getItems().add(" ** Player 2 is playing again!");
                    }
                     else {
                        if (!cFourInfo.p1Played.isEmpty()) {
                            serverLog.getItems().add("Player 1 Played: " + cFourInfo.p1Played);
                        }
                        if (!cFourInfo.p2Played.isEmpty()) {
                            serverLog.getItems().add("Player 2 Played: " + cFourInfo.p2Played);
                        }
                        if(cFourInfo.playerWon.equals("1")){ // player 1 WON!
                            serverLog.getItems().add("    PLAYER 1 WON THE GAME!");
                        }
                        if(cFourInfo.playerWon.equals("2")){ // player 2 WON!
                            serverLog.getItems().add("    PLAYER 2 WON THE GAME!");
                        }
                    }
                });
            });
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Server");
        serverLog = new ListView<String>();
        cFourInfo = new CFourInfo();

        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });


            numClientsConnected.setStyle("-fx-font-size: 20; -fx-font-weight: Bold; -fx-fill: Orange");
            numClientsConnected.setTextAlignment(TextAlignment.CENTER);
        VBox vBox = new VBox(serverLog, numClientsConnected);
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(20);
            vBox.setBackground(new Background(new BackgroundImage(new Image("background.jpg"),null, null, null, null)));
            vBox.setPadding(new Insets(20));


    // Start menu
        Text title = new Text("WELCOME TO CONNECT FOUR - SERVER");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: Bold; -fx-fill: Orange");
        Text portPromptText = new Text("    Enter a port number and then press the start button.");
        portPromptText.setStyle("-fx-font-size: 16; -fx-font-weight: Bold; -fx-fill: white");
        portField = new TextField();
            portField.setPromptText("5555");
        Button setPort = new Button("DEFAULT");
            EventHandler<ActionEvent> setPortNumber = actionEvent -> portField.setText("5555");
        setPort.setOnAction(setPortNumber);
        HBox portNumberHBox = new HBox(portField, setPort);
        portNumberHBox.setSpacing(40);

        startButton = new Button("Start the server");
            startButton.setOnAction(startServer);
        startMenu = new VBox(title, portPromptText, portNumberHBox, startButton);
        startMenu.setBackground(new Background(new BackgroundImage(new Image("background.jpg"),null, null, null, null)));
        startMenu.setAlignment(Pos.TOP_LEFT);
        startMenu.setPadding(new Insets(40));
        startMenu.setSpacing(20);

        stackPane = new StackPane(vBox, startMenu );

        Scene scene = new Scene(stackPane, 600, 600);
        scene.getRoot().setStyle("-fx-font-family: 'serif'");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
