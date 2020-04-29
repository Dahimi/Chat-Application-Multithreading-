package client;

public class ClientGuiController extends Client{
    private ClientGuiModel model = new ClientGuiModel();
    private ClientGuiView  view = new ClientGuiView(this);
    
    public static void main(String[] args) {
    	new ClientGuiController().run();
    }
    public void run() {
    	getSocketThread().run();
    }
    @Override
    protected String getServerAddress() {
    	// TODO Auto-generated method stub
    	return view.getServerAddress();
    }
    @Override
    protected int getServerPort() {
    	// TODO Auto-generated method stub
    	return view.getServerPort();
    }
    @Override
    protected String getUserName() {
    	// TODO Auto-generated method stub
    	return view.getUserName();
    }
    protected SocketThread getSocketThread(){
        return new GuiSocketThread();
    }
    public ClientGuiModel getModel() {
		// TODO Auto-generated method stub
		return this.model;
	}
    public class GuiSocketThread extends Client.SocketThread{
    	
    	
        protected  void processIncomingMessage(String message){
            model.setNewMessage(message);
            view.refreshMessages();
        }
        protected void informAboutAddingNewUser(String userName){
            model.addUser(userName);
            view.refreshUsers();
        }
        protected void informAboutDeletingNewUser(String userName){
            model.deleteUser(userName);
            view.refreshUsers();
        }
        protected void notifyConnectionStatusChanged(boolean clientConnected){
            view.notifyConnectionStatusChanged(clientConnected);
        }
    }
	    
}
