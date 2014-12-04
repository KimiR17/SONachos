package nachos.threads;

import nachos.machine.*;
import java.util.*;
/**
 * An implementation of condition variables that disables interrupt()s for
 * synchronization.
 *
 * <p>
 * You must implement this.
 *
 * @see	nachos.threads.Condition
 */
public class Condition2 {
	private ArrayList<KThread> WaitingThreadList;
    /**
     * Allocate a new condition variable.
     *
     * @param	conditionLock	the lock associated with this condition
     *				variable. The current thread must hold this
     *				lock whenever it uses <tt>sleep()</tt>,
     *				<tt>wake()</tt>, or <tt>wakeAll()</tt>.
     */
    public Condition2(Lock conditionLock) {
	this.conditionLock = conditionLock;
	this.WaitingThreadList = new ArrayList<KThread>();
    }

    /**
     * Atomically release the associated lock and go to sleep on this condition
     * variable until another thread wakes it using <tt>wake()</tt>. The
     * current thread must hold the associated lock. The thread will
     * automatically reacquire the lock before <tt>sleep()</tt> returns.
     */
    public void sleep() {
	Lib.assertTrue(conditionLock.isHeldByCurrentThread());

	//Disabling interrupts to provide atomicity
	boolean interruption = Machine.interrupt().disable();

	//Thread must be appended to the list
	WaitingThreadList.add(KThread.currentThread());

	conditionLock.release();

	//Block current thread
	KThread.currentThread().sleep();

	conditionLock.acquire();

	Machine.interrupt().restore(interruption);
    }

    /**
     * Atomically release the associated lock and go to sleep o
     * Wake up at most one thread sleeping on this condition variable. The
     * current thread must hold the associated lock.
     */
    public void wake() {
	Lib.assertTrue(conditionLock.isHeldByCurrentThread());

	//Again disabling interruptions to provide atomicity
	boolean interruption = Machine.interrupt().disable();

	//If there are threads to be awakened(list is not empty) in the list, get the first one and unblock it
	if( !( WaitingThreadList.isEmpty() ) ){
		KThread thread = WaitingThreadList.get(0);
		WaitingThreadList.remove(0);
		thread.ready();
	}

	Machine.interrupt().restore(interruption);



    }

    /**
     * Wake up all threads sleeping on this condition variable. The current
     * thread must hold the associated lock.
     */
    public void wakeAll() {
	Lib.assertTrue(conditionLock.isHeldByCurrentThread());

	boolean interruption = Machine.interrupt().disable();

	//Iterate through the whole list awakening every thread sequentially
	for(Integer i = 0 ; i < WaitingThreadList.size() ; i++){
		KThread thread = WaitingThreadList.get(i);
		thread.ready();
	}
	WaitingThreadList.clear();
	Machine.interrupt().restore(interruption);
    }

    public void selfTest(){
			Condition2Test.start();
    }
    private Lock conditionLock;
}
