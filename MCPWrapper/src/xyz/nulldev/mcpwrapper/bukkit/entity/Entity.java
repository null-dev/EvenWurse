package xyz.nulldev.mcpwrapper.bukkit.entity;

import xyz.nulldev.mcpwrapper.bukkit.Location;
import xyz.nulldev.mcpwrapper.bukkit.World;
import xyz.nulldev.mcpwrapper.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

/**
 * Represents a base entity in the world
 */
public interface Entity {

    /**
     * Gets the entity's current position
     *
     * @return a new copy of Location containing the position of this entity
     */
    public Location getLocation();

    /**
     * Stores the entity's current position in the provided Location object.
     * <p>
     * If the provided Location is null this method does nothing and returns
     * null.
     *
     * @return The Location object provided or null
     */
    public Location getLocation(Location loc);

    /**
     * Gets this entity's current velocity
     *
     * @return Current travelling velocity of this entity
     */
    public Vector getVelocity();

    /**
     * Sets this entity's velocity
     *
     * @param velocity New velocity to travel with
     */
    public void setVelocity(Vector velocity);

    /**
     * Returns true if the entity is supported by a block. This value is a
     * state updated by the server and is not recalculated unless the entity
     * moves.
     *
     * @return True if entity is on ground.
     */
    public boolean isOnGround();

    /**
     * Gets the current world this entity resides in
     *
     * @return World
     */
    public World getWorld();

    /**
     * Teleports this entity to the given location. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param location New location to teleport this entity to
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Location location);

    /**
     * Teleports this entity to the target Entity. If this entity is riding a
     * vehicle, it will be dismounted prior to teleportation.
     *
     * @param destination Entity to teleport this entity to
     * @return <code>true</code> if the teleport was successful
     */
    public boolean teleport(Entity destination);

    /**
     * Returns a list of entities within a bounding box centered around this
     * entity
     *
     * @param x 1/2 the size of the box along x axis
     * @param y 1/2 the size of the box along y axis
     * @param z 1/2 the size of the box along z axis
     * @return List<Entity> List of entities nearby
     */
    public List<Entity> getNearbyEntities(double x, double y, double z);

    /**
     * Returns a unique id for this entity
     *
     * @return Entity id
     */
    public int getEntityId();

    /**
     * Returns the entity's current fire ticks (ticks before the entity stops
     * being on fire).
     *
     * @return int fireTicks
     */
    public int getFireTicks();

    /**
     * Sets the entity's current fire ticks (ticks before the entity stops
     * being on fire).
     *
     * @param ticks Current ticks remaining
     */
    public void setFireTicks(int ticks);

    /**
     * Returns the entity's maximum fire ticks.
     *
     * @return int maxFireTicks
     */
    public int getMaxFireTicks();

    /**
     * Mark the entity's removal.
     */
    public void remove();

    /**
     * Returns true if this entity has been marked for removal.
     *
     * @return True if it is dead.
     */
    public boolean isDead();

    /**
     * Returns false if the entity has died or been despawned for some other
     * reason.
     *
     * @return True if valid.
     */
    public boolean isValid();

    /**
     * Gets the primary passenger of a vehicle. For vehicles that could have
     * multiple passengers, this will only return the primary passenger.
     *
     * @return an entity
     */
    public abstract Entity getPassenger();

    /**
     * Set the passenger of a vehicle.
     *
     * @param passenger The new passenger.
     * @return false if it could not be done for whatever reason
     */
    public abstract boolean setPassenger(Entity passenger);

    /**
     * Check if a vehicle has passengers.
     *
     * @return True if the vehicle has no passengers.
     */
    public abstract boolean isEmpty();

    /**
     * Eject any passenger.
     *
     * @return True if there was a passenger.
     */
    public abstract boolean eject();

    /**
     * Returns the distance this entity has fallen
     *
     * @return The distance.
     */
    public float getFallDistance();

    /**
     * Sets the fall distance for this entity
     *
     * @param distance The new distance.
     */
    public void setFallDistance(float distance);

    /**
     * Returns a unique and persistent id for this entity
     *
     * @return unique id
     */
    public UUID getUniqueId();

    /**
     * Gets the amount of ticks this entity has lived for.
     * <p>
     * This is the equivalent to "age" in entities.
     *
     * @return Age of entity
     */
    public int getTicksLived();

    /**
     * Sets the amount of ticks this entity has lived for.
     * <p>
     * This is the equivalent to "age" in entities. May not be less than one
     * tick.
     *
     * @param value Age of entity
     */
    public void setTicksLived(int value);

    /**
     * Get the type of the entity.
     *
     * @return The entity type.
     */
    //TODO
    //    public EntityType getType();

    /**
     * Returns whether this entity is inside a vehicle.
     *
     * @return True if the entity is in a vehicle.
     */
    public boolean isInsideVehicle();

    /**
     * Leave the current vehicle. If the entity is currently in a vehicle (and
     * is removed from it), true will be returned, otherwise false will be
     * returned.
     *
     * @return True if the entity was in a vehicle.
     */
    public boolean leaveVehicle();

    /**
     * Get the vehicle that this player is inside. If there is no vehicle,
     * null will be returned.
     *
     * @return The current vehicle.
     */
    public Entity getVehicle();
}
