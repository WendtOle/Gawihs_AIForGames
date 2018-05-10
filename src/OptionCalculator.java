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
        ArrayList<Move> moves = getAllPossibleMovements(teamNumber);
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

    public ArrayList<Move> getAllPossibleMovements(int teamNumber){
        int teamIndex = teamNumber - 1;
        Point[] teamStonePositions = master.teamPosition[teamIndex];

        //ich speicher zu jedem der 5 teamsteine die potentiellen nachbarn geordnet ab, aber nur falls sie frei sein sollten
        ArrayList<ArrayList<Point>> possibleMoveTOposition= new ArrayList();
        for (int stoneIndex = 0; stoneIndex < 5; stoneIndex++){
            Point currentStonePosition = teamStonePositions[stoneIndex];
            if (isStoneLayingFree(teamNumber, currentStonePosition))
                possibleMoveTOposition.add(stoneIndex,getLegallyAccebleNeighbours(currentStonePosition, teamNumber));
            else
                possibleMoveTOposition.add(stoneIndex,new ArrayList());
        }

        ArrayList<Move> possibleMovements = new ArrayList();

        for (int moveStoneIndex = 0; moveStoneIndex < 5; moveStoneIndex ++){
            Point currentStonePosition = teamStonePositions[moveStoneIndex];

            if (isStoneLayingFree(teamNumber, currentStonePosition)) {
                Set<Point> moveTOPositionsForCurrentStone = gatherAllAccessablesNeighboursButOwn(possibleMoveTOposition, moveStoneIndex);

                for (Point moveToPosition: moveTOPositionsForCurrentStone){
                    Move move = new Move(currentStonePosition.x,currentStonePosition.y,moveToPosition.x,moveToPosition.y);
                    possibleMovements.add(move);
                }
            }
        }
        return possibleMovements;
    }

    private int getCountOfAllPossibleMovementsForTeam(int teamNumber){
        return getAllPossibleMovements(teamNumber).size();
    }

    public int getBoardRatingForTeam(int teamNumber){
        int result = 0;

        for (int i = 1; i < 4; i++) {
            int currentResult = getCountOfAllPossibleMovementsForTeam(i);
            if (i == teamNumber)
                result += currentResult * 2;
            else
                result -= currentResult;
        }
        return result;
    }

    private Set<Point> gatherAllAccessablesNeighboursButOwn(ArrayList<ArrayList<Point>> possibleMoveTOposition, int moveStoneIndex) {
        Set<Point> moveTOPositionsForCurrentStone = new HashSet();
        for(int remainStoneIndex = 0; remainStoneIndex < 5; remainStoneIndex ++){
            if (remainStoneIndex != moveStoneIndex){
                moveTOPositionsForCurrentStone.addAll(possibleMoveTOposition.get(remainStoneIndex));
            }
        }
        return moveTOPositionsForCurrentStone;
    }

    private boolean isStoneLayingFree(int teamNumber, Point currentStonePosition) {
        return teamNumber == master.board.whichTeamIsOnTop(currentStonePosition);
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
