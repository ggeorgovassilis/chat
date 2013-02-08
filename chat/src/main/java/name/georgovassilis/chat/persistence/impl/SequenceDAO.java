package name.georgovassilis.chat.persistence.impl;
import name.georgovassilis.chat.persistence.ISequenceDAO;
import org.springframework.stereotype.Repository;

/**
 * Currently having a problem with JPA and hsqldb - can't get auto sequence. Since this is an in-memory database anyway
 * there is no harm done if we fake squences like this
 * @author george
 *
 */
@Repository("SequenceDAO")
public class SequenceDAO implements ISequenceDAO{

	private int currentCounter = 0;
	private Object mutex = new Object();
	
	@Override
	public int getNextInSequence() {
		synchronized(mutex){
			return ++currentCounter;
		}
	}

}
