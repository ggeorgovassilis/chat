	
$(function(){
	$.fn.editable.defaults.mode = 'inline';
	$("#username").editable({
		success:function(){
			$("#username").editable("disable");
			sync();
			window.setInterval("sync()",2000);
		}
	});
});

var _dummy = $("<div></div>");

function escape(text){
	return _dummy.text(text).html();
}

function getCurrentUserLogin(){
	return $("#username").text();
}

function getCurrentChatPartner(){
	return $("#onlineusers option:selected").val();
}

function updateOnlineUsers(){
	$.ajax({
		dataType: "json",
		url: "ajax/users/list",
		success: function(data){
			session.onlineUsers = data;
			var onlineusers = $("#onlineusers");
			var selected = getCurrentChatPartner();
			onlineusers.empty();
			for (var i=0;i<data.activeUsers.length;i++){
				var user = data.activeUsers[i]; 
				var unreadMessageCount = getUnreadMessageCountWithPartner(user);
				var line = $("<option value='"+escape(user)+"'>"+escape(user+" ("+unreadMessageCount)+")</option>");
				if (user == selected)
					line.attr("selected","true");
				onlineusers.append(line);
			}
			if (data.activeUsers.length == 0){
				onlineusers.append("<div class=message>No users online</div>");
			}
		}
		});
}

function getUnreadMessageCountWithPartner(user){
	var conversation = session.conversations[user];
	if (!conversation)
		return 0;
	var count = 0;
	for (var i=0;i<conversation.messages.length;i++)
		if (!(conversation.messages[i].seen==true))
			count++;
	return count;
}

function sendTextToChat(){
	var line = $("#input").val();
	$("#input").val("");
	var recipient = getCurrentChatPartner();
	$.ajax({
		dataType: "json",
		type:"POST",
		url: "ajax/users/"+recipient+"/messages/from/"+getCurrentUserLogin()+"?text="+line,
		success: function(data){
		}
		});
}

var previousUserTalking = "";

function renderMessageLine(msg){
	var wnd = $("#chatwindow");
	var css ="message";
	if (previousUserTalking == msg.sender)
		css+=" same-sender";
	previousUserTalking = msg.sender;
	var line = $("<tr class='"+css+"'><td class=sender>"+escape(msg.sender)+"</td><td class=text>"+escape(msg.text)+"</td></tr>");
	line.insertBefore($("#chatwindow .input"));
	msg.seen = true;
}

function doesMessageInvolveUser(message, login){
	return (message.sender == login || message.recipient == login);	
}

function rememberMessage(message){
	var otherUser = message.sender;
	if (otherUser == getCurrentUserLogin())
		otherUser = message.recipient;
	var conversation = session.getConversationWith(otherUser);
	conversation.messages.push(message);
	session.lastReadMessageId = Math.max(session.lastReadMessageId, message.id);
}

function showChatWith(user){
	var conversation = session.getConversationWith(user);
	var wnd = $("#chatwindow");
	var me = getCurrentUserLogin();
	if (session.currentChatPartnerWindow!=user){
		previousUserTalking = "";
		session.currentChatPartnerWindow=user;
		$("#chatwindow .message").remove();
		for (var i=0;i<conversation.messages.length;i++)
			renderMessageLine(conversation.messages[i]);
	}
	$.ajax({
		dataType: "json",
		url: "ajax/users/"+me+"/messages?lastReadMessageId="+session.lastReadMessageId,
		success: function(data){
			for (var i=0;i<data.list.length;i++){
				var message = data.list[i];
				rememberMessage(message);
				if (doesMessageInvolveUser(message, user)){
					renderMessageLine(message);
				}
			}
		}
		});
}

function sync(){
	showChatWith(getCurrentChatPartner());
	updateOnlineUsers();
}

$("#input").keypress(function(e){
	 if(e.which === 13){
		 sendTextToChat();
	 }
});

$("#onlineusers").change(function(e){
	var user = getCurrentChatPartner();
	showChatWith(user);
});

var session = {
		currentChatPartnerWindow:"",
		getConversationWith:function(user){
			var c = session.conversations[user]||{messages:[]};
			session.conversations[user] = c;
			return c;
		},
		lastReadMessageId:-1,
		onlineUsers:{hash:0},
		conversations:{}
};
