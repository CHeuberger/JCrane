/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jcrane;

import static java.awt.Color.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

/**
 * @author Carlos F. Heuberger, 2023-08-02
 *
 */
public class Settings {

    private static Settings instance = new Settings();
    
    public static Settings instance() { return instance; }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    private Settings() {
        //
    }
    
    public Dimension preferredSize() { return new Dimension(800, 500); }
    public Insets gap() { return new Insets(10, 10, 0, 10); }

    public Color worldBackground() { return LIGHT_GRAY; }
    
    public int tableHeight() { return 10; }
    public Color tableColor() { return BLACK; }
    
    public int craneHeight() { return 5; }
    public int craneHalfBase() { return 7; }
    public int craneMinHorz() { return craneHeight() + craneHalfBase(); }
    public int craneMinVert() { return 20; }
    public Color craneColor() { return GRAY.darker(); }
    public Color craneBaseColor() { return BLACK; }

    public int horizontalVel() { return 2; }
    public int verticalVel() { return 2; }
}
