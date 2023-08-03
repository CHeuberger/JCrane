/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jcrane.model;

import static cfh.jcrane.model.Crane.Dir.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;

import cfh.jcrane.Settings;
import cfh.jcrane.model.Block.Rect;

/**
 * @author Carlos F. Heuberger, 2023-08-02
 *
 */
public class Crane {

    public enum Dir {
        STOP, LEFT, RIGHT, UP, DOWN;
    }
    
    private int x = Settings.instance().craneMinHorz();
    private int y = Settings.instance().craneMinVert();

    private Block block = null;
    private Dir horizontal = STOP;
    private Dir vertical = STOP;
    
    

    public boolean isEmpty() {
        return block == null;
    }

    public void pick(Rect b) {
        if (block != null) {
            throw new IllegalArgumentException("unable to pickup " + b + ", already loaded: " + block);
        }
        block = b;
    }

    public void paint(Graphics2D gg) {
        var settings = Settings.instance();
        var w = gg.getClipBounds().width;
        var h = gg.getClipBounds().height;
        var base = settings.craneHeight() + 2 * settings.craneHalfBase();

        gg.setColor(settings.craneColor());
        gg.fillRect(0, h-settings.craneHeight(), w, settings.craneHeight());
        gg.fillRect(x, h-y, settings.craneHeight(), y);
        
        gg.setColor(settings.craneBaseColor());
        gg.fillRect(x-settings.craneHalfBase(), h-y, base, settings.craneHeight());
        
        if (block != null) {
            block.moveTop(x+settings.craneHeight()/2, h-y);
            block.paint(gg);
        }
    }

    void update(int width, int height, Collection<Block> blocks) {
        var settings = Settings.instance();
        var nx = x + switch (horizontal) {
            case LEFT -> -1;
            case STOP -> 0;
            case RIGHT -> +1;
            default -> throw new IllegalArgumentException("invalid horizontal; " + horizontal);
        } * settings.horizontalVel();
        var ny = y + switch (vertical) {
            case UP -> -1;
            case STOP -> 0;
            case DOWN -> +1;
            default -> throw new IllegalArgumentException("invalid horizontal; " + horizontal);
        } * settings.verticalVel();

        var bh = block==null ? 0 : block.height();
        
        if (   nx < settings.craneMinHorz() 
            || nx > width-settings.craneMinHorz()
            || ny < settings.craneMinVert()
            || ny+bh > height)
        {
            stop();
            return;
        }
        
        var base = settings.craneHeight() + 2 * settings.craneHalfBase();
        var bound = new Rectangle(nx-settings.craneHalfBase()-1, height-ny, base, ny);
        for (var b : blocks) {
            if (b != block && b.intersects(bound)) {
                stop();
                return;
            }
        }

        x = nx;
        y = ny;
    }

    public void moveHorz(Dir dir) {
        horizontal = switch (dir) {
            case STOP, LEFT, RIGHT -> dir;
            default -> throw new IllegalArgumentException("invalid horizontal: " + dir);
        };
    }
    
    public void moveVert(Dir dir) {
        vertical = switch (dir) {
            case STOP, UP, DOWN -> dir;
            default -> throw new IllegalArgumentException("invalid vertical: " + dir);
        };
    }
    
    public void stop() {
        horizontal = STOP;
        vertical = STOP;
    }
}
