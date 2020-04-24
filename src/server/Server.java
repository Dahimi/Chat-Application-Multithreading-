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
        private final Connection connection ;
        public Handler(Socket  socket) throws IOException {
            this.socket = socket;
            connection = new Connection(socket);
        }
        @Override
        public void run(){
            
        }
        private String serverHandshake(Connection connection) throws IOException,ClassNotFoundException{
        	while(true) {
             connection.send(new Message(MessageType.NAME_REQUEST,"Enter your username" ));
             Message response = connection.receive();
             if(!isValidResponse(response)) continue;
             if(!isValidUsername(response.getData())) continue ;
             connectionMap.put(response.getData(), connection);
             connection.send(new Message(MessageType.NAME_ACCEPTED, "Request accepted"));
             return response.getData();
        	}
        	
        } 
        private boolean isValidResponse(Message response) {
        	return !(response == null || response.getType() != MessageType.USER_NAME );        	
        }
        private boolean isValidUsername(String username) {
        	return !(username.isEmpty() || connectionMap.containsKey(username));
        }
    }
    
}