package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Model.Account;
import Model.Address;
import Model.InstanceTool;

public class SocketRequestHttp {
	
	public SocketRequestHttp() {};

	public void notifyConnexionServlet(Account loggedAccount) {
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		
		Map<Object, Object> data = new HashMap<>();
        data.put("pseudo", loggedAccount.getNickname());
        data.put("username", loggedAccount.getUsername());
        data.put("addr1", loggedAccount.getAddress().getIP().getAddress()[0]);
        data.put("addr2", loggedAccount.getAddress().getIP().getAddress()[1]);
        data.put("addr3", loggedAccount.getAddress().getIP().getAddress()[2]);
        data.put("addr4", loggedAccount.getAddress().getIP().getAddress()[3]);
        data.put("add","1");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(InstanceTool.buildFormDataFromMap(data))
                .uri(URI.create(SocketInternalNetwork.PresentServer))
                .setHeader("User-Agent", "MessengerApp") 
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
			httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			System.out.println("InternalSocket: Error 1 in notifyConnexionServlet");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("InternalSocket: Error 2 in notifyConnexionServlet");
			e.printStackTrace();
		}
	}
	
	public void notifyDiscoServer(Account loggedAccount) {
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		
		Map<Object, Object> data = new HashMap<>();
        data.put("nickname", loggedAccount.getNickname());
        data.put("username", loggedAccount.getUsername());
        data.put("addr1", loggedAccount.getAddress().getIP().getAddress()[0]);
        data.put("addr2", loggedAccount.getAddress().getIP().getAddress()[1]);
        data.put("addr3", loggedAccount.getAddress().getIP().getAddress()[2]);
        data.put("addr4", loggedAccount.getAddress().getIP().getAddress()[3]);
        data.put("add","0");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(InstanceTool.buildFormDataFromMap(data))
                .uri(URI.create(SocketInternalNetwork.PresentServer))
                .setHeader("User-Agent", "MessengerApp") 
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
			httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			System.out.println("InternalSocket: Error 1 in notifyConnexionServlet");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("InternalSocket: Error 2 in notifyConnexionServlet");
			e.printStackTrace();
		}
	}


}
