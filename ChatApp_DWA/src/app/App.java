package app;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.ObjectName;

import beans.Host;
import transactions.ChatChatCommunicator;
import transactions.ChatUserCommunicator;

@Singleton
@Startup
public class App {

	private static String port;
	private static String host;
	private static InetAddress ip;
	private static String hostname;
	private String full;
	private ChatUserCommunicator cuc;
	private ChatChatCommunicator ccc;
	
	@PostConstruct
	public void init() {
		try {
			/*port =  ManagementFactory.getPlatformMBeanServer()
					   .getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http"), "port")
					   .toString();
			host = ManagementFactory.getPlatformMBeanServer()
									.getAttribute(new ObjectName("jboss.as:interface=public"), "inet-address")
									.toString();*/
			ip = InetAddress.getLocalHost();
			hostname = ip.getHostName();
			System.out.println("POGLEDAJMEMALAMOJA: " + ip + ":" + hostname);
			cuc = new ChatUserCommunicator();
			ccc = new ChatChatCommunicator();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Host getHost() {
		Host ret = new Host();
		ret.setAlias(hostname);
		ret.setHostAddress(ip.toString());
		
		return ret;
	}	
}
