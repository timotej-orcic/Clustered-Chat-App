package app;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.ObjectName;

import beans.Host;
import transactions.JMSTransactions;

@Singleton
@Startup
public class App {

	private static String port;
	private static String host;
	private static InetAddress ip;
	private static String hostname;
	private String full;
	private JMSTransactions transactions;
	
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
			transactions = new JMSTransactions();
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
