package name.georgovassilis.chat.model.dto;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class MessageListDTO implements Serializable{

	private List<MessageDTO> list = new ArrayList<MessageDTO>();

	public List<MessageDTO> getList() {
		return list;
	}

	public void setList(List<MessageDTO> list) {
		this.list = list;
	}
	
}
