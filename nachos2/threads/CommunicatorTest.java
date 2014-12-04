package nachos.threads;

import nachos.machine.*;
import java.util.*;


public class CommunicatorTest{
	public static class JeohvahsWitness implements Runnable{

		private Random random_gibberish_generator;
		private Random random_delay_generator;
		private String ID;
		private int number_of_words;
		private Communicator commun;

		public JeohvahsWitness(String ID, int number_of_words, Communicator commun){
			random_gibberish_generator = new Random();
			random_delay_generator = new Random();
			this.ID = ID;
			this.number_of_words = number_of_words;
			this.commun = commun;
		}

		public int generateGibberish(){return random_gibberish_generator.nextInt(255);}//Generates a word between 0 and 255
		public int generateDelay(){return random_delay_generator.nextInt(8192);}//Generates a random delay between 0 and 8192

		public void run(){
			System.out.println("Jeohvah's Witness number ::"+ID+":: ready to speak gibberish");

			for(int i = 0 ; i < number_of_words ; i++){
				Long time = Machine.timer().getTime() + 1000;
				System.out.println(ID+":: sleeping until ::"+(Machine.timer().getTime() + time));
				ThreadedKernel.alarm.waitUntil(time);
				System.out.println(ID+"'s delay is over, will now speak!");
				commun.speak(generateGibberish());
			}

			System.out.println("Witness ::"+ID+":: has been murdered");
		}
	}

	public static class PoorHuman implements Runnable{
		private String ID;
		private int bleeding_ear;
		private int number_of_words;
		private Communicator commun;

		public PoorHuman(String ID, int number_of_words, Communicator commun){
			this.ID = ID;
			this.commun = commun;
			this.number_of_words = number_of_words;
		}


		public void run(){
			System.out.println("Poor human called ::"+ID+":: ready to have his eardrums pierced");
			for(int i = 0 ; i < number_of_words ; i++){
				System.out.println(ID+":: sleeping until ::"+(Machine.timer().getTime()));
				ThreadedKernel.alarm.waitUntil(Machine.timer().getTime() + 1500);
				System.out.println(ID+"'s delay is over, will now listen!");
				int word = commun.listen();
				System.out.println(ID+"'s listening result ::"+word);
			}
			System.out.println("Human ::"+ID+":: has died of exsanguination");
		}
	}

	public static boolean start(){
		KThread speakers[] = new KThread[4];
		KThread listeners[] = new KThread[4];
		Communicator comm = new Communicator();

		speakers[0] = new KThread(new JeohvahsWitness(("Mohammed"),2,comm));
		speakers[0].setName("Mohammed");

		speakers[1] = new KThread(new JeohvahsWitness(("Madhukar"),2,comm));
		speakers[1].setName("Madhukar");

		speakers[2] = new KThread(new JeohvahsWitness(("Gertrudes"),2,comm));
		speakers[2].setName("Gertrudes");

		speakers[3] = new KThread(new JeohvahsWitness(("Krishna"),2,comm));
		speakers[3].setName("Krishna");

		listeners[0] = new KThread(new PoorHuman(("Bob"),2,comm));
		listeners[0].setName("Bob");

		listeners[1] = new KThread(new PoorHuman(("Delicia_de_condicao_2"),2,comm));
		listeners[1].setName("Delicia_de_condicao_2");

		listeners[2] = new KThread(new PoorHuman(("Delicia_de_funcao"),2,comm));
		listeners[2].setName("Delicia_de_funcao");

		listeners[3] = new KThread(new PoorHuman(("Jailson"),2,comm));
		listeners[3].setName("Jailson");


		//First two tests consist of, Speak then Listen :: Listen then speak
		System.out.println("\n******************\n\tStarting Communicator Test\n");
		speakers[0].fork();
		listeners[0].fork();
		speakers[0].join();
		listeners[0].join();

		listeners[1].fork();
		speakers[1].fork();
		listeners[1].join();
		speakers[1].join();

		speakers[2].fork();
		speakers[3].fork();
		listeners[2].fork();
		listeners[3].fork();
		speakers[2].join();
		speakers[3].join();
		listeners[3].join();
		listeners[2].join();
		System.out.println("\n******************\n\nEnding Communicator Test\n");
		return true;
	}

}
