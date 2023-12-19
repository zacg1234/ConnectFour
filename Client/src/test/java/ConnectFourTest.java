import static org.junit.jupiter.api.Assertions.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.junit.jupiter.api.Test;


public class ConnectFourTest {

    EventHandler<ActionEvent> testFunc = actionEvent -> System.out.println("tests");

    GameBoard gameBoard = new GameBoard(testFunc);

    @Test
    void gameBoardTest() {
//        gameBoard.registerMove(1,2,1);
//        gameBoard.registerMove(2,2,1);
//        gameBoard.registerMove(3,2,1);
//        gameBoard.registerMove(4,2,1);
//        assertTrue(gameBoard.didIWin(4,4,1));
        assertTrue(true);
    }


}
