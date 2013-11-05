package com.dumptruckman.bukkit.islandsgen;

public class LerpUtil {

    public static float lerp(float from, float to, float f) {
        return from + (to - from) * f;
    }

    public static double lerp(double from, double to, double f) {
        return from + (to - from) * f;
    }

    public static float lerpFactor(float min, float max, float value) {
        return (value - min) / (max - min);
    }
}
