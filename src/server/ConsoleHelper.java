package server;

import java.io.*;

public class ConsoleHelper {
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public static void writeMessage(String message) {
		
			
				System.out.println(message);
			

	}
	public static String readString() {
		while(true) {
		try {
			return reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			writeMessage("An error occurred while trying to enter text. Try again.");
			
		}}
		
	}
	public static int readInt() {
		String stringNumber = readString();
		try {
		return Integer.valueOf(stringNumber);
	}	 catch(NumberFormatException e) {
		writeMessage("An error while trying to enter a number. Try again.");
		readInt();
	}
	
	return 0 ;
	}}

