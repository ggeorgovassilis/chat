package name.georgovassilis.chat.service;

import name.georgovassilis.chat.model.dto.*;

public interface IChatService {

	ActiveUsersDTO getListOfActiveUsers();
	void logon(String username);
	void logoff(String username);
	void sendMessage(String userFrom, String userTo, String text);
	MessageListDTO getMessagesFor(String user, int lastReadMessageId);
	MessageListDTO getMessagesBetween(String recipient, String sender, int lastReadMessageId);
}
