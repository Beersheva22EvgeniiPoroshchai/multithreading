package telran.multithreading.symbolsprinter;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SymbolsPrinter  extends Thread  {
	
	String str;
	long timeout;

	
	public SymbolsPrinter(String str, long timeout) {
		this.str = str;
		this.timeout = timeout;
	}
	
	@Override
	public void run() {
		char[] strToCharAr = str.toCharArray();
		int ind = 0;
		while(true) {
			System.out.println(strToCharAr[ind]);
			try {
				sleep(timeout);
			} catch (InterruptedException e) {
				ind++;
			}
		}
		
		
	}
		
}

	
	
			
		
		
	
	


