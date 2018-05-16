import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

import java.io.File;
import java.io.IOException;

//da wo man hinsetzt sollten genug umgebungssteine stehen, wenn man sich auf einen platz mit keiner umgebung stellt dann ist das eher schlecht

public class GawihsClient{
    public static void main(String[] args) throws IOException {

        NetworkClient client = new NetworkClient("localhost", "", ImageIO.read(new File("PizzaRick.jpg")));

        GameMaster master = new GameMaster(client.getMyPlayerNumber());

        OptionCalculator optionCalculator = new OptionCalculator();

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        while(true) {
            Move move = client.receiveMove();

            if (move == null) {
                if (master.roundMeter.getValue() != master.ownPlayerNumber){
                    master.performIllegalMoveForNnextElementextTeamAndMoveOn();
                }

                Move nextMove;

                if (master.ownPlayerNumber==1) {
                    GameMaster tempGameMaster = new GameMaster(master.ownPlayerNumber - 1, master.board.clone(), master.roundMeter.clone(), master.cloneTeamposition());
                    nextMove = optionCalculator.alphabeta(tempGameMaster, 5, Integer.MIN_VALUE, Integer.MAX_VALUE).move;
                } else
                    nextMove = optionCalculator.getRandomMovement(client.getMyPlayerNumber() + 1, master);

                client.sendMove(nextMove);
            }
             else {
                master.performMove(move);
                master.nextPlayer();
            }

        }
    }
}