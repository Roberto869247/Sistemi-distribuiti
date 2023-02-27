package registro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Registry {
	private HashMap<String, ServiceData> servizi;
	private static final int port = 3000;
	private static ServerSocket Server;
	
	public Registry() {
		servizi = new HashMap<String, ServiceData>();
		try {
			Server = new ServerSocket(port);
			System.out.println("Server in ascolto sulla porta " + port);
		} catch (IOException e) {
			System.out.println("Server errore in apertura porta");
			e.printStackTrace();
		}
	}
	
	public void loop() {
		while(true) {
			PrintStream out = null;
			BufferedReader in = null;
			try {
				System.out.println("In attesa di nuove connessioni ...");
				Socket client = Server.accept();
				System.out.println("Connessione effettuata con "+ client.getInetAddress());
				
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out = new PrintStream(client.getOutputStream(), true);
				
				String command=in.readLine();
				String[] commands = command.split(",");
				
				if(commands[0].equals("bind")) {            
					out.println(this.bind(commands[1], commands[2], commands[3], commands[4]));
					System.out.println("Servizio registrato!");
				} else if(commands[0].equals("unbind")) {
					out.println(this.unbind(commands[1]));
					System.out.println("Servizio eliminato!");
				}else if(commands[0].equals("rebind")) {
					out.println(this.rebind(commands[1], commands[2], commands[3], commands[4]));
					System.out.println("Servizio modificato!");
				}else if(commands[0].equals("lookup")) {
					out.println(this.lookup(commands[1]));
					System.out.println("Servizio trovato!");
				}
				out.close();
				in.close();
				client.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void main (String args[]) {
		Registry r = new Registry();
		r.loop();
	}
	
	public boolean bind(String nomeServizio, String ip, String porta, String protocollo) {
		ServiceData s = new ServiceData();
		s.setIp(ip);
		s.setPorta(porta);
		s.setProtocollo(protocollo);
		if(servizi.containsKey(nomeServizio))
			return false;
		this.servizi.put(nomeServizio, s);
		return true;
	}
	public boolean unbind(String nomeServizio) {
		if(!servizi.containsKey(nomeServizio))
			return false;
		servizi.remove(nomeServizio);
		return true;
	}
	public boolean rebind(String nomeServizio, String ip, String porta, String protocollo) {
		if(!this.unbind(nomeServizio))
			return false;
		return this.bind(nomeServizio, ip, porta, protocollo);
	}
	public String lookup(String nomeServizio) {
		if(!servizi.containsKey(nomeServizio))
			return null;
		return servizi.get(nomeServizio).toString();
	}
}
