import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class GameButton extends Button {
    private final int x;
    private final int y;
    private int status;
    GameButton(int x, int y, EventHandler<ActionEvent> onclick){
        super();
        this.x = x;
        this.y = y;
        setStatus(0);
        this.setMinSize(70, 80);
        this.setOnAction(onclick);
        //this.setText("(" + x + "," + y + ")");
    }

    public int getX () {
        return x;
    }
    public int getY () {
        return y;
    }

    public void setStatus(int i){
        if(i == 0){
            this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            status = 0;
        }
        else if (i == 1) {
            this.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            status = 1;
        }
        else if (i == 2){
            this.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, CornerRadii.EMPTY, Insets.EMPTY)));
            status = 2;
        }
        else if (i == 3) { // player 1 won
            this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else  { // player 2 won
            this.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    public int getStatus() {
        return status;
    }
}
