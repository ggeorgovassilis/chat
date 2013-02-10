package name.georgovassilis.chat.model.domain;

import javax.persistence.*;
import java.util.Date;
import java.io.Serializable;

/**
 * A message is a piece of text sent between users
 * @author george georgovassilis
 *
 */
@Entity
public class Message implements Serializable {

	public enum Status {
		unread, read;
	}

	@Id
	private Integer id;
	/**
	 * Message payload. No restrictions on format or size.
	 */
	private String text;
	
	/**
	 * Date the message was sent
	 */
	private Date sent;

	/**
	 * ID of recipient
	 */
	private String recipient;
	
	/**
	 * ID of sender
	 */
	private String sender;
	
	/**
	 * Message status. Initially unread.
	 */
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
