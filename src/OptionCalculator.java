import lenz.htw.gawihs.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class OptionCalculator {

    double[] ratingParamters;
    double posMovOwn,posMovEneme;

    public OptionCalculator(double[] ratingParamters) {
        this.ratingParamters = ratingParamters;
        this.posMovOwn = ratingParamters[0];
        this.posMovEneme = ratingParamters[1];
    }

    public Move getRandomMovement(int teamNumber, GameMaster master) {
        ArrayList<Move> moves = getAllPossibleMovements(teamNumber, master);
        Point randomStoneFromTeam = master.teamPosition[teamNumber - 1][0];
        Move nextMove = new Move(randomStoneFromTeam.x, randomStoneFromTeam.y, -1, -1);
        if (moves.size() > 0) {
            Random rand = new Random();
            nextMove = moves.get(rand.nextInt(moves.size()));
        } else {
            System.out.println();
        }
        return nextMove;
    }

    public ArrayList<Move> getAllPossibleMovements(int teamNumber, GameMaster master) {
        int teamIndex = teamNumber - 1;
        Point[] teamStonePositions = master.teamPosition[teamIndex];

        //ich speicher zu jedem der 5 teamsteine die potentiellen nachbarn geordnet ab, aber nur falls sie frei sein sollten
        ArrayList<ArrayList<Point>> possibleMoveTOposition = new ArrayList();
        for (int stoneIndex = 0; stoneIndex < 5; stoneIndex++) {
            Point currentStonePosition = teamStonePositions[stoneIndex];
            if (isStoneLayingFree(teamNumber, currentStonePosition, master))
                possibleMoveTOposition.add(stoneIndex, getLegallyAccebleNeighbours(currentStonePosition, teamNumber, master));
            else
                possibleMoveTOposition.add(stoneIndex, new ArrayList());
        }

        ArrayList<Move> possibleMovements = new ArrayList();

        for (int moveStoneIndex = 0; moveStoneIndex < 5; moveStoneIndex++) {
            Point currentStonePosition = teamStonePositions[moveStoneIndex];

            if (isStoneLayingFree(teamNumber, currentStonePosition, master)) {
                Set<Point> moveTOPositionsForCurrentStone = gatherAllAccessablesNeighboursButOwn(possibleMoveTOposition, moveStoneIndex);

                for (Point moveToPosition : moveTOPositionsForCurrentStone) {
                    Move move = new Move(currentStonePosition.x, currentStonePosition.y, moveToPosition.x, moveToPosition.y);
                    possibleMovements.add(move);
                }
            }
        }
        return possibleMovements;
    }

    //warum müssen wir den GameMaster übergeben?
    public double getBoardRatingForTeam(int teamNumber, GameMaster master) {
        double rating = 0.;
        rating += weightPossibleMovementPositions(teamNumber, master, rating);
        return rating;
    }

    private double weightPossibleMovementPositions(int teamNumber, GameMaster master, double rating) {
        for (int i = 1; i < 4; i++) {
            int currentResult = getAllPossibleMovements(i, master).size();
            if (i == teamNumber)
                rating += currentResult * posMovOwn;
            else
                rating -= currentResult * posMovEneme;
        }
        return rating;
    }

    private Set<Point> gatherAllAccessablesNeighboursButOwn(ArrayList<ArrayList<Point>> possibleMoveTOposition, int moveStoneIndex) {
        Set<Point> moveTOPositionsForCurrentStone = new HashSet();
        for (int remainStoneIndex = 0; remainStoneIndex < 5; remainStoneIndex++) {
            if (remainStoneIndex != moveStoneIndex) {
                moveTOPositionsForCurrentStone.addAll(possibleMoveTOposition.get(remainStoneIndex));
            }
        }
        return moveTOPositionsForCurrentStone;
    }

    private boolean isStoneLayingFree(int teamNumber, Point currentStonePosition, GameMaster master) {
        return teamNumber == master.board.whichTeamIsOnTop(currentStonePosition);
    }

    private ArrayList<Point> getLegallyAccebleNeighbours(Point point, int teamNumber, GameMaster master) {
        ArrayList<Point> allNeighbours = master.getNeighbhrousOfStone(point);
        ArrayList<Point> legallyAccebleNeighbours = new ArrayList<>();
        for (Point currentNeightbour : allNeighbours) {
            boolean isStillPlaceOnField = master.board.isStillPlaceOnField(currentNeightbour);
            boolean isOtherStoneOnfEndOfMovementFieldNotOwnStone = false;
            try {
                isOtherStoneOnfEndOfMovementFieldNotOwnStone = master.board.whichTeamIsOnTop(currentNeightbour) != teamNumber;
            } catch (IndexOutOfBoundsException e) {
            }
            if (isStillPlaceOnField & isOtherStoneOnfEndOfMovementFieldNotOwnStone)
                legallyAccebleNeighbours.add(currentNeightbour);
        }
        return legallyAccebleNeighbours;
    }

    public Move alphaBetaStartUp(GameMaster master, int depth, double alpha, double beta){
        ArrayList<Move> moves = this.getAllPossibleMovements(master.roundMeter.getValue(), master);
        Move bestMove = new Move(0,0,-1,-1);
        if (moves.isEmpty())
            return bestMove;

        double rating;

        for (Move currentMove : moves) {
            GameMaster tempGameMaster = new GameMaster(master.ownPlayerNumber-1, master.board.clone(), master.roundMeter.clone(), master.cloneTeamposition());

            tempGameMaster.performMove(currentMove); //perform move on board
            int currentTeam = master.roundMeter.getValue();
            tempGameMaster.nextPlayer();

            rating = alphaBeta(tempGameMaster,depth - 1, alpha, beta);

            if (currentTeam == master.ownPlayerNumber) { //if maximizingPlayer
                if(rating > alpha) {
                    alpha = rating;
                    bestMove = currentMove;
                }
            } else { //if minimizing player
                if(rating < beta) {
                    beta = rating;
                }
            }
            if (beta <= alpha) break;
        }
        return bestMove;

    }

    public double alphaBeta(GameMaster master, int depth, double alpha, double beta) {
        double rating;
        ArrayList<Move> moves = this.getAllPossibleMovements(master.roundMeter.getValue(), master);

        if (depth == 0 || moves.isEmpty()) { //if depth = 0 or node is a terminal node
            return getBoardRatingForTeam(master.ownPlayerNumber, master);
        }

        for (Move currentMove : moves) {
            GameMaster tempGameMaster = new GameMaster(master.ownPlayerNumber-1, master.board.clone(), master.roundMeter.clone(), master.cloneTeamposition());

            tempGameMaster.performMove(currentMove); //perform move on board
            int currentTeam = master.roundMeter.getValue();
            tempGameMaster.nextPlayer();

            rating = alphaBeta(tempGameMaster,depth - 1, alpha, beta);

            if (currentTeam == master.ownPlayerNumber) { //if maximizingPlayer
                if(rating > alpha) {
                    alpha = rating;
                }
            } else { //if minimizing player
                if(rating < beta) {
                    beta = rating;
                }
            }
            if (beta <= alpha) break;
        }
        return (master.roundMeter.getValue() == master.ownPlayerNumber) ? alpha : beta;

    }

}
