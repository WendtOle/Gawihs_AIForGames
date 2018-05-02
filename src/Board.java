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

    public ArrayList<Point> getPossibleMoveToPositionFrom(Point coordinate){
        ArrayList<Point> possibleNeighborFields = new ArrayList<>();
        int counter = 0;
        for (Point neighborField :neighbors){
            Point newCoordinate = new Point(coordinate.x + neighborField.x,coordinate.y + neighborField.y);
            if (isExitingField(newCoordinate)){
                if( isLegalMoveToField(newCoordinate)) {
                    possibleNeighborFields.add(newCoordinate);
                }
            }
        }
        return possibleNeighborFields;
    }

    private boolean isExitingField(Point x){
        try {
            int valueOnBoard = boardRepr[x.x][x.y];
            if (valueOnBoard != 255 & x.x >= 0 & x.x <= 9 & x.y >= 0 & x.y <= 9){
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    public boolean performMove(Move move){
        //could put in error catch thing when team is not own team or sth. like that
        int team = getTeamOfUppestStone(new Point(move.fromX,move.fromY));
        takeAwayUppestStone(new Point(move.fromX,move.fromY));
        placeStoneOnField(new Point(move.toX,move.toY),team);
        changeStonePositon(move,team);
        return true;
    }

    private void changeStonePositon(Move move, int team){
        for (Point stonePos : teamPosition[team - 1]){
            if (stonePos.x == move.fromX & stonePos.y == move.fromY){
                stonePos.x = move.toX;
                stonePos.y = move.toY;
            }
        }
    }

    public int getStateOfBoard(Point point) {
        return boardRepr[point.x][point.y];
    }

    private boolean isLegalMoveToField(Point point){
        int state = getStateOfBoard(point);
        if (state < 16 & getTeamOfUppestStone(point) != teamNumber)
            return true;
        else
            return false;
    }

    private int getTeamOfUppestStone(Point point) {
        return getStateOfBoard(point) & 0xf;
    }

    private void takeAwayUppestStone(Point point){
        boardRepr[point.x][point.y] = boardRepr[point.x][point.y] >> 4;
    }

    private void placeStoneOnField(Point point,int team){
        boardRepr[point.x][point.y] = (boardRepr[point.x][point.y] << 4) | team;
    }

    public ArrayList<Move> gatherAllPossibleMovements(int teamNumber){
        int teamIndex = teamNumber - 1;
        Point[] teamStonePositions = this.teamPosition[teamIndex];
        
        ArrayList<ArrayList<Point>> possibleMoveToPosition= new ArrayList();
        for (int stoneIndex = 0; stoneIndex < 5; stoneIndex++){
            Point currentStonePosition = teamStonePositions[stoneIndex];
            possibleMoveToPosition.add(stoneIndex,getPossibleMoveToPositionFrom(currentStonePosition));
        }
        
        ArrayList possibleMovements = new ArrayList();

        for (int moveStoneIndex = 0; moveStoneIndex < 5; moveStoneIndex ++){
            Point moveStonePosition = teamStonePositions[moveStoneIndex];

            Set<Point> moveToPositions = new HashSet();
            for(int remainStoneIndex = 0; remainStoneIndex < 5; remainStoneIndex ++){
                if (remainStoneIndex != moveStoneIndex){
                   moveToPositions.addAll(possibleMoveToPosition.get(remainStoneIndex));
                }
            }

            for (Point moveToPosition: moveToPositions){
                possibleMovements.add(new Move(moveStonePosition.x,moveStonePosition.y,moveToPosition.x,moveToPosition.y));
            }
        }
        return possibleMovements;
    }
}
