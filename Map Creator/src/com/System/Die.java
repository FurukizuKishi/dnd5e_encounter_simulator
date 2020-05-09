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
        return rollOut()[0];
    }
    public int[] rollOut() {
        int[] roll = new int[number + 1];
        int total = 0;
        for (int i = 1; i < roll.length; i += 1) {
            roll[i] = (int) Math.round(Math.random() * (sides - 1)) + 1;
            total += roll[i];
        }
        roll[0] = total;
        return roll;
    }

    public String toString() {
        if (sides > 1) {
            return number + "d" + sides;
        }
        return Integer.toString(number);
    }
}
