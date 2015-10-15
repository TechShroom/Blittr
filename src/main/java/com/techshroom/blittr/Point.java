package com.techshroom.blittr;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Point {

    public abstract Vector3d getPosition();

    public abstract Vector3d getNormal();

    public abstract Vector2d getTexturePosition();

}
