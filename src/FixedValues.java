import java.awt.*;

public class FixedValues {

    static Point[] neighbors = {
            new Point(0,1),new Point(1,1),
            new Point(-1,0),new Point(1,0),
            new Point(-1,-1),new Point(0,-1)
    };

    static Point[] nonExistingFields = {
            new Point(0,8), new Point(1,8), new Point(2,8), new Point(3,8),
            new Point(0,7), new Point(1,7), new Point(2,7),
            new Point(0,6), new Point(1,6),
            new Point(0,5),
            new Point(4,4),
            new Point(8,3),
            new Point(7,2), new Point(8,2),
            new Point(6,1), new Point(7,1), new Point(8,1),
            new Point(5,0), new Point(6,0), new Point(7,0),new Point(8,0)};
}
