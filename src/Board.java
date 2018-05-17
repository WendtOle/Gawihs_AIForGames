import java.awt.*;
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

    public Board(){
        for (int[] row: boardRepr){
            Arrays.fill(row,0);
        }
        setRow(FixedValues.nonExistingFields,0xff);
    }

    public Board(int [][] board) {
        this.boardRepr = board;
    }

    public void setPlayerOnBoard(Point[][] teamPosition){
        for (int i = 0; i < teamPosition.length; i++) {
            setRow(teamPosition[i],i + 1);
        }
    }

    private void setRow(Point[]coordinates, int valueToPlace){
        for (Point coordinate :coordinates){
            setField(coordinate,valueToPlace);
        }
    }

    public void setField(Point point, int valueToPlace){
        boardRepr[point.x][point.y] = valueToPlace;
    }

    public int get(Point point) throws IndexOutOfBoundsException {
        return boardRepr[point.x][point.y];
    }

    public boolean isExitingField(Point x){
        try {
            int valueOnBoard = get(x);
            if (valueOnBoard != 255 & x.x >= 0 & x.x <= 9 & x.y >= 0 & x.y <= 9){
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    public boolean isEmtpy(Point point){
        try {
            return get(point) == 0;
        } catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    public void destroyField(Point point){
        int notExistingFieldIdentifier = 0xff;
        setField(point,notExistingFieldIdentifier);
    }

    public void placeStoneOneField(Point point, int team){
        boardRepr[point.x][point.y] = (boardRepr[point.x][point.y] << 4) | team;
    }

    public int whichTeamIsOnTop(Point point) throws IndexOutOfBoundsException {
        int upperStoneMask = 0xf;
        return get(point) & upperStoneMask;
    }

    public void removeUpperStoneFromField(Point point) {
        int newValue = get(point) >> 4;
        if (newValue == 0)
            destroyField(point);
        else
            setField(point,newValue);

    }

    public boolean isStillPlaceOnField(Point point) {
        try {
            return get(point) < 16;
        } catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    public void removeLowerStoneFromField(Point point){
        int newValue = get(point) & 0xf;
        setField(point, newValue);
    }

    public Board clone () {
        int[][] clone = new int[9][9];

        for(int i=0; i < boardRepr.length; i++)
            for(int j=0; j < boardRepr[i].length; j++)
                clone[i][j] = boardRepr[i][j];

        return new Board(clone);
    }
}
