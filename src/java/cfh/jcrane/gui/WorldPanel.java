/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jcrane.gui;

import static java.awt.GridBagConstraints.BASELINE_LEADING;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.RELATIVE;
import static java.awt.GridBagConstraints.REMAINDER;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import cfh.jcrane.Settings;
import cfh.jcrane.model.Block;
import cfh.jcrane.model.Crane.Dir;
import cfh.jcrane.model.World;

/**
 * @author Carlos F. Heuberger, 2023-08-02
 *
 */
public class WorldPanel extends JPanel {

    private final World world;

    WorldPanel(World world) {
        this.world = requireNonNull(world, "world: null");

        registerKeyAction("typed \n", this::typedReturn);
        registerKeyAction("pressed RIGHT", this::pressedRight);
        registerKeyAction("released RIGHT", this::releasedHorz);
        registerKeyAction("pressed LEFT", this::pressedLeft);
        registerKeyAction("released LEFT", this::releasedHorz);
        registerKeyAction("pressed UP", this::pressedUp);
        registerKeyAction("released UP", this::releasedVert);
        registerKeyAction("pressed DOWN", this::pressedDown);
        registerKeyAction("released DOWN", this::releasedVert);
        
        var factory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                var thread = new Thread(r, "Animation");
                thread.setDaemon(true);
                return thread;
            }
        };
        Executors.newSingleThreadScheduledExecutor(factory).scheduleWithFixedDelay(this::update, 200, 10, MILLISECONDS);
    }

    @Override
    public Dimension getPreferredSize() {
        return Settings.instance().preferredSize();
    }

    private void typedReturn(ActionEvent e) {
        if (!world.crane().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Crane not empty");
            return;
        }
        var insets = new Insets(0, 0, 0, 0);
        var cLabel = new GridBagConstraints(0, RELATIVE, 1, 1, 0.0, 0.0, BASELINE_LEADING, NONE, insets, 0, 0);
        var cField = new GridBagConstraints(1, RELATIVE, REMAINDER, 1, 0.0, 0.0, BASELINE_LEADING, HORIZONTAL, insets, 0, 0);
        var panel = new JPanel(new GridBagLayout());
        
        var width = new JTextField(10);
        panel.add(new JLabel("Width:"), cLabel);
        panel.add(width, cField);
        
        var height = new JTextField(10);
        panel.add(new JLabel("Height:"), cLabel);
        panel.add(height, cField);
        
        var opt = JOptionPane.showConfirmDialog(this, panel, "Create Block", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                var w = Integer.parseInt(width.getText());
                var h = Integer.parseInt(height.getText());
                var b = new Block.Rect("", Color.RED, w, h);  // TODO name and color
                world.crane().pick(b);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "invalid dimensions");
            }
        }
    }
    
    private void pressedRight(ActionEvent e) {
        world.crane().moveHorz(Dir.RIGHT);
    }
    
    private void pressedLeft(ActionEvent e) {
        world.crane().moveHorz(Dir.LEFT);
    }
    
    private void releasedHorz(ActionEvent e) {
        world.crane().moveHorz(Dir.STOP);
    }
    
    private void pressedUp(ActionEvent e) {
        world.crane().moveVert(Dir.UP);
    }
    
    private void pressedDown(ActionEvent e) {
        world.crane().moveVert(Dir.DOWN);
    }
    
    private void releasedVert(ActionEvent e) {
        world.crane().moveVert(Dir.STOP);
    }
    
    private void update() {
        world.update(getWidth(), getHeight());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var gg = (Graphics2D)g.create();
        try {
            var settings = Settings.instance();
            var w = getWidth();
            var h = getHeight();

            if (settings.worldBackground() != null) {
                gg.setBackground(settings.worldBackground());
                gg.clearRect(0, 0, w, h);
            }

            world.paint(gg);
        } finally {
            gg.dispose();
        }
    }

    private void registerKeyAction(String key, Consumer<ActionEvent> action) {
        getInputMap().put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction(key) {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.accept(e);
            }
        });
    }
}
