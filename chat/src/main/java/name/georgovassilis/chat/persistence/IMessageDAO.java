package name.georgovassilis.chat.persistence;

import name.georgovassilis.chat.model.domain.*;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

public interface IMessageDAO extends CrudRepository<Message,String>{

	@Query("select m from Message m where (m.recipient = ?1 or m.sender = ?1) and m.id > ?2 order by m.sent")
	List<Message> findMessagesForRecipient(String recipient, int minId);

	@Query("select m from Message m where ((m.recipient = ?1 and m.sender = ?2 ) or (m.recipient = ?2 and m.sender = ?1 )) and m.id > ?3 order by m.sent")
	List<Message> findMessagesBetweenUsers(String recipient, String sender, int minId);
}
