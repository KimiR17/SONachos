package nachos.threads;

import nachos.machine.*;
import java.util.*;

public class AlarmTest{

	private static class DingDong implements Runnable{

		private String ID;
		private int number_of_hits;
		private int interval;

		public DingDong(String ID, int number_of_hits, int interval){
			this.ID = ID;
			this.number_of_hits = number_of_hits;
			this.interval = interval;
		}
		public void run(){
			//This method will hit the thread's gong a certain number of times in given intervals
			System.out.println("Starting Ding Process for ::"+ID+":: Next Ding at ::"+(Machine.timer().getTime() + interval));
			for(int i = 0 ; i < number_of_hits ; i++){

				Long nextDing = Machine.timer().getTime() + interval;
				ThreadedKernel.alarm.waitUntil(nextDing);

				System.out.println("**DING** Hitting thread ::"+ID+"'s gong ::"+(number_of_hits-i)+":: More times. \n\tTime of hit::"+Machine.timer().getTime()+":: Next Hit At ::"+(interval+Machine.timer().getTime()));
			}
			System.out.println("\t"+ID+"'s Time of Death::"+Machine.timer().getTime());
		}

	}
	private static KThread threads[] =  new KThread[2];
	private static Integer thread_amount;

	public AlarmTest(){
		threads = new KThread[2];
		this.thread_amount = 2;
	}

	public static boolean start(){
		System.out.println("\n*******************\n\tAlarm Test Beginning\n");

		//Creates a thread and starts it
		threads[0] = new KThread(new DingDong("Ding1",10,12345));
		threads[0].setName("Ding1");
		threads[0].fork();

		//Creates a second thread with a different name, duration and interval
		threads[1] = new KThread(new DingDong("Dong2",3,98765));
		threads[1].setName("Dong2");
		threads[1].fork();

		//Postpones the main thread's execution so the other threads have a chance to execute
		System.out.println("Main thread is going to snore a bit.");
		ThreadedKernel.alarm.waitUntil(14653110);

		System.out.println("\n*******************\n\tAlarm Test Ending\n");
		return true;
	}
}
