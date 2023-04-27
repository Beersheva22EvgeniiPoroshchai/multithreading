package telran.multithreading;

import java.util.ArrayList;
import java.util.List;

import telran.multithreading.consumers.Receiver;
import telran.multithreading.producers.Sender;

public class SenderReceiversAppl {

	private static final int N_MESSAGES = 10_000;
	private static final int N_RECEIVERS = 30;

	public static void main(String[] args) throws InterruptedException {
		MessageBox mesBox = new MessageBox();
		Sender sender = new Sender(mesBox, N_MESSAGES);
		sender.start();
		
		List<Receiver> receivers = new ArrayList<>();
		
		for (int i = 0; i < N_RECEIVERS; i++) {
			
			Receiver receiver = new Receiver(mesBox);
			receivers.add(receiver);
			receiver.start();
		
		}
		sender.join();
		receivers.forEach(r -> r.interrupt());
		receivers.forEach(r -> {
			try {
				r.join();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		});
		System.out.println("Messages amount: " + Receiver.count);

	
	
		}
		
		

	}


