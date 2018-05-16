import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoundRobin {

    private List<Integer> items = new ArrayList<>();

    public RoundRobin(){
        for (int i = 1; i < 4; i++)
            items.add(i);
    }

    public RoundRobin(List<Integer> items) {
        this.items = items;
    }

    public void next() {
        Collections.rotate(items, -1);
    }

    public int getValue() {
        return items.get(0);
    }

    public void removeElementWithValue(int number) {
        items.remove(Integer.valueOf(number));
    }

    public RoundRobin clone() {
        List<Integer> clonedItems = new ArrayList<>();
        for (Integer item : items)
            clonedItems.add(item);
        return new RoundRobin(clonedItems);
    }
}

