import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LanguageModel {
	public ArrayList<Bigram> bigramList = new ArrayList<Bigram>();
	public int bigramInstanceCount;
	public static float smoothFactor = 0.5f;
	public String language;
	//public char[] characters;
	
	public LanguageModel(char[] chars, String filename, String lang){
		language = lang;
		//use array to make bigrams
        for(int i = 0; i < chars.length; i++){
            for(int j = 0; j < chars.length; j++){
                bigramList.add(new Bigram(chars[i],chars[j]));
            }
            bigramList.add(new Bigram(chars[i],'?'));
        }
        for(int j = 0; j < chars.length; j++){
            bigramList.add(new Bigram('?',chars[j]));
        }
        bigramInstanceCount = 0;
        //this.characters = chars;
        
        readText(filename);
	}
	
	public void readText(String filename){
		//initialize
		BufferedReader br = null;
		char currChar = '?';
		char prevChar = '?';
		try{
            br = new BufferedReader(new FileReader(filename));
            //read characters one by one and add new bigram
            int currentAscii = br.read();
            while(currentAscii != -1){
            	bigramInstanceCount++;
            	prevChar = currChar;
            	currChar = LanguageIdentifier.validateChar((char)currentAscii);
            	if(prevChar != '?' || currChar != '?'){
            		for(int i = 0; i < bigramList.size(); i++){
            			if(bigramList.get(i).equals(prevChar, currChar)){
            				bigramList.get(i).count++;
            				bigramInstanceCount++;
            				break;
            			}
            		}
            	}
                currentAscii = br.read();
            }
            br.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
		
		//add .5 to every count (smoothing)
		for(int i = 0; i < bigramList.size(); i++){
			bigramList.get(i).count += 0.5f;
		}
		
		//set probabilities
		for(int i = 0; i < bigramList.size(); i++){
			setBigramProbability(i);
		}
	}
	
	public String toString(){
		String returnThis = "";
		for(int i = 0; i < bigramList.size(); i++){
			returnThis += bigramList.get(i).toString() + "\n";
		}
		returnThis += "Total bigram instances: " + bigramInstanceCount;
		return returnThis;
	}
	
	/*public char validateChar(char c){
		c = Character.toUpperCase(c);
		for(int i = 0; i < LanguageIdentifier.characters.length; i++){
			if(c == characters[i]){
				return c;
			}
		}
		return '?';
	}*/
	
	public boolean setBigramProbability(int index){
		Bigram tempBigram = bigramList.get(index);
		double prob = ((double)tempBigram.count + smoothFactor)/((double)bigramInstanceCount + (float)Bigram.bigramCount * smoothFactor);
		tempBigram = null;
		return bigramList.get(index).setProbability(prob);
	}
	
	public double getBigramProbability(char one, char two){
		for(int i = 0; i < bigramList.size(); i++){
			if(bigramList.get(i).equals(one, two)){
				return bigramList.get(i).getProbability();
			}
		}
		return -1.0;
	}
}
