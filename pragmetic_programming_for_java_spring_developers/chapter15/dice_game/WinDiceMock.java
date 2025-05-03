package chapter15.dice_game;

public class WinDiceMock implements Dice {
    @Override
    public int roll() {
        return 6;
    }
}
