import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class GameBoard extends GridPane {
    //boolean playerWon;
    ArrayList<ArrayList<GameButton>> board;

    ArrayList<GameButton> winningList;

    GameBoard(EventHandler<ActionEvent> onclick){
        super();

        winningList = new ArrayList<>();
        //playerWon = false;
        board = new ArrayList<ArrayList<GameButton>>(7);
        for (int i = 0; i < 7; i++) {
            board.add(new ArrayList<GameButton>(6));
        }
        for (int x = 0; x < 7; x++) {
            for ( int y = 0; y < 6; y++ ){
                GameButton button = new GameButton(x, y, onclick);
                board.get(x).add(button);
                //System.out.println(board.get(x) + ", ");
                this.add(button, x, y);
            }
        }
        this.setHgap(5);
        this.setVgap(5);
    }

    public int[] registerMove(int x, int y, int player) {
        while (y < 5 && board.get(x).get(y + 1).getStatus() == 0) {
            y++;
        }
        board.get(x).get(y).setStatus(player);
        return new int[]{ x, y};
    }

    public boolean didIWin(int x, int y, int player){
        int winnerNumber = 3;
        if(player == 2){
            winnerNumber = 4;
        }
        int upperLeftX = x;
        int upperLeftY = y ;

        int upperRightX = x ;
        int upperRightY = y;

        while (upperLeftX > 0 && upperLeftY > 0 ){
            upperLeftX--; // to the left
            upperLeftY--; // up
        }

        while (upperRightX < 6 && upperRightY > 0 ){
            upperRightX++; // to the right
            upperRightY--; // up
        }

        if(!checkLeftToRight(y,player)) {
            if (!checkTopToBottom(x,player)) {
                if (!checkUpperLeftToLowerRight(upperLeftX, upperLeftY, player)) {
                    if (!checkUpperRightToLowerLeft(upperRightX, upperRightY, player)) {
                        return false;
                    }
                }
            }
        }
        for (GameButton button : winningList) {
            button.setStatus(winnerNumber);
        }
        return true;
    }

    private boolean checkLeftToRight(int y, int player){
        int inARow = 0;
        for ( int x = 0; x <= 6; x++) {
            GameButton currButton = board.get(x).get(y);
            if(currButton.getStatus() == player) { // if this square is mine
                inARow++; // add in a row
                winningList.add(currButton);
            }
            else {
                inARow = 0; // reset in a row
                winningList.clear();

            }
            if (inARow == 4) {
                return true; // 4 in a row
            }
        }
        return false;
    }
    private boolean checkTopToBottom(int x, int player){
        int inARow = 0;
        for ( int y = 0; y < 6; y++) {
            GameButton currButton = board.get(x).get(y);
            if(currButton.getStatus() == player) { // if this square is mine
                inARow++; // add in a row
                winningList.add(currButton);
            }
            else {
                inARow = 0; // reset in a row
                winningList.clear();
            }
            if (inARow == 4) {
                return true; // 4 in a row
            }
        }
        return false;
    }
    private boolean checkUpperLeftToLowerRight(int x, int y, int player){
        int inARow = 0;
        while(x <= 6 && y <= 5 ) {
            GameButton currButton = board.get(x).get(y);
            if(currButton.getStatus() == player) { // if this square is mine
                inARow++; // add in a row
                winningList.add(currButton);
            }
            else {
                inARow = 0; // reset in a row
                winningList.clear();
            }
            if (inARow == 4) {
                return true; // 4 in a row
            }
            x++;
            y++;
        }
        return false;
    }
    private boolean checkUpperRightToLowerLeft(int x, int y, int player){
        int inARow = 0;
        while(x >= 0 && y <= 5 ) {
            GameButton currButton = board.get(x).get(y);
            if(currButton.getStatus() == player) { // if this square is mine
                inARow++; // add in a row
                winningList.add(currButton);
            }
            else {
                inARow = 0; // reset in a row
                winningList.clear();
            }
            if (inARow == 4) {
                return true; // 4 in a row
            }
            x--;
            y++;
        }
        return false;
    }




}
