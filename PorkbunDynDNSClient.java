package com.porkbun;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONArray;
import org.json.JSONObject;

/**
*/
public class PorkbunDynDNSClient
{
	static String endpoint = "";
	static String apikey = "";
	static String secretapikey = "";

 	public static void main(String[] args)
	{
		if (args.length < 3)
		{
			System.out.println("Required arguments are domain, subdomain, record type\nExample 1: yourdomain.com \"www\" A\nExample 2: yourdomain.com \"\" A");
			return;
		}

		String domainName = args[0].toLowerCase();
		String subDomain = args[1].toLowerCase();
		String recordType = args[2].toUpperCase();

		String hostName = domainName;
		if(subDomain.length() > 0)
			hostName = subDomain+"."+domainName;

		// read config file
		try
		{
			File config = new File("./config.json");
			FileInputStream fis = new FileInputStream(config);
			byte[] data = new byte[(int) config.length()];
			fis.read(data);
			fis.close();
			String configStr = new String(data, "UTF-8");

			JSONObject configObj = new JSONObject(configStr);

			endpoint = configObj.get("endpoint").toString();
			apikey = configObj.get("apikey").toString();
			secretapikey = configObj.get("secretapikey").toString();

			System.out.println("API endpoint: "+endpoint);
			System.out.println("apikey: "+apikey);
			System.out.println("secretapikey: "+secretapikey);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		String realIp = "";

		// ping API to get current IP
		JSONObject pingResult = ping();
		if(!pingResult.get("status").toString().equals("SUCCESS"))
		{
			System.out.println("Could not get ping result from API.");
			System.out.println(pingResult);
			System.exit(0);
		}
		System.out.println(pingResult);
		realIp = pingResult.get("yourIp").toString().toLowerCase();
		System.out.println("Detected current IP as "+realIp+".");

		// get current records
		JSONObject retrieveResult = retrieve(domainName);
		if(!retrieveResult.get("status").toString().equals("SUCCESS"))
		{
			System.out.println("Could not get records from API.");
			System.out.println(retrieveResult);
			System.exit(0);
		}
		System.out.println(retrieveResult);
		JSONArray records = (JSONArray)retrieveResult.get("records");
		for(int i = 0; i < records.length(); i++)
		{
			JSONObject record = (JSONObject)records.get(i);
			String currentId = record.get("id").toString().toLowerCase();
			String currentName = record.get("name").toString().toLowerCase();
			String currentType = record.get("type").toString().toUpperCase();
			String currentContent = record.get("content").toString().toLowerCase();

			if(currentName.equals(hostName) && currentType.equals(recordType))
			{
				System.out.println(currentType+" record for "+currentName+" is currently "+currentContent+".");
				if(!currentContent.equals(realIp))
				{
					System.out.println("Modifying current DNS record due to IP address mismatch.");

					// edit record
					JSONObject editResult = edit(domainName, currentId, subDomain, currentType, realIp, "300", "0");
					if(!editResult.get("status").toString().equals("SUCCESS"))
					{
						System.out.println("Could not edit record via API.");
						System.out.println(editResult);
						System.exit(0);
					}
					System.out.println(editResult);
				}
			}
		}
	}

	static JSONObject edit(String domain, String id, String name, String type, String content, String ttl, String prio)
	{
		JSONObject data = new JSONObject();
		data.put("name", name);
		data.put("type", type);
		data.put("content", content);
		data.put("ttl", ttl);
		data.put("prio", prio);
		String commandEndpoint = endpoint+"/dns/edit/"+domain+"/"+id;
		JSONObject result = sendCommand(commandEndpoint, data);

		return(result);
	}

	static JSONObject retrieve(String domain)
	{
		JSONObject data = new JSONObject();
		String commandEndpoint = endpoint+"/dns/retrieve/"+domain;
		JSONObject result = sendCommand(commandEndpoint, data);

		return(result);
	}

	static JSONObject ping()
	{
		JSONObject data = new JSONObject();
		String commandEndpoint = endpoint+"/ping";
		JSONObject result = sendCommand(commandEndpoint, data);

		return(result);
	}

	static JSONObject sendCommand(String url, JSONObject data)
	{
		JSONObject result = null;

		data.put("secretapikey", secretapikey);
		data.put("apikey", apikey);

		try (final CloseableHttpClient httpclient = HttpClients.createDefault())
		{
            		final HttpPost httpPost = new HttpPost(url);

    			StringEntity entity = new StringEntity(data.toString());
			httpPost.setEntity(entity);

            		try (final CloseableHttpResponse response = httpclient.execute(httpPost)) 
			{
                		System.out.println(response.getCode() + " " + response.getReasonPhrase());
                		final HttpEntity entity2 = response.getEntity();
				String responseString = EntityUtils.toString(entity2, "UTF-8");
				result = new JSONObject(responseString);

                		EntityUtils.consume(entity2);
            		}
			catch(Exception e) 
			{
  				e.printStackTrace();
				return(null);
			}
		}
		catch(Exception e) 
		{
  			e.printStackTrace();
			return(null);
		}

		return(result);
	}
}
