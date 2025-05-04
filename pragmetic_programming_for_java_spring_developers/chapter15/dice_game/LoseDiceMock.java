package chapter15.dice_game;

public class LoseDiceMock implements Dice {
    @Override
    public int roll() {
        return 1;
    }
}
