package PreProcess;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;


public class WebAnalyzer {

	private static final String ENDPAGE = "**************************************************";	

	public WebAnalyzer(){
		
	}

	public ArrayList<URL> doAnalyzer(BufferedWriter bfWriter, URL url, String htmlDoc) {
	
		System.out.println("in doing analyzer the size of doc is: " + htmlDoc.length());
		System.err.println("*******Crawl " + url + " successful*******");

		ArrayList<URL> urlInHtmlDoc = (new HtmlParser()).urlDetector(htmlDoc);	
		saveDoc(bfWriter, url, htmlDoc);
		return urlInHtmlDoc;
	}
	
	public void WriteAfterDoc(BufferedWriter bfw, URL url, String afterDoc){
		
		String afterAnalyse = (new HtmlParser()).html2Text(afterDoc);
		
		afterAnalyse = afterAnalyse.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
		saveDoc(bfw, url, afterAnalyse);		
	}	
	
	private void saveDoc(BufferedWriter bfWriter, URL url, String htmlDoc) {

		try {									
			Date date = new Date();     
			String dateStr = "date:" + date.toString() + "\n";
			
			InetAddress address = InetAddress.getByName(url.getHost()); 
			String IPStr = address.toString();
			IPStr = "IP:" + IPStr.substring(IPStr.indexOf("/")+1, IPStr.length()) + "\n";
			
			String htmlLen = "length:" + htmlDoc.length() + "\n";
			
			bfWriter.append(url.toString()+"\n");
			bfWriter.append(dateStr);
			bfWriter.append(IPStr);
			bfWriter.append(htmlLen);
			bfWriter.newLine();
			
			bfWriter.append(htmlDoc);

			bfWriter.newLine();
			
			bfWriter.flush();			
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
