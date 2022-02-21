package com.dlw.action;

import java.util.Base64;

public class getData {
	public static String getSFXMLData(String template_Name, String channel_Name, String email_Address, String email_Subject) {
		//SF call to get the data
		
	    String xmlDatavanSF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
	    		+ "<Jobdata template=\""+template_Name+"\">\r\n"
	    		+ "  <to>Tove</to>\r\n"
	    		+ "  <channel>\""+channel_Name+"\"</channel>\r\n"
	    		+ "  <from>Jani</from>\r\n"
	    		+ "  <heading>\'"+email_Subject+"\'</heading>\r\n"
	    		+ "  <body>\'"+email_Address+"\'</body>\r\n"
	    		+ "</Jobdata>";
	    String sfDataToBase64 = Base64.getEncoder().encodeToString(xmlDatavanSF.getBytes());
	    System.out.println(sfDataToBase64);
	    return sfDataToBase64;
	}
}
