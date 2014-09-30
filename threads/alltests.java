package nachos.threads;

import nachos.machine.*;
import java.util.*;



public class ThreadedTest{
	protected KThread test_main;
	protected KThread[] test_children;
	
	public virtual boolean createThreads(Integer count);
	public virtual boolean runTests();
}




public class AlarmTest extends ThreadedTest{
	public AlarmTest(){}
	public boolean createThreads(Integer count);
	public boolean runTests();
}
public class Condition2Test extends ThreadedTest{
	
}
public class JoinTest extends ThreadedTest{
	public JoinTest(){
		test_main = new KThread();
		test_children = new KThread[2]();
		for(int i = 0 ; i < 2 ; i++){
			test_children[i]
		}
	}
}