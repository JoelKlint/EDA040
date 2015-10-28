package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;

public class SpinController extends PeriodicThread {
	AbstractWashingMachine mach;
	RTEvent currentEvent;
	int currentAction, currentDirection, directionChangeDelay = 60000, directionChangeTimer;
	long lastDirectionChange;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: Fix period time
		this.mach = mach;
		directionChangeDelay = (int) (directionChangeDelay / speed);
	}

	public void perform() {
		if ((currentEvent = mailbox.tryFetch()) != null && currentEvent instanceof SpinEvent) {
			switch (((SpinEvent) currentEvent).getMode()) {

			case SpinEvent.SPIN_SLOW:
				currentAction = SpinEvent.SPIN_SLOW;
				currentDirection = AbstractWashingMachine.SPIN_LEFT;
				mach.setSpin(currentDirection);
				lastDirectionChange = System.currentTimeMillis();
				directionChangeTimer = 0;
				break;

			case SpinEvent.SPIN_FAST:
				currentAction = SpinEvent.SPIN_FAST;
				mach.setSpin(currentAction);
				break;

			case SpinEvent.SPIN_OFF:
				currentAction = SpinEvent.SPIN_OFF;
				mach.setSpin(currentAction);
				break;

			}
		}

		if (currentAction == SpinEvent.SPIN_SLOW
				&& (directionChangeTimer += System.currentTimeMillis() - lastDirectionChange) > directionChangeDelay) {
			switch (currentDirection) {
			case AbstractWashingMachine.SPIN_LEFT:
				currentDirection = AbstractWashingMachine.SPIN_RIGHT;
				break;
			case AbstractWashingMachine.SPIN_RIGHT:
				currentDirection = AbstractWashingMachine.SPIN_LEFT;
				break;
			}
			lastDirectionChange = System.currentTimeMillis();
			directionChangeTimer = 0;
			mach.setSpin(currentDirection);

		}
	}
}