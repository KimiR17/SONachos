package nachos.threads;

import nachos.machine.*;
import java.util.*;

public class JoinTest{	
	private ArrayList<KThread> test_threads;
	private Integer thread_amount;
	
	public JoinTest(){
		this.test_threads = new ArrayList<KThread>(10);
		this.thread_amount = 10;
	}

	public boolean start(){
		//For every even thread mark it for join with the next queued thread
		for(int i = 0 ; i < thread_amount ; i+=2){
			KThread thread = test_threads.get(i);//Get the thread
			System.out.println("Thread "+thread.getName()+" ready to run");
			thread.fork();//Force a context switch
			System.out.println("Marked thread "+thread.getName()+" for join");
			thread.join();//Mark it for join
			System.out.println("Thread "+thread.getName()+" joined successfully!");
			
		}
		return true;
	}
}