package server ;
import java.net.*;
import java.util.Map;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
public class Server {
	private static Map<String, Connection> connectionMap = new ConcurrentHashMap<String, Connection>();
    public static void sendBroadcastMessage(Message message) {
    	for(Map.Entry<String, Connection>  pair : connectionMap.entrySet()) {
    		try {
    		((Connection) pair.getValue()).send(message);
    		} catch(IOException e ) {
    			ConsoleHelper.writeMessage("The message couldn't be sent ");
    		}
    	}
    }
	
    	public static void main(String[] args){
        ServerSocket serverSocket = null ;
        try{
        int port  = ConsoleHelper.readInt();
        serverSocket = new ServerSocket(port);
        ConsoleHelper.writeMessage("The server is running ");
        while(true){
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket); 
                handler.start();
            }
        } catch(Exception e){
            try{
            if( serverSocket != null) serverSocket.close();
            }
            catch(IOException ex){
                
            }
            ConsoleHelper.writeMessage(e.getMessage());
        }
        
    }
    private static class Handler extends Thread {
        private final Socket socket ;
        public Handler(Socket  socket){
            this.socket = socket;
            
        }
        @Override
        public void run(){
            
        }
    }
    
}