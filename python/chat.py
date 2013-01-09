#!/usr/bin/python

# message format: msgid|TTL|nick|message
#   msgid - random number; I use uuid4
#   TTL - number; increase on each resend; stop resending if TTL > 12 
#   nick - incoming nick; don't resend if nick is my nick
#   message - the text message

import time, threading, socket, uuid
import signal, sys

PORT=4889
MAXRESEND=12

ss = None
addr = None
run = True

def signal_handler(signal, frame):
	global run
	print "INT received, stopping"
	run = False

def get_listen_socket():
	s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	s.settimeout(1);
	s.bind(("0.0.0.0", PORT))
	return s

def get_send_pair():
	s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
	s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
	host = "<broadcast>"
	addr = (host, PORT) 
	return (s, addr)

def send_message(nick, message, uuid, ttl=0):
	global ss
	global addr
	if ( ss is None or addr is None):
		(ss, addr) = get_send_pair();
	data = "%s|%s|%s|%s" % (uuid, ttl, nick, message)
	ss.sendto(data,addr)


def thread_repeater(ls, mynick):
	global run
	incomingmsgs = {}
	while(run):
		try:
			(data, address) = ls.recvfrom(1024)
		except:
			continue
		print data
		(iUuid, iTTL, iNick, iMessage)  = data.split('|', 4)
		# add it to incoming queue if not there
		if not iUuid in incomingmsgs:
			print "MSG: %s> %s" % (iNick, iMessage)
			incomingmsgs[iUuid] = 1	

		# if I'm not the original sender, reroute it
		if (iNick != mynick):
			print "RESEND: %s> %s" % (iNick, iMessage)
			TTL = int(iTTL)
			if (TTL < MAXRESEND):
				time.sleep(1/2.0)
				send_message(iNick, iMessage, iUuid, TTL+1)
		
def load_nickity():
	nick = ""
	try:
		f = open(".nick", "r")
		nick = f.readline()
		f.close()
	except:
		nick = raw_input("Nick? >")
		f = open(".nick", "w")
		f.write(nick)
		f.close()
	return nick

def main():
	global run
	# set stop handler
	signal.signal(signal.SIGINT, signal_handler)

	# get nickity
	if (len(sys.argv) > 1):
		mynick = sys.argv[1]
	else:
		mynick = load_nickity()
	print "nick is %s" % mynick

	if (len(sys.argv) > 2):
		#just broadcast the message without the router
		send_message(mynick, sys.argv[2], uuid.uuid4())
		return
	# set up listener
	ls = get_listen_socket()
	# set up repeater
	repeater = threading.Thread(target = thread_repeater, args= (ls, mynick))
	repeater.start()
	# send test message
	while (run):	
		i = raw_input(">")
		if (len(i) > 0):
			send_message(mynick, i, uuid.uuid4())

if __name__ == "__main__":
	main()
