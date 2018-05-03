package packages.application;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.ObjectName;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import packages.beans.Host;
import packages.transactions.UserChatCommunicator;
@ApplicationPath("/rest")
@Startup
@Singleton
public class App extends Application{
	private static String port;
	private static String host;
	private static InetAddress ip;
	private static String hostname;
	private String full;
	private UserChatCommunicator transactions;
	
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
			transactions = new UserChatCommunicator();
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
