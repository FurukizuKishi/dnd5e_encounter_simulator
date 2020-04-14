package com.System;

public class Die {
    private int number;
    private int sides;
    private int rolls = 0;

    public Die(int number, int sides) {
        this.number = number;
        this.sides = sides;
    }
    public Die(int number, int sides, int rolls) {
        this(number, sides);
        this.rolls = rolls;
    }
    public Die(String code) {
        this(Integer.parseInt(code.toLowerCase().split("d")[0]), Integer.parseInt(code.toLowerCase().split("d")[1]));
    }
    public Die(String code, int rolls) {
        this(code);
        this.rolls = rolls;
    }
    public Die(int value) {
        this(value, 1);
    }

    public int roll() {
        int roll = 0;
        for (int die : rollOut()) {
            roll += die;
        }
        return roll;
    }
    public int[] rollOut() {
        int[] roll = new int[sides];
        for (int i = 0; i < roll.length; i += 1) {
            roll[i] = (int) Math.round(Math.random() * (sides - 1)) + 1;
        }
        return roll;
    }

    public String toString() {
        if (sides > 1) {
            return number + "d" + sides;
        }
        return Integer.toString(number);
    }
}
