/*
.:DRV:.

Contains the code for running the code in MEDFinder.
*/


import java.util.Scanner;


public class MinEditDistanceMain {


	public static void main(String[] args) {

		//creates the objects
		MEDFinder medf = new MEDFinder();
		Scanner console = new Scanner(System.in);

		//code to enter first input
		System.out.print("Enter the first word: ");
		String scanned1 = console.next(); 
		 
		//code to enter second input
		System.out.print("Enter the second word: ");
		String scanned2 = console.next();
		
		console.close();
				
			
		//we pass the inputs to the method in the MEDFinder file and assign the returned matrix to the variable "finalMatrix"
		String[][] finalMatrix = medf.findDistance(scanned1, scanned2); 
		
			
		//final step: take the string in the bottom right corner and return it (fyi: it's in string form!).
		String MinEdDistance = finalMatrix[scanned2.length()][scanned1.length()];
		
		System.out.println("The minimum edit distance of those two words is: " + MinEdDistance);
		
		//also prints the matrix itself 
		for(int k = 0 ; k<scanned2.length()+2; k++){
			
			for(int j=0; j<scanned1.length()+2; j++){

				System.out.print(finalMatrix[k][j] + " ");
			}
		
			System.out.println();
		}
	}
}

