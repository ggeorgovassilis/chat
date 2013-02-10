package name.georgovassilis.chat.service;

import name.georgovassilis.chat.model.dto.*;

/**
 * Function definition for the chat service
 * @author george georgovassilis
 *
 */
public interface IChatService {

	/**
	 * Get a list of all online users, including the requesting user
	 * @return
	 */
	ActiveUsersDTO getListOfActiveUsers();
	
	/**
	 * Signs a user on. If the user does not exist yet, then the account is created and set as online
	 * @param username
	 */
	void logon(String username);
	
	/**
	 * Sign a user off
	 * @param username
	 */
	void logoff(String username);
	
	/**
	 * Send a message
	 * @param userFrom Login of sender
	 * @param userTo Login of recipient
	 * @param text Text to send
	 */
	void sendMessage(String userFrom, String userTo, String text);
	
	/**
	 * Return an (empty) list of messages for designated recipient
	 * @param recipient Login of recipient
	 * @param lastReadMessageId Messages returned will have an ID greater than this. Set to -1 for all messages
	 * @return
	 */
	MessageListDTO getMessagesFor(String recipient, int lastReadMessageId);

	/**
	 * Return messages exchanged between two users. Semantics of {@link #getMessagesFor(String, int)} apply
	 * @param recipient
	 * @param sender
	 * @param lastReadMessageId
	 * @return
	 */
	MessageListDTO getMessagesBetween(String recipient, String sender, int lastReadMessageId);
}
