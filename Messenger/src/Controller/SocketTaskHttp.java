package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import Model.Address;
import Model.InstanceTool;

public class SocketTaskHttp extends TimerTask{
	ConcurrentHashMap<String, Address> userCo;
	Long ts;
	String username;
	UserInterface ui;
	public SocketTaskHttp(ConcurrentHashMap<String, Address> userCo, Long ts, String username,UserInterface ui){
		this.userCo = userCo;
		this.ts = ts;
		this.username=username;
		this.ui=ui;
	}

	@Override
	public void run() {
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		
		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(SocketInternalNetwork.PresentServer +"?type="+InstanceTool.Ident_Code.CoList+"&ts="+ this.ts))
				.setHeader("User-Agent", "EasyChat")
				.build();
		 HttpResponse<String> response;
		try {
			Long tmp = System.currentTimeMillis();
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			this.ts = tmp;
			//System.out.println(response.body());
			BufferedReader out = new BufferedReader(new StringReader(response.body()));
			String val = out.readLine();
			val = out.readLine();
			while(!val.equals("--rm--")){
				String nickname = val;
				String usernamehttp = out.readLine();
				String addr = out.readLine();
				if(username!=usernamehttp) {
					this.userCo.put(usernamehttp, new Address(InetAddress.getByName(addr.substring(1)), nickname, usernamehttp));
				}
				System.out.println("Ajout de " + username + " Taille de la liste " + this.userCo.size());
				ui.updateUsers(userCo);
				val = out.readLine();
			}
			val = out.readLine();
			while(val != null) {
				this.userCo.remove(val);
				System.out.println("Rm de " + val + " Taille de la liste " + this.userCo.size());
				val = out.readLine();
				ui.updateUsers(userCo);
			}
						
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}

		
		
	}
	
	public void remove() {
		HttpClient httpClient = HttpClient.newBuilder()
	            .version(HttpClient.Version.HTTP_2)
	            .build();
		
		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(SocketInternalNetwork.PresentServer +"?type="+InstanceTool.Ident_Code.CoSpecificList+"&ts="+ this.ts))
				.setHeader("User-Agent", "MessengerApp")
				.build();
		 HttpResponse<String> response;
		try {
			Long tmp = System.currentTimeMillis();
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			this.ts = tmp;
			//System.out.println(response.body());
			BufferedReader out = new BufferedReader(new StringReader(response.body()));
			String val = out.readLine();
			val = out.readLine();
			while(!val.equals("--rm--")){
				String nickname = val;
				String usernamehttp = out.readLine();
				String addr = out.readLine();
				if(username!=usernamehttp) {
					this.userCo.put(usernamehttp, new Address(InetAddress.getByName(addr.substring(1)), nickname, usernamehttp));
				}
				System.out.println("Add of " + username + ", size of userlist " + this.userCo.size());
				ui.updateUsers(userCo);
				val = out.readLine();
			}
			val = out.readLine();
			while(val != null) {
				this.userCo.remove(val);
				System.out.println("Remove of " + username + ", size of userlist " + this.userCo.size());
				val = out.readLine();
				ui.updateUsers(userCo);
			}
						
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}

		
		
	}
}

	




	


