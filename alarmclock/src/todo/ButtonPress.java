package todo;

import done.ClockInput;
import done.ClockOutput;
import se.lth.cs.realtime.semaphore.Semaphore;

public class ButtonPress extends Thread {
	private static ClockInput input;
	private static Semaphore sem;
	private static SharedData data;

	public ButtonPress(ClockInput input, ClockOutput out, SharedData data) {
		sem = input.getSemaphoreInstance();
		ButtonPress.input = input;
		ButtonPress.data = data;
	}

	public void run() {
		while (true) {
			sem.take();
			if (input.getChoice() == 1) {
				data.setAlarm(input.getValue());
			} else if (input.getChoice() == 2) {
				data.setTime(input.getValue());
			}
			sem.give();
		}
	}
}
