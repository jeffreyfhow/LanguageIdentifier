import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LanguageIdentifier {

	public static char[] characters = new char[1];
	public static void main(String[] args) {
		// Read character file
        BufferedReader br = null;
        //char[] characters = new char[1];
        int charCount = 0;
        LanguageModel englishModel;
        LanguageModel frenchModel;
        LanguageModel spanishModel;
        
        //---------------------------------------------------------------------
        //						SET UP CHARACTERS
        //---------------------------------------------------------------------
        try{
            br = new BufferedReader(new FileReader("CharacterList.txt"));
            br.mark(150);
            
            //get count
            int currentAscii = br.read();
            while(currentAscii != -1){
                charCount++;
                currentAscii = br.read();
            }
            //init array and reset reader
            characters = new char[charCount];
            br.reset();
            charCount = 0;
            currentAscii = -1;
            
            //read characters into array
            currentAscii = br.read();
            while(currentAscii != -1){
                characters[charCount] = (char)currentAscii;
                currentAscii = br.read();
                charCount++;
            }
            br.close();
            
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
        finally{
        	//use array to make bigrams and populate from texts
            englishModel = new LanguageModel(characters, "EnglishText.txt", "ENGLISH");
            frenchModel = new LanguageModel(characters, "FrenchText.txt", "FRENCH");
            spanishModel = new LanguageModel(characters, "SpanishText.txt", "SPANISH");
            //characters = null;
        }
        
        
        //---------------------------------------------------------------------
        //						GET INPUT STRING
        //---------------------------------------------------------------------
        Scanner keyboard = new Scanner(System.in);
        String loop;
        do{
	        System.out.println("Please enter a sentence and press Enter: ");
	        String sentence = keyboard.nextLine();
	        char currChar = '?';
	        char prevChar = '?';
	        double englishAccumScore = 0.0f;
	        double frenchAccumScore = 0.0f;
	        double spanishAccumScore = 0.0f;
	        String tempString;
	        for (int i = 0; i < sentence.length(); i++){
	        	prevChar = currChar;
	        	currChar = validateChar(sentence.charAt(i));
	        	System.out.println("Bigram: " + prevChar + currChar);
	        	englishAccumScore += testBigram(prevChar,currChar, englishModel);
	        	frenchAccumScore += testBigram(prevChar,currChar, frenchModel);
	        	spanishAccumScore += testBigram(prevChar,currChar, spanishModel);
	        	System.out.println();
	        }
	        System.out.println("------ACCUMULATED SCORES------");
	        System.out.println("English: " + englishAccumScore);
	        System.out.println("French: " + frenchAccumScore);
	        System.out.println("Spanish: " + spanishAccumScore);
	        String displayThis = "THE SENTENCE IS IN: ";
	        if(englishAccumScore > frenchAccumScore && englishAccumScore > spanishAccumScore){
	        	displayThis += "ENGLISH";
	        }
	        else if(englishAccumScore < frenchAccumScore && frenchAccumScore > spanishAccumScore){
	        	displayThis += "FRENCH";
	        }
	        else if(englishAccumScore < spanishAccumScore && frenchAccumScore < spanishAccumScore){
	        	displayThis += "SPANISH";
	        }
	        else{
	        	displayThis += "THERE WAS A TIE!";
	        }
	        System.out.println(displayThis);
	        System.out.println("---------------------------------");
	        System.out.println("Would you like to try again? [Y/N]");
	        loop = keyboard.nextLine();
        } while(loop.equalsIgnoreCase("y") || loop.equalsIgnoreCase("yes"));
        
        System.out.println("Thank you for using Jeff's Language Identifier!");
	}
	
	public static char validateChar(char c){
		c = Character.toUpperCase(c);
		for(int i = 0; i < characters.length; i++){
			if(c == characters[i]){
				return c;
			}
		}
		return '?';
	}
	
	public static double testBigram(char prev, char curr, LanguageModel lm){
		double returnThis = -1.0;
		for (int i = 0; i < lm.bigramList.size(); i++){
			if(lm.bigramList.get(i).equals(prev, curr)){
				returnThis = lm.bigramList.get(i).getProbability();
				break;
			}
		}
		
		if(returnThis == -1.0){
			return returnThis;
		}
		
		String tempString = lm.language + ": P(" + prev + ", " + curr + ") = " 
				+ returnThis + " ==> log prob of sequence so far: " ;
    	
		returnThis = Math.log10(returnThis);
		tempString += returnThis;
		System.out.println(tempString);
		
		return returnThis;
	}

}
