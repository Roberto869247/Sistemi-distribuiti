package registro;

public class ServiceData {
	private String ip;
	private String porta;
	private String protocollo;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPorta() {
		return porta;
	}
	public void setPorta(String porta) {
		this.porta = porta;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public String toString() {
		return "ip:" + this.ip + ",porta:" + this.porta +",protocollo:" + this.protocollo;
	}
}
