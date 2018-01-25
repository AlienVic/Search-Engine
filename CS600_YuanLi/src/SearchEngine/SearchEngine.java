package SearchEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TrieNode{
	private char _charValue;
	private static final int intSize = 62;// A-Z a-z 0-9 26+26+10
	private boolean _boolIsCName; //if it is company name
	private boolean _boolIsWord; //if it is a word
	private String _strPrimaryName;//
	private int _intCount; //Count of CompanyName
	TrieNode[] tnChildren = new TrieNode[intSize];//child node
	
	//set node char
	public TrieNode(char cr) {
		setValue(cr);
	}

	//set node value
	public void setValue(char cr) {
		_charValue = cr;
	}

	public char getValue() {
		return _charValue;
	}
	
	public boolean isKeyWord() {
		return _boolIsCName;
	}
	
	
	public void setisKeyWord(boolean boolValue) {
		_boolIsCName = boolValue;
	}
	
	//if it is a word
	public boolean isWord() { 
		return _boolIsWord;
	}
	
	public void setIsWord(boolean boolValue) {
		_boolIsWord = boolValue;
	}
	
	public void setPrimaryName(String strName)
	{
		_strPrimaryName=strName;
	}
	
	public String PrimaryName()
	{
		return _strPrimaryName;
	}
	
	public void AddCount(){
		_intCount++;
	}
	
	public int CountNumber()
	{
		return _intCount;
	}
}

class Primary_Name
{
	public String strPrimaryName;
	public int intTotalCount;
}

public class SearchEngine {
	
	
	//create Trie Tree for dictionary
	private void insertIntoTree(TrieNode tnRoot, String[] strArrWords, String strPrimary) {
		if(strArrWords==null||strArrWords.length==0)return;
		//for loop
		for (int j = 0; j < strArrWords.length; j++) {
			String strWord=strArrWords[j];
			
			if(null == tnRoot || null == strWord || "".equals(strWord))return;
			
			char[] charArr = strWord.toCharArray();//change char into charArray
			//for loop
			for(int i = 0; i < charArr.length; i++){
				
				int intIndex;
				
				if(charArr[i] >= 'A' && charArr[i] <= 'Z')
				{
					intIndex = charArr[i]-'A'+10;
				}
				else if(charArr[i] >= 'a' && charArr[i] <= 'z') 
				{
					intIndex = charArr[i] - 'a'+10+26;
				}
				else if(charArr[i] >= 'a' && charArr[i] <= 'z') 
				{
					intIndex = charArr[i] - '0';
				}
				else
				{
					//non-letter char
					return;
				}
				
				TrieNode child_node = tnRoot.tnChildren[intIndex];
				if(null == child_node)
				{//if did not find
					TrieNode tnNewNode = new TrieNode(charArr[i]);//create new root
					if(i == charArr.length-1)
					{
						//save the word in the tree
						tnNewNode.setIsWord(true);
						if(j==strArrWords.length-1)
						{
							tnNewNode.setisKeyWord(true);
							tnNewNode.setPrimaryName(strPrimary);
						}
					}
					tnRoot.tnChildren[intIndex] = tnNewNode;//connect node
					tnRoot = tnNewNode;
				}
				else
				{
					if(i == charArr.length-1&&j==strArrWords.length-1)
					{
						child_node.setisKeyWord(true);
						child_node.setPrimaryName(strPrimary);
					}
					tnRoot = child_node;//update root
				}
			}
		}
	}
	
	
	private static TreeMap<String,Primary_Name> getStatisticResult(TrieNode tnRoot, TreeMap<String,Primary_Name> hmPrime) {
		if(tnRoot == null){
			return hmPrime;
		}
			
		if(tnRoot.isKeyWord()){
			if(hmPrime.containsKey(tnRoot.PrimaryName())) {
				hmPrime.get(tnRoot.PrimaryName()).intTotalCount=hmPrime.get(tnRoot.PrimaryName()).intTotalCount+tnRoot.CountNumber();
			} else {
				Primary_Name pnNew=new Primary_Name();
				pnNew.strPrimaryName=tnRoot.PrimaryName();
				pnNew.intTotalCount=tnRoot.CountNumber();
				hmPrime.put(tnRoot.PrimaryName(), pnNew);
			}
		}
		
		for(TrieNode node : tnRoot.tnChildren){
			if(node != null){
				getStatisticResult(node,hmPrime);
			}
		}
		return hmPrime;
	}
	
	
	
	//Get the index terms in the dictionary
	public static TrieNode getCompanyDic(String strPath) throws IOException{
		SearchEngine ttCompany = new SearchEngine();//create the tree
		TrieNode tnRoot = new TrieNode(' ');//create the root node
		
		//read the file after crawling from Internet
  		File fileOut = new File(strPath);  		
		BufferedReader bReader = new BufferedReader(new FileReader(fileOut));

		String strLine;
		while((strLine=bReader.readLine())!=null)
		{
			String strPrimary = "";
			String[] strCpnNames=strLine.split("	");
			strPrimary=strCpnNames[0];
			for(String strName:strCpnNames)
			{
				String[] strWords = strName.split(" ");
				strWords = filterIllegalChar(strWords);
				ttCompany.insertIntoTree(tnRoot, strWords,strPrimary);
			}
		}
  		
  		return tnRoot;
	}
	
