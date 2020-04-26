package utilities;



import java.io.*;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;

public class Connection implements Closeable{
	private final Socket socket ;
	private final  ObjectOutputStream out ;
	private final ObjectInputStream in ;
	private ReentrantLock outputlock = new ReentrantLock();
	private ReentrantLock inputlock = new ReentrantLock();
	public Connection(Socket socket) throws IOException {
		this.socket = socket;
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new  ObjectInputStream(socket.getInputStream());
		
	}
	public void send(Message message) throws IOException{
		synchronized(out) {
		out.writeObject(message);
	}
		
		
	}
	public Message receive() throws IOException, ClassNotFoundException{
		Message message = null;
		synchronized(in) {
			 message =(Message) in.readObject();		
			return message;
			}
		}
	public SocketAddress getRemoteSocketAddress() {
		return socket.getRemoteSocketAddress();
	}
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		out.close();
		in.close();
		socket.close();
	}

	}
	
	


