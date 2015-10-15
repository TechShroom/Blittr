package com.techshroom.blittr;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DrawTriangle {

    public abstract Point getPoint1();

    public abstract Point getPoint2();

    public abstract Point getPoint3();

}
