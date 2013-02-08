package name.georgovassilis.chat.service.impl;


import name.georgovassilis.chat.model.dto.*;
import name.georgovassilis.chat.model.domain.*;
import name.georgovassilis.chat.model.domain.User.Status;
import name.georgovassilis.chat.service.IChatService;
import name.georgovassilis.chat.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service("ChatService")
@Transactional
public class ChatServiceImpl implements IChatService{
	
	@Autowired
	private IUserDAO userDAO;
	@Autowired
	private IMessageDAO messageDAO;

	@Autowired
	private ISequenceDAO sequenceDAO;

	@Override
	public ActiveUsersDTO getListOfActiveUsers() {
		List<User> list = userDAO.findByStatus(Status.online);
		ActiveUsersDTO activeUsers = new ActiveUsersDTO();
		for (User user:list){
			activeUsers.getActiveUsers().add(user.getLogin());
			activeUsers.setHash(activeUsers.getHash()+user.getLogin().hashCode());
		}
		return activeUsers;
	}

	@Override
	public void logon(String username) {
		User user = userDAO.findByLogin(username);
		if (user == null){
			user = new User();
			user.setLogin(username);
		}
		user.setStatus(Status.online);
		userDAO.save(user);
	}

	@Override
	public void logoff(String username) {
		User user = userDAO.findByLogin(username);
		if (user!=null){
			user.setStatus(Status.offline);
			userDAO.save(user);
		}
	}

	@Override
	public void sendMessage(String sender, String recipient, String text) {
		Message message = new Message();
		message.setId(sequenceDAO.getNextInSequence());
		message.setRecipient(recipient);
		message.setSender(sender);
		message.setText(text);
		message.setSent(new Date());
		messageDAO.save(message);
	}
	
	protected MessageDTO convert(Message m){
		MessageDTO dto = new MessageDTO();
		dto.setSender(m.getSender());
		dto.setRecipient(m.getRecipient());
		dto.setId(m.getId());
		dto.setSent(m.getSent());
		dto.setText(m.getText());
		return dto;
	}

	@Override
	public MessageListDTO getMessagesFor(String user, int lastReadMessageId) {
		List<Message> messages = messageDAO.findMessagesForRecipient(user, lastReadMessageId);
		MessageListDTO result = new MessageListDTO();
		for (Message m:messages){
			result.getList().add(convert(m));
		}
		return result;
	}

	@Override
	public MessageListDTO getMessagesBetween(String recipient, String sender, int lastReadMessageId) {
		List<Message> messages = messageDAO.findMessagesBetweenUsers(recipient, sender, lastReadMessageId);
		MessageListDTO result = new MessageListDTO();
		for (Message m:messages){
			result.getList().add(convert(m));
		}
		return result;
	}

}
