package name.georgovassilis.chat.model.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Models a user. Users are identified by their unique login
 * @author george georgovassilis
 *
 */
@Entity
public class User implements Serializable{

	public enum Status {
		offline, online;
	};


	/**
	 * Unique user login. No restrictions on format or size.
	 */
	@Id
	private String login;
	/**
	 * User status: online or offline
	 */
	private Status status = Status.offline;
	
	/**
	 * Last message a user has read
	 */
	private int lastMessageRead;

	public int getLastMessageRead() {
		return lastMessageRead;
	}

	public void setLastMessageRead(int lastMessageRead) {
		this.lastMessageRead = lastMessageRead;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
