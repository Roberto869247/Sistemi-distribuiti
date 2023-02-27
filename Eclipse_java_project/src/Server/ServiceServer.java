package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServiceServer {
	private String nomeServizio;
	private String ip;
	private String port;
	private String protocol;
	
	private int portaRegistry;
	private String ipRegistry;
	
	protected ServiceServer(String nomeServizio, String ip, String porta, String protocollo, int  portaRegistry, String ipRegistry) {
		this.nomeServizio = nomeServizio;
		this.ip = ip;
		this.port = porta;
		this.protocol = protocollo;
		this.portaRegistry = portaRegistry;
		this.ipRegistry = ipRegistry;
		try {
			Socket socket = new Socket(ipRegistry, portaRegistry); //indirizzo del Registry
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			
			out.println("bind," + this.nomeServizio + "," + this.ip + "," + this.port + "," + this.protocol);
			String result = in.readLine();
			if(result.equals("true"))
				System.out.println("Servizio " + this.nomeServizio + " salvato nel Registry correttamente");
			else System.out.println("Servizio " + this.nomeServizio + " già presente nel Registry");
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}

	protected void setIp(String ip) {
		this.ip = ip;
		try {
			Socket socket = new Socket(ipRegistry, portaRegistry); //indirizzo del Registry
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			
			out.println("rebind," + this.nomeServizio + "," + this.ip + "," + this.port + "," + this.protocol);
			String result = in.readLine();
			if(result.equals("true"))
				System.out.println("Servizio " + this.nomeServizio + " modificato correttamente");
			else System.out.println("Servizio " + this.nomeServizio + " non presente nel Registry");
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}

	protected void setPort(String port) {
		this.port = port;
		try {
			Socket socket = new Socket(ipRegistry, portaRegistry); //indirizzo del Registry
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			
			out.println("rebind," + this.nomeServizio + "," + this.ip + "," + this.port + "," + this.protocol);
			String result = in.readLine();
			if(result.equals("true"))
				System.out.println("Servizio " + this.nomeServizio + " modificato correttamente");
			else System.out.println("Servizio " + this.nomeServizio + " non presente nel Registry");
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}

	protected void setProtocol(String protocol) {
		this.protocol = protocol;
		try {
			Socket socket = new Socket(ipRegistry, portaRegistry); //indirizzo del Registry
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			
			out.println("rebind," + this.nomeServizio + "," + this.ip + "," + this.port + "," + this.protocol);
			String result = in.readLine();
			if(result.equals("true"))
				System.out.println("Servizio " + this.nomeServizio + " modificato correttamente");
			else System.out.println("Servizio " + this.nomeServizio + " non presente nel Registry");
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}

	protected void delete() {
		try {
			Socket socket = new Socket(ipRegistry, portaRegistry); //indirizzo del Registry
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			
			out.println("unbind," + this.nomeServizio);
			String result = in.readLine();
			if(result.equals("true"))
				System.out.println("Servizio " + this.nomeServizio + " cancellato correttamente");
			else System.out.println("Servizio " + this.nomeServizio + " non presente nel Registry");
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	protected void lookup() {
		try {
			Socket socket = new Socket(ipRegistry, portaRegistry); //indirizzo del Registry
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			
			out.println("lookup," + this.nomeServizio);
			String result = in.readLine();
			System.out.println("Dati servizio " + this.nomeServizio + ": " + result);
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	protected void setPortaRegistry(int portaRegistry) {
		this.portaRegistry = portaRegistry;
	}

	protected void setIpRegistry(String ipRegistry) {
		this.ipRegistry = ipRegistry;
	}

	protected String getIp() {
		return ip;
	}

	protected String getPort() {
		return port;
	}

	protected String getProtocol() {
		return protocol;
	}
}
