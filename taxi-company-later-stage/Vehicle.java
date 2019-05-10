import java.util.Date;
import java.util.ArrayList;

/**
 * Model the common elements of taxis and shuttles.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public abstract class Vehicle implements Actor
{
    private TaxiCompany company;
    // Where the vehicle is.
    private Location location;
    // Where the vehicle is headed.
    private Location targetLocation;
    // Record how often the vehicle has nothing to do.
    private int idleCount;
    // Record the time the vehicle picks up its current passenger
    protected Date pickupTime;
    // Record the duration of each journey
    protected Date journeyDurations;
    
    /**
     * Constructor of class Vehicle
     * @param company The taxi company. Must not be null.
     * @param location The vehicle's starting point. Must not be null.
     * @throws NullPointerException If company or location is null.
     */
    public Vehicle(TaxiCompany company, Location location)
    {
        if(company == null)
        {
            throw new NullPointerException("company");
        }
        if(location == null)
        {
            throw new NullPointerException("location");
        }
        this.company = company;
        this.location = location;
        targetLocation = null;
        idleCount = 0;
    }
    
    /**
     * Notify the company of our arrival at a pickup location.
     */
    public void notifyPickupArrival()
    {
        company.arrivedAtPickup(this);
    }
    
    /**
     * Notify the company of our arrival at a
     * passenger's destination.
     */
    public void notifyPassengerArrival(Passenger passenger)
    {
        if( passenger == null )
        {
            throw new NullPointerException("Invalid Passenger");
        }
        company.arrivedAtDestination(this, passenger);
    }
    
    /**
     * Receive a pickup location.
     * How this is handled depends on the type of vehicle.
     * @param location The pickup location.
     */
    public abstract void setPickupLocation( Location location );
    
    /**
     * Receive a passenger.
     * How this is handled depends on the type of vehicle.
     * @param passenger The passenger.
     */
    public abstract void pickup( Passenger passenger );
    
    /**
     * Is the vehicle free?
     * @return Whether or not this vehicle is free.
     */
    public abstract boolean isFree();
    
    /**
     * Offload any passengers whose destination is the
     * current location.
     */
    public abstract void offloadPassenger();
    
    /**
     * @return Where this vehicle is currently located.
     */
    public Location getLocation()
    {
        if( location == null )
        {
            throw new NullPointerException("Invalid Location - null");
        }
        return location;
    }
    
    /**
     * Set the current location.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
        if(location != null)
        {
            this.location = location;
        }
        else
        {
            throw new NullPointerException("Invalid Location");
        }
    }
    
    /**
     * Get the target location.
     * @return Where this vehicle is currently headed, or null
     *         if it is idle.
     */
    public Location getTargetLocation()
    {
        return targetLocation;
    }
    
    /**
     * Set the required target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setTargetLocation(Location location)
    {
        if(location != null)
        {
            targetLocation = location;
        }
        else
        {
            throw new NullPointerException("Invalid Location");
        }
    }
    
    /**
     * Clear the target location.
     */
    public void clearTargetLocation()
    {
        targetLocation = null;
    }

    /**
     * @return On how many steps this vehicle has been idle.
     */
    public int getIdleCount()
    {
        return idleCount;
    }
    
    /**
     * Increment the number of steps on which this vehicle
     * has been idle.
     */
    public void incrementIdleCount()
    {
        idleCount++;
    }
}
