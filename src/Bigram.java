
public class Bigram {
	public char first;
    public char second;
    public float count;
    private double probability;
    public static int bigramCount = 0;
    
    public Bigram(char one, char two){
        first = one;
        second = two;
        count = 0.0f;
        probability = -1.0;
        bigramCount++;
    }
    
    public boolean equals(Bigram b){
        return first == b.first && second == b.second;
    }
    
    public boolean equals(char one, char two){
    	return first == one && second == two;
    }
    
    public String toString(){
        return  "first: " + first + "; second: " + second + 
        		"; count: " + count + " prob: " + probability;
    }
    
    public boolean setProbability(double d){
    	if(probability < 0.0){
    		probability = d;
    		return true;
    	}
    	return false;
    }
    
    public double getProbability(){
    	return probability;
    }
}
