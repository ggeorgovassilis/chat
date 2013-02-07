package name.georgovassilis.chat.model.domain;

import javax.persistence.Entity;
import javax.persistence.Id;;

@Entity
public class User {

	public enum Status {
		offline, online;
	};

	@Id
	private String login;
	private Status status = Status.offline;

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
