package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerBanca extends ServiceServer{
	private HashMap<Integer, ContoCorrente> conti;
	private int count;
	
	protected ServerBanca(int  portaRegistry, String ipRegistry) {
		super("contoCorrenteMiaBanca", "localhost", "3200", ";", portaRegistry, ipRegistry);
		conti = new HashMap<Integer, ContoCorrente>();
		count = 0;
	}
	
	protected ContoCorrente getConti(int id) {
		return conti.get(id);
	}
	
	protected String getAllId() {
		String ids="";
		Iterator i = conti.keySet().iterator();
		 while(i.hasNext()) {
	         int element = (int) i.next();
	         ids=ids + "," + element;
	      }
		 return ids.substring(1);
	}
	
	protected void loop() throws IOException {
	    ServerSocket s = new ServerSocket(Integer.parseInt(getPort()));
	    System.out.println("ServerBanca partito su ip: " + getIp() + " con porta "+ getPort());
	    try {
	      while (true) {
	        Socket socket = s.accept();
	        try {  
	          new ServeClientBanca(this, socket);  //ad ogni connessione accettata associo un thread
	        } catch (IOException e) {
	        	socket.close();
	        }
	    }
	    } finally {
	      s.close();
	    }
	}
	protected synchronized int nuovoConto(List<String> clienti) {
		ContoCorrente c = new ContoCorrente(clienti);
		conti.put(++count, c);
		return count;
	}
	protected synchronized boolean versa(int idConto, String cliente, int quantità) {
		if(!conti.containsKey(idConto))
			return false;
		ContoCorrente c = conti.get(idConto);
		if(!c.getClientiConto().contains(cliente)) //verifico che il conto sia intestato a quel cliente
			return false;
		c.setCreditoResiduo(c.getCreditoResiduo() + quantità);
		return true;
	}
	protected synchronized boolean preleva(int idConto, String cliente, int quantità) {
		if(!conti.containsKey(idConto))
			return false;
		ContoCorrente c = conti.get(idConto);
		if(!c.getClientiConto().contains(cliente))
			return false;
		if(c.getCreditoResiduo()<quantità) //il credito residuo sarebbe negativo
			return false;
		c.setCreditoResiduo(c.getCreditoResiduo() - quantità);
		return true;
	}
	protected synchronized boolean bonifico(int idContoPrelievo, String clientePrelievo, int idContoVersamento, int quantità) {
		if(idContoPrelievo == idContoVersamento)
			return false;  //non posso fare bonifico a me stesso
		if(!conti.containsKey(idContoPrelievo) || !conti.containsKey(idContoVersamento))
			return false;
		ContoCorrente p = conti.get(idContoPrelievo);
		ContoCorrente v = conti.get(idContoVersamento);
		if(!p.getClientiConto().contains(clientePrelievo))
			return false;
		if(p.getCreditoResiduo()<quantità) //il credito residuo sarebbe negativo
			return false;
		p.setCreditoResiduo(p.getCreditoResiduo() - quantità);
		v.setCreditoResiduo(v.getCreditoResiduo() + quantità);
		return true;
	}
	
}
