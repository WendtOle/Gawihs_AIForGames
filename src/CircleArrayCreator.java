public class CircleArrayCreator {
    public static CircleArray createCircleArray(int size, int valueToStart){
        CircleArray lastElement = new CircleArray(valueToStart + size - 1);
        CircleArray previousElement = lastElement;
        for (int i = size - 1; i > 0; i--){
            CircleArray currentElement = new CircleArray(i + valueToStart - 1, previousElement);
            previousElement = currentElement;
        }
        lastElement.setNextElement(previousElement);
        return previousElement;
    }
}
