package com.shcompany.raspberry.ultrasonic;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiBcmPin;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPinNumberingScheme;

public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {

    	//BCM Numbering
    	GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
    	
    	//Create gpio controller
        final GpioController gpio = GpioFactory.getInstance();


        GpioPinDigitalOutput sensorTriggerPin =  gpio.provisionDigitalOutputPin(RaspiBcmPin.GPIO_05); // Trigger pin as OUTPUT;
        GpioPinDigitalInput sensorEchoPin = gpio.provisionDigitalInputPin(RaspiBcmPin.GPIO_06,PinPullResistance.PULL_DOWN); // Echo pin as INPUT        

        //On exit, forcefully shutdown all GPIO monitoring threads and scheduled tasks
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	gpio.shutdown();
            }
        }));        
        
        //Loops and toggle pin
    	while(true){    
    		
    		try {
    			
    			Thread.sleep(2000);
    			sensorTriggerPin.high(); // Make trigger pin HIGH
    			Thread.sleep((long) 0.01);// Delay for 10 microseconds
    			sensorTriggerPin.low(); //Make trigger pin LOW
    		
    			System.out.println("Waiting...High status...");
    			while(sensorEchoPin.isLow()){ //Wait until the ECHO pin gets HIGH
    				    				
    			}
    			long startTime= System.nanoTime(); // Store the surrent time to calculate ECHO pin HIGH time.
    			
    			System.out.println("Waiting...Low status...");
    			while(sensorEchoPin.isHigh()){ //Wait until the ECHO pin gets LOW
    				
    			}
    			long endTime= System.nanoTime(); // Store the echo pin HIGH end time to calculate ECHO pin HIGH time.
    		
    			System.out.println("Distance :"+((((endTime-startTime)/1e3)/2) / 29.1) +" cm"); //Printing out the distance in cm  
    			Thread.sleep(1000);
    			
    		}catch(Exception e){
    			//NOP
    		}
    		
    		
    		
    	}  
    }
}
