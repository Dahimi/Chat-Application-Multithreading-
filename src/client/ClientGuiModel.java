package client;

import java.util.*;

public class ClientGuiModel implements ClientModel {
	private final Set<String> allUserNames = new HashSet<String>();
	private String newMessage ;
	@Override
	public void addUser(String newUserName) {
		// TODO Auto-generated method stub
		allUserNames.add(newUserName);
	}

	@Override
	public void deleteUser(String userName) {
		// TODO Auto-generated method stub
		allUserNames.remove(userName);
	}

	public Set<String> getAllUserNames() {
		return Collections.unmodifiableSet(allUserNames);
	}

	public String getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(String newMessage) {
		this.newMessage = newMessage;
	}

}
