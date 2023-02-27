package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

//classe che lancia il Mainthread per operare nel server della banca
//e il thread che inseriesce e operazioni da fare

public class ClientBancaMain implements Runnable{
	private OperationBuffer opr;
	private static String[] poolNomi = {"Sergio", "Roberto", "Giovanni", "Anna", "Maria", "Mauro", "Patrizia", "Daniela", "Simone", "Francesca"};
	
	public ClientBancaMain (OperationBuffer opr) throws IOException {
		this.opr = opr;
	}
		
	public static void main(String args[]) throws IOException, InterruptedException {
		int portaRegistry = 3000;
		String ipRegistry = "localhost";
		Random r= new Random(); 
		
		int cointestato = r.nextInt(3)+1;  //scelgo random quanti intestatari per il conto che creo (max 3)
		String intestatari = "";
		while(cointestato>0) {
			cointestato--;
			int i=r.nextInt(10);  //scelgo random un nome intestatario conto
			intestatari = intestatari + "," + poolNomi[i]; //stringa intestatari
		}
		intestatari = intestatari.substring(1);
		String client = intestatari.split(",")[0]; //prendo il primo nome e lo uso come utente client che fa le operazioni sul conto
		
		OperationBuffer op = new OperationBuffer(3);
		ClientBancaMain c = new ClientBancaMain(op);
		
		String[] infoServer = cercaServizio(ipRegistry, portaRegistry).split(",");  //metodo che si connette al Registry e recupera informazioni
		String[] ip = infoServer[0].split(":");   //ogni campo è separato da un :
		String[] port = infoServer[1].split(":");
		String[] protocol = infoServer[2].split(":");
		
		String idConto = comunicaConServer(ip[1], port[1], "nuovoConto", null, intestatari, null); //metodo che si connette al Server e creo il conto
		System.out.print("Nuovo conto creato con id " + idConto + " per il cliente " + client);
		
		//gestisco i nomi intestatari
		if(intestatari.split(",").length>1) {
			System.out.print(" cointestato con ");
			for(int i=1; i<intestatari.split(",").length; i++)
				System.out.print(intestatari.split(",")[i] + " ");
		}
		System.out.println();
		
		Thread t = new Thread (c); //creo il Thread che inserische le operazioni
		t.start();
		
		long startTime = System.currentTimeMillis(); //conto fino a 10 secondi e chiudo tutto
		while(System.currentTimeMillis() - startTime < 10000) {
			String funzione = op.get(); //recupero l'operaione se disponibile
			int v=r.nextInt(100)+1;  //scelgo random una quantità di euro per versare/prelevare/bonifico
			System.out.print("Thread main eseguo " + funzione + " di " + v + " euro");
			String response = comunicaConServer(ip[1], port[1], funzione, idConto, client, (v+"")); //comunico l'operazione al server
			if(response.equals("errore"))
				System.out.println("Errore operazione non eseguibile");
			else System.out.println("Nuovo saldo disponibile sul conto: " + response);
		}
		t.interrupt();
		System.out.println("Client terminato");
	}

	private static String comunicaConServer(String ip, String port, String funzione, String idConto, String cliente, String quantità) {
		//prima creo il comando poi comunico con il server
		String response="";
		String comando = funzione + ";";    //ogni campo è separato da un ;
		String conto = "";
		if(funzione.equals("versa")) {
			comando +=  idConto + ";" + cliente + ";" + quantità;
			System.out.println();
 		} else if(funzione.equals("preleva")) {
 			comando +=  idConto + ";" + cliente + ";" + quantità;
 			System.out.println();
		} else if(funzione.equals("bonifico")) {
			conto = riceviContoVersamento(ip, port, Integer.parseInt(idConto));
			if(conto==null)
				return "errore";
			System.out.println(" al conto con ID: " + conto);
			comando +=  idConto + ";" + cliente + ";" + conto + ";" + quantità;
		} else {
			comando +=  cliente;
		}
		try {
			Socket socket = new Socket(ip, Integer.parseInt(port));   //connessione al Server della banca
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			out.println(comando);
			response = in.readLine();
			out.close();
			in.close();
			socket.close();
		}catch(Exception e) { 
			e.printStackTrace();
		}
		return response;
	}

	private static String riceviContoVersamento(String ip, String port, int idConto) {
		//metodo per ritornare un conto esistente (per bonifico)
		String[] response = null;
		try {
			Socket socket = new Socket(ip, Integer.parseInt(port));   //connessione al Server della banca
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			out.println("getAllId");
			response = in.readLine().split(",");
		} catch(Exception e) { 
			e.printStackTrace();
		}
		Random r= new Random();
		int i=r.nextInt(response.length);
		while(i==idConto)
			i=r.nextInt(response.length);
		return response[i];
	}

	private static String cercaServizio(String ip, int porta) {
		String infoServizio = null;
		try {
			Socket socket = new Socket(ip, porta); //connessione al Registry per recuperare i dati
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			
			out.println("lookup,contoCorrenteMiaBanca");
			infoServizio = in.readLine();
			if(infoServizio.equals("null"))
				System.out.println("Nome del servizio selezionato non presente nel Registry");
			else System.out.println("Informazioni del servizio: " + infoServizio);
			out.close();
			in.close();
			socket.close();
		}catch(Exception e) { 
			e.printStackTrace();
		}
		return infoServizio;
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(200);
				Random r= new Random();
				int d=r.nextInt(3);  //scelgo random un'operazione da inserire nel buffer
				if(d==0) {
					opr.put("preleva");
				} else if(d==1) {
					opr.put("versa");
				}else {
					opr.put("bonifico");
				}
				if(Thread.currentThread().isInterrupted())
					break;
			} catch(InterruptedException e){
				System.out.println("Thread creato interrotto");
				break;
			}  
					
		}
	}
}
