import java.io.Serializable;

public class CFourInfo implements Serializable {
    private static final long SerialVersionUID = 101;
    public String p1Played = ""; // format: x y
    public String p2Played = ""; // format: x y
    public Boolean has2Players = false;
    public String playerTurn = "1";
    public String playerWon = "0";

    CFourInfo(){
        p1Played = ""; // format: x y
        p2Played = ""; // format: x y
        playerTurn = "1";
        has2Players = false;
    }
}
