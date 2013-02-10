package name.georgovassilis.chat.model.dto;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * List of online users 
 * @author george georgovassilis
 *
 */
public class ActiveUsersDTO implements Serializable{

	private List<String> activeUsers = new ArrayList();
	private int hash;

	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	public List<String> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<String> activeUsers) {
		this.activeUsers = activeUsers;
	}
}
