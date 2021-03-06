package app;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import beans.Host;
import modelView.JsonArray;
import modelView.JsonString;
import transactions.ChatChatCommunicator;
import transactions.ChatUserCommunicator;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandLineException;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

@Singleton
@Startup
public class App {

	private static String port;
	private static String host;
	private static String ip;
	private static String hostname;
	
	@PostConstruct
	public void init() {
		try {
			ip = InetAddress.getLocalHost().toString();
			hostname = InetAddress.getLocalHost().getHostName();
			System.out.println("IP pa Port lol: " + ip + ":" + hostname);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Host getHost() throws CommandLineException, InstanceNotFoundException, AttributeNotFoundException, MalformedObjectNameException, ReflectionException, MBeanException {
		Host ret = new Host();
		
		port =  ManagementFactory.getPlatformMBeanServer()
				   .getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http"), "port")
				   .toString();
		host = ManagementFactory.getPlatformMBeanServer()
								.getAttribute(new ObjectName("jboss.as:interface=public"), "inet-address")
								.toString();
		
		String address = ip.toString().split("/")[1] + ":" + port;
		String alias = host + "/" + hostname;
		ret.setAlias(alias);
		ret.setHostAddress(address);
		
		return ret;
    }
	
	
	/*public static List<String> getChildrenInfo() {
		String domainController = "localhost";
        int domainPort = 9990;
 
        CommandContext ctx = CommandContextFactory.getInstance().newCommandContext("admin", "admin".toCharArray());
        ctx.connectController(domainController, domainPort);
        ModelNode cliCommand = null;
        try {
            // Get list of Hosts in the Domain
            String request1 = ":read-children-names(child-type=host)";
            cliCommand = ctx.buildRequest(request1);
            String hostJSON = executeCommand(ctx, cliCommand);
 
            List<String> listHost = parseJSONArray(hostJSON);
 
            for (String hostName : listHost) {
                System.out.println("--------- Host " + hostName);
 
                // Get list of Servers in the Host
                String request2 = "/host=" + hostName + ":read-children-names(child-type=server)";
                cliCommand = ctx.buildRequest(request2);
                String serverJSON = executeCommand(ctx, cliCommand);
                List<String> listServers = parseJSONArray(serverJSON);
 
                for (String server : listServers) {
 
                    // Get port-offset of each Server
                    String request3 = "/host=master/server-config=" + server + ":read-attribute(name=socket-binding-port-offset)";
                    cliCommand = ctx.buildRequest(request3);
                    String portJSON = executeCommand(ctx, cliCommand);
                    String port = parseJSONString(portJSON);
                    System.out.println("---------------- Server " + server + " port " + port);
                }
            }
 
        } catch (CommandFormatException e) {
            e.printStackTrace();
        }
 
        ctx.disconnectController();
	}
	
	public static String executeCommand(CommandContext ctx, ModelNode modelNode) {
		 
        ModelControllerClient client = ctx.getModelControllerClient();
        if (client != null) {
            try {
                ModelNode response = client.execute(modelNode);
                return (response.toJSONString(true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection Error! The ModelControllerClient is not available.");
        }
        return null;
    }
 
    private static List<String> parseJSONArray(String JSON) {
        JsonArray obj = gson.fromJson(JSON, JsonArray.class);
 
        return obj.getResult();
 
    }
 
    private static String parseJSONString(String JSON) {
        JsonString obj = gson.fromJson(JSON, JsonString.class);
 
        return obj.getResult();
 
    }*/
}
