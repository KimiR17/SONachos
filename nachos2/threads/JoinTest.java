package nachos.threads;

import nachos.machine.*;


public class JoinTest {

    private static class LoopThread implements Runnable {
	LoopThread(String name, int iterations) {this.name = name; this.iterations = iterations;}

        public void run() {
            for (int i=0; i<iterations; i++) {
                System.out.println(name + " looped " + i + " times ::"+(iterations-i)+" iterations remaining;");
                KThread.yield();
            }
            System.out.println(name + " done");
        }


        private String name;
	private int iterations;
    }

    private static class JoinThread implements Runnable {
        JoinThread(String name, KThread thread1, KThread thread2) {
            this.name = name;
            this.thread1 = thread1;
            this.thread2 = thread2;
        }

        public void run() {

            if (thread1 != null) {
              System.out.println("Thread ::"+ name+":: Joining with ::"+thread1.toString());
              thread1.join();
              System.out.println("Done");
            }
            if (thread2 != null) {
              System.out.println("Thread ::"+ name+":: Joining with ::"+thread2.toString());
              thread2.join();
              System.out.println("Done");
            }
            System.out.println("Thread ::"+name+":: has finished");
        }

        private String name;
        private KThread thread1;
        private KThread thread2;
    }

    /**
     * Tests whether this module is working.
     */
    public static void runTest() {

        System.out.println("Begin Join Test");

        KThread loopThreads[] = new KThread[5];
        for (int i=0; i < 5; i++) {
          loopThreads[i] = new KThread(new LoopThread("loop_:"+(i+i+2),(i+i+2)));
          loopThreads[i].setName("loop_:"+(i+i+2));
          loopThreads[i].fork();
        }

        KThread join_1 = new KThread(new JoinThread("join_:1",loopThreads[0],loopThreads[1]));
        join_1.setName("join_:1");
        join_1.fork();

        KThread join_2 = new KThread(new JoinThread("join_:2",loopThreads[1],loopThreads[3]));
        join_2.setName("join_:2");
        join_2.fork();

        KThread join_3 = new KThread(new JoinThread("join_:3",join_1,loopThreads[4]));
        join_3.setName("join_:3");
        join_3.fork();

        for (int i=0; i < 5; i++){
		loopThreads[i].join();
        }
        join_1.join();
        join_2.join();
        join_3.join();

        System.out.println("End of Join Test");
    }

}
