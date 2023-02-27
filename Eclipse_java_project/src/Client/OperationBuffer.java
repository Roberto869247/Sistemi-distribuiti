package Client;

import java.util.ArrayList;
import java.util.List;

public class OperationBuffer {
	public List<String> buffer;
	private int numberInBuffer = 0;
	private int size;
	
	public OperationBuffer(int s) {
		size=s;
		buffer= new ArrayList<String>(size);
	}
	public synchronized void put(String op) throws InterruptedException{
		while(numberInBuffer==size) {
			wait();
		}
		numberInBuffer++;
		buffer.add(op);
		notifyAll();
	}

	public synchronized String get() throws InterruptedException {
		while(numberInBuffer==0)
			wait();
		numberInBuffer--;
		String s = buffer.get(0);
		buffer.remove(0);
		notifyAll();
		return s;
	}
}
