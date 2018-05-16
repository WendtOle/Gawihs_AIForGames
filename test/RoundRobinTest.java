import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoundRobinTest {
    @Test
    public void test(){
        RoundRobin roundRobin = new RoundRobin();

        assertEquals(roundRobin.getValue(), 1);
        roundRobin.next();
        assertEquals(roundRobin.getValue(), 2);
        roundRobin.next();
        assertEquals(roundRobin.getValue(), 3);
        roundRobin.next();
        assertEquals(roundRobin.getValue(), 1);
        roundRobin.next();

        roundRobin.removeElementWithValue(2);
        assertEquals(roundRobin.getValue(), 3);
        roundRobin.next();
        assertEquals(roundRobin.getValue(), 1);

    }
}
