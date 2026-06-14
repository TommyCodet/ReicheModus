package de.reichemodus.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class TeamData {

    private final String name;
    private final Set<UUID> members;

    private boolean alive;
    private boolean eliminated;
    private boolean bananaMissing;

    private String bananaWorld;
    private double bananaX;
    private double bananaY;
    private double bananaZ;

    public TeamData(String name) {
        this.name = name;
        this.members = new HashSet<>();
        this.alive = true;
        this.eliminated = false;
        this.bananaMissing = false;
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

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public boolean isBananaMissing() {
        return bananaMissing;
    }

    public void setBananaMissing(boolean bananaMissing) {
        this.bananaMissing = bananaMissing;
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public boolean hasMember(UUID uuid) {
        return members.contains(uuid);
    }

    public String getBananaWorld() {
        return bananaWorld;
    }

    public void setBananaWorld(String bananaWorld) {
        this.bananaWorld = bananaWorld;
    }

    public double getBananaX() {
        return bananaX;
    }

    public void setBananaX(double bananaX) {
        this.bananaX = bananaX;
    }

    public double getBananaY() {
        return bananaY;
    }

    public void setBananaY(double bananaY) {
        this.bananaY = bananaY;
    }

    public double getBananaZ() {
        return bananaZ;
    }

    public void setBananaZ(double bananaZ) {
        this.bananaZ = bananaZ;
    }

    public void setBananaLocation(Location location) {
        if (location == null) {
            bananaWorld = null;
            bananaX = 0;
            bananaY = 0;
            bananaZ = 0;
            return;
        }

        bananaWorld = location.getWorld().getName();
        bananaX = location.getX();
        bananaY = location.getY();
        bananaZ = location.getZ();
    }

    public Location getBananaLocation() {
        if (bananaWorld == null) {
            return null;
        }

        World world = Bukkit.getWorld(bananaWorld);

        if (world == null) {
            return null;
        }

        return new Location(
                world,
                bananaX,
                bananaY,
                bananaZ
        );
    }
}
