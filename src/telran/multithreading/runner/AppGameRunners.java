package telran.multithreading.runner;

import telran.multithreading.*;

import java.util.stream.IntStream;

public class AppGameRunners {

	private static final int MAX_THREADS = 20;
	private static final int MIN_DISTANCE = 10;
	private static final int MAX_DISTANCE = 1000;
	private static final int MIN_SLEEP = 2;
	private static final int MAX_SLEEP = 5;

	public static void main(String[] args) {
		
		InputOutput io = new StandardInputOutput();
		Item[] items = getItems();
		
		Menu menu = new Menu ("Game runner", items);
		
		menu.perform(io);
		
	}
	
	private static Item[] getItems() {
		Item [] res = {
				Item.of("Start game", AppGameRunners:: startGame),
				Item.exit()
		};
		return res;
		
		
		
	}
	
	static void startGame (InputOutput io) {
		int nThreads = io.readInt("Enter an ammount racers", "Wrong ammount",
				2, MAX_THREADS);
		int distance = io.readInt("Enter distance", "Wrong distance",
				MIN_DISTANCE, MAX_DISTANCE);
		Race race = new Race (distance, MIN_SLEEP, MAX_SLEEP);
		Runner[] runners = new Runner[nThreads];
		startRunners(runners, race);
		joinRunners(runners);
		displayWinner(race);
		
		
		
	}
	
	
	
	private static void displayWinner(Race race) {
		System.out.println("Runner " + race.getWinner() + " win");
		
	}

	private static void joinRunners(Runner[] runners) {
		IntStream.range(0, runners.length).forEach( i -> {
			try {
				runners[i].join();
				
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}
			
			
		});
		
	}

	private static void startRunners(Runner[] runners, Race race) {
		IntStream.range(0, runners.length).forEach(i -> {
			
			runners[i] = new Runner(race, i + 1);
			runners[i].start();
			
			
	});
	
		

	}
}


