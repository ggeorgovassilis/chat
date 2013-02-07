package name.georgovassilis.chat.service;

import name.georgovassilis.chat.model.dto.*;

public interface IChatService {

	ActiveUsersDTO getListOfActiveUsers();
	void logon(String username);
	void logoff(String username);
}
