import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BoardTest {

    private Board board;

    @Before
    public void before(){
        GameMaster master = new GameMaster(1);
        board = master.board;
    }

    @Test
    public void testBoundariesOfCreatedBoard() {
        for (Point nonExistingField: FixedValues.nonExistingFields){
            assertFalse(board.isExitingField(nonExistingField));
        }
    }

    @Test
    public void testComplexSzenarioOnOneField() {
        Point testingPoint = new Point(0,0);
        assertEquals(1,board.whichTeamIsOnTop(testingPoint));

        assertTrue(board.isStillPlaceOnField(testingPoint));

        board.placeStoneOneField(testingPoint,2);
        assertEquals(2,board.whichTeamIsOnTop(testingPoint));

        assertFalse(board.isStillPlaceOnField(testingPoint));

        board.removeLowerStoneFromField(testingPoint);
        assertEquals(2,board.whichTeamIsOnTop(testingPoint));
        assertTrue(board.isStillPlaceOnField(testingPoint));

        board.placeStoneOneField(testingPoint,3);
        assertEquals(3,board.whichTeamIsOnTop(testingPoint));
        assertFalse(board.isStillPlaceOnField(testingPoint));

        board.removeUpperStoneFromField(testingPoint);
        assertEquals(2,board.whichTeamIsOnTop(testingPoint));
        assertTrue(board.isStillPlaceOnField(testingPoint));
    }

    @Test
    public void testAutomticDescructionOfFieldWhenLastStoneIsRemoved(){
        Point testPoint = new Point(0,0);
        board.removeUpperStoneFromField(testPoint);
        assertFalse(board.isExitingField(testPoint));
    }

    @Test
    public void testIsFieldEmpty(){
        assertFalse(board.isEmtpy(new Point(0,0)));
        assertTrue(board.isEmtpy(new Point(0,1)));
    }

    @Test
    public void testIfThereIsPlaceOnNonExistingField() {
        assertFalse(board.isStillPlaceOnField(new Point(4,4)));
    }

    @Test
    public void testAccessOutOfBoundsExceptionPoint() {
        assertFalse(board.isStillPlaceOnField(new Point(-1,-1)));
        assertFalse(board.isExitingField(new Point(-1,-1)));
        assertFalse(board.isEmtpy(new Point(-1,-1)));
    }
}
