package todo;

import done.ClockInput;
import done.ClockOutput;
import se.lth.cs.realtime.semaphore.MutexSem;

public class SharedData {

	private int alarmsToGo;

	private int currentHours;
	private int currentMinutes;
	private int currentSeconds;

	private int currentTime;
	private int alarmTime;

	private ClockOutput out;
	private ClockInput input;
	private MutexSem mutex;

	public SharedData(ClockOutput out, ClockInput input) {
		this.out = out;
		this.input = input;
		mutex = new MutexSem();

		mutex.take();
		long milliTime = System.currentTimeMillis();
		fillCurrentTimeFromSystemTime(milliTime);
		showTime();
		mutex.give();
	}

	private void fillCurrentTimeFromSystemTime(long milliSeconds) {
		long rest = milliSeconds;
		// hh
		long totHours = rest / 3600000;
		currentHours = (int) totHours % 24;
		currentHours += 2;

		// MM convert
		long totMinutes = rest / 60000;
		currentMinutes = (int) totMinutes % 60;

		// SS convert
		long totSeconds = rest / 1000;
		currentSeconds = (int) totSeconds % 60;
	}

	public void secondTick() {
		mutex.take();
		currentSeconds++;
		if (currentSeconds == 60) {
			currentMinutes++;
			currentSeconds = 0;

			if (currentMinutes == 60) {
				currentHours++;
				currentMinutes = 0;

				if (currentHours == 24) {
					currentHours = 0;
					currentMinutes = 0;
					currentSeconds = 0;
				}
			}
		}
		showTime();
		if (currentTime == alarmTime && input.getAlarmFlag()) {
			alarmsToGo = 19;
			out.doAlarm();
		}

		else if (alarmsToGo > 0) {
			out.doAlarm();
			alarmsToGo--;
		}
		mutex.give();

	}

	private void showTime() {
		// Felhantering
		String hours;
		if (currentHours < 10) {
			hours = "0" + currentHours;
		} else {
			hours = "" + currentHours;
		}
		String minutes;
		if (currentMinutes < 10) {
			minutes = "0" + currentMinutes;
		} else {
			minutes = "" + currentMinutes;
		}
		String seconds;
		if (currentSeconds < 10) {
			seconds = "0" + currentSeconds;
		} else {
			seconds = "" + currentSeconds;
		}

		// print
		String totTime = hours + minutes + seconds;
		currentTime = Integer.parseInt(totTime);

		out.showTime(currentTime);

	}

	public void setAlarm(int value) {
		alarmsToGo = 0;
		alarmTime = value;

	}

	public void setTime(int value) {
		alarmsToGo = 0;
		value = input.getValue();
		String newTime = String.valueOf(value);
		String hours;
		String minutes;
		String seconds;
		if (newTime.length() == 5) {
			hours = newTime.substring(0, 1);
			minutes = newTime.substring(1, 3);
			seconds = newTime.substring(3, 5);
		} else {
			hours = newTime.substring(0, 2);
			minutes = newTime.substring(2, 4);
			seconds = newTime.substring(4, 6);
		}
		mutex.take();
		currentHours = Integer.parseInt(hours);
		currentMinutes = Integer.parseInt(minutes);
		currentSeconds = Integer.parseInt(seconds);
		mutex.give();
	}
}
