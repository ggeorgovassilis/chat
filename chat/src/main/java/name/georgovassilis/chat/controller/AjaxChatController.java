package name.georgovassilis.chat.controller;

import org.slf4j.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import name.georgovassilis.chat.model.dto.*;
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

	@RequestMapping(value = "/users/{recipient}/messages/from/{sender}", method = RequestMethod.GET)
	public @ResponseBody MessageListDTO getMessagesBetweenUsers(@PathVariable String recipient, @PathVariable String sender, int lastReadMessageId) {
		MessageListDTO result = chatService.getMessagesBetween(recipient, sender, lastReadMessageId);
		return result;
	}

	@RequestMapping(value = "/users/{recipient}/messages/from/{sender}", method = RequestMethod.POST)
	public @ResponseBody void getMessagesBetweenUsers(@PathVariable String recipient, @PathVariable String sender, String text) {
		chatService.sendMessage(sender, recipient, text);
	}
}
