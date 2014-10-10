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
    //* 0 - There is a speaker without a listener ready!
    //* 1 - There is a listener without a speaker ready!
    //* 2 - There is both a listener and a speaker ready!
	//* 3 - There is a word to be consumed
	//* 4 - The word has been consumed
    //* 5 - Initial/Reset state
    private Integer state;
    private Integer my_word;
    
    
    
    private Lock communicator_mutex;
    //*Conditions analogous to the communicator states
    //* pos 0 - Listener arrived
    //* pos 1 - Listener has left
	//* pos 5 - Speaker has arrived
    //* pos 2 - Speaker has left
    //* pos 3 - Word is ready
    //* pos 4 - Word has been read
    private Condition2[] conditions;
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
	    this.state = 5;
	    
	    this.communicator_mutex = new Lock();
	    this.conditions = new Condition2[6];
	    for(int i = 0 ; i < 6 ; i++){ this.conditions[i] = new Condition2(communicator_mutex); }
	    
	    
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
	    
	    if(this.state == 1){//There is already have a listener ready
			//Transition into listener and speaker ready state
			this.state = 2;
			//Signals the speaker has arrived condition
			this.conditions[5].wake();
			//Signals the speaker has left condition
			this.conditions[2].sleep();
			//Produce my word
			this.my_word = word;
			//Transitions into word ready to be consumed state
			this.state = 3;
			//Signals that there is a word ready
			this.conditions[3].wake();
			//While the word is not yet consumed, sleep the word consumed condition
			while(this.state == 4){
				this.conditions[4].sleep(); //Sleeps the word has been consumed condition
			}
	    }else{
		    //Transition into "Speaker without a listener" state
		    this.state = 0;
		    //Signals that the speaker has not left
		    this.conditions[2].sleep();
			
			//While there isn't a listener ready to receive the word
			while(this.state != 2){//Listener and speaker ready state
				this.conditions[0].sleep();//Sleeps the listener has arrived condition
			}
		}
	    
	    //Now that the communicator has spoken, transition back to the initial state and signal that the speaker has left
	    this.state = 5;
	    this.conditions[2].wake();
	    
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
		//Get the mutex lock
		communicator_mutex.acquire();
		
		if(this.state == 0){//If there is a speaker waiting for a listener
			//Transition into listener and speaker ready state
			this.state = 2;
			//Signals the listener has left condition
			this.conditions[1].sleep();
			//Wakes the listener has arrived condition
			this.conditions[0].wake();
			
			//While there isn't a word ready to be consumed
			while(this.state != 3){
				//Signals the word is ready condition
				this.conditions[3].sleep();
			}
			
			//Now that there is a word ready to be consumed, consume it!
			Integer received_word = this.my_word;
			//Transition into word consumed state
			this.state = 4;
			//Signal the word has been consumed condition
			this.conditions[4].wake();
			
		}else{
			//There is not a speaker waiting for a listener
			//Transition into listener without a speaker state
			this.state = 1;
			this.conditions[1].sleep();
			
			//While there isn't a listener and a speaker ready
			while(this.state != 2){
				this.conditions[5].sleep();//Sleeps the speaker has arrived condition
			}
		}	
		
		//Now that both parties have done their job
		//Transition back to initial state
		this.state = 5;		
		//Awakes the listener has left condition
		this.conditions[1].wake();
		
		communicator_mutex.release();
		
		return this.my_word;
    }
    
    public void selfTest(){
	    CommunicatorTest dummy = new CommunicatorTest();
	    dummy.start();
    }
}
