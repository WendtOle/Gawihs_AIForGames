import java.util.ArrayList;
import java.util.Arrays;

public class Board {
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

    int[][] neighbors = {
            {0,1},{1,1},
            {-1,0},{1,0},
            {-1,-1},{0,-1}
    };

    public Board(){
        for (int[] row: boardRepr){
            Arrays.fill(row,0);
        }

        int[][] nonExistingFields = {
                {0,8},{1,8},{2,8},{3,8},
                {0,7},{1,7},{2,7},
                {0,6},{1,6},
                {0,5},
                {8,3},
                {7,2},{8,2},
                {6,1},{7,1},{8,1},
                {5,0},{6,0},{7,0},{8,0}};
        setField(nonExistingFields,0xff);
        int[][] teamOnePlacement    = {{0,0},{1,0},{2,0},{3,0},{4,0}};
        setField(teamOnePlacement,0x10);
        int[][] teamTwoPlacement    = {{0,4},{1,5},{2,6},{3,7},{4,8}};
        setField(teamTwoPlacement,0x20);
        int[][] teamThreePlacement  = {{8,4},{8,5},{8,6},{8,7},{8,8}};
        setField(teamThreePlacement,0x30);

    }
    private void setField(int[][]coordinates, int valueToPlace){
        for (int[] coordinate :coordinates){
            boardRepr[coordinate[0]][coordinate[1]] = valueToPlace;
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
