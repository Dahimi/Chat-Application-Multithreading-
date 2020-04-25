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
        private  Connection connection ;
        private  String userName ;
        public Handler(Socket  socket) throws IOException {
            this.socket = socket;
            
        }
        @Override
        public void run(){
            try {
            connection = new Connection(socket);
        	ConsoleHelper.writeMessage("new connection is established " + socket.getRemoteSocketAddress());
        	}
            catch(IOException e1) {
            	closeConnection(connection);
            	ConsoleHelper.writeMessage("An error occurred while communicating with the remote address." + socket.getRemoteSocketAddress());
            }
            try {
            userName = serverHandshake(connection);   
            }
            catch(IOException | ClassNotFoundException e2) {
            	ConsoleHelper.writeMessage("An error occurred while communicating with the remote address." + socket.getRemoteSocketAddress());
            	closeConnection(connection);
            }
            try {
            sendBroadcastMessage(new Message(MessageType.USER_ADDED, "New user added"));
            notifyUsers(connection, userName);
            serverMainLoop(connection, userName);
            }
            catch(IOException | ClassNotFoundException e) {
            	ConsoleHelper.writeMessage("An error occurred while communicating with the remote address." + socket.getRemoteSocketAddress());
            	closeConnection(connection);
            }
            finally {
            	if(connectionMap.containsKey(userName)) {
            	    connectionMap.remove(userName);
            	    sendBroadcastMessage(new Message(MessageType.USER_REMOVED , userName));
            	}
            	closeConnection(connection);
            }
           ConsoleHelper.writeMessage(" the connection with the remote address: " + socket.getRemoteSocketAddress() +" is closed" );
           
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
        private void notifyUsers(Connection connection, String userName) throws IOException{
            for(Map.Entry<String , Connection > pair : connectionMap.entrySet()){
                if(pair.getKey().equals(userName)) continue;
                connection.send(new Message(MessageType.USER_ADDED, pair.getKey()));
            }  
         }  
        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException{
        	  Message messageReceived = null ;
        	  while(true) {
        		  if((messageReceived = connection.receive()).getType() == MessageType.TEXT) {
        			  Message messageToBeSent = new Message(MessageType.TEXT, userName + ": "+ messageReceived.getData());
        			  sendBroadcastMessage(messageToBeSent);
        		  }
        		  else ConsoleHelper.writeMessage("message couldn't get sent");
        		  }
        }
        
        private boolean isValidResponse(Message response) {
        	return !(response == null || response.getType() != MessageType.USER_NAME );        	
        }
        private boolean isValidUsername(String username) {
        	return !(username.isEmpty() || connectionMap.containsKey(username));
        }
        private void closeConnection(Connection connection) {
        	if(connection != null) {
        		try {
        			connection.close();
        		 } catch(IOException ex) {
        			 
        		}
        	}
        }
    }
    
}
