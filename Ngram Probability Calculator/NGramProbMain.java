/*
.:DRV:.

This program requests an input string (it should be either a unigram or a bigram) and it calculates its probability (unigram prob if input is a unigram; unigram and bigram prob if input is bigram) in the given text (here set to "The sayings of Confucious").

*/


import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class NGramProbMain {
	
	
	public static void main(String[] args) {
			
		
		String fileName = "confucius.txt";
		  
		//Makes a string array from the words in the text file
        	String[] array = fileToArray(fileName);
       	 
	    
		System.out.print("Enter a 1- or 2-word string: ");
		 
		//Creates an array from the input string
		Scanner console = new Scanner(System.in); 
		String[] inputArray = inputStringToArray(console); 
		console.close();
		 		
		
		//Initializes the constructor and passes the string array of words from the text file to it
		NGramProbCalculator ngProb = new NGramProbCalculator(array);
		
		probabilities(inputArray, ngProb);
		
	}


	
	public static String[] fileToArray(String fileName){

        
		String[] array = null;
        	String line = null;
        	String st = null;

	    	try {
	    		// FileReader reads text files in the default encoding.
	        	FileReader fileReader = new FileReader(fileName);

	        	// Always wrap FileReader in BufferedReader.
	        	BufferedReader bufferedReader = new BufferedReader(fileReader);

	        	StringBuilder sb = new StringBuilder();
	           
	        	while((line = bufferedReader.readLine()) != null) {
	               
	        		//append each line with a space between them	        	   
	        		sb = sb.append(line + " ");
	        	}   
	           
	    		//Turns SB into String
	    		st = sb.toString();
	            
	          	    		
	    		//substitute full-stops by " #", 2 or more spaces between words by a single space, and eliminates all commas and semi-colons
	    		st = st.replaceAll("\\.(?=\\s|$)", " #");
	    		st = st.replaceAll("\\s{2,}", " ");
	    		String sentence = st.replaceAll(";|,", "");	
	    		
	    		array = sentence.split(" ");
	    		
	    		for(int i=0; i< array.length ; i++){
	    			
	    			if( array[i].endsWith(".")){
	    	
	    				int length = array[i].length();
	    				String subSt = array[i].substring(0,length-1);
	    				array[i] = subSt;
	    				
	    			}
	    		}
		
	    		//Always close files.
	     		bufferedReader.close();   
		}
	       
	    catch(FileNotFoundException ex) {

	    	System.out.println("Unable to open file '" + fileName + "'");                
	    }
	    
	    catch(IOException ex) {

	    	System.out.println( "Error reading file '" + fileName + "'");                  
	          
	    }


	return array;  		
	}
	
	

	public static String[] inputStringToArray(Scanner scan){


		//Reads the whole line and split it into its words
		String input = scan.nextLine();
		String[] inputArray = input.split(" ");
		
		return inputArray;
	}


	public static void probabilities(String[] inputArray, NGramProbCalculator ngProb){

		if(inputArray.length == 1){
			
			//computing the probability of a string as unigrams (ie. prob of the string based on the individual probabilities of its parts
			float probabilityUni = ngProb.uniProb(inputArray);
			System.out.println("The unigram probability of the string \"" + inputArray[0] + "\" in the given text is: " + probabilityUni);
		}
				
		else if(inputArray.length == 2){
			
			//computing the probability of a string as unigrams (ie. prob of the string based on the individual probabilities of its parts
			float probabilityUni = ngProb.uniProb(inputArray);
			System.out.println("The unigram probability of the string \"" + inputArray[0] + " " + inputArray[1] + "\" in the given text is: " + probabilityUni);
			
			//computing the probability of a string as a bigram (ie. prob for W to be followed by W-1 in the given text) 
			float probabilityBi = ngProb.bigramProb(inputArray);
			System.out.println("The bigram probability of the string \"" + inputArray[0] + " " + inputArray[1] + "\" in the given text is: " + probabilityBi);
			
			System.out.println();

			//other possible continuations of the first word in the bigram
			HashMap<String,Integer> continuations = ngProb.otherContinuations(inputArray);
			System.out.println("The word \"" + inputArray[0] + "\" is also followed by (word, # of times):");
			
			for(String key : continuations.keySet()){
							
				System.out.print(key + " " + continuations.get(key) + ", ");
			}
			
		}
	}
}
