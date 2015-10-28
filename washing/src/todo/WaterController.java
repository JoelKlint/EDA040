package todo;

import done.AbstractWashingMachine;
import se.lth.cs.realtime.PeriodicThread;
import se.lth.cs.realtime.event.RTEvent;

public class WaterController extends PeriodicThread {
	AbstractWashingMachine mach;
	private RTEvent currentEvent;
	private int currentMode;
	private double targetLevel;
	private WashingProgram washingProgram;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: Fix period time
		this.mach = mach;
	}

	public void perform() {
		if ((currentEvent = mailbox.tryFetch()) != null && currentEvent instanceof WaterEvent) {
			switch (((WaterEvent) currentEvent).getMode()) {
			case WaterEvent.WATER_FILL:
				currentMode = WaterEvent.WATER_FILL;
				startFill();
				targetLevel = ((WaterEvent) currentEvent).getLevel() / 20;
				washingProgram = (WashingProgram) currentEvent.getSource();
				break;

			case WaterEvent.WATER_DRAIN:
				currentMode = WaterEvent.WATER_DRAIN;
				startDrain();
				targetLevel = 0;
				washingProgram = (WashingProgram) currentEvent.getSource();
				break;

			case WaterEvent.WATER_IDLE:
				currentMode = WaterEvent.WATER_IDLE;
				allPumpsOff();
				break;
			}
		}

		if ((currentMode == WaterEvent.WATER_FILL && mach.getWaterLevel() >= targetLevel)
				|| (currentMode == WaterEvent.WATER_DRAIN && mach.getWaterLevel() <= targetLevel)) {
			allPumpsOff();
			washingProgram.putEvent(new AckEvent(this));
		}

	}

	private void allPumpsOff() {
		mach.setFill(false);
		mach.setDrain(false);
	}

	private void startFill() {
		mach.setDrain(false);
		mach.setFill(true);
	}

	private void startDrain() {
		mach.setFill(false);
		mach.setDrain(true);
	}

}