package todo;

import done.*;

public class WashingController implements ButtonListener {

	private AbstractWashingMachine theMachine;
	private double theSpeed;
	private SpinController spin;
	private TemperatureController temp;
	private WaterController water;
	private WashingProgram currentProgram;

	public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		this.theMachine = theMachine;
		this.theSpeed = theSpeed;
		spin = new SpinController(this.theMachine, this.theSpeed);
		temp = new TemperatureController(this.theMachine, this.theSpeed);
		water = new WaterController(this.theMachine, this.theSpeed);
		spin.start();
		temp.start();
		water.start();
		currentProgram = new WashingProgram3(this.theMachine, this.theSpeed, temp, water, spin);
	}

	public void processButton(int theButton) {

		switch (theButton) {
		case 0:
			currentProgram.interrupt();
			try {
				currentProgram.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			currentProgram = new WashingProgram1(theMachine, theSpeed, temp, water, spin);
			currentProgram.start();
			break;
		case 2:
			currentProgram = new WashingProgram2(theMachine, theSpeed, temp, water, spin);
			currentProgram.start();
			break;
		case 3:
			currentProgram = new WashingProgram3(theMachine, theSpeed, temp, water, spin);
			currentProgram.start();
			break;
		}
	}
}