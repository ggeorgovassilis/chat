package name.georgovassilis.chat.model.domain;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;

@Entity
public class Message implements Serializable {

	public enum Status {
		unread, read;
	}

	@Id
	private Integer id;
	private String text;
	private Date sent;
	private String recipient;
	private String sender;
	private Status status = Status.unread;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
