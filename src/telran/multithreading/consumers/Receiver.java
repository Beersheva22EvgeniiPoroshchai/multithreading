package telran.multithreading.consumers;

import java.util.concurrent.atomic.AtomicInteger;

import telran.multithreading.MessageBox;

public class Receiver extends Thread {
	
	private MessageBox messageBox;
	public static AtomicInteger count = new AtomicInteger(0);


	public Receiver(MessageBox messageBox) {
	this.messageBox = messageBox;

}
		private void display(String msg) {
		System.out.printf("thread: %s; received message: %s\n", getName(), msg);
		count.incrementAndGet();
		
	}
	
	
	@Override
	public void run() {
		
			while (true) {
			String msg;
			try {
				msg = messageBox.take();
				display (msg);
			} catch (InterruptedException e) {
				do {
					msg = messageBox.get();
					if (msg != null) {
						display(msg);
					}
					
				} while (msg != null);
					
				break;

					
				}
			}
	}
}
