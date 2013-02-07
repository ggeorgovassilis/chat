package name.georgovassilis.chat.service.impl;


import name.georgovassilis.chat.model.dto.ActiveUsersDTO;
import name.georgovassilis.chat.model.domain.User;
import name.georgovassilis.chat.model.domain.User.Status;
import name.georgovassilis.chat.service.IChatService;
import name.georgovassilis.chat.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

@Service("ChatService")
@Transactional
public class ChatServiceImpl implements IChatService{
	
	@Autowired
	private IUserDAO userDAO;

	@Override
	public ActiveUsersDTO getListOfActiveUsers() {
		List<User> list = userDAO.findByStatus(Status.online);
		ActiveUsersDTO activeUsers = new ActiveUsersDTO();
		for (User user:list){
			activeUsers.getActiveUsers().add(user.getLogin());
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

}
