package telran.multithreading.producers;

import telran.multithreading.MessageBox;

public class Sender extends Thread {
	
	private MessageBox mesBox;
	private int nMessages;
	
	
	public Sender(MessageBox mesBox, int nMessages) {
		this.mesBox = mesBox;
		this.nMessages = nMessages;
	}
	
	
	
	@Override
	public void run() {
		
		for (int i = 1; i <= nMessages; i++) {
			
			try {
				mesBox.put("message " + i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				
		}
		
	}

}
