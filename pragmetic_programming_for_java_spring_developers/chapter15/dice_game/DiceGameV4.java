package chapter15.dice_game;

import java.util.ArrayList;
import java.util.List;

public class DiceGameV4 {
    public GameResult play(Dice dice, int threshold) {
        int rollResult = dice.roll();
        if (rollResult > threshold) {
            return new GameResult(String.valueOf(rollResult), "당신이 이겼습니다.");
        } else {
            return new GameResult(String.valueOf(rollResult), "당신이 졌습니다.");
        }
    }
}
