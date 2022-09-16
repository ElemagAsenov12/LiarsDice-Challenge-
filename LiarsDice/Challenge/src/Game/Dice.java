package Game;

public class Dice implements RollableDice {
    private int currentFaceValue;

    public void setCurrentFaceValue(int currentFaceValue) {
        this.currentFaceValue = currentFaceValue;
    }

    public int getCurrentFaceValue() {
        return currentFaceValue;
    }

    @Override
    public void roll() {
        this.currentFaceValue = (int) Math.floor((Math.random() * 6) + 1);
    }
}
