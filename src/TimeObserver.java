import java.util.ArrayList;

public class TimeObserver {

    public enum Output{
        ALL,BASIC,NOTHING
    }

    long startTime;
    ArrayList<Long> turnDurations = new ArrayList<>();
    Output output;

    public TimeObserver(Output output) {
        this.output = output;
    }

    public void init(){
        startTime = System.currentTimeMillis();
    }

    public void addMoveTime(){
        long end = System.currentTimeMillis();
        long curTurnDuration = end - startTime;
        turnDurations.add(curTurnDuration);
        double currentTurnDurationSeconds = curTurnDuration / 1000.;
        if (output == Output.ALL)
            System.out.println("cur: " + String.format("%.2f",currentTurnDurationSeconds) + " sec");
    }

    public void printDuration(){
        if (output != Output.NOTHING)
            System.out.println(turnDurations.toString());
    }
}
