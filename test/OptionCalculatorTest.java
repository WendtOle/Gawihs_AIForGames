import lenz.htw.gawihs.Move;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class OptionCalculatorTest {

    OptionCalculator optionCalculator = new OptionCalculator(new GameMaster(1));

    @Test
    public void test(){
        ArrayList<Move> moves01 = optionCalculator.gatherAllPossibleMovements(1);
        assertEquals(28,moves01.size());
        ArrayList<Move> moves02 = optionCalculator.gatherAllPossibleMovements(2);
        assertEquals(28,moves02.size());
        ArrayList<Move> moves03 = optionCalculator.gatherAllPossibleMovements(3);
        assertEquals(28,moves03.size());
    }

    @Test
    public void testOptionWithMoveBefore(){
        optionCalculator.master.performMove(new Move(0,0,1,1),1);

        ArrayList<Move> moves01 = optionCalculator.gatherAllPossibleMovements(1);
        assertEquals(31,moves01.size());
    }
}
