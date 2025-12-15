package com.javakaian.shooter.shapes;

import java.util.HashMap;
import java.util.Map;

public class StatisticsVisitor implements GameObjectVisitor {

    private int totalPlayers = 0;
    private int totalEnemies = 0;
    private int totalBullets = 0;
    private int totalSpikes = 0;
    private int totalPlacedSpikes = 0;
    private int totalHealth = 0;
    private Map<String, Integer> teamCounts = new HashMap<>();

    @Override
    public void visit(Player player) {
        totalPlayers++;
        totalHealth += player.getHealth();

        String team = player.getTeamPlayer() != null ?
                player.getTeamPlayer().getTeamName() : "None";
        teamCounts.merge(team, 1, Integer::sum);
    }

    @Override
    public void visit(Enemy enemy) {
        totalEnemies++;
    }

    @Override
    public void visit(Bullet bullet) {
        totalBullets++;
    }

    @Override
    public void visit(Spike spike) {
        totalSpikes++;
    }

    @Override
    public void visit(PlacedSpike spike) {
        totalPlacedSpikes++;
    }

    public String generateReport() {
        float avgHealth = totalPlayers > 0 ? (float) totalHealth / totalPlayers : 0;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Players: %d, Enemies: %d, Bullets: %d, Spikes: %d, PlacedSpikes: %d, Avg Health: %.1f%n",
                totalPlayers, totalEnemies, totalBullets, totalSpikes, totalPlacedSpikes, avgHealth));

        sb.append("Team Counts: ");
        if (teamCounts.isEmpty()) {
            sb.append("None");
        } else {
            teamCounts.forEach((team, count) -> sb.append(team).append(":").append(count).append(" "));
        }

        return sb.toString().trim();
    }

    // Optional getters if you want to access stats programmatically
    public int getTotalPlayers() { return totalPlayers; }
    public int getTotalEnemies() { return totalEnemies; }
    public int getTotalBullets() { return totalBullets; }
    public int getTotalSpikes() { return totalSpikes; }
    public int getTotalPlacedSpikes() { return totalPlacedSpikes; }
    public float getAverageHealth() { return totalPlayers > 0 ? (float) totalHealth / totalPlayers : 0; }
    public Map<String, Integer> getTeamCounts() { return teamCounts; }
}