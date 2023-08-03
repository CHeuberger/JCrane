/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jcrane.model;

import java.awt.Graphics2D;

import cfh.jcrane.Settings;

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

    private Block block;
    private Dir horizontal = Dir.STOP;
    private Dir vertical = Dir.STOP;
    
    public void paint(Graphics2D gg) {
        var settings = Settings.instance();
        var w = gg.getClipBounds().width;
        var h = gg.getClipBounds().height;
        
        if (x < settings.craneMinHorz()) {
            x = settings.craneMinHorz();
        } else if (x > w-settings.craneMinHorz()) {
            x = w-settings.craneMinHorz();
        }
        if (y < settings.craneMinVert()) {
            y = settings.craneMinVert();
        } else if (y > h) {
            y = h;
        }

        gg.setColor(settings.craneColor());
        gg.fillRect(0, h-settings.craneHeight(), w, settings.craneHeight());
        gg.fillRect(x, h-y, settings.craneHeight(), y);
        var cw = settings.craneHalfBase();
        gg.fillRect(x-cw, h-y, cw+settings.craneHeight()+cw, settings.craneHeight());
    }

    void update() {
        x += switch (horizontal) {
            case LEFT -> -1;
            case STOP -> 0;
            case RIGHT -> +1;
            default -> throw new IllegalArgumentException("invalid horizontal; " + horizontal);
        } * Settings.instance().horizontalVel();
        y += switch (vertical) {
            case UP -> -1;
            case STOP -> 0;
            case DOWN -> +1;
            default -> throw new IllegalArgumentException("invalid horizontal; " + horizontal);
        } * Settings.instance().verticalVel();
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
        horizontal = Dir.STOP;
        vertical = Dir.STOP;
    }
}
