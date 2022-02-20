package com.dlw.standaloneretouch.controller;

import java.util.HashMap;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.dlw.action.Parsejson;

@Controller
public class RetouchController {
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;
	
	@RequestMapping("/ReTouchApplication")
	public String getOTDSToken() {
		
		System.out.println("+++++++++++++++++++Request starts here+++++++++++++++++++");
		return "retouch.html";
	}
	
	@RequestMapping(value = "/ReTouchFrameworkapp/fetchdocumenturl", method = RequestMethod.POST)
	@ResponseBody
	public String generateURL(@RequestBody RequestDTO RequestDTO) {
			//Get OTDS token first
	        System.out.println("#### Template is: "+RequestDTO.getRadTemplate());
	        System.out.println("#### Channel is: "+RequestDTO.getChannel());
	        String otdsUrl = environment.getProperty("otds.url");
		    String otdsuserName = environment.getProperty("otds.otdsuserName");
			String passWord = environment.getProperty("otds.userPassword");
			System.out.println("OTDS url is:"+otdsUrl);
			System.out.println("OTDS userName is:"+otdsuserName);
			System.out.println("OTDS passWord is:"+passWord);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HashMap<String, String> map = new HashMap<>();
			map.put("userName", otdsuserName);
			map.put("password", passWord);
			HttpEntity<HashMap<String, String>> entity = new HttpEntity<HashMap<String, String>>(map,headers);
			ResponseEntity<String> response = restTemplate.exchange(otdsUrl, HttpMethod.POST, entity, String.class);
			JSONObject jsonObject = new JSONObject(response.getBody());
			System.out.println("OTDS Access_Token:"+jsonObject.getString("ticket"));
			
			// get the XML data
			String xmlcontent = com.dlw.action.getData.getSFXMLData(RequestDTO.getRadTemplate(), RequestDTO.getChannel());
			//Create the document
			String getDocUrl = environment.getProperty("retouch.createDocument");
			String themeId = environment.getProperty(RequestDTO.getRadTemplate());
			System.out.println("OTDS getDocUrl:"+getDocUrl);
			System.out.println("OTDS themeId:"+themeId);
			HttpHeaders docHeaders = new HttpHeaders(); 
			docHeaders.setContentType(MediaType.APPLICATION_JSON);
			docHeaders.set("OTDSTicket", jsonObject.getString("ticket"));
			String jsonInput = "{\r\n" + "   \"theme\":{\r\n"
						+ "      \"id\":\""+themeId+"\"\r\n"
						+ "   },\r\n" + "   \"content\":{\r\n"
						+ "      \"data\":\""+xmlcontent+"\",\r\n"
						+ "      \"contentType\":\"text/xml\"\r\n" + "   }\r\n" + "}";
			HttpEntity<String> entityRes = new HttpEntity<String>(jsonInput.toString(), docHeaders);
			ResponseEntity<String> responseResults = restTemplate.exchange(getDocUrl, HttpMethod.POST, entityRes, String.class);
			System.out.println("Create document json response is :"+responseResults.getBody().toString());
			JSONObject jsonObjectDocument = new JSONObject(responseResults.getBody());
			String temp = responseResults.getHeaders().get("Set-Cookie").get(0);
			System.out.println("jsession: :"+temp);
	        int endIndex = temp.toString().indexOf(";");
	        String jsessionId = temp.toString().substring(11, endIndex);
	        System.out.println("JSESSIONID is: " + temp.toString().substring(11, endIndex));
			String messageId = jsonObjectDocument.getJSONObject("data").get("messageId").toString();
			String docId = jsonObjectDocument.getJSONObject("data").get("id").toString();
			String revision = jsonObjectDocument.getJSONObject("data").get("revision").toString();
			System.out.println("Message Id: "+messageId);
			System.out.println("Docuemnt Id: "+docId);
			System.out.println("Revision Id: "+revision);

			//Prepare ReTouch stand alone URL
			String reTouchURL = ""+environment.getProperty("retouch.baseUrl")+"/retouch/index.html#/tenant/"+environment.getProperty("retouch.tenantId")+"/domain/"+environment.getProperty("retouch.ApplicationDomainId")+"/edit/"+messageId+"/WEB?language="+environment.getProperty("retouch.lang")+"";
			String dataStream = reTouchURL +"|" + jsonObject.getString("ticket") +"|" + docId +"|" + revision +"|" + messageId +"|" + jsessionId;
			System.out.println("ReTouch URL: "+reTouchURL);
			System.out.println("Datastream: "+dataStream);
		return dataStream;
	       
	   }
	
