package de.reichemodus.model;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class TeamData {

    private final String name;
    private final Set<UUID> members;

    private boolean alive;
    private boolean bananaMissing;
    private Location bananaLocation;

    public TeamData(String name) {
        this.name = name;
        this.members = new HashSet<>();
        this.alive = true;
    }

    public String getName() {
        return name;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isBananaMissing() {
        return bananaMissing;
    }

    public void setBananaMissing(boolean bananaMissing) {
        this.bananaMissing = bananaMissing;
    }

    public Location getBananaLocation() {
        return bananaLocation;
    }

    public void setBananaLocation(Location bananaLocation) {
        this.bananaLocation = bananaLocation;
    }
}
