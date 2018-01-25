package SearchEngine;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import PreProcess.*;

public class Spider{
	public static void main(String[] args) {
		
		ArrayList<URL> urls = new ArrayList<URL>();
		try {			
			
			urls.add(new URL("https://www.state.gov/"));
			
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		GetPage page = new GetPage(urls);
		page.start();
		Dispatcher disp = new Dispatcher(urls);		
		WebAnalyzer web = new WebAnalyzer();		
		
		System.out.println("******Start crawl WebPage!!*******");
		
	}	
}
