import lenz.htw.gawihs.Move;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BoardMovementTest {
    private Board board;
    private Move move;
    private int[] statesAfterMove;
    private String message;

    @Before
    public void before(){
        board = new Board(1);
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] {
                {"legalMove",new Move(0,0,1,1),new int[]{0,0x1}}
        });
    }

    public BoardMovementTest(String message, Move move, int[] statesAfterMove) {
        this.move = move;
        this.statesAfterMove = statesAfterMove;
    }

    @Test
    public void test() {
        board.performMove(move);
        assertEquals("from " + message, statesAfterMove[0],board.getStateOfBoard(new Point(move.fromX,move.fromY)));
        assertEquals("to " + message, statesAfterMove[1],board.getStateOfBoard(new Point(move.toX,move.toY)));

    }
}
