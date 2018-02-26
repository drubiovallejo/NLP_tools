/**
 * @author David Rubio Vallejo
 * 
 * This class defines a Naive Bayes classifier. The training and development data are a collection of movie reviews
 * available from NLTK
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NaiveBayes {
	
	private HashMap<String, Double> prior = new HashMap<String,Double>();
	private HashMap<String, Integer> vocabulary = new HashMap<String, Integer>();
	private HashMap<String, HashMap<String, Double>> likelihood = new HashMap<String, HashMap<String, Double>>();
	private HashMap<String, HashMap<String, Double>> inverse_likelihood = new HashMap<String, HashMap<String,Double>>();
	//Can be modified in the main method
	public Set<String> word_set = new HashSet<String>();


	/**
	 * Trains a NB classifier from sample of movie reviews in training folder
	 * @throws IOException
	 */
	public void train() throws IOException{
		
		Path p = Paths.get(System.getProperty("user.dir"), "src","movie_reviews", "train");
		
		Object[] subpathArray = Files.walk(p).toArray();
		
		String[] categories = {"pos", "neg"};
		
		//For every subpath from where this file is executed
		for(Object elem : subpathArray){
			//Proceed if subpath doesnt refer to a directory (ie, its a file)
			if(! Files.isDirectory((Path) elem)){
				for(String cat : categories){
					//If the path has the name of the category in it
					if(elem.toString().contains(cat)){
						//If HashMap "prior" doesnt have the category yet,
						//add it and give it value 1 (seen once
						if(this.prior.get(cat) == null){
							this.prior.put(cat, 1.0);
						}
						//Else, add 1 to the value
						else{
							this.prior.put(cat, this.prior.get(cat)+1);
						}
						//Create scanner object to read the file
						File f = new File( elem.toString());
						Scanner fileInput = new Scanner(f);
						//While there are words in the file...
						while(fileInput.hasNext()){
							//Get the next one
							String word = fileInput.next();
							//Add it to the vocabulary
							this.addToVocab(word);
							//Add it to the inverseLikelihood map
							this.addToInverseLikelihood(cat,word);
							//And if the set of words of interest has it, add to likelihood map too
							if(word_set.contains(word)){
								this.addToLikelihood(cat, word);
							}
						}
					}
				}
			}
		}
		
		double prior_size = this.prior.get("pos") + this.prior.get("neg");
		//Stores the priors as logs
		this.prior.put("pos", Math.log10((this.prior.get("pos")/prior_size)));
		this.prior.put("neg", Math.log10((this.prior.get("neg")/prior_size)));
		
		//Gets number of tokens in each category
		Double total_pos = 0.0;
		for(Double value : this.inverse_likelihood.get("pos").values()){
			total_pos = total_pos + value;
		}
		Double total_neg = 0.0;
		for(Double value : this.inverse_likelihood.get("neg").values()){
			total_neg = total_neg + value;
		}
		//Stores the tokens in a map	
		HashMap<String,Double> number_tokens = new HashMap<String,Double>();
		number_tokens.put("pos", total_pos);
		number_tokens.put("neg", total_neg);
		
		//For each word of interest
		for(String word : this.word_set){
			//For each class
			for(String clas : this.prior.keySet()){
				//Store the likelihood of that word in the class
				this.likelihood.get(word).put(clas, 
								Math.log( (this.likelihood.get(word).get(clas) +1) /
								(number_tokens.get(clas) + this.vocabulary.size()) ));
			}
		}
	}
	
	
	/**
	 * Tests the trained NB model on the development set folder
	 * @return Map of word to map of label (correct, predicted) - category (pos, neg)
	 * @throws IOException
	 */
	public HashMap<String, HashMap<String, String>> test() throws IOException{
		
		HashMap<String, HashMap<String, String>> results = new HashMap<String, HashMap<String, String>>();
			
		Path p = Paths.get(System.getProperty("user.dir"), "src","movie_reviews", "dev");
		
		Object[] subpathArray = Files.walk(p).toArray();
		
		//For each path in the devset
		for(Object devFile : subpathArray){
			//Proceed if it is not a directory
			if(! Files.isDirectory((Path) devFile)){
											
				HashMap<String,String> labelToCat = new HashMap<String,String>();
				//If path contains word "pos", assign this value to label "correct"
				if(devFile.toString().contains("pos")){
					labelToCat.put("correct", "pos");
				}
				//Else, assign value "neg" to label
				else{
					labelToCat.put("correct", "neg");
				}
				//Put this hashmap in the results map
				results.put(devFile.toString(), labelToCat);
				
				//Map to store class probabilities
				HashMap<String,Double> classProbs = new HashMap<String,Double>();
				//Scanner object to read each file
				File f = new File( devFile.toString());
				Scanner fileInput = new Scanner(f);
				//For each class
				for(String clas : this.prior.keySet()){
					double sumLikelihoods = 1.0;
					//While the file has a next word
					while(fileInput.hasNext()){
						//Get it and if it is in the set of words of interest, get its likelihood and store it
						String word = fileInput.next();
						if(this.word_set.contains(word)){
							sumLikelihoods = sumLikelihoods + this.likelihood.get(word).get(clas);
						}
					}
					//Store the class-probability for this file
					classProbs.put(clas, this.prior.get(clas) * sumLikelihoods);
					//Create new scanner object to reset the pointer to the beginning of the file
					fileInput = new Scanner(f);
				}

				//The predicted class for a given file will be that which has a greater probability
				String chosenClas = "";
				double prob = -1;
				for(String clas : classProbs.keySet()){
					if(classProbs.get(clas) > prob){
						prob = classProbs.get(clas);
						chosenClas = clas; 
					}
				}
				//Add predicted category to map
				results.get(devFile.toString()).put("predicted", chosenClas);
			}
		}	
		return results;
	}
	
	
	/**
	 * Evaluates the map created in the test method and prints metrics
	 * @param results
	 */
	public void evaluate(HashMap<String, HashMap<String, String>> results){
		
		//true positive, false negative, false positive, true negative
		double tp = 0;
		double fn = 0;
		double fp = 0;
		double tn = 0;
		
		//For each document in the input map calculate values accordingly
		for(String doc : results.keySet()){
			if(results.get(doc).get("correct").equals("pos") &&
					results.get(doc).get("predicted").equals("pos")	){
				tp += 1;
			}
			else if(results.get(doc).get("correct").equals("pos") &&
					results.get(doc).get("predicted").equals("neg")){
				fn += 1;		
			}
			else if(results.get(doc).get("correct").equals("neg") &&
					results.get(doc).get("predicted").equals("pos")){
				fp += 1;		
			}
			else{
				tn += 1;
			}
		}
		
		//Calculates metrics		
		double precision = tp/ (tp+fp);
		double recall = tp/ (tp+fn);
		double f1 = (2*precision*recall) / (precision+recall);
		double accuracy = (tp+tn) / (tp+tn+fp+fn);
		
		System.out.println(tp + " " + fn + " " + fp);
		System.out.println("Precision score for DevSet = " + precision);
		System.out.println("Recall score for DevSet = " + recall);
		System.out.println("F1 score for DevSet = " + f1);
		System.out.println("Accuracy for DevSet = " + accuracy);
	}
	

	/**
	 * Helper method. Adds word to vocabulary map
	 * @param word
	 */
	private void addToVocab(String word){
		
		if(this.vocabulary.get(word) == null){
			this.vocabulary.put(word, 1);
		}
		else{
			this.vocabulary.put(word, this.vocabulary.get(word)+1);
		}
	}
	
	/**
	 * Helper method. Adds category label and word to inverseLikelihood map
	 * @param cat
	 * @param word
	 */
	private void addToInverseLikelihood(String cat, String word){
		//adds word to inverseLikelihood map
		if(this.inverse_likelihood.get(cat) == null){
			HashMap<String, Double> wordInvLikMap = new HashMap<String,Double>();
			this.inverse_likelihood.put(cat, wordInvLikMap);
			this.inverse_likelihood.get(cat).put(word, 1.0);
		}
		else{
			if(this.inverse_likelihood.get(cat).get(word) == null){
				this.inverse_likelihood.get(cat).put(word, 1.0);
			}
			else{
				this.inverse_likelihood.get(cat).put(word, 
						this.inverse_likelihood.get(cat).get(word)+1);	
			}
		}
	}

	/**
	 * Helper method. Adds category label and word to likelihood map
	 * @param cat
	 * @param word
	 */
	private void addToLikelihood(String cat, String word){
		//instantiates the map of category to likelihood the first time this method is called
		if(this.likelihood.get(word) == null){
			HashMap<String, Double> catLikMap = new HashMap<String,Double>();
			this.likelihood.put(word, catLikMap);
			this.likelihood.get(word).put(cat, 1.0);
		}
		else{
			if(this.likelihood.get(word).get(cat) == null){
				this.likelihood.get(word).put(cat, 1.0);
			}
			else{
				this.likelihood.get(word).put(cat, 
						this.likelihood.get(word).get(cat)+1);	
			}
		}
	}

}
