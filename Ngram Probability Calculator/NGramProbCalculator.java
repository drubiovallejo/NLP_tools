/*
.:DRV:.

This program requests an input string (it should be either a unigram or a bigram) and it calculates its probability (unigram prob if input is a unigram; unigram and bigram prob if input is bigram) in the given text (here set to "The sayings of Confucious").

*/

import java.util.*;

public class NGramProbCalculator {
	
	String[] arr;
	

	public  NGramProbCalculator(String[] array1){
			arr = array1;
	};
	
	

	//Calculates the prob of a string based on the prob of each of its words independently considered (unigrams)
	public float uniProb(String[] array){
		
		float count = 0;
		float finalProb = 1;
		
		int nbWords = array.length;
		
		for(int q=0; q<nbWords; q++){
			for(int j=0; j< arr.length; j++){
				if(arr[j].equals(array[q])){
					count+=1;
				}
			}
			
			float prob = count/(arr.length);
			finalProb = finalProb * prob;
		}
		return finalProb;
	}
	


	//Calculates the probability of a bigram
	public float bigramProb(String[] array){

		float count = 0;
		
		//create a String[] of bigrams of the input text based on the array in the constructor via the ArrayList "arrayListBigrs"
		ArrayList<String> arrayListBigrs = new ArrayList<String>();
		
		//array.length-1 because the number of bigrams is always one less than the number of unigrams
		for(int t=0; t<(arr.length-1); t++){
			String concat = arr[t] + " " + arr[t+1];
			
			arrayListBigrs.add(concat);
		}
		
		String[] arrayBigrs = new String[arrayListBigrs.size()];
		
		//this is now the array of bigrams that make up the original text
		arrayBigrs = arrayListBigrs.toArray(arrayBigrs);
		
		
		
		//Calculates how many times we see the bigram (numerator in the probability equation)
		String bigramString = new String();
		
		for(int h=0; h<2; h++){
			bigramString = bigramString + " " + array[h];
		}
		
		String trimmed = bigramString.trim();
		
		for(int g=0; g<arrayBigrs.length; g++){
			if(arrayBigrs[g].equals(trimmed)){
				count+=1;
			}
			
		
		}
		
		
		//Calculates how many times w-1 (the first word in the bigram) appears (the denominator)
		
		int nbWords = arr.length;
		int countWminus1 = 0;
		
		for(int q=0; q<nbWords; q++){
			if(array[0].equals(arr[q])){
				countWminus1+=1;
			}
		}
		
			
		//The result of the probability:
		float probOfBigram = count/countWminus1;
		
		return probOfBigram;
	}
	



	public HashMap<String,Integer> otherContinuations(String[] array){
		
		HashMap<String, Integer> hashmapOfContinuations = new HashMap<String, Integer>();
		String wMinus1 = array[0];
		
		int nbWords = arr.length;
		//int count = 0;
		
		for(int q=0; q<nbWords; q++){
			
			String noPunctuation = null;
			
			//to ensure that we do not go out of bounds in calculating the q+1 position in the array when q is already the last one
			if(q<nbWords-1){
			 noPunctuation = arr[q+1].replaceAll(",|;|:", "");
			}
			
			
			if(wMinus1.equals(arr[q]) && ! hashmapOfContinuations.containsKey(noPunctuation)){
				hashmapOfContinuations.put(noPunctuation,1);
			}
			
			else if(wMinus1.equals(arr[q]) &&  hashmapOfContinuations.containsKey(noPunctuation)){
				
				int count = hashmapOfContinuations.get(noPunctuation);
				hashmapOfContinuations.put(noPunctuation,count+1);
			}
		}
		
		return hashmapOfContinuations;
	}
	
	
	

	//This function calculates the prob of a single word string in the array that the construction in this class was initialized with
	public float eachWordProb(String word){
			
		float count = 0;
			
		for(int j=0; j< arr.length; j++){
			if(arr[j].equals(word)){
				count+=1;
			}
		}
				
		float prob = count/(arr.length);
			
		return prob;
	}
	
}
