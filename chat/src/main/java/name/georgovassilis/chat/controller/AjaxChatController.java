package name.georgovassilis.chat.controller;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import name.georgovassilis.chat.model.dto.ActiveUsersDTO;
import name.georgovassilis.chat.service.*;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AjaxChatController {

	private static final Logger logger = LoggerFactory.getLogger(AjaxChatController.class);
	@Autowired
	private IChatService chatService;

	@RequestMapping(value = "/users/list", method = RequestMethod.GET)
	public @ResponseBody
	ActiveUsersDTO getActiveUsers() {
		return chatService.getListOfActiveUsers();
	}

	@RequestMapping(value = "/users/logon")
	public @ResponseBody void logonUser(String value) {
		chatService.logon(value);
	}

	@RequestMapping(value = "/users/logoff/{name}", method = RequestMethod.GET)
	public @ResponseBody void logoffUser(@PathVariable String name) {
		chatService.logoff(name);
	}

}
