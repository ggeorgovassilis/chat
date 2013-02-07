package name.georgovassilis.chat.model.dto;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class ActiveUsersDTO implements Serializable{

	private List<String> activeUsers = new ArrayList();

	public List<String> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<String> activeUsers) {
		this.activeUsers = activeUsers;
	}
}
