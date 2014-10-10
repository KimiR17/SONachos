package nachos.threads;

import nachos.machine.*;
import java.util.*;


public class CommunicatorTest{
	private ArrayList<KThread> test_threads;
	private Integer thread_amount;
	private Communicator comm;
	
	CommunicatorTest(){
		this.test_threads = new ArrayList<KThread>(20);
		this.thread_amount = 20;
	};
	
	public boolean start(){
			//The tests will be made in batches, first we'll test calling one speak and one listen respectively, then one listen and one speak.
			//After those tests four listens will be called randomly
			System.out.println("/*First batch of tests for Communicator class. */");
			System.out.println("/*Speaking word 32 on communicator, no listener is present");
			comm.speak(32);
			System.out.println("/*Listening...");
			System.out.println("/*Result: "+comm.listen());
			
			System.out.println("/*Listening to communicator, no speaker is present (default word is 0 but there can be a random word spoken)"+comm.listen());

			System.out.println("/*Speaking word 66");
			comm.speak(66);
			System.out.println("/*Listening once more: "+comm.listen());
			
			System.out.println("/*------Second Batch of Tests----------*/");
			System.out.println("/*Speaking twice and then listening once");
			System.out.println("/*Speaking 42 then 99");
			comm.speak(42);
			comm.speak(99);
			System.out.println("/*Listening now...");
			System.out.println("/*Result: "+comm.listen());
			
			return true;
	}
	
}