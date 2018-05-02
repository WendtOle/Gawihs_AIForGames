import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

import java.io.File;
import java.io.IOException;

public class GawihsClient {
    public static void main(String[] args) throws IOException {

        NetworkClient client = new NetworkClient("localhost", "", ImageIO.read(new File("PizzaRick.jpg")));

        Board board = new Board(client.getMyPlayerNumber());

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        while(true) {
            Move move = client.receiveMove();
            if (move == null) {
                //ich bin dran
                System.out.print("ich mach mein Move als " + client.getMyPlayerNumber() + ". Spieler");
                switch (client.getMyPlayerNumber()) {
                    case 0:
                        client.sendMove(new Move(0, 0, 1, 1));
                        break;
                    case 1:
                        client.sendMove(new Move(0, 4, 1, 4));
                        break;
                    case 2:
                        client.sendMove(new Move(8, 8, 7, 7));
                        break;

                }
            }
             else
                System.out.println(move.toString());
            //baue zug in meine spielfeldrepräsentation ein
        }
    }
}