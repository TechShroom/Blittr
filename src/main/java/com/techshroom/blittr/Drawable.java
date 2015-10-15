package com.techshroom.blittr;

/**
 * Represents an object that can be drawn.
 */
public interface Drawable {

    /**
     * Draw this object using the given {@link DrawHandler}.
     * 
     * @param handler
     *            - The handler to draw with
     */
    void draw(DrawHandler handler);

    /**
     * A hint for draw handlers so they only call {@link #draw(DrawHandler)}
     * when needed.
     * 
     * @return {@code true} if this object needs to be redrawn
     */
    boolean isDirty();

}
