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
	protected static final String PresenceServer = "https://srv-gei-tomcat.insa-toulouse.fr/Messenger/PresenceServer";

	
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
			System.out.println("InternalSocket: Error 1 dans notifyConnexionServlet");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("InternalSocket: Error 2 dans notifyConnexionServlet");
			e.printStackTrace();
		}
	}
	
	/*protected void getCoListfromServer() {
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(SocketInternalNetwork.PresentServer))
				.setHeader("User-Agent", "MessengerApp")
				.build();
		 try {
			 httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			
		} catch (IOException e) {
			System.out.println("InternalSocket: Error 1 dans getCoListfromServer");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("InternalSocket: Error 2 dans getCoListfromServer");
			e.printStackTrace();
		}
	}*/
	
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
			System.out.println("InternalSocket: Error 1 dans notifyConnexionServlet");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("InternalSocket: Error 2 dans notifyConnexionServlet");
			e.printStackTrace();
		}
	}

	/*public void startExecutor() {
		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	    executorService.scheduleWithFixedDelay(new SocketTaskHttp(this.connectedUserList , ts), 0, 5, TimeUnit.SECONDS);
	}*/
	


}
