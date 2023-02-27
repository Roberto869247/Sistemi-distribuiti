package Server;

import java.util.*;

public class ContoCorrente {
	private int creditoResiduo;
	private List<String> clientiConto;
	
	public int getCreditoResiduo() {
		return creditoResiduo;
	}

	public void setCreditoResiduo(int creditoResiduo) {
		this.creditoResiduo = creditoResiduo;
	}

	public List<String> getClientiConto() {
		return clientiConto;
	}

	public ContoCorrente(List<String> clienti) {
		clientiConto = clienti;
		creditoResiduo = 0;
	}
}
