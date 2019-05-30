package com.nenufarinformatica.modernartui;

import android.content.Context;
import android.graphics.Color;
import java.util.Random;

public class RandomColor {
    private int maxStep;
    private static int minDiference = 100; // min difference between R,G,B (avoid grey color)
    private static int minDistanceColor = 100; // min difference between colors
    private static Random mRandom = new Random();
    private int[] initialColorRgb;
    private int[] finalColorRgb;
    private int initialColor;
    private int finalColor;

    public RandomColor(Context context) {
        maxStep = context.getResources().getInteger(R.integer.maxStep);
        initialColorRgb = RandomColor.newColorRgb();
        finalColorRgb = RandomColor.newColorRgb();
        //don't want "same" color
        initialColor = Color.rgb(initialColorRgb[0], initialColorRgb[1], initialColorRgb[2]);
        finalColor = Color.rgb(finalColorRgb[0], finalColorRgb[1], finalColorRgb[2]);
        // I want to ensure that finalcolor is "distant" from initialcolor
        while (distance(
                initialColorRgb[0], initialColorRgb[1], initialColorRgb[2],
                finalColorRgb[0], finalColorRgb[1], finalColorRgb[2]
               ) < minDistanceColor) {
            finalColorRgb = RandomColor.newColorRgb();
            finalColor = Color.rgb(finalColorRgb[0], finalColorRgb[1], finalColorRgb[2]);
        }
    }

    public RandomColor(Context context, String serialized) {
        maxStep = context.getResources().getInteger(R.integer.maxStep);
        initialColorRgb = new int[3];
        finalColorRgb = new int[3];
        String[] retval = serialized.split(";");
        int initialSerializedColor = Integer.parseInt(retval[0]);
        int finalSerializedColor = Integer.parseInt(retval[1]);
        initialColorRgb[0] = Color.red(initialSerializedColor);
        initialColorRgb[1] = Color.green(initialSerializedColor);
        initialColorRgb[2] = Color.blue(initialSerializedColor);
        finalColorRgb[0] = Color.red(finalSerializedColor);
        finalColorRgb[1] = Color.green(finalSerializedColor);
        finalColorRgb[2] = Color.blue(finalSerializedColor);
        initialColor = Color.rgb(initialColorRgb[0], initialColorRgb[1], initialColorRgb[2]);
        finalColor = Color.rgb(finalColorRgb[0], finalColorRgb[1], finalColorRgb[2]);
    }

    public String serialized() {
        return Integer.toString(initialColor) + ";" + Integer.toString(finalColor);
    }

    public int actualColor(int step) {
        // step <= 0 -> initialColor
        // step >= maxStep -> finalColor
        int[] actualColorRgb = new int[3];
        if (step <= 0) {
            actualColorRgb = initialColorRgb;
        }
        else if (step >= maxStep) {
            actualColorRgb = finalColorRgb;
        }
        else {
            for (int i = 0; i < 3; i++) {
                actualColorRgb[i] = actualColorComponent(initialColorRgb[i], finalColorRgb[i], step);
            }
        }
        return Color.rgb(actualColorRgb[0], actualColorRgb[1], actualColorRgb[2]);
    }

    private int actualColorComponent(int begin, int end, int step) {
        double f = ((double)begin) + ((((double)end) - ((double)begin))/((double)maxStep))*((double)step);
        return ((int) f);
    }

    public static int[] newColorRgb() {
        int[] ret = {0,0,0};
        for(int i=0; i<3; i++) {
            ret[i] = mRandom.nextInt(256);
        }
        //dont want "grey" color
        while (
                (ret[0] <= ret[1]+minDiference) && (ret[0] >= ret[1]-minDiference) &&
                (ret[0] <= ret[2]+minDiference) && (ret[0] >= ret[2]-minDiference) &&
                (ret[1] <= ret[2]+minDiference) && (ret[1] >= ret[2]-minDiference)
        ) {
            ret[1] = mRandom.nextInt(256);
            ret[2] = mRandom.nextInt(256);
        }
        return ret;
    }

    private double distance(int r1, int g1, int b1, int r2, int g2, int b2) {
        return Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2));
    }
}
