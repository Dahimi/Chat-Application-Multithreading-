package client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utilities.ConsoleHelper;

public class BotClient extends Client {
	
	public static void main(String[] args) {
		new BotClient().run();
	}
	
	protected BotSocketThread getSocketThread() {
		return new BotSocketThread();
	}
	protected boolean shouldSendTextFromConsole() {
		return false;
	}
	protected String getUserName() {
		double random_number = Math.random() * 100;
		return 	"date_bot_" + random_number; 
	}
	public class BotSocketThread  extends SocketThread{
		protected void clientMainLoop() throws ClassNotFoundException, IOException {
			sendTextMessage("Hello, there. I'm a bot. I understand the following commands: date, day, month, year, time, hour, minutes, seconds.");
			super.clientMainLoop();
		}
		protected void processIncomingMessage(String message) {
			super.processIncomingMessage(message);
			String[] array = message.split(": ");
    		String username = array[0];
			String command = array[1];
			SimpleDateFormat dateFormat ;
			String format = null;
			switch(command) {
			case "date" : format =  "d.MM.YYYY";
				break ;
			case "day": format = "d";
			break ;
			case "month": format = "MMMM";
			break ;
			case "year": format = "YYYY";
			break ;
			case "time": format =  "H:mm:ss";
			break ;
			case "hour": format = "H";
			break ;
			case "minutes": format = "m";
			break ;
			case "seconds": format = "s";
			break ;
			}
			if(format != null) {
				dateFormat = new SimpleDateFormat(format);
				Date date = Calendar.getInstance().getTime();
				String timeResponse = dateFormat.format(date);
				String response = "Information for " + username + ": " + timeResponse;
				sendTextMessage(response);	
			}
			
		}
	}
}
