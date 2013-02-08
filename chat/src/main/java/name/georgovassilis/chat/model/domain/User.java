package name.georgovassilis.chat.model.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class User implements Serializable{

	public enum Status {
		offline, online;
	};

	@Id
	private String login;
	private Status status = Status.offline;
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
