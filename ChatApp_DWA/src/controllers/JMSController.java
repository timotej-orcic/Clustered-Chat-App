package controllers;

import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.LoginData;
import transactions.JMSTransactions;

public class JMSController {

	private JMSTransactions transactions = new JMSTransactions();
	private JSONParser parser = new JSONParser();
	
	public void loginJMS(String loginData) throws ParseException {
		
		JSONObject json = (JSONObject) parser.parse(loginData);
		LoginData logData = new LoginData(json.get("uname").toString(),json.get("password").toString());
		transactions.sendMessage(loginData);
	}
}
