import lenz.htw.gawihs.Move;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GameMasterTest {

    GameMaster master;

    @Before
    public void before(){
        //Board board = new Board();
        master = new GameMaster(1);
    }

    @Test
    public void testGetCurrentTeamNumber(){
        assertEquals(1,master.getCurrentTeamNumber());
        assertEquals(2,master.getCurrentTeamNumber());
        assertEquals(3,master.getCurrentTeamNumber());
        assertEquals(1,master.getCurrentTeamNumber());
    }

    @Test
    public void testRemovePlayer01(){
        master.removePlayerFromGame(1);
        assertEquals(2,master.getCurrentTeamNumber());
        assertEquals(3,master.getCurrentTeamNumber());
        assertEquals(2,master.getCurrentTeamNumber());
    }

    @Test
    public void testRemovePlayer02(){
        assertEquals(1,master.getCurrentTeamNumber());
        master.removePlayerFromGame(2);
        assertEquals(3,master.getCurrentTeamNumber());
        assertEquals(1,master.getCurrentTeamNumber());
    }

    @Test
    public void testPerformLegalMove(){
        master.performMove(new Move(0,0,1,1),1);
        assertFalse(master.board.isExitingField(new Point(0,0)));
        assertEquals(1,master.board.whichTeamIsOnTop(new Point(1,1)));
    }

    @Test
    public void testPerformIllegalMove(){
        int teamNumber = 1;
        int teamIndex = 0;
        master.performMove(new Move(0,0,1,2),teamNumber);
        assertTrue(master.board.isEmtpy(new Point(1,2)));
        for(Point stonePos:master.teamPosition[teamIndex]){
            assertFalse(master.board.isExitingField(stonePos));
        }
        assertEquals(2,master.getCurrentTeamNumber());
        assertEquals(3,master.getCurrentTeamNumber());
        assertEquals(2,master.getCurrentTeamNumber());
    }

    @Test
    public void testPerformIllegalMoveWithStapleLowerWillBeRemoved(){
        //setup -> staple on 0 3 -> team 2 on top
        master.performMove(new Move(1,0,0,1),1);
        master.performMove(new Move(0,0,0,2),1);
        master.performMove(new Move(0,1,0,3),1);
        master.performMove(new Move(1,5,0,3),2);

        Point testPoint = new Point(0,3);
        assertFalse(master.board.isStillPlaceOnField(testPoint));
        assertEquals(2,master.board.whichTeamIsOnTop(testPoint));

        //illegal Move from team One
        master.performMove(new Move(-1,-1,-1,-1),1);

        assertTrue(master.board.isStillPlaceOnField(testPoint));
        assertEquals(2,master.board.whichTeamIsOnTop(testPoint));
        assertEquals(2,master.getCurrentTeamNumber());
    }

    @Test
    public void testPerformIllegalMoveWithStapleUpperWillBeStillBeThere(){
        //setup -> staple on 0 3 -> team 2 on top
        master.performMove(new Move(1,0,0,1),1);
        master.performMove(new Move(0,0,0,2),1);
        master.performMove(new Move(0,1,0,3),1);
        master.performMove(new Move(1,5,0,3),2);

        Point testPoint = new Point(0,3);
        assertFalse(master.board.isStillPlaceOnField(testPoint));
        assertEquals(2,master.board.whichTeamIsOnTop(testPoint));

        //illegal Move from team One
        master.performMove(new Move(-1,-1,-1,-1),2);

        assertFalse(master.board.isStillPlaceOnField(testPoint));
        assertEquals(2,master.board.whichTeamIsOnTop(testPoint));
        assertFalse(master.board.isExitingField(new Point(1,5)));
        assertFalse(master.board.isExitingField(new Point(2,6)));
        assertFalse(master.board.isExitingField(new Point(3,7)));
        assertFalse(master.board.isExitingField(new Point(4,8)));

        assertEquals(1,master.getCurrentTeamNumber());
        assertEquals(3,master.getCurrentTeamNumber());
    }

    @Test
    public void testWeirdBuck() {
        master.performMove(new Move(-1,-1,-1,-1));
        master.performMove(new Move(0,4,0,3));
        assertEquals(2,master.board.whichTeamIsOnTop(new Point(0,3)));
        assertFalse(master.board.isExitingField(new Point(0,0)));
        assertFalse(master.board.isExitingField(new Point(4,0)));
    }
}
