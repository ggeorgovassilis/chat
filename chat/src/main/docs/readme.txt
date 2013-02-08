todo
---------------------

nicer UI
load history with user on login
show notification for new unread message
unread message count is not updated because hashcode on userlist doesnt change unless users log on or out

Architecture:
-----------------

JSON service running atop an in-memory database.

Chat protocol:
-----------------
logon with username
logoff with username
getMessagesFor username, lastReadMessageId
getMessagesBetween sender, recipient, lastReadMessageId
sendMessage sender, recipient, text


Assumptions/Known issues:
-----------------
No authentications, authorisation or access control: knowledge of the login name allows full access to everything
No persistence required; restarting the service erases everything
Doing polling
Doesn't recuperate well from errors
Chat history can become arbitrarily large and is always retransmitted
Hash function is simple, collisions are likely

Extending
------------------

Protocol with deltas: server announces snapshot IDs to the client; client then requests deltas from the last known snapshot ID.
Snapshots consist of the online user list, received messages
Multiple chat servers: several types:
1. connection endpoints handle just the TCP/HTTP connections (websockets, long polling) with the clients. 
   Async implementation capable of handling tens of thousands of connections. They talk over fast messaging queues with message hubs
2. message hubs handle logic, persist and distribute messages to other message hubs and connection endpoints. They implement an overlapping modulo N partitioning
scheme in which multiple hubs are responsible for any given user or message.

CAP is hard; I'd prioritise them as such: availability + partition tolerance > consistency
Humans are used to errorneous communication and prefer a bad yet responsive channel over a good yet slow channel   