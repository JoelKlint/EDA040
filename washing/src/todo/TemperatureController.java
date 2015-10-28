package todo;

import se.lth.cs.realtime.*;
import done.AbstractWashingMachine;

public class TemperatureController extends PeriodicThread {
	AbstractWashingMachine mach;
	TemperatureEvent event;
	private double upperlimit, lowerlimit;

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: Fix period time
		this.mach = mach;
	}

	public void perform() {
		TemperatureEvent newEvent = (TemperatureEvent) this.mailbox.tryFetch();

		// We have an new event in mailbox
		if (newEvent != null) {
			event = newEvent;
			mach.setHeating(false);
		}

		// We have an event
		if (event != null) {
			int mode = event.getMode();
			
			switch (mode){
			case TemperatureEvent.TEMP_SET:
				upperlimit = event.getTemperature();
				lowerlimit = upperlimit - 2;
				// if too hot
				if(mach.getTemperature() > upperlimit)
					mach.setHeating(false);
				else if(mach.getTemperature() < lowerlimit)
				mach.setHeating(true);
				break;
			case TemperatureEvent.TEMP_IDLE:
				double temp = event.getTemperature();
				break;
			}

		}

	}
}