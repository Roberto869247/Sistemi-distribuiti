package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;


public class ServeClientBanca extends Thread{
	  private Socket socket;
	  private BufferedReader in;
	  private PrintWriter out;
	  private ServerBanca sb;

	  public ServeClientBanca(ServerBanca sb, Socket s) throws IOException {
		    socket = s;
		    this.sb = sb;
		    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
		    start(); 
		  }
	  public void run() {
		    try {
		    	String command = in.readLine();         
		    	String[] commands = command.split(";");  //ogni campo è separato da un ;
		    	
		    	if(commands[0].equals("nuovoConto")) {
					String[] clienti = commands[1].split(",");
					ArrayList<String> l = new ArrayList<String>();
					for(int i = 0; i < clienti.length; i++) {
						if(!l.add(clienti[i]))
							out.println("errore");
					}
					out.println(sb.nuovoConto(l));   //nuovo conto creato, gli passo l'id
				} else if(commands[0].equals("versa")) {
					String result = "" + sb.versa(Integer.parseInt(commands[1]), commands[2], Integer.parseInt(commands[3]));
					if(result.equals("true"))
						out.println(sb.getConti(Integer.parseInt(commands[1])).getCreditoResiduo()); //versamento effettuato, ritorno nuovo saldo
					else out.println("errore");
				}else if(commands[0].equals("preleva")) {
					String result = "" + sb.preleva(Integer.parseInt(commands[1]), commands[2], Integer.parseInt(commands[3]));
					if(result.equals("true"))
						out.println(sb.getConti(Integer.parseInt(commands[1])).getCreditoResiduo()); //prelievo effettuato, ritono nuovo saldo
					else out.println("errore");
				}else if(commands[0].equals("bonifico")) {
					String result = "" + sb.bonifico(Integer.parseInt(commands[1]), commands[2], Integer.parseInt(commands[3]), Integer.parseInt(commands[4]));
					if(result.equals("true"))
						out.println(sb.getConti(Integer.parseInt(commands[1])).getCreditoResiduo()); //bonifico effetuuato, ritorno nuovo saldo
					else out.println("errore");
				}else {
					out.println(sb.getAllId());
				}
		      } catch (IOException e) {
		    	  System.out.println(e.getMessage());
		    } finally {
		      try {
		        socket.close();
		      } catch (IOException e) {
		    	  System.out.println("Socket not closed");
		      }
		    }
		}
}
