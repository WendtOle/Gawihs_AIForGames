import lenz.htw.gawihs.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OptionCalculator {

    GameMaster master;

    public OptionCalculator(GameMaster master){
        this.master = master;
    }

    public Move getRandomMovement(int teamNumber) {
        ArrayList<Move> moves = gatherAllPossibleMovements(teamNumber);
        Point randomStoneFromTeam = master.teamPosition[teamNumber - 1][0];
        Move nextMove = new Move(randomStoneFromTeam.x,randomStoneFromTeam.y,-1,-1);
        if (moves.size() > 0) {
            Random rand = new Random();
            nextMove = moves.get(rand.nextInt(moves.size()));
        }
        else {
            System.out.println();
        }
        return nextMove;
    }

    public ArrayList<Move> gatherAllPossibleMovements(int teamNumber){
        int teamIndex = teamNumber - 1;
        Point[] teamStonePositions = master.teamPosition[teamIndex];

        //ich speicher zu jedem der 5 teamsteine die potentiellen nachbarn geordnet ab, aber nur falls sie frei sein sollten
        ArrayList<ArrayList<Point>> possibleMoveToPosition= new ArrayList();
        for (int stoneIndex = 0; stoneIndex < 5; stoneIndex++){
            Point currentStonePosition = teamStonePositions[stoneIndex];
            if (teamNumber == master.board.whichTeamIsOnTop(currentStonePosition))
                possibleMoveToPosition.add(stoneIndex,getLegallyAccebleNeighbours(currentStonePosition, teamNumber));
            else
                possibleMoveToPosition.add(stoneIndex,new ArrayList());
        }

        ArrayList possibleMovements = new ArrayList();

        for (int moveStoneIndex = 0; moveStoneIndex < 5; moveStoneIndex ++){
            Point moveStonePosition = teamStonePositions[moveStoneIndex];

            if (teamNumber == master.board.whichTeamIsOnTop(moveStonePosition)) {
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
        }
        return possibleMovements;
    }

    private ArrayList<Point> getLegallyAccebleNeighbours(Point point,int teamNumber) {
        ArrayList<Point> allNeighbours = master.getNeighbhrousOfStone(point);
        ArrayList<Point> legallyAccebleNeighbours = new ArrayList<>();
        for (Point currentNeightbour:allNeighbours){
            boolean isStillPlaceOnField = master.board.isStillPlaceOnField(currentNeightbour);
            boolean isOtherStoneOnfEndOfMovementFieldNotOwnStone = false;
            try {
                isOtherStoneOnfEndOfMovementFieldNotOwnStone = master.board.whichTeamIsOnTop(currentNeightbour) != teamNumber;
            } catch(IndexOutOfBoundsException e){}
            if (isStillPlaceOnField & isOtherStoneOnfEndOfMovementFieldNotOwnStone)
                legallyAccebleNeighbours.add(currentNeightbour);
        }
        return legallyAccebleNeighbours;
    }

}