	public static void readArticleLine(TrieNode tnRoot)
	{
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please input article line: ");
		do{
			String strLine=sc.nextLine();
			if(strLine==null||strLine=="")continue;
			if(isFinalLine(strLine))return;
			
		}while (true);
	}
	
	//split the sentence
	public static String[] parsingArticle(String strLine)
	{
		/*Regular Expression: Terminator*/
		String regEx="[。？！?.!]";
		Pattern p = Pattern.compile(regEx);

		/*split sentence by Terminator*/
		String[] strSentences = p.split(strLine);
		for (int i = 0; i < strSentences.length; i++) {
			strSentences[i]=strSentences[i].trim();
		}

		for(String str:strSentences)
		{
			System.out.println(str);
		}
		
		return strSentences;
	}
	
	//return every word in the sentence
	public static String[] parsingSentence(String strSentence)
	{
		/*Regular Expression: Space*/
		String regEx="\\s";
		Pattern p = Pattern.compile(regEx);

		String[] strWords = p.split(strSentence);
		for (int i = 0; i < strWords.length; i++) {
			strWords[i]=strWords[i].trim();
		}

		for(String strItems:strWords)
		{
			System.out.println(strItems);
		}
		
		return strWords;
	}
	
	//if it is final line
	public static Boolean isFinalLine(String strLine)
	{
		if(strLine==null||strLine.length()==0)return false;
		
		String   strReg  =  "[^.]";                     
        Pattern   p   =   Pattern.compile(strReg);     
        Matcher   m   =   p.matcher(strLine); 
        String strResult=m.replaceAll("").trim(); 
        if(strLine.equals(strResult))
        {
        	return true;
        }
        else{return false;}
	}
	
	//filter the illegal char
	public static String[] filterIllegalChar(String[] strArr)
	{
		if(strArr==null||strArr.length==0)return null;
		
		for (int i = 0; i < strArr.length; i++) {
	        String   strReg  =  "[^a-zA-Z0-9]";                     
	        Pattern   p   =   Pattern.compile(strReg);     
	        Matcher   m   =   p.matcher(strArr[i]); 
	        strArr[i]=m.replaceAll("").trim(); 
		}
        return strArr;
	}
	
	public static boolean hasChildNode(TrieNode tn)
	{
		for (int i = 0; i < tn.tnChildren.length; i++) {
			if(tn.tnChildren[i]!=null){return true;}
		}
		return false;
	}
	
public static void main(String[] args) throws IOException{
		
		
		GeneOrginDict orginDict = new GeneOrginDict();
		HashMap<String, Set<String>> thisMap = orginDict.origincalDct();
		
		TrieNode tnRoot = getCompanyDic("OriginalDictionary.txt");
		//Construct a TreeMap for KeyWord and URL
		TreeMap<String,Primary_Name> hmPrime= new TreeMap<String,Primary_Name>();
		TreeMap<String,Primary_Name> hmResult=getStatisticResult(tnRoot,hmPrime);
		
		File file = new File("Dictionary.txt");
		file.createNewFile();
		
		FileWriter fileWritter = new FileWriter(file.getName(),false);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		try {
			for (Map.Entry entry : hmResult.entrySet()) {
				String key = entry.getKey().toString();
				bufferWritter.append(key);
				bufferWritter.newLine();
			}
			bufferWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//End of Construct TreeMap
		
		System.out.println("******************************");
		System.out.println("**      Search Engine       **");
		System.out.println("******************************");
		System.out.println("Find keyword in the dictionary");
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input the keyword: ");
		boolean end = false;
		do{
			String strLine = sc.nextLine().toLowerCase();
			StringBuffer UrlResult = new StringBuffer();
			
			if(strLine==null||strLine==""||strLine.length()==0){
				continue;
			}
			if(isFinalLine(strLine)){
				break;
			}
			String[]  keyWords = strLine.split(" ");
			
			
			
			System.out.println("**********************");
			System.out.println("The valid input is:");

			Set<String> list  = new HashSet<String>();
			
			for(int i = 0; i < keyWords.length;i++) {
				if(thisMap.get(keyWords[i]) == null){
					continue;
				}else{
					
					System.err.print(keyWords[i]+" ");
					if(i > 0){
						list.retainAll(thisMap.get(keyWords[i]));
					}else {
						list = thisMap.get(keyWords[i]);
					}
				}
				
			}
			System.out.println("\r\n");
			
			if (list != null) {
			
	  	   		for ( String a : list) {
	  	   			UrlResult.append(a+"\n");
	  	   		}
	  	   		
	  	   		System.out.println("The keyword is found in the following URLs:");
	  	   		System.out.println();
	  	   		System.out.println(UrlResult.toString());
			} else {
				System.out.println("This keyword is not in the dictionary.");
			}
	  	   	
			end = true;
		}while (!end);
		
		System.out.println();
		System.out.println("Quit!");
	}
	
	
	
}