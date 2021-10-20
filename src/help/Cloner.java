package help;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cloner {
	public void cloner(String path, String file1, String file2) throws IOException
	{
	
	FileWriter f = new FileWriter(path+file2, false);
	BufferedWriter bw = new BufferedWriter(f);
	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path+file1))); 
 String line;    
 while ((line = reader.readLine()) != null) {   
 	bw.write(line);
		bw.newLine();
		bw.flush();
		 
     }
   bw.close();
   reader.close();
   File file=new File(path+"permut.txt");
   file.delete();
	}
}
