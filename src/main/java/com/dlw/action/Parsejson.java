package com.dlw.action;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Parsejson {
	public static String parseJSONForDocumentParameters(String jsonInput, String key) throws ParseException {
		// Creating a JSONParser object
		System.out.println("comes here");
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null, jsonObjectBody;
			jsonObject = (JSONObject) jsonParser.parse(jsonInput);
			jsonObjectBody = (JSONObject) jsonObject.get("data");

			JSONObject jsonArrayDocuments = (JSONObject) jsonObjectBody.get("document");
	         System.out.println("documents:" + jsonArrayDocuments);
	         //jsonArrayDocuments.get("revision");
	         //System.out.println(jsonArrayDocuments.get("revision").toString());
	         String revisionId = jsonArrayDocuments.get("revision").toString();
		return revisionId;
	}

	public static String getParameterValueFromDocuments(JSONArray jsonArrayDocuments, String key) {
		// Iterating the contents of the array
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = jsonArrayDocuments.iterator();
		return (String) iterator.next().get(key);
	}
}
