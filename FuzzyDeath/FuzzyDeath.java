import robocode.*;

import java.awt.Color;
import java.awt.geom.*;
import java.util.*;

import FuzzySystem.FuzzySystem;

 
/**
 * AntiGravityBot -
 .
 */
public class FuzzyDeath extends AdvancedRobot
{
    /**
     * run: SnippetBot's default behavior
     */
    Hashtable targets;              //all enemies are stored in the hashtable
    Enemy target;                   //our current enemy
    final double PI = Math.PI;      //just a constant
    int direction = 1;              //direction we are heading... 1 = forward, -1 = backwards
    double firePower;               //the power of the shot we will be using
    double midpointstrength = 0;    //The strength of the gravity point in the middle of the field
    int midpointcount = 0;          //Number of turns since that strength was changed.
    double battleFieldWidth;        //BattleField Width
    double battleFieldHeight;       //BattleField Height
    FuzzySystem fs;                 //Main FuzzySystem  (Distance,Speed) -> (FirePower)
    boolean noMoreScan = false;
     
     
     
    public void run() {
    
        targets = new Hashtable();
        target = new Enemy();
        target.setDistance(100000);                     //initialize the distance so that we can select a target
        setColors(Color.black,Color.black,Color.red);    //sets the colors of the robot
        //the next two lines mean that the turns of the robot, gun and radar are independent
        battleFieldWidth = getBattleFieldWidth();
        battleFieldHeight = getBattleFieldHeight();
        fs = new FuzzySystem(battleFieldWidth, battleFieldHeight);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);   
        //turns the radar right around to get a view of the field
        
         
         
