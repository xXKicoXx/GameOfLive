package games.conways;

public class ConwaysGoL {

	public ConwaysGoL() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new Runnable() {
			public void run() {
				new GUI();
			}
		}.run();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}

}
