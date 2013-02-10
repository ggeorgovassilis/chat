Introduction
------------------------------------

The following is a description of a simple, AJAX based chat server with a java backed which I am submitting as a work sample for your evaluation.
In the remainder of this document I lay out a component overview, describe design and implementation choices, point out shortcomings and elaborate on improvements.

Protocol overview
------------------------------------

The communication protocol foresees that a chat client polls a REST service and receives JSON responses:

- login via a HTTP post to URL: WEB_APP_BASE/ajax/users/logon?value=<username>
- sending a message to a recipient via a HTTP post to URL: WEB_APP_BASE/ajax/users/<recipient>/messages/from/<sender<
- receiving all newer messages starting after a known message ID: WEB_APP_BASE/ajax/users/<recipient>/messages?lastReadMessageId=<ID of last read message>
- receiving all messages from a sender after a known message ID: WEB_APP_BASE/ajax/users/<recipient>/messages/from/<sender>?lastReadMessageId=<ID of last read message>
- receiving a list of all logged in users: WEB_APP_BASE/ajax/users/list

Requests are stateless, URLs are non-cacheable

The protocol foresees the following entities:

- Users, which consist solely of their login name, which is also their ID.
- Messages which consist of an ID, the sender and recipient name, a date of their creation and the message text.
- Lists of users
- Lists of messages

Component overview: client
------------------------------------

The client is a single page AJAX application using jquery. The UI consists of three areas: login, online users and conversation with a selected user.
The client communicates with the server through HTTP requests, submits new messages and periodically polls for new messages. For every conversation it
initially requests the complete message history from the server; subsequent requests query only newer messages.

The client keeps a registry of past and current conversations which it initially requests from the server. When the user switches conversations by selecting a different
user from the online users list the conversation area is immediately populated with all past messages of the conversation (if any) and updated with any new messages
that arrive from that moment on.

Component overview: server
------------------------------------

The server consists of a Spring AJAX/REST controller backed by a transactional service, JPA DAOs and an in-memory HSQLDB database.
Web layer: The Spring binds several methods of the AjaxChatController to HTTP and takes over the conversion from DTOs to JSON. The AjaxChatController mostly passes requests
directly to the ChatService
Service layer: The ChatService implements business logic, queries DAOs and converts JPA managed entities to DTOs which are safe for transmitting to the client.
Persistence layer: Using declarative DAOs which are automatically implemented by spring-data and JPA. Messages are stored permanently in the persistence layer and can be
retrieved by clients at any time in the future.

The domain model consists of:

- Users with a login name which is also their identifier and an activity state (not used in the implementation)
- Messages with an ID, creation date, a status (not used), sender and recipient and the message payload

Assumptions/Known issues:
------------------------------------

No authentications, authorisation or access control: knowledge of the login name allows full access to everything
No server-side long-term persistence; restarting the application erases users and chat history
Doing HTTP polling
No error handling in protocol, server or client
Chat history can become arbitrarily large and is always retransmitted
No size limitation in requests or spam protection
Hash function is simple, collisions are likely
No JSONP
AJAX handling is not safe from cross-site request forgery
Application would require reverse proxy in production for setting correct HTTP headers (static content cacheable, AJAX URLs non-cacheable)
Servlet container needs to handle URL encoding; stock Tomcat installation will garble non-ASCII characters 
Client doesn't handle logoff or changing user names well

Improving upon the implementation 
------------------------------------

A robust implementation will most importantly require a protocol redesign which allows for error recovery and involves acknowledgement of receiving messages on both
the server and client side. Messages sent by the client will require an ID assigned by the client and servers will acknowledge reception of that message. Clients need
to be able to identify network errors (timeouts, error codes etc) and resume communication with the server. Receiving duplicate messages from the server should not be
a problem for clients since they can identify such messages by their ID. Likewise, servers should discard duplicate messages sent by clients.

The second most important improvement is the unification of service methods for querying for new messages and the list of online users under a single method: a method
that returns any changes in state since the last request, identified by a request ID. As such, user logons or logoffs could be modelled as chat messages with special content that the client interprets
in a different way.

The third improvement focuses on network scalability: clients should implement more efficient request techniques such as websockets and long HTTP polling which need be be
reflected on the server with similar endpoints (i.e. asynchronous 3.0 servlets) 

The third improvement focuses on server-side scalability: clients would connect to different service endpoints distributed amongst multiple datacenters.

The design identifies the following components:

- Clients which communicate over websockets, HTTP long polling or HTTP short polling (or for that matter any applicable network protocol) with the services

- Network endpoints which consist of asynchronous servlets following the servlet API 3.0 specification. 
Their role is to handle tens of thousands of client connections, provide simple statistics and bind clients to chat hubs. Clients connect to hubs based on 
preferences such as geographical proximity and network load. Clients know of several endpoints and connect to endpoints according to certain policies such as 
availability, load and proximity. Endpoints are stateless as far as messages are concerned: they do not retransmit messages on their own (apart from any 
retransmission of network packets foreseen by the underlying network protocol). Any retransmission necessary is initiated by hubs.

