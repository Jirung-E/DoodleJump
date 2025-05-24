package kr.ac.tukorea.ge.and.jirung_e.doodlejump.game.player;

public enum Action {
    LEFT,           // 0b00
    RIGHT,          // 0b01
    LEFT_CROUCH,    // 0b10 = 0b00(LEFT) | 0b10(CROUCH)
    RIGHT_CROUCH,   // 0b11 = 0b01(RIGHT) | 0b10(CROUCH)
}