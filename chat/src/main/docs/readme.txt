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

doing http poll
doesn't recuperate well from errors

Assumptions:
-----------------
No authentications, authorisation or access control: knowledge of the login name allows full access to everything
No persistence required; restarting the service erases everything