        while(true) {
            antiGravMove();                 //Move the bot
   
            doGun();
            setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
       
            execute();                      //execute all commands
        }
    }
     
    //fuzzy??
    void doFirePower(double fire ) {
         
        firePower = fire;
        fire(firePower);
         
        System.out.println("sparato a: " + firePower); 
         
    }
     
    void antiGravMove() {
        double xforce = 0;
        double yforce = 0;
        double force;
        double ang;
        GravPoint p;
        Enemy en;
        Enumeration e = targets.elements();
        //cycle through all the enemies.  If they are alive, they are repulsive.  Calculate the force on us
        while (e.hasMoreElements()) {
            en = (Enemy)e.nextElement();
            if (en.isLive()) {
                p = new GravPoint(en.getX(),en.getY(), -1000);
                force = p.getPower()/Math.pow(getRange(getX(),getY(),p.getX(),p.getY()),2);
                //Find the bearing from the point to us
                ang = normaliseBearing(Math.PI/2 - Math.atan2(getY() - p.getY(), getX() - p.getX()));
                //Add the components of this force to the total force in their respective directions
                xforce += Math.sin(ang) * force;
                yforce += Math.cos(ang) * force;
            }
        }
         
        /**The next section adds a middle point with a random (positive or negative) strength.
        The strength changes every 5 turns, and goes between -1000 and 1000.  This gives a better
        overall movement.**/
        midpointcount++;
        if (midpointcount > 5) {
            midpointcount = 0;
            midpointstrength = (Math.random() * 2000) - 1000;
        }
        p = new GravPoint(getBattleFieldWidth()/2, getBattleFieldHeight()/2, midpointstrength);
        force = p.getPower()/Math.pow(getRange(getX(),getY(),p.getX(),p.getY()),1.5);
        ang = normaliseBearing(Math.PI/2 - Math.atan2(getY() - p.getY(), getX() - p.getX()));
        xforce += Math.sin(ang) * force;
        yforce += Math.cos(ang) * force;
        
        /**The following four lines add wall avoidance.  They will only affect us if the bot is close
        to the walls due to the force from the walls decreasing at a power 3.**/
        xforce += 5000/Math.pow(getRange(getX(), getY(), getBattleFieldWidth(), getY()), 3);
        xforce -= 5000/Math.pow(getRange(getX(), getY(), 0, getY()), 3);
        yforce += 5000/Math.pow(getRange(getX(), getY(), getX(), getBattleFieldHeight()), 3);
        yforce -= 5000/Math.pow(getRange(getX(), getY(), getX(), 0), 3);
         
        //Move in the direction of our resolved force.
        goTo(getX()-xforce,getY()-yforce);
    }
     
    /**Move towards an x and y coordinate**/
     
     
    void goTo(double x, double y) {
        double dist = 20;
        double angle = Math.toDegrees(absbearing(getX(),getY(),x,y));
        double r = turnTo(angle);
        setAhead(dist * r);
    }
 
 
    /**Turns the shortest angle possible to come to a heading, then returns the direction the
    the bot needs to move in.**/
    int turnTo(double angle) {
        double ang;
        int dir;
        ang = normaliseBearing(getHeading() - angle);
        if (ang > 90) {
            ang -= 180;
            dir = -1;
        }
        else if (ang < -90) {
            ang += 180;
            dir = -1;
        }
        else {
            dir = 1;
        }
        setTurnLeft(ang);
        return dir;
    }
 
    /**keep the scanner turning**/
    void doScanner() {
        setTurnRadarLeftRadians(2*PI);
    }
     
    /**Move the gun to the predicted next bearing of the enemy**/
    void doGun() {
        long time = getTime() + (int)Math.round((getRange(getX(),getY(),target.getX(),target.getY())/(20-(3*firePower))));
        Point2D.Double p = target.guessPosition(time);
         
        //offsets the gun by the angle to the next shot based on linear targeting provided by the enemy class
        double gunOffset = getGunHeadingRadians() - (Math.PI/2 - Math.atan2(p.y - getY(), p.x - getX()));
        setTurnGunLeftRadians(normaliseBearing(gunOffset));
    }
     
     
    //if a bearing is not within the -pi to pi range, alters it to provide the shortest angle
    double normaliseBearing(double ang) {
        if (ang > PI)
            ang -= 2*PI;
        if (ang < -PI)
            ang += 2*PI;
        return ang;
    }
     
    //if a heading is not within the 0 to 2pi range, alters it to provide the shortest angle
    double normaliseHeading(double ang) {
        if (ang > 2*PI)
            ang -= 2*PI;
        if (ang < 0)
            ang += 2*PI;
        return ang;
    }
     
    //returns the distance between two x,y coordinates
    public double getRange( double x1,double y1, double x2,double y2 )
    {
        double xo = x2-x1;
        double yo = y2-y1;
        double h = Math.sqrt( xo*xo + yo*yo );
        return h;  
    }
     
    //gets the absolute bearing between to x,y coordinates
    public double absbearing( double x1,double y1, double x2,double y2 )
    {
        double xo = x2-x1;
        double yo = y2-y1;
        double h = getRange( x1,y1, x2,y2 );
        if( xo > 0 && yo > 0 )
        {
            return Math.asin( xo / h );
        }
        if( xo > 0 && yo < 0 )
        {
            return Math.PI - Math.asin( xo / h );
        }
        if( xo < 0 && yo < 0 )
        {
            return Math.PI + Math.asin( -xo / h );
        }
        if( xo < 0 && yo > 0 )
        {
            return 2.0*Math.PI - Math.asin( -xo / h );
        }
        return 0;
    }
 
 
    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
    	setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
    	antiGravMove();
        Enemy en = new Enemy();
        if (targets.containsKey(e.getName())) {
            en = (Enemy)targets.get(e.getName());
        } else {
            en = new Enemy();
            targets.put(e.getName(),en);
        }
        //the next line gets the absolute bearing to the point where the bot is
        double absbearing_rad = getHeadingRadians()+e.getBearingRadians();
        //this section sets all the information about our target
        //double latVel = e.getVelocity() * Math.sin(e.getHeadingRadians() - absbearing_rad);//enemies later velocity
        
    	
       
        double h = normaliseBearing(e.getHeadingRadians() - en.getHeading());
        h = h/(getTime() - en.getCtime());
        en.setName(e.getName());
        en.setChangehead(h);
        en.setX(getX()+Math.sin(absbearing_rad)*e.getDistance()); //works out the x coordinate of where the target is
        en.setY(getY()+Math.cos(absbearing_rad)*e.getDistance()); //works out the y coordinate of where the target is
        en.setBearing(e.getBearingRadians());
        en.setHeading(e.getHeadingRadians());
        en.setCtime(getTime());             //game time at which this scan was produced
        en.setSpeed(e.getVelocity());
        en.setDistance(e.getDistance());   
        en.setLive(true);
        // call doFirePower
        doGun();
        
        if ((en.getDistance() < target.getDistance())||(target.isLive() == false)) {
            target = en;
        }
      //  System.out.println("ciao" + fs); 
        double fire = fs.setFire(en.getDistance(), en.getSpeed());
        doFirePower(fire);
        
      //  System.out.println("spara a: " + fire);
     
    }
         
    public void onRobotDeath(RobotDeathEvent e) {
        Enemy en = (Enemy)targets.get(e.getName());
        en.setLive(false);     
    }  
}