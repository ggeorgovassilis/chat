package name.georgovassilis.chat.persistence;

import name.georgovassilis.chat.model.domain.*;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Handles message persistence.
 * @author george georgovassilis
 *
 */
public interface IMessageDAO extends CrudRepository<Message,String>{

	/**
	 * Find all messages for a recipient after (not including) the message denoted by minId
	 * @param recipient login of recipient
	 * @param minId Find messages with IDs after minId. Set to -1 for all messages
	 * @return Returns (maybe empty) list of messages
	 */
	@Query("select m from Message m where (m.recipient = ?1 or m.sender = ?1) and m.id > ?2 order by m.sent")
	List<Message> findMessagesForRecipient(String recipient, int minId);

	/**
	 * Find all messages exchanged between recipient and sender, ordered by date. Same semantics for minId apply as in
	 * {@link #findMessagesForRecipient(String, int)}
	 * @param recipient
	 * @param sender
	 * @param minId
	 * @return
	 */
	@Query("select m from Message m where ((m.recipient = ?1 and m.sender = ?2 ) or (m.recipient = ?2 and m.sender = ?1 )) and m.id > ?3 order by m.sent")
	List<Message> findMessagesBetweenUsers(String recipient, String sender, int minId);
}
