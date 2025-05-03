package chapter15.dice_game;

public class DiceGameV2 {
    public void play(Dice dice, int threshold) {
        int rollResult = dice.roll();
        System.out.println("주사위 숫자는 " + rollResult + "입니다.");
        if (rollResult > threshold) {
            System.out.println("당신이 이겼습니다.");
        } else {
            System.out.println("당신이 졌습니다.");
        }
    }
}
