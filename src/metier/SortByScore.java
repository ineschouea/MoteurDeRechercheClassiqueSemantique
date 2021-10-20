package metier;

import java.util.Comparator;

public class SortByScore implements Comparator<Document> {
	
	  public int compare(Document a, Document b) 
	    { 
		  if(a.getScore()<b.getScore()){
	           return 1;
	       }else{
	           if(a.getScore()>b.getScore()){
	            return -1;   
	           }else{
	               return 0;
	         
	    } 

}
}
}