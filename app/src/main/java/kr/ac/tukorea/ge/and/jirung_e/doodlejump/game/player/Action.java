package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player;

//public enum Action {
//    LEFT,           // 0b00
//    RIGHT,          // 0b01
//    LEFT_CROUCH,    // 0b10 = 0b00(LEFT) | 0b10(CROUCH)
//    RIGHT_CROUCH,   // 0b11 = 0b01(RIGHT) | 0b10(CROUCH)
//    ATTACK,          // 0b100
//    ATTACK_CROUCH,    // 0b110 = 0b100(ATTACK) | 0b10(CROUCH)
//}

public class Action {
    public static final int LEFT = 0b000;
    public static final int RIGHT = 0b001;
    public static final int ATTACK = 0b010;
    public static final int CROUCH = 0b100;
    public static final int DIRECTION_MASK = 0b001;
}