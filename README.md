# JavaChatClient

Using Multicast sockets to create a simple chat server.

Fixed IP Address of 239.0.202.1
Port Number of 40202

Basically using multicast sockets creates a group using the IP address on the port above.

Allows users that are connected to this chat client to chat with each other!
This is done by creating another multicast socket that is running in another thread to repeat whatever is sent to the group along with the senders IP address.

Simple! :D
