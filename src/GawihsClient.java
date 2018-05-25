import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

//da wo man hinsetzt sollten genug umgebungssteine stehen, wenn man sich auf einen platz mit keiner umgebung stellt dann ist das eher schlecht

public class GawihsClient{
    public static void main(String[] args) throws IOException {

        for(int i = 0; i < args.length; i++)
            System.out.print(args[i] + " ");
        System.out.print("\n");

        int timeLimitInMs = Integer.parseInt(args[4]);
        String hostname = args[5];

        String[] arguments_ = Arrays.copyOfRange(args,0,4);

        double[] arguments = Arrays.stream(arguments_)
                .mapToDouble(Double::parseDouble)
                .toArray();

        String[] depths_ = Arrays.copyOfRange(args,6,args.length);

        int[] depths = Arrays.stream(depths_)
                .mapToInt(Integer::parseInt)
                .toArray();

        NetworkClient client = new NetworkClient(hostname, "Cadwaladr ap Rhys Trefnant", ImageIO.read(new File("img.jpg")));

        GameMaster master = new GameMaster(client.getMyPlayerNumber());
        System.out.println(client.getMyPlayerNumber());

        OptionCalculator optionCalculator = new OptionCalculator(arguments);

        client.getTimeLimitInSeconds();

        client.getExpectedNetworkLatencyInMilliseconds();

        int turnCounter = -1;

        while(true) {
            try {
                Move move = client.receiveMove();
                long startTime = System.currentTimeMillis();

                if (move == null) {
                    if (master.roundMeter.getValue() != master.ownPlayerNumber){
                        master.performIllegalMoveForNnextElementextTeamAndMoveOn();
                    }

                    Move nextMove = optionCalculator.getRandomMovement(master.ownPlayerNumber, master);

                    List<Callable<Move>> callList = new ArrayList<Callable<Move>>();

                    for (int currentDepth : depths) {
                        callList.add( () -> {
                            GameMaster tempGameMaster = new GameMaster(master.ownPlayerNumber - 1, master.board.clone(), master.roundMeter.clone(), master.cloneTeamposition());
                            return optionCalculator.alphaBetaStartUp(tempGameMaster, currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        });
                    }

                    ExecutorService executor = Executors.newFixedThreadPool(3);
                    System.out.print(System.currentTimeMillis() - startTime + " ");
                    List<Future<Move>> futures = executor.invokeAll(callList,timeLimitInMs,TimeUnit.MILLISECONDS);

                    System.out.print("\rreached only random" + "\r");
                    for (int j = 0; j < futures.size(); j++) {
                        Future<Move> future = futures.get(j);
                        if (future.isCancelled()) {
                            break;
                        }
                        nextMove = future.get();
                        System.out.print("\rreached depth: " + depths[j] + "\r");
                    }
                    System.out.println();


                    client.sendMove(nextMove);
                    executor.shutdownNow();

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(turnCounter);
    }
}