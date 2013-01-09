SnFChat
=======

Store and Forward UDP chat - for use on AdHoc networks

This  project  implements a store-and-forward protocol for
text messages over ad-hoc wirless networks.

It uses UDP broadcast to send simple packets to all the group.
When a packet is received, it is repeated if the destination is 
not reached. 

This creates a flood effect, which is limited by the TTL of a 
message. This flood is actually intentional in order to 
enhance the change of message delivery.

Each message has a unique id (UUID 4) to prevent multiple 
processing of the same message.

Happy hacking !
