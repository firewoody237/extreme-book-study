package chapter15.dice_game;

import java.util.ArrayList;
import java.util.List;

public class DiceGameV5 {
    public List<Event> play(Dice dice, int threshold) {
        ArrayList<Event> events = new ArrayList<>();
        int rollResult = dice.roll();
        events.add(new Event("주사위 결과", String.valueOf(rollResult)));
        if (rollResult > threshold) {
            events.add(new Event("게임 결과", "당신이 이겼습니다."));
        } else {
            events.add(new Event("게임 결과", "당신이 졌습니다."));
        }
        return events;
    }
}
