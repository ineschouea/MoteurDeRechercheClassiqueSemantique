package metier;

public class Standard_Weighting {

	public static double getTF(double count, String type){

		if(type.equals("b")) if(count>0) return 1; else return 0;

		if(type.equals("l")) return 1+Math.log(count);

		return count; // default

	}

	

	public static double getIDF(double df, double N, String type){

		if(type.equals("s")) return Math.log((N+1)/(df+0.5));

		if(type.equals("p")) return Math.log((N-df)/df);

		if(type.equals("b")) return Math.log((N-df+0.5)/(df+0.5));

		return Math.log(N/df); // default

	}

}