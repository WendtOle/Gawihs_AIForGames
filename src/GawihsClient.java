import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

//da wo man hinsetzt sollten genug umgebungssteine stehen, wenn man sich auf einen platz mit keiner umgebung stellt dann ist das eher schlecht

public class GawihsClient{
    public static void main(String[] args) throws IOException {

        for(int i = 0; i < args.length; i++)
            System.out.print(args[i] + " ");
        System.out.print("\n");

        NetworkClient client = new NetworkClient("localhost", "", ImageIO.read(new File("PizzaRick.jpg")));

        GameMaster master = new GameMaster(client.getMyPlayerNumber());
        System.out.println(client.getMyPlayerNumber());

        double[] arguments = Arrays.stream(args)
                .mapToDouble(Double::parseDouble)
                .toArray();

        OptionCalculator optionCalculator = new OptionCalculator(arguments);

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        TimeObserver timeObserver = new TimeObserver(TimeObserver.Output.BASIC);;

        int turnCounter = -1;

        while(true) {
            try {
                Move move = client.receiveMove();

                if (move == null) {
                    if (master.roundMeter.getValue() != master.ownPlayerNumber){
                        master.performIllegalMoveForNnextElementextTeamAndMoveOn();
                    }

                    Move nextMove;
                    timeObserver.init();

                    GameMaster tempGameMaster = new GameMaster(master.ownPlayerNumber - 1, master.board.clone(), master.roundMeter.clone(), master.cloneTeamposition());
                    nextMove = optionCalculator.alphaBetaStartUp(tempGameMaster, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);

                    timeObserver.addMoveTime();

                    client.sendMove(nextMove);
                    turnCounter ++;
                }
                 else {
                    if (master.roundMeter.getValue() != master.board.whichTeamIsOnTop(new Point(move.fromX,move.fromY))){
                        master.performIllegalMoveForNnextElementextTeamAndMoveOn();
                    }
                    master.performMove(move);
                    master.nextPlayer();
                }
            } catch(RuntimeException e) {
                break;
            }
        }
        timeObserver.printDuration();
        System.out.println(turnCounter);

    }
}