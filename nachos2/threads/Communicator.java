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
    //* 0 Counts the number of speakers waiting
    //* 1 Counts the number of listeners waiting
    //* 2 Counts the number of active speakers
    //* 3 Counts the number of active listeners
    //* 4 Counts the number of listeners waiting for a word
    private int state[];
    private Integer my_word;



    private Lock communicator_mutex;
    //*Conditions analogous to the communicator states
    //* pos 0 - Listener sleeping
    //* pos 1 - Speaker sleeping
    //* pos 2 - Word Produced
    //* pos 3 - ready to produce word
    private Condition2[] conditions;
    /**
    * Allocate a new communicator.
    */
    public Communicator() {
        state = new int[5];


        this.my_word = 0;

        this.communicator_mutex = new Lock();
        this.conditions = new Condition2[4];
        for(int i = 0 ; i < 4 ; i++){ this.conditions[i] = new Condition2(communicator_mutex); }


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

        //There is a high probability that more than one speakers will be ready at a time so there is a need
        // To store all of them until a listener awakes the thread. After this WHILE there is a 100% chance that there will be a listener waiting for a message.
        while(state[0] > 0){
            state[0]++;
                System.out.println("\tSpeaker ::"+KThread.currentThread().getName()+"::taking a nap!");
            this.conditions[1].sleep();
                System.out.println("\tSpeaker ::"+KThread.currentThread().getName()+"::Ready!");
            state[0]--;
        }

        //Signals that there is an active speaker
        System.out.println("\tSpeaker ::"+KThread.currentThread().getName()+"::Active!");
        state[2]++;

        //If there are active listeners
        if(state[3] > 0 ){
            System.out.println("\tSpeaker ::"+KThread.currentThread().getName()+"::Had an active listener, producing word and leaving!");
            //Signals that there is a word ready
            this.my_word = word;
            this.conditions[2]. wake();

        }else{
            //There are inactive listeners
            if(state[1] > 0){
                System.out.println("\tSpeaker ::"+KThread.currentThread().getName()+"::Had no active listeners, waking one!");
                this.conditions[0].wake();

            }else{

                //If there are no listeners present spinlock until one arrives
                System.out.println("\tSpeaker ::"+KThread.currentThread().getName()+"::Had no listeners alive, sleeping!");
                while(state[4] <= 0){
                    state[0]++;
                    this.conditions[3].sleep();
                    state[0]--;
                }

            }

            System.out.println("\tSpeaker ::"+KThread.currentThread().getName()+"::Has awoken a listener and will now produce its word!");
            this.my_word = word;

            //Signals that there is a word ready
            this.conditions[2].wake();

            //If there is another speaker ready it up
            if(state[0] > 0){
                System.out.println("\tSpeaker ::"+KThread.currentThread().getName()+"::Is waking up the next speaker!");
                this.conditions[1].wake();
            }
            state[2]--;

            //Release the lock
            communicator_mutex.release();
            System.out.println("\tSpeaker ::"+KThread.currentThread().getName()+"::Has left the building!");
            return;
        }



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


        //Locks the thread so the speakers follow a FIFO order
        while(state[1] > 0){
            System.out.println("\tListener ::"+KThread.currentThread().getName()+"::had other listeners in queue, going to wait!");
            state[1]++;
            this.conditions[0].sleep();
            state[1]--;
        }

        //Signals that there is a listener ready
        System.out.println("\tListener ::"+KThread.currentThread().getName()+"::Is now ready!");
        state[3]++;

        //If there is an active speaker we should get the word
        if(state[2] > 0 ){
            System.out.println("\tListener ::"+KThread.currentThread().getName()+"::found an active speaker, awakening speaker and sleeping until it produces a word!");
            this.state[4]++;
            this.conditions[3].wake();
            this.conditions[2].sleep();
            this.state[4]--;
            state[3]--;
            communicator_mutex.release();
            return this.my_word;
        }else{
            //If there is no active speaker the thread should wake one;
            if(state[0] > 0){
                System.out.println("\tListener ::"+KThread.currentThread().getName()+"::found no active speakers, waking a waiting one!");
                this.conditions[0].wake();
            }else{
                //If there are no speakers present just wait for one to arrive
                while(state[0] <= 0){
                    System.out.println("\tListener ::"+KThread.currentThread().getName()+"::Found no speakers present, going to sleep until one arrives!");
                    state[3]--;
                    state[1]++;
                    this.conditions[0].sleep();
                    state[1]--;
                    state[3]++;
                }
            }
            //And then sleep in the waiting word condition
            System.out.println("\tListener ::"+KThread.currentThread().getName()+"::Is now ready to read a word!");
            this.conditions[2].sleep();


            state[3]--;

            //Release the lock
            communicator_mutex.release();

            //Now that we have finished if there are other listeners waiting for their turns we have to signal them
            if(state[1] > 0){
                this.conditions[0].wake();
            }
            System.out.println("\tListener ::"+KThread.currentThread().getName()+"::Has finished its job and is now going to suicide!");
            return this.my_word;
        }
    }

    public static void selfTest(){
        CommunicatorTest.start();
    }
}
