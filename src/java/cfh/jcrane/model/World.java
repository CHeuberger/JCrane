/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jcrane.model;

import static java.util.Objects.*;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cfh.jcrane.Settings;

/**
 * @author Carlos F. Heuberger, 2023-08-02
 *
 */
public class World {

    private final Crane crane;
    private final List<Block> blocks = new ArrayList<>();
    
    public World(Crane crane) {
        this.crane = requireNonNull(crane, "crane: null");
    }
    
    public void add(Block block) {
        blocks.add(requireNonNull(block, "block: null"));
    }
    
    public Crane crane() { return crane; }
    
    public Collection<Block> blocks() { return Collections.unmodifiableCollection(blocks); }
    
    public boolean canCreate(Block block) {
        var bound = block.bound();
        for (var b: blocks) {
            if (b.intersects(bound)) {
                return false;
            }
        }
        return true;
    }
    
    public void update(int width, int height) {
        var settings = Settings.instance();
        var gap = settings.gap();
        crane.update(
              width-gap.left-gap.right
            , height-gap.top-settings.tableHeight()-gap.bottom
            , blocks());
    }

    public void paint(Graphics2D gg) {
        var settings = Settings.instance();
        var w = gg.getClipBounds().width;
        var h = gg.getClipBounds().height;
        var gap = settings.gap();
        
        gg.setColor(settings.tableColor());
        gg.fillRect(0, h-settings.tableHeight()-gap.bottom, w, settings.tableHeight());
        
        h -= gap.top + settings.tableHeight() + gap.bottom;
        w -= gap.left + gap.right;
        gg.translate(gap.left, gap.top+h);
        gg.scale(+1, -1);
        gg.setClip(0, 0, w, h);
        
        for (var block : blocks) {
            block.paint(gg);
        }
        
        crane.paint(gg);
    }
}
