package com.techshroom.blittr;


public interface DrawHandler {
    
    void drawTriangle(DrawTriangle tri);
    
    void registerDrawable(Drawable draw);
    
    void redraw();

}
