package todo;

import done.AbstractWashingMachine;

public class WashingProgram2 extends WashingProgram1 {

	protected WashingProgram2(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
	}

	@Override
	protected void wash() throws InterruptedException {
		
		System.out.println("Washing Program 2 started");

		// Lock hatch
		myMachine.setLock(true);

		// Fill water
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 10));
		mailbox.doFetch();

		// Start spin
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));

		// Raise tempetarure to 40 degrees
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 40));

		// Wait 15 minutes
		// Thread.sleep((long) (15 * 60 * 1000 / mySpeed));

		// Reset machine
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		this.mailbox.doFetch();

		
		System.out.println("Pre-wash finished");
		
		//
		// Execute WashingProgram 1 with 90 degrees
		//

		// Fill water
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 10.0));
		this.mailbox.doFetch();

		// Set temp to 60
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 90.0));

		// Turn on spin
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));

		// Wait 30 min
		Thread.sleep((long) (30 * 60 * 1000 / mySpeed));  // TODO: uncomment

		// Drain machine
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		this.mailbox.doFetch();
		
		//Rinse machine 5 times
		int timesRinsed = 0;
		while(timesRinsed < 5)	{
			// Fill water
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 10.0));
			this.mailbox.doFetch();
			
			//Wait for two minutes
			Thread.sleep((long) (2 * 60 * 1000 / mySpeed));  // TODO: uncomment
			
			//Drain machine
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
			this.mailbox.doFetch();
			
			timesRinsed++;
		}
		
		//Centrifuge for 5 minutes
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));
		Thread.sleep((long) (5 * 60 * 1000 / mySpeed)); // TODO: uncomment
		
		//Stop spin
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		
		//Unlock machine
		myMachine.setLock(false);
		
		System.out.println("Washing Program 2 finished");
		
		

	}

}
