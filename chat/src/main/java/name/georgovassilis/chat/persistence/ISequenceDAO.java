package name.georgovassilis.chat.persistence;

/**
 * Getting around minor HSQLDB nuisance for getting unique IDs 
 * @author george georgovassilis
 *
 */
public interface ISequenceDAO {

	/**
	 * Returns an increasing counter
	 * @return
	 */
	int getNextInSequence();
}
