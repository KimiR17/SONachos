package nachos.threads;

import nachos.machine.*;
import java.util.*;

public class AlarmTest{
	private ArrayList<KThread> test_threads;
	private Integer thread_amount;
	
	public AlarmTest(){
		this.test_threads = new ArrayList<KThread>(10);
		this.thread_amount = 10;
	}

	public boolean start(){
		for(int i = 0 ; i < thread_amount ; i++){
		}
		return true;
	}
}