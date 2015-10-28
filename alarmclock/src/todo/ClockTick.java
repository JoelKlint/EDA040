package todo;

public class ClockTick extends Thread {
	private SharedData data;

	public ClockTick(SharedData data) {
		this.data = data;
	}

	public void run() {
		while (true) {
			long t, diff;
			t = System.currentTimeMillis();
			data.secondTick();
			t += 1000;
			diff = t - System.currentTimeMillis();
			if (diff > 0)
				try {
					Thread.sleep(diff);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
}
