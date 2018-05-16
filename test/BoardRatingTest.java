import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BoardRatingTest {
    @Test
    public void testRatingWithStartConfiguration(){
        GameMaster master = new GameMaster(1, new Board(), new RoundRobin());
        OptionCalculator calculator = new OptionCalculator(master);
        assertEquals(0,calculator.getBoardRatingForTeam(1));
    }
}
