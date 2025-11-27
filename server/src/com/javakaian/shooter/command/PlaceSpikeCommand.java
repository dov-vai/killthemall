package com.javakaian.shooter.command;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.shapes.PlacedSpike;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.shapes.GameObjectComposite;

import java.util.List;

/**
 * Command for placing a spike
 * Can be undone if the spike hasn't been consumed (damaged a player)
 */
public class PlaceSpikeCommand implements Command {
    private Player player;
    private PlacedSpike placedSpike;
    private List<PlacedSpike> placedSpikes;
    private GameObjectComposite worldObjects; // <- new
    private float x;
    private float y;
    private float rotation;

    public PlaceSpikeCommand(Player player, List<PlacedSpike> placedSpikes, GameObjectComposite worldObjects,
                             float x, float y, float rotation) {
        this.player = player;
        this.placedSpikes = placedSpikes;
        this.worldObjects = worldObjects;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    @Override
    public void execute() {
        if (player.hasSpikes()) {
            player.removeSpike();
            placedSpike = new PlacedSpike(x, y, 35, rotation, player.getId());
            placedSpike.setVisible(true); // <- ensure it's visible
            placedSpikes.add(placedSpike);
            worldObjects.add(placedSpike);  // <- ADD to worldObjects so clients see it
        }
    }

    @Override
    public void undo() {
        if (canUndo()) {
            placedSpikes.remove(placedSpike);
            worldObjects.remove(placedSpike); // <- remove from world
            player.addSpike();
        }
    }

    @Override
    public boolean canUndo() {
        return placedSpike != null && !placedSpike.isConsumed() && placedSpike.isVisible();
    }

    public PlacedSpike getPlacedSpike() {
        return placedSpike;
    }
}
