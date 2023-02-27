package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

//classe che crea client per recuperare informazioni sul server di servizi selezionati nel terminale e le stanpa nel terminale

public class ClientService {
	private Socket socket;
	private BufferedReader in;
	private PrintStream out;
	private String infoServizio;
	
	public static String[] poolServiziRegistry = {"scuola", "libri", "posta", "contoCorrenteMiaBanca"};
	
	public ClientService(int port, String ip, String nomeServizio) throws IOException {
		try {
			socket = new Socket(ip, port); //connessione al Registry per recuperarei dati
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream(), true);
			
			out.println("lookup," + nomeServizio);
			infoServizio = in.readLine();
			if(infoServizio.equals("null"))
				System.out.println("Nome del servizio selezionato non presente nel Registry");
			else System.out.println(infoServizio);
		}catch(Exception e) { 
			e.printStackTrace();
		}
		out.close();
		in.close();
		socket.close();
	}
	
	public static void main(String args[]) throws IOException, InterruptedException {
		int portaRegistry = 3000;
		String ipRegistry = "localhost";
		Random r= new Random();
		int i=r.nextInt(4);
		System.out.println("Recupero le informazioni del servizio " + poolServiziRegistry[i] + " dal Registry");
		ClientService c = new ClientService(portaRegistry, ipRegistry, poolServiziRegistry[i]);
	}
}