package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
	
    //*Communicator States
    //* 0 - There is a word to be read
    //* 1 - There is a speaker to be heard
    //* 2 - There is a listener to be spoken to
    //* 3 - Starting State 
    private Integer state;
    
    
    
    private Lock communicator_mutex;
    //*Conditions analogous to the communicator states
    //* pos 0 - checks if the listener arrived
    //* pos 1 - checks if the listener left
    //* pos 2 - checks if the speaker has left
    //* pos 3 - checks if the word is ready to be read
    //* pos 4 - checks if the word has been read
    private Condition2 conditions;
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
	    this.state = 3;
	    
	    this.communicator_mutex = new Lock();
	    this.conditions = new Condition2[5];
	    for(int i = 0 ; i < 5 ; i++){ this.conditions[i] = new Condition2(communicator_mutex); }
	    
	    
    }

    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     *
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param	word	the integer to transfer.
     */
    public void speak(int word) {
	    
    }

    /**
     * Wait for a thread to speak through this communicator, and then return
     * the <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return	the integer transferred.
     */    
    public int listen() {
	return 0;
    }
}