	@RequestMapping(value = "/ReTouchFrameworkapp/SubmitDocument", method = RequestMethod.POST)
	@ResponseBody
	public HttpStatus checkSubmit(@RequestBody RequestDTO RequestDTO) throws ParseException{
       
			System.out.println("#### SubmitDocument: OTDS Ticket is: "+RequestDTO.getOtdsticket());
			System.out.println("#### SubmitDocument: DocID is: "+RequestDTO.getDocid());
			System.out.println("#### SubmitDocument: Revision is: "+RequestDTO.getRevision());
			System.out.println("#### SubmitDocument: Messege Id is: "+RequestDTO.getMessageid());
			System.out.println("#### SubmitDocument: JSESSION Id is: "+RequestDTO.getJsessionId());
	       
	       //Get the latest revision id from preview
	       String previewURL = ""+environment.getProperty("retouch.serviceGateway")+"/v1/documents/"+RequestDTO.getMessageid()+"/preview";
	       System.out.println("#### SubmitDocument: previewURL: "+previewURL);
	       HttpHeaders previewHeaders = new HttpHeaders(); 
	       previewHeaders.add("Cookie", "JSESSIONID="+RequestDTO.getJsessionId());
	       HttpEntity<String> entityPreview = new HttpEntity<String>(previewHeaders);
	       ResponseEntity<String> resultPreview = restTemplate.exchange(previewURL, HttpMethod.GET, entityPreview, String.class);
	       //LOGGER.info("#### SubmitDocument: preview success: "+resultPreview.getBody().toString());
	       String revID = Parsejson.parseJSONForDocumentParameters(resultPreview.getBody().toString(), "revision");
	       System.out.println("#### SubmitDocument: The Revision ID from preview: "+revID);
	       
	       String submitURL = ""+environment.getProperty("retouch.serviceGateway")+"/v2/documents/"+RequestDTO.getDocid().toString()+"/?where_revision="+revID+"";
	       System.out.println("#### SubmitDocument: submitURL: "+submitURL);
	       HttpHeaders docSubmitHeaders = new HttpHeaders(); 
	       docSubmitHeaders.setContentType(MediaType.APPLICATION_JSON);
	       docSubmitHeaders.add("Cookie", "JSESSIONID="+RequestDTO.getJsessionId());
	       docSubmitHeaders.set("OTDSTicket", RequestDTO.getOtdsticket() );
	       String jsonSubmitInput = "{\r\n" + 
					"      \"processingstate\" : \"3\"\r\n" + 
					"}";
	       HttpEntity<String> entitySubmit = new HttpEntity<String>(jsonSubmitInput.toString(), docSubmitHeaders);
	       restTemplate.exchange(submitURL, HttpMethod.PUT, entitySubmit, String.class);
	       
	       int revNumber = Integer.parseInt(revID);
	       revNumber = revNumber+1;
	       System.out.println("#### SubmitDocument: Revised Revision number is: "+revNumber);
	       String submitURLFinal = ""+environment.getProperty("retouch.serviceGateway")+"/v2/documents/"+RequestDTO.getDocid().toString()+"/?where_revision="+revNumber+"";
	       String jsonSubmitInputFinal = "{\r\n" + 
					"      \"processingstate\" : \"5\"\r\n" + 
					"}";
	       HttpEntity<String> entitySubmitFinal = new HttpEntity<String>(jsonSubmitInputFinal.toString(), docSubmitHeaders);
	       ResponseEntity<String> responseResultsSubmitFinal = restTemplate.exchange(submitURLFinal, HttpMethod.PUT, entitySubmitFinal, String.class);
	       JSONObject jsonObjectSubmitFinal = new JSONObject(responseResultsSubmitFinal.getBody());
	       System.out.println("#### SubmitDocument: Status is : "+jsonObjectSubmitFinal.getString("status"));
	       System.out.println("+++++++++++++++++++Request ends here+++++++++++++++++++");
	       return responseResultsSubmitFinal.getStatusCode();
	       
	   }
	
}
