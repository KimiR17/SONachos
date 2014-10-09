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
    //* 0 - There is a word ready
    //* 1 - There is a speaker to be heard
    //* 2 - There is a listener to be spoken to
    //* 3 - Starting State 
    private Integer state;
    private Integer my_word;
    
    
    
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
	    //Get the mutex lock 
	    communicator_mutex.acquire();
	    while(this.state == 1){//While state signals that there is a speaker to be heard	    
		this.conditions[2].sleep();    //Sleeps the speaker has left condition 
	    }
	    
	    //Transitions into the speaker to be heard state 
	    this.state = 1;
	    
	    //Now that we have a speaker, wait for a listener to arrive
	    while(this.state != 2){//Listener has arrived state
		    this.conditions[0].sleep();//Sleeps the listener has arrived thread
	    }
	    
	    //Now that we have a listener, i'll produce my word
	    this.my_word = word;
	    this.state = 0; //Transitions into word ready state
	    this.conditions[3].wake()//Wakes the word ready condition
	    
	    //Now that the word has been produced and the machine has transitioned, wait for the word to be consumed
	    while(this.state == 0){
		    this.conditions[4].sleep(); //Sleeps the word has been consumed condition
	    }
	    
	    //Now that the communicator has spoken, transition back to the initial state and signal that the speaker has left
	    this.state = 3;
	    this.condition[2].wake();
	    
	    //Release the lock
	    communicator_mutex.release();
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
