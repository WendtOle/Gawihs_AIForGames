import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class BoardTest {

    private Board board;
    private Point startField;
    private Point[] expectedNeighbours;
    private String message;

    @Before
    public void before(){
        board = new Board(1);
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] {
                {"normal_emptyBoard",new Point(1,1),new Point[] {new Point(1,2),new Point(2,2),new Point(0,1),new Point(2,1),new Point(0,0),new Point(1,0)}},
                {"onEdge_emptyBoard",new Point(0,4),new Point[]{new Point(1,5),new Point(1,4),new Point(0,3)}},
                {"onEdge_emptyBoard",new Point(2,6),new Point[]{new Point(3,7),new Point(3,6),new Point(1,5),new Point(2,5)}},
                {"onEdge_emptyBoard",new Point(6,2),new Point[]{new Point(6,3),new Point(7,3),new Point(5,2),new Point(5,1)}},
                {"onEdge_emptyBoard",new Point(0,0),new Point[]{new Point(0,1),new Point(1,1),new Point(1,0)}},
                {"onEdge_emptyBoard",new Point(8,8),new Point[]{new Point(7,8),new Point(7,7),new Point(8,7)}}
        });
    }

    public BoardTest(String message, Point input, Point[] expected) {
        startField = input;
        expectedNeighbours = expected;
    }

    @Test
    public void test() {
        assertArrayEquals(message, expectedNeighbours, board.getPossibleMoveToPositionFrom(startField).toArray());
    }
}