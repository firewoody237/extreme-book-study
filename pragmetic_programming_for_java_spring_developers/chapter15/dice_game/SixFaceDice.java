package chapter15.dice_game;

import java.util.Random;

public class SixFaceDice implements Dice{
    private final Random random = new Random();
    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
