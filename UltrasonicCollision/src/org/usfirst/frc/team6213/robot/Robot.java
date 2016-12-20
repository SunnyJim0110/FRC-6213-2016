/* The base robot code for the 2016 competition. Autonomous and Shooter functionality is disabled.
 * The code for Autonomous mode was written on the fly to try to score, pretty useless now
*/

package org.usfirst.frc.team6213.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Ultrasonic;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot { // Prep everything
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    // public long curTime; // For Autonomous
	public static double maxSpeed; // Used to set speed of robot
    String autoSelected;
    SendableChooser chooser;
    VictorSP left; // Left Victor
    VictorSP right; // Right Victor
    RobotDrive move; // Drive train Motors
    Joystick xbox; // Xbox Controller
	CANTalon ballWheel ; // Talon to control the ball wheel motor
	Timer timer; // Timer
	DigitalInput inPort0; // DIO 0 for digital input
	DigitalOutput outPort1; // DIO 1 for digital output
	Ultrasonic front1; // Ultrasonic sensor 1 for front of bot
	double front1Distance; // Distance from front1 in inches

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() { // Initialize everything
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        timer = new Timer();
        inPort0 = new DigitalInput(0);
        outPort1 = new DigitalOutput(1);
        front1 = new Ultrasonic(outPort1,inPort0);
        left = new VictorSP(0);
        right = new VictorSP(1);
        move = new RobotDrive(left,right);
        ballWheel = new CANTalon(1);
        xbox = new Joystick(0);
        maxSpeed = 0.4;
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		// curTime = System.currentTimeMillis(); // Time at start of autonomous
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() { // More ignore :D
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    		// autoMove(left,right);
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() { // Portion of code used to drive the robot during teleop mode
    	double Fmove = xbox.getRawAxis(3); // Right trigger, forwards speed
    	double Rmove = xbox.getRawAxis(2); // Left trigger, backwards speed
    	double Turn = xbox.getRawAxis(0); // Left stick X, turning
    	/*boolean bButton = xbox.getRawButton(2); // B button status
    	boolean  yButton = xbox.getRawButton(4);  // Y button status
    	ballWheel.enable(); // Enable the CAN Talon */
    	boolean aButton = xbox.getRawButton(1); // A button status
    	front1Distance = front1.getRangeInches(); // Distance from ultrasonic sensor
    	System.out.println(front1Distance);
    	
    	if(aButton){ // Checks max speed of robot, changes it
    		if(maxSpeed == 0.4){
    			maxSpeed = 1;
    			timer.delay(0.25);
    		}
    		else{
    			maxSpeed = 0.4;
    			timer.delay(0.25);
    		}
    	}
    	
    	if(Fmove > 0){ // Moving Forwards
    		move.drive(Fmove * -maxSpeed , Turn);
    	}
    	
    	else if(Rmove > 0){ // Moving Backwards
    		move.drive(Rmove * maxSpeed, Turn);
    	}	
    	//Old Shooter Code
    	/*if(bButton){ // B button down, moves towards robot, intake
        	ballWheel.set(0.55);
    	}
    	
    	else if(yButton){ // Y button, moves away from robot, shoots
        	ballWheel.set(-1);
    	}
    	else{ // Keep ball motor still by default
    		ballWheel.set(0);
    	}*/
    }
 
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    // This method was used to try to score during the autonomous period. It isn't really useful but is being kept in for future reference if needed
    
    /* public void autoMove(VictorSP leftSide, VictorSP rightSide){  
    	ballWheel.enable(); // Enable the CAN Talon
    	long startTime = curTime; // Time at start of autonomous
    	long back = startTime + (long)(3500); // 3.5 seconds backwards
    	long turn = back + (long)(750); // 0.75 Turn
    	long forward = turn + (long)(1000); // 1 forward
    	long shoot = forward + (long)(1000); // shoot time
    	long curFuncTime = System.currentTimeMillis(); // Current time, used to compare
    	left = leftSide; // Left Motor
    	right = rightSide; // Right Motor
    	
    	while(curFuncTime <= back){ // Moves robot backwards for 4 seconds
    		left.set(0.6); // Back
    		right.set(-0.6); // Back
    		curFuncTime = System.currentTimeMillis();
    	}
    	while(curFuncTime <= turn){ // Turns left (back,forward) for three quarters of a second
    		left.set(1.0); // Back
    		right.set(1.0); // Forward
    		curFuncTime = System.currentTimeMillis();
    	}
    	while(curFuncTime <= forward){ // Forward for one second
    		left.set(-0.6);
    		right.set(0.6);
    		curFuncTime = System.currentTimeMillis();
    	}
    	while(curFuncTime <= shoot){ // Shoot for 1 second
    		ballWheel.set(-1);
    		curFuncTime = System.currentTimeMillis();
    	}

    }
    
    public void CollisionStop(VictorSP leftSide, VictorSP rightSide){
    	
    } */    
}