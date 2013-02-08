package name.georgovassilis.chat.persistence;


import name.georgovassilis.chat.model.domain.*;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface IUserDAO extends CrudRepository<User,String>{

	User findByLogin(String login);
	List<User> findByStatus(User.Status status);
}