- Chat hubs implement business logic, perform user authentication, persist messages and route messages to other chat hubs. They do not maintain directly connections with
clients but instead receive and send messages from/to network endpoints and other hubs. Multiple hubs are responsible for a single client and its messages based on a partitioning
scheme (such as client.id modulo N). The scheme is known to all hubs, hence it is easy for a hub to contact other hubs when sending messages to a client or retrieving messages
from a hub. Hubs always inform other hubs in the same partition about changes they need to know.

A conversation between two clients C1 and C2 will involve at most two network endpoints N1 and N2 and two hubs H1 and H2:

C1 <-----> N1----H1-----H2----N2<-----> C2

Use cases:

Login
--------------
When a client connects to the system it knows of a built-in list of network endpoints which it tries to connect to. Endpoints might redirect the client to other endpoints.
Once a connection has been established, the client will try to authenticate with the system. The endpoint knows which hub to route traffic from the client to based on the ID
of the client and the partitioning scheme mentioned earlier. In case of a successful logon, the responsible hub will inform other hubs that the user came online.

Logoff
--------------
Either through a logoff or a timeout the hub marks the user as offline, informs other hubs and updates any running conversations.

Sending messages
---------------

When a client sends a message, it submits that message through an endpoint to its designated hub. This hub persists the message for the sender and routes it to one of the hubs
responsible for the recipient, who will try to deliver it to the recipient.

Receiving messages
---------------

A client either is constantly constantly connected to an endpoint or polls it; in either case the endpoint will generate at some point a request for new messages which it
sends to the hub responsible for the client. The hub will retrieve the message from its persistent store and deliver it to the client.

Acknowledging messages
-----------------

Once a client receives a message it acknowledges it by sending a respective message through the endpoint to its responsible hub.

Handling errors: general considerations
------------------------------

A distributed system's response to errors is characterised by consistency, availability and partition tolerance.

Consistency: what is the difference of the intended state of the system and the actual state during the errornous operation and after the error condition has been resolved. Ideally,
after the error condition has been resolved, the view of all users on their conversations does reflect the intended course of the conversation, i.e. no messages were lost, messages
appear in the right order and are intact.

Availability: to which extent can the system be used during a failure. Ideally, when a component fails a standby component takes its place or the workload is routed to other components
without any data being tampered with or lost.

Partition tolerance: the extent to which the system behaves as a single system even when large parts are divided into partitions, i.e. in the case of a network outage between datacenters.

General consensus about failure tolerance in distributed systems is a design choice between different degrees of consistency, availability and partition tolerance.
In the design proposed, the priority lies on availability: in the case of a major failure, users should be able to chat at least with some other users, be able to receive some
messages and send some messages. The more, the better. This means temporarily sacrificing consistency (i.e. users might temporarily lose old messages while new messages arrive, leaving 
"gaps" in conversations) or partition tolerance (i.e. users in Africa might be able to chat with users in Europe but not in America). As the failure gradually subsides, messages
are routed between hubs and the incomplete conversation history of two users affected by the failure is supplemented retroactively.


Handling network errors between the client and an endpoint
----------------------------------------------------------

Since any type of message needs to be acknowledged by the recipient, network errors are easily mitigated by retransmitting messages until the clients acknowledge them to the hub.

Handling network endpoint errors
-------------------------------------

When an endpoints becomes unavailable clients will drop their connections with that endpoint and connect to other endpoints. To the client this looks like a network errors between
the client and the endpoint, see previous section. Since hubs can service multiple endpoints, the addition or removal of an endpoint does not inhibit the hub's functionality.

Handling single hub errors
-----------------------------------

Single hub errors are easy to handle: the hub goes offline and is replaced by another hub. Clients or hubs with unacknowledged messages to that hub retransmitt them to the new hub.
When an entirely new hub comes into service, it initially has no data on users or conversations. It will listen to broadcasts of other hubs with which it shares the same data
partition and also query them for old data. Once it has finished synchronizing it will announce itself to endpoints and hubs and assume regular service.

Handling multiple hub errors
-----------------------------------

This should be the case during a major network outage where entire datacenters cannot communicate between each other. In some cases clients will be able to mitigate problems by
connecting to different endpoints. In case a hub detects such a condition, it will try to synchronize with other hubs in its data partition. Synchronization assumes timestamps
on messages and a reasonably well tuned clock between all hubs i the system.

Synchronizing messages between hubs
-------------------------------------

This is the case where two hubs reconciliate conversations after a network partition. For example, let's assume that hub H1 and hub H2 have the following view on a conversation
between client C1 and client C2 consisting of the messages m1,m2,m3 and m4:

H1: m1,m2,m4
H2: m1,m3,m4

A sender ID, recipient ID and message ID assigned by the client uniquely identifies a message in this scenario. When hubs are able to communicate again they will exchange messages
for that conversation, ordering the messages by their timestamp and eventually reaching a consistent view on the conversation.
