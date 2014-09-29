package nachos.threads;

import nachos.machine.*;
import java.util.*;
/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    private ArrayList<KThread> WaitingThreads;
    private ArrayList<Long> ThreadTimer;
    
    public Alarm() {
	Machine.timer().setInterruptHandler(new Runnable() {
		public void run() { timerInterrupt(); }
	    });
	this.WaitingThreads = new ArrayList<KThread>();
	this.ThreadTimer = new ArrayList<Long>();
    }

    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
	    
	KThread.currentThread().yield();
	
	boolean interruption = Machine.interrupt().disable();
	//Iterates through the thread queue in search of a thread to be awakened
	for(Integer IT = 0; IT < ThreadTimer.size(); IT++){
		if(ThreadTimer.get(IT) <= Machine.timer().getTime()){
			//Awaken the thread
			KThread thread = WaitingThreads.get(IT);
			//Cleanup the lists
			WaitingThreads.remove(IT);
			ThreadTimer.remove(IT);
			//Unblock the thread
			thread.ready();
		}
	}
	
	Machine.interrupt().restore(interruption);
	
    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {
	// for now, cheat just to get something working (busy waiting is bad)
// 	long wakeTime = Machine.timer().getTime() + x;
// 	while (wakeTime > Machine.timer().getTime())
// 	    KThread.yield();
	
	    boolean interrupt = Machine.interrupt().disable();
	    
	    
	    //This implementation works because the threads will always 
	    WaitingThreads.add(KThread.currentThread());
	    ThreadTimer.add(Machine.timer().getTime()+x);
	    
	    KThread.currentThread().sleep();
	    
	    Machine.interrupt().restore(interrupt);
    }
}
