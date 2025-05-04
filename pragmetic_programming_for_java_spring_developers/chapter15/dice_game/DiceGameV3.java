package chapter15.dice_game;

import java.util.ArrayList;
import java.util.List;

public class DiceGameV3 {
    public List<String> play(Dice dice, int threshold) {
        ArrayList<String> results = new ArrayList<>();
        int rollResult = dice.roll();
        results.add("주사위 숫자는 " + rollResult + "입니다.");
        if (rollResult > threshold) {
            results.add("당신이 이겼습니다.");
        } else {
            results.add("당신이 졌습니다.");
        }
        return results;
    }
}
