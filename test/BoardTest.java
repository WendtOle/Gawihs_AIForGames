import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class BoardTest {

    private Board board;
    private int[] startField;
    private int[][] expectedNeighbours;
    private String message;

    @Before
    public void before(){
        board = new Board();
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][] {
                {"normal_emptyBoard",new int[]{1,1},new int[][]{{1,2},{2,2},{0,1},{2,1},{0,0},{1,0}}},
                {"onEdge_emptyBoard",new int[]{0,4},new int[][]{{1,5},{1,4},{0,3}}},
                {"onEdge_emptyBoard",new int[]{2,6},new int[][]{{3,7},{3,6},{1,5},{2,5}}},
                {"onEdge_emptyBoard",new int[]{6,2},new int[][]{{6,3},{7,3},{5,2},{5,1}}},
                {"onEdge_emptyBoard",new int[]{0,0},new int[][]{{0,1},{1,1},{1,0}}},
                {"onEdge_emptyBoard",new int[]{8,8},new int[][]{{7,8},{7,7},{8,7}}}
        });
    }

    public BoardTest(String message, int[] input, int[][] expected) {
        startField = input;
        expectedNeighbours = expected;
    }

    @Test
    public void test() {
        assertArrayEquals(message, expectedNeighbours, board.getPossibleNeighborFields(startField));
    }
}