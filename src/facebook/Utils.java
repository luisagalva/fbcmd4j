package facebook;


import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.internal.org.json.JSONObject;

public class Utils {
	private static final Logger logger = LogManager.getLogger(Utils.class);
	
	public static Facebook generate(){
		 Facebook facebook = null;
		 
	try {
		URL url = new URL("https://graph.facebook.com/v2.6/device/login");
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("access_token", "128503667820019|c2adfd5519dec3063145f6ce16f1e5bb");
        params.put("scope", "public_profile,user_actions.news,user_posts,publish_actions");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        String response = sb.toString();
        
        JSONObject obj = new JSONObject(response);
        String code = obj.getString("code");
        String userCode = obj.getString("user_code");
        
        System.out.println("Redireccionando... " );
        Desktop.getDesktop().browse(new URI("https://www.facebook.com/device"));
		System.out.println("Ingresa el código: " + userCode);

		String accessToken = "";
		while(accessToken.isEmpty()) {
	        try {
	            TimeUnit.SECONDS.sleep(5);
	        } catch (InterruptedException e) {
				logger.error(e);
	        }

	        URL url1 = new URL("https://graph.facebook.com/v2.6/device/login_status");
	        params = new LinkedHashMap<>();
	        params.put("access_token", "128503667820019|c2adfd5519dec3063145f6ce16f1e5bb");
	        params.put("code", code);

	        postData = new StringBuilder();
	        for (Map.Entry<String,Object> param : params.entrySet()) {
	            if (postData.length() != 0) postData.append('&');
	            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	            postData.append('=');
	            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	        }
	        postDataBytes = postData.toString().getBytes("UTF-8");

	        HttpURLConnection conn1 = (HttpURLConnection)url1.openConnection();
	        conn1.setRequestMethod("POST");
	        conn1.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        conn1.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	        conn1.setDoOutput(true);
	        conn1.getOutputStream().write(postDataBytes);

	        try {
	        	in = new BufferedReader(new InputStreamReader(conn1.getInputStream(), "UTF-8"));
		        sb = new StringBuilder();
		        for (int c; (c = in.read()) >= 0;)
		            sb.append((char)c);		        
		        response = sb.toString();
		        
		        obj = new JSONObject(response);
		        accessToken = obj.getString("access_token");
	        } catch(IOException ignore) {
	        }
	    }
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthAppId("128503667820019")
		  .setOAuthAppSecret("5ca8d9424234296bc456db0cb837bddc")
		  .setOAuthAccessToken(accessToken)
		  .setOAuthPermissions("public_profile,user_actions.news,user_posts,publish_actions");
		FacebookFactory ff = new FacebookFactory(cb.build());
		  facebook = ff.getInstance();
		
		
		System.out.println("Ha iniciado sesión exitosamente");
		System.out.println(" ");
		logger.info("Configuración guardada exitosamente.");
		return facebook;
	} catch(Exception e) {
		logger.error(e);
		return facebook;
	}
	
	}	
	
	
	public static void postFeed(ResponseList<Post> feed, Scanner scanner ){
		for (Post p : feed) {
				System.out.println("Story: " + p.getStory());
				System.out.println("Mensaje: " + p.getMessage());
			System.out.println("--------------------------------");
		}		
		
		System.out.println("Guardar resultados en un archivo de texto? 1=Si  2=No");
		
		try{
			int selection = scanner.nextInt();
			
			switch(selection){
				case 1:
					
					break;
			
					
				default:
					break;
			}
			
		}catch(InputMismatchException ex){
			System.out.println("Ocurrió un errror, favor de revisar log.");
			logger.error("Opción inválida. %s. \n", ex.getClass());
			scanner.next();}
		
			
		}
	}





