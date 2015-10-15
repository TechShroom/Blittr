package com.techshroom.blittr;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DrawTriangle {

    public static DrawTriangle of(RenderPoint p1, RenderPoint p2,
            RenderPoint p3) {
        return new AutoValue_DrawTriangle(p1, p2, p3);
    }

    DrawTriangle() {
    }

    public abstract RenderPoint getPoint1();

    public abstract RenderPoint getPoint2();

    public abstract RenderPoint getPoint3();

}
