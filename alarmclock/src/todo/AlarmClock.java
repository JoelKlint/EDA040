package todo;

import done.ClockInput;
import done.ClockOutput;

public class AlarmClock extends Thread {

	private static ClockInput input;
	private static ClockOutput output;
	private static SharedData data;

	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		data = new SharedData(output, input);
		ClockTick tick = new ClockTick(data);
		ButtonPress press = new ButtonPress(input, output, data);
		press.start();
		tick.start();
	}

	// The AlarmClock thread is started by the simulator. No
	// need to start it by yourself, if you do you will get
	// an IllegalThreadStateException. The implementation
	// below is a simple alarmclock thread that beeps upon
	// each keypress. To be modified in the lab.
	// public void run() {
	// while (true) {
	// sem.take();
	// output.doAlarm();
	//
	// }
	// }
}
