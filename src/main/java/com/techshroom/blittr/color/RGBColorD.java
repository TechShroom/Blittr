package com.techshroom.blittr.color;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RGBColorD {

    private static final double INT_SCALE = 256d;

    public static RGBColorD of(int red, int green, int blue) {
        return of(red / INT_SCALE, green / INT_SCALE, blue / INT_SCALE);
    }

    public static RGBColorD of(double red, double green, double blue) {
        return new AutoValue_RGBColorD(red, green, blue);
    }

    RGBColorD() {
    }

    public abstract double getRed();

    public abstract double getGreen();

    public abstract double getBlue();

}
