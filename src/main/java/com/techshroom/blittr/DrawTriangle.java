package com.techshroom.blittr;

import com.google.auto.value.AutoValue;

/**
 * Represents one triangle to be drawn in OpenGL's {@code GL_TRIANGLE} mode.
 * Shapes are made of multiple draw triangles.
 * 
 * N.B.: Don't use this class outside of the render system! This class is only
 * for building more complex shapes. Use {@link Triangle} everywhere else.
 */
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
