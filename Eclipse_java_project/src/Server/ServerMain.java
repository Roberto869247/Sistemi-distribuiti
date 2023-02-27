package Server;

import java.io.IOException;

//classe che crea server generici che vengono registrati nel registry
//lancia anche un server della banca che dopo essersi registrato nel registry crea thread per gestire connessioni alla sua porta

public class ServerMain {

	public static void main(String[] args) throws IOException {
		int portaRegistry = 3000;
		String ipRegistry = "localhost";
		//creo servizi per testare tutti i possibili casi del Registry
		ServiceServer s1 = new ServiceServer("posta", "posta.it", "80", "protocollo posta", portaRegistry, ipRegistry);
		ServiceServer s2 = new ServiceServer("libreria", "libri.com", "90", "protocollo libreria", portaRegistry, ipRegistry);
		ServiceServer s3 = new ServiceServer("scuola", "scuola.it", "100", "protocollo scuola", portaRegistry, ipRegistry);
		ServiceServer s4 = new ServiceServer("scuola", "scuola.de", "2500", "protocollo scuola2", portaRegistry, ipRegistry);
		ServiceServer s5 = new ServiceServer("panificio", "pane.it", "2000", "protocollo panificio", portaRegistry, ipRegistry);
		s5.setIp("pane.com");
		s5.setPort("2300");
		s5.setProtocol("protocollo panificio2");
		s5.lookup();             //fa una lookup su sè stesso per controllare i dati modificati
		s5.delete();
		s5.lookup();
		ServerBanca s = new ServerBanca(portaRegistry, ipRegistry);
		s.loop();
	}
}
