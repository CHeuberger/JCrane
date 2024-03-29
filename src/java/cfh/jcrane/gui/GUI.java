/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jcrane.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cfh.jcrane.model.Block;
import cfh.jcrane.model.Crane;
import cfh.jcrane.model.World;

/**
 * @author Carlos F. Heuberger, 2023-08-02
 *
 */
public class GUI {
    
    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> new GUI(args));
    }
    
    private GUI(String... args) {
        var crane = new Crane();
        var world = new World(crane);
        
        // XXX
        world.add(new Block.Rect(0, 0, "1", Color.YELLOW, 40, 40));
        
        var worldPanel = new WorldPanel(world);
        
        var layout = new BorderLayout();
        
        var frame = new JFrame("JCrane");
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setLayout(layout);
        frame.add(worldPanel, layout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
