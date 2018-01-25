package SearchEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GeneOrginDict{

	public HashMap<String, Set<String>> origincalDct() throws IOException {

		 HashMap<String, Set<String>> invertedIndexMap = new HashMap<String, Set<String>>();
		
		for (int n = 1; n <= 5; n++){
			BufferedReader br = new BufferedReader(new FileReader("DocAfterAna//afterAna_"+n+".txt"));
		
			String line="";
			StringBuffer buffer = new StringBuffer();
			
			StringBuffer bufferUrl = new StringBuffer();
			
			
			
			if ((line=br.readLine()) != null) {
				bufferUrl.append(line.trim());
			}
			for(int i = 0;i < 3;i++) {
				line=br.readLine();
			}
			while((line=br.readLine())!=null){
				buffer.append(line.trim()+" ");
			
			}
			
			
			String fileContent = buffer.toString();
			String UrlContent = bufferUrl.toString();
		
			String regEx="\\s";
			Pattern p = Pattern.compile(regEx);

			String[] strWords = p.split(fileContent);
			
			for (int i = 0; i < strWords.length; i++) {
				strWords[i]= strWords[i].trim().toLowerCase();
				
				if ( !invertedIndexMap.containsKey(strWords[i].toString()) ) {
					Set<String> tempSet = new HashSet<String>();
					tempSet.add(UrlContent);
					
					invertedIndexMap.put(strWords[i].toString(), tempSet);
					
				} else {
					Set<String> tempSet = new HashSet<String>();
					tempSet = invertedIndexMap.get(strWords[i].toString());
					tempSet.add(UrlContent);					
					invertedIndexMap.put(strWords[i].toString(), tempSet);
				}				
			}
		
			try{			
				File file = new File("OriginalDictionary.txt");
				file.createNewFile();
				FileWriter fileWritter;
				if(n == 1){
					fileWritter = new FileWriter(file.getName(),false);
				}else{
					fileWritter = new FileWriter(file.getName(),true);
				}
				
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				for(String strItems:strWords){
					
					String regEx_spe = "[`~!@#$%^&*()+=|{}':;',-//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"\']";  
			        Pattern pa = Pattern.compile(regEx_spe);  
			        Matcher m = pa.matcher(strItems);  
			        String dict_word = m.replaceAll("").trim().toLowerCase();
			        if(dict_word != null && dict_word.length()!=0){
			        	bufferWritter.append(dict_word);
						bufferWritter.newLine();
			        }
					
				}
				bufferWritter.close();
				//System.out.println("Add file_"+n+" to dictionary!");
				
			}catch(IOException e){
				e.printStackTrace();
		    }
		}
		return invertedIndexMap;
	}
}
