import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class CircleArrayTest {
    @Test
    public void testInitializedValues(){
        CircleArray circle = CircleArrayCreator.createCircleArray(3,1);

        assertEquals(1,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(2,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(3,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(1,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(2,circle.getValue());
    }

    @Test
    public void testRemoveElementFromArray01(){
        CircleArray circle = CircleArrayCreator.createCircleArray(3,1);
        circle = circle.removeElementWithValue(1);

        assertEquals(2,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(3,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(2,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(3,circle.getValue());
    }

    @Test
    public void testRemoveElementFromArray02(){
        CircleArray circle = CircleArrayCreator.createCircleArray(3,1);
        circle = circle.removeElementWithValue(2);

        assertEquals(3,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(1,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(3,circle.getValue());
    }

    @Test
    public void testRemoveElementFromArray03(){
        CircleArray circle = CircleArrayCreator.createCircleArray(3,1);
        circle = circle.removeElementWithValue(3);

        assertEquals(1,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(2,circle.getValue());
        circle = circle.getNextElement();
        assertEquals(1,circle.getValue());
    }
}
