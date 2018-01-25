package PreProcess;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class GetPage {
	
	private ArrayList<URL> urls;
	private int gatherNum = 5;
	
	public GetPage() {
		
	}
	public GetPage(ArrayList<URL> urls) {
		this.urls = urls;
	}
	
	public void start() {
		Dispatcher disp = new Dispatcher(urls);
		
		for(int i = 0; i < gatherNum; i++)
		{
			Thread gather = new Thread(new DealDoc(String.valueOf(i+1), disp));
			gather.start();
		}
	}
	
	public String getDocumentAt(URL url) {
		
		StringBuffer document = new StringBuffer();
		
		try {
			
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			document = new StringBuffer();
			
			while((line = reader.readLine()) != null){
				if(!line.trim().isEmpty())
					document.append(line + "\n");
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return document.toString();
	}
	
	
}
