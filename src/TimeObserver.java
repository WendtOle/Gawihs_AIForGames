import java.util.ArrayList;

public class TimeObserver {
    long startTime;
    ArrayList<Long> turnDurations = new ArrayList<>();

    public void init(){
        startTime = System.currentTimeMillis();
    }

    public void addMoveTime(){
        long end = System.currentTimeMillis();
        long curTurnDuration = end - startTime;
        turnDurations.add(curTurnDuration);
        double currentTurnDurationSeconds = curTurnDuration / 1000.;
        System.out.println("cur: " + String.format("%.2f",currentTurnDurationSeconds) + " sec");
    }

    public void printDuration(){
        System.out.println(turnDurations.toString());
    }
}
