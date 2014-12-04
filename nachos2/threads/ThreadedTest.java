package nachos.threads;

import nachos.machine.*;
import java.util.*;



public class ThreadedTest{
	protected ArrayList<KThread> test_threads;
	protected Integer thread_amount;
	virtual public boolean readyThreads(Integer count);
	virtual public boolean start();
}




public class AlarmTest extends ThreadedTest{
	public AlarmTest(){
		test_threads = new ArrayList<KThread>(10);
		thread_amount = 10;
	}
	public boolean createThreads(Integer count){
		for(int i = 0 ; i < count && count <= thread_amount ; i++){
			KThread thread = test_threads.get(i);
			thread.setName("AlarmThreadTest#"+i);
			thread.fork();
			thread.run();
		}
	}
	public boolean runTests(){
		for(int i = 0 ; i < thread_amount ; i++){
			KThread thread = test_threads.get(i);
			System.out.println("Thread "+thread.getName()+" waiting "+i*1000+" ticks.");
			thread.waitUntil(i*1000);
		}
	}
}
public class Condition2Test extends ThreadedTest{
	
}
public class JoinTest extends ThreadedTest{
	public JoinTest(){
		test_threads = new ArrayList<KThread>(10);
		thread_amount = 10;
	}
	public boolean readyThreads(Integer count){
		for(int i = 0 ; i < count && count <= thread_amount ; i++){
			KThread thread = test_threads.get(i);
			thread.setName("JoinThreadTest#"+i);
			thread.fork();
			thread.run();
		}
		return true;
	}
	public boolean runTests(){
		//For every even thread mark it for join with the next queued thread
		for(int i = 0 ; i < thread_amount ; i+=2){
			KThread thread = test_threads.get(i);//Get the thread
			System.out.println("Thread "+thread.getName()+" ready to run");
			thread.runNextThread();//Force a context switch
			System.out.println("Marked thread "+thread.getName()+" for join");
			thread.join();//Mark it for join
			System.out.println("Thread "+thread.getName()+" joined successfully!");
			
		}
	}
}