package name.georgovassilis.chat.model.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link Message}
 * @author george georgovassilis
 *
 */
public class MessageDTO implements Serializable {

	private int id;
	private Date sent;
	private String sender;
	private String recipient;
	private String text;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	public String getSender() {
		return sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
