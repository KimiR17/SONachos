package nachos.threads;

import nachos.machine.*;
import java.util.*;


public class CommunicatorTest{
	private ArrayList<KThread> test_threads;
	private Integer thread_amount;
	
	CommunicatorTest(){
		this.test_threads = new ArrayList<KThread>(10);
		this.thread_amount = 10;
	};
	
	public boolean start(){
			return true;
	}
	
}