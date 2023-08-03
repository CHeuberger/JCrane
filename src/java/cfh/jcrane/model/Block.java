/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jcrane.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * @author Carlos F. Heuberger, 2023-08-02
 *
 */
public sealed abstract class Block {

    protected final String name;
    protected final Color color;
    
    protected int x;
    protected int y;
    
    protected Block(String name, Color color) {
        assert name != null;
        assert color != null;
        this.name = name;
        this.color = color;
    }
    
    protected abstract boolean intersects(Rectangle bound);
    
    protected abstract void paint(Graphics2D gg);

    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final class Rect extends Block {
        
        private final int width;
        private final int height;
        
        public Rect(String name, Color color, int width, int height) {
            super(name, color);
            if (width <= 0) throw new IllegalArgumentException("invalid width: " + width);
            if (height <= 0) throw new IllegalArgumentException("invalid height: " + height);
            this.width = width;
            this.height = height;
        }
        
        @Override
        protected boolean intersects(Rectangle bound) {
            return bound.intersects(x, y, width, height);
        }

        @Override
        protected void paint(Graphics2D gg) {
            gg.setColor(color);
            gg.fillRect(x, y, width, height);
            gg.setColor(Color.BLACK);
            gg.drawRect(x, y, width, height);
        }
    }
}
