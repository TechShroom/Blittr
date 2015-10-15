package com.techshroom.blittr;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RenderPoint {

    public static RenderPoint.Builder builder() {
        return new AutoValue_RenderPoint.Builder().normal(new Vector3d())
                .texturePosition(new Vector2d());
    }

    @AutoValue.Builder
    public static interface Builder {

        Builder position(Vector3d pos);

        Builder normal(Vector3d normal);

        Builder texturePosition(Vector2d pos);

        RenderPoint build();

    }

    RenderPoint() {
    }

    public abstract Vector3d getPosition();

    public abstract Vector3d getNormal();

    public abstract Vector2d getTexturePosition();

}
