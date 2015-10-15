package com.techshroom.blittr.debug;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techshroom.blittr.DrawHandler;
import com.techshroom.blittr.DrawTriangle;
import com.techshroom.blittr.Drawable;

public final class DebugDrawHandler implements DrawHandler {

    private final Logger logger;
    private final DrawHandler actualImpl;

    public DebugDrawHandler(DrawHandler delegate) {
        this.actualImpl = delegate;
        this.logger = LogManager.getLogger(delegate);
    }

    @Override
    public void drawTriangle(DrawTriangle tri) {
        this.logger.entry(tri);
        this.actualImpl.drawTriangle(tri);
        this.logger.exit();
    }

    @Override
    public void registerDrawable(Drawable draw) {
        this.logger.entry(draw);
        this.actualImpl.registerDrawable(draw);
        this.logger.exit();
    }

    @Override
    public void redraw() {
        this.logger.entry();
        this.actualImpl.redraw();
        this.logger.exit();
    }

    @Override
    public String toString() {
        return "Debug" + this.actualImpl;
    }

    @Override
    public int hashCode() {
        return this.actualImpl.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof DebugDrawHandler
                && this.actualImpl == ((DebugDrawHandler) obj).actualImpl);
    }

}
