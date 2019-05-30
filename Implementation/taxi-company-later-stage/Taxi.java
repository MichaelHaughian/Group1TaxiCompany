import java.awt.Image;
import java.time.DateTimeException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.naming.directory.InvalidAttributesException;
import javax.swing.ImageIcon;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;

/**
 * A taxi is able to carry a single passenger.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class Taxi extends Vehicle implements DrawableItem
{
    private Passenger passenger;
    // Maintain separate images for when the taxi is empty
    // and full.
    private Image emptyImage, passengerImage;
    
    /**
     * Constructor for objects of class Taxi
     * @param company The taxi company. Must not be null.
     * @param location The vehicle's starting point. Must not be null.
     * @throws NullPointerException If company or location is null.
     */
    public Taxi( TaxiCompany company, Location location )
    {
        super(company, location);
        // Load the two images.
        
        emptyImage = new ImageIcon(getClass().getResource(
                                "images/taxi.jpg")).getImage();

        passengerImage = new ImageIcon(getClass().getResource(
                                "images/taxi+person.jpg")).getImage();
    }
    
    /**
     * Move towards the target location if we have one.
     * Otherwise record that we are idle.
     */
    public void act()
    {
        Location target = getTargetLocation();
        if(target != null)
        {
            // Find where to move to next.
            Location next = getLocation().nextLocation(target);
            setLocation(next);
            if(next.equals(target))
            {
                if( passenger != null )
                {
                    notifyPassengerArrival(passenger);
                    offloadPassenger();
                }
                else
                {
                    notifyPickupArrival();
                }
            }
        }
        else
        {
            incrementIdleCount();
        }
    }

    /**
     * Is the taxi free?
     * @return Whether or not this taxi is free.
     */
    public boolean isFree()
    {
        return getTargetLocation() == null && passenger == null;
    }
    
    /**
     * Receive a pickup location. This becomes the
     * target location.
     * @param location The pickup location.
     */
    public void setPickupLocation( Location location )
    {
        if( location == null )
        {
            throw new NullPointerException("Invalid location - null");
        }
        setTargetLocation( location );
    }
    
    /**
     * Receive a passenger.
     * Set their destination as the target location.
     * @param passenger The passenger.
     */
    public void pickup(Passenger passenger)
    {
        if( passenger == null )
        {
            throw new NullPointerException("Invalid passenger - null");
        }
        
        pickupTime = new Date();
        
        this.passenger = passenger;
        
        setTargetLocation( passenger.getDestination() );
    }

    /**
     * Offload the passenger.
     */
    public void offloadPassenger()
    {
        Date dropOffTime = new Date();
        
        if( 0 > dropOffTime.compareTo( pickupTime ) )
        {
            throw new DateTimeException("Can't drop passenger off before you've picked them up");
        }
         else
        { 
            Date journeyDuration =  new Date( dropOffTime.getTime() - pickupTime.getTime() );
            DateFormat duration = new SimpleDateFormat("ss");
            String shuttleDuration = "Taxi" + duration.format(journeyDuration);
            
            try
            {
                FileWriter fileWriter = new FileWriter(new File("C:/CS112GP/DateTime.txt"), true );
                BufferedWriter writer = new BufferedWriter(fileWriter);
                writer.write(shuttleDuration + " \n");
                writer.close();
            }
            catch( IOException e )
            {
            
            }
        }
        
        pickupTime = null;
        passenger = null;
        clearTargetLocation();
    }
    
    /**
     * Return an image that describes our state:
     * either empty or carrying a passenger.
     */
    public Image getImage()
    {
        if(passenger != null)
        {
            return passengerImage;
        }
        else
        {
            return emptyImage;
        }
    }

    /**
     * Return details of the taxi, such as where it is.
     * @return A string representation of the taxi.
     */
    public String toString()
    {
        if( getLocation() == null )
        {
            throw new NullPointerException( "Invalid Location - null" );
        }
        return "Taxi at " + getLocation();
    }
}
