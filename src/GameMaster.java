import lenz.htw.gawihs.Move;

import java.awt.*;
import java.util.ArrayList;

public class GameMaster {
    int ownPlayerNumber;
    Board board;

    RoundRobin roundMeter;
    Point[][] teamPosition = {
            {new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0),new Point(4,0)},
            {new Point(0,4),new Point(1,5),new Point(2,6),new Point(3,7),new Point(4,8)},
            {new Point(8,4),new Point(8,5),new Point(8,6),new Point(8,7),new Point(8,8)}
    };

    public GameMaster(int ownPlayerIndex){
        ownPlayerNumber = ownPlayerIndex + 1;
        this.board = new Board();
        this.roundMeter = new RoundRobin();
        board.setPlayerOnBoard(teamPosition);
    }

    public GameMaster(int ownPlayherIndex, Board board, RoundRobin roundRobin, Point[][] teamPosition) {
        ownPlayerNumber = ownPlayherIndex + 1;
        this.board = board;
        this.roundMeter = roundRobin;
        this.teamPosition = teamPosition;
    }

    public void performMove(Move move){
        performMove(move,roundMeter.getValue());
    }

    public void performMove(Move move, int teamNumber){
        if (isLegalMove(move,teamNumber)) {
            board.removeUpperStoneFromField(new Point(move.fromX,move.fromY));
            board.placeStoneOneField(new Point(move.toX,move.toY),teamNumber);
            changeStonePositon(move,teamNumber);
        } else {
            removePlayer(teamNumber);
        }
    }

    public Point[][] cloneTeamposition() {
        Point[][] clone = new Point[teamPosition.length][teamPosition[0].length];

        for(int i=0; i < teamPosition.length; i++)
            for(int j=0; j < teamPosition[i].length; j++)
                clone[i][j] = new Point (teamPosition[i][j].x, teamPosition[i][j].y);
        return clone;
    }

    private void removePlayer(int teamNumber) {
        for (Point positionOfFieldToDestroy : teamPosition[teamNumber - 1]) {
            if (board.whichTeamIsOnTop(positionOfFieldToDestroy) == teamNumber) {
                if (board.isStillPlaceOnField(positionOfFieldToDestroy))
                    board.destroyField(positionOfFieldToDestroy);
            } else {
                board.removeLowerStoneFromField(positionOfFieldToDestroy);
            }
        }
        //roundMeter = roundMeter.removeElementWithValue(teamNumber);
        roundMeter.removeElementWithValue(teamNumber);
    }

    private boolean isLegalMove(Move move, int teamNumber){
        Point startPoint = new Point(move.fromX,move.fromY);
        Point endPoint = new Point(move.toX,move.toY);

        boolean isThereAMoveableStone = false;
        boolean isOtherStoneOnfEndOfMovementFieldNotOwnStone = false;

        try {
            isThereAMoveableStone = board.whichTeamIsOnTop(startPoint) > 0;
            isOtherStoneOnfEndOfMovementFieldNotOwnStone = board.whichTeamIsOnTop(endPoint) != teamNumber;
        } catch (IndexOutOfBoundsException e){}

        boolean isTherePlaceOnEndOfMovementField = board.isStillPlaceOnField(endPoint);
        boolean isOtherStoneReachableThroughNeigbour = isFieldReachableThroughNeighbour(startPoint,endPoint,teamNumber);

        return  isThereAMoveableStone &
                isTherePlaceOnEndOfMovementField &
                isOtherStoneOnfEndOfMovementFieldNotOwnStone &
                isOtherStoneReachableThroughNeigbour;

    }

    private boolean isFieldReachableThroughNeighbour(Point startPoint, Point endPoint, int teamNumber) {
        Point[] ownStones = teamPosition[teamNumber-1];
        for (Point stonePosition: ownStones){
            if (stonePosition != startPoint) {
                ArrayList<Point> neighborFields = getNeighbhrousOfStone(stonePosition);
                if (neighborFields.contains(endPoint)){
                    return true;
                };
            }
        }
        return false;
    }

    public ArrayList<Point> getNeighbhrousOfStone(Point stone){
        ArrayList<Point> neighbours = new ArrayList<>();
        for (Point neighborMutator :FixedValues.neighbors){
            Point neighbour = new Point(stone.x + neighborMutator.x,stone.y + neighborMutator.y);
            neighbours.add(neighbour);
        }
        return neighbours;
    }

    private void changeStonePositon(Move move, int team){
        for (int i = 0; i < 5; i++) {
            Point currentStonePos = teamPosition[team - 1][i];
            if (currentStonePos.x == move.fromX & currentStonePos.y == move.fromY) {
                teamPosition[team - 1][i].x = move.toX;
                teamPosition[team - 1][i].y = move.toY;
            }
        }
    }


    public void performIllegalMoveForNnextElementextTeamAndMoveOn() {
            int actualTeam = roundMeter.getValue();
            Point stoneFromActualTeam = teamPosition[actualTeam - 1][0];
            Move illegalMove = new Move(stoneFromActualTeam.x,stoneFromActualTeam.y,-1,-1);
            performMove(illegalMove,actualTeam);
    }

    public void nextPlayer() {
        roundMeter.next();
        //roundMeter = roundMeter.getNextElement();
    }
}
