import lenz.htw.gawihs.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Board {
    int teamNumber;
    int [][] boardRepr = new int[9][9];

    // 1111 / 0xf   -> dont exist
    // 0000 / 0x0   -> empty
    // 0001 / 0x1   -> player one
    // 0010 / 0x2   -> player two
    // 0011 / 0x3   -> player three

    // extra 4 bits for potential movement options :
    // 0001 ... player one could go here
    // 0101 ... player three and player one could go here
    // ...

    Point[] neighbors = {
            new Point(0,1),new Point(1,1),
            new Point(-1,0),new Point(1,0),
            new Point(-1,-1),new Point(0,-1)
    };

    Point[][] teamPosition = {
            {new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0),new Point(4,0)},
            {new Point(0,4),new Point(1,5),new Point(2,6),new Point(3,7),new Point(4,8)},
            {new Point(8,4),new Point(8,5),new Point(8,6),new Point(8,7),new Point(8,8)}
    };

    public Board(int teamNumber){
        this.teamNumber = teamNumber;
        for (int[] row: boardRepr){
            Arrays.fill(row,0);
        }

        Point[] nonExistingFields = {
                new Point(0,8), new Point(1,8), new Point(2,8), new Point(3,8),
                new Point(0,7), new Point(1,7), new Point(2,7),
                new Point(0,6), new Point(1,6),
                new Point(0,5),
                new Point(8,3),
                new Point(7,2), new Point(8,2),
                new Point(6,1), new Point(7,1), new Point(8,1),
                new Point(5,0), new Point(6,0), new Point(7,0),new Point(8,0)};
        setField(nonExistingFields,0xff);

        for (int i = 0; i < teamPosition.length; i++) {
            setField(teamPosition[i],i + 1);
        }
    }

    private void setField(Point[]coordinates, int valueToPlace){
        for (Point coordinate :coordinates){
            boardRepr[coordinate.x][coordinate.y] = valueToPlace;
        }
    }

    public int[][] getPossibleNeighborFields(int[] coordinate){
        int[][] possibleNeighborFields = new int[6][2];
        int counter = 0;
        for (int[] neighborField :neighbors){
            int[] newCoordinate = {coordinate[0] + neighborField[0],coordinate[1] + neighborField[1]};
            if (isLegalField(newCoordinate[0],newCoordinate[1])){
                possibleNeighborFields[counter] = newCoordinate;
                counter ++;
            }
        }
        return Arrays.copyOfRange(possibleNeighborFields, 0, counter);
    }

    private boolean isLegalField(int x, int y){
        try {
            int valueOnBoard = boardRepr[x][y];
            if (valueOnBoard != 255 & x >= 0 & x <= 9 & y >= 0 & y <= 9){
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }
}
