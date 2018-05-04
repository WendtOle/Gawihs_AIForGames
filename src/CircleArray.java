import java.util.LinkedList;

public class CircleArray {
    private int value;
    private CircleArray nextElement;

    public CircleArray(int value,CircleArray nextElement){
        this.value = value;
        this.nextElement = nextElement;
    }

    public CircleArray(int value){
        this(value,null);
    }

    public int getValue(){
        return value;
    }

    public void setNextElement(CircleArray nextElement){
        this.nextElement = nextElement;
    }

    public CircleArray getNextElement() {
        return nextElement;
    }

    public CircleArray removeElementWithValue(int value){
        CircleArray previousElement = null;
        CircleArray currentElement = this;
        int currentValue = currentElement.value;
        while (currentValue != value | previousElement == null){
            previousElement = currentElement;
            currentElement = currentElement.nextElement;
            currentValue = currentElement.value;
        }
        previousElement.nextElement = currentElement.nextElement;
        return  previousElement.nextElement;
    }
}
