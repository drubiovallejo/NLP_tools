
public class MEDFinder {
	
	public MEDFinder(){};
	
	String[][] matrix;
	
	
	public String[][] findDistance(String x, String y){
		
		int lengthX = x.length();
		int lengthY = y.length();
		
		int nbRows = lengthY +2 ;
		int nbColumns = lengthX +2;
		
	matrix = new String[nbRows][nbColumns];
		
	matrix[0][0] = "#";
	matrix[0][1] = "#";
	matrix[1][0] = "#";
	matrix[1][1] = "0";
	
	int count1 = 0;
	int count2 = 0;
	
	//populate  row 0 with the letters from the first word (starting in column 2)
	for(int i=2; i< nbColumns  ; i++){
		
		matrix[0][i] = x.substring(count1, count1+1);
		count1+=1;
				
	}
	
	//populate row 1 with the (string form of the) digits 1 thru length of the first word
	for(int k=2; k<nbColumns; k++){
		matrix[1][k] = Integer.toString(k-1);
	}
	
	//populate column 0 with the letters from the second word (starting in row 2)
	for(int j=2; j< nbRows ; j++){
		matrix[j][0] = y.substring(count2, count2+1);
		count2+=1;
	}
	
	//populate column 1 with the (string form of the) digits 1 thru length of the second word
	for(int l=2; l<nbRows; l++){
			matrix[l][1] = Integer.toString(l-1);
	}
	
	
	
	//this is now the actual implementation of the Levenshtein algorithm
	
	for(int m=2; m<(nbRows); m++){
		for(int n=2; n<(nbColumns); n++){
									
			int deletionValue = (Integer.parseInt(matrix[m-1][n])) +1;
			
			int insertionValue = (Integer.parseInt(matrix[m][n-1])) +1;
			
			int substitutionValue;
			
			//we need to start counting substrings from the 0 position in the string! Hence substring(m-2,m-1)
			if(y.substring(m-2,m-1).equals(x.substring(n-2, n-1))){
				
				substitutionValue = Integer.parseInt(matrix[m-1][n-1]);
			}
			
			//value of substitution in Levenshtein is +2
			else{ 
				substitutionValue = (Integer.parseInt(matrix[m-1][n-1])) +1;
			}
			
			int min1 = Math.min(deletionValue, insertionValue);
			int minFinal = Math.min(min1, substitutionValue);
			
			matrix[m][n] = Integer.toString(minFinal);
			
		
		}
	}
	
	
	
		return matrix;
	}
	
	

}
