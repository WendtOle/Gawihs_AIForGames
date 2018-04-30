import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

import java.io.File;
import java.io.IOException;

public class GawihsClient {
    public static void main(String[] args) throws IOException {
        Board board = new Board();
        NetworkClient client = new NetworkClient("localhost", "", ImageIO.read(new File("PizzaRick.jpg")));

        client.getMyPlayerNumber();

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();



        while(true) {
            Move move = client.receiveMove();
            if (move == null) {
                //ich bin dran
                client.sendMove(new Move(1,2, 3,5));
            } else
                System.out.println();
            //baue zug in meine spielfeldrepräsentation ein
        }
    }
}