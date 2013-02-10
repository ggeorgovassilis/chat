package name.georgovassilis.chat.persistence;


import name.georgovassilis.chat.model.domain.*;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Manages user persistence
 * @author george georgovassilis
 *
 */
public interface IUserDAO extends CrudRepository<User,String>{

	/**
	 * Find a user by login, otherwise return null
	 * @param login
	 * @return
	 */
	User findByLogin(String login);
	
	/**
	 * Find users by their status
	 * @param status
	 * @return
	 */
	List<User> findByStatus(User.Status status);
}
