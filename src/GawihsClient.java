import javax.imageio.ImageIO;

import lenz.htw.gawihs.Move;
import lenz.htw.gawihs.net.NetworkClient;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.*;

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
                long startTime = System.currentTimeMillis();

                if (move == null) {
                    if (master.roundMeter.getValue() != master.ownPlayerNumber){
                        master.performIllegalMoveForNnextElementextTeamAndMoveOn();
                    }

                    Move nextMove;
                    timeObserver.init();

                    GameMaster tempGameMaster = new GameMaster(master.ownPlayerNumber - 1, master.board.clone(), master.roundMeter.clone(), master.cloneTeamposition());

                    Callable<Move> task4 = () -> {
                        return optionCalculator.alphaBetaStartUp(tempGameMaster, 5, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    };

                    Callable<Move> task3 = () -> {
                        if(Thread.currentThread().isInterrupted())
                            throw new RuntimeException();
                        return optionCalculator.alphaBetaStartUp(tempGameMaster, 4, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    };

                    Callable<Move> task2 = () -> {
                        if(Thread.currentThread().isInterrupted())
                            throw new RuntimeException();
                        return optionCalculator.alphaBetaStartUp(tempGameMaster, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    };

                    ExecutorService executor = Executors.newFixedThreadPool(3);
                    Future<Move> futureDepth4 = executor.submit(task4);
                    Future<Move> futureDepth3 = executor.submit(task3);
                    Future<Move> futureDepth2 = executor.submit(task2);

                    nextMove = optionCalculator.getRandomMovement(master.ownPlayerNumber, master);

                    String output = "random";
                    while(System.currentTimeMillis() - startTime <= 4800 - client.getExpectedNetworkLatencyInMilliseconds()) {
                        // still time left
                        if (futureDepth2.isDone()){
                            nextMove = futureDepth2.get();
                            output = "depth 3";
                        }
                        if (futureDepth3.isDone()) {
                            nextMove = futureDepth3.get();
                            output = "depth 4";
                        }
                        if (futureDepth4.isDone()) {
                            nextMove = futureDepth4.get();
                            output = "depth 5";
                        }
                    }
                    client.sendMove(nextMove);
                    System.out.println(output);

                    executor.shutdownNow();
                    futureDepth2.get();
                    futureDepth3.get();
                    futureDepth4.get();

                    timeObserver.addMoveTime();
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
        timeObserver.printDuration();
        System.out.println(turnCounter);

    }
}