package telran.multithreading.symbolsprinter;

import java.util.Scanner;

public class SymbolsPrinterContr {
	
public static void main(String[] args){
		
		Scanner newScanner = new Scanner (System.in);
		SymbolsPrinter printer = new SymbolsPrinter("abc", 1000);
		printer.setDaemon(true);;
		printer.start();
		
		while (!newScanner.nextLine().equals("q")) {
			printer.interrupt();
		}

	}
	
	
	}
	
	
	

