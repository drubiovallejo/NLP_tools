/**
 * @author David Rubio Vallejo
 * 
 * This class contains the main method to create a NB object, train it and evaluate it
 */

import java.io.IOException;


public class NaiveBayesMain {

	public static void main(String[] args) throws IOException {
		
		//Creates a NB object
		NaiveBayes nb = new NaiveBayes();
		
		//Adds words to set of words of interest
		nb.word_set.add("great");
		nb.word_set.add("no");
		nb.word_set.add("don't");
		nb.word_set.add("bad");
		nb.word_set.add("could");
		nb.word_set.add("plot");
	
		//Trains the model
		nb.train();
		//Tests it on the devset and prints evaluation metrics
		nb.evaluate(nb.test());
	
	}

}
