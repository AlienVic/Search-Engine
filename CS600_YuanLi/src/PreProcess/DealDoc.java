package PreProcess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import PreProcess.GetPage;

public class DealDoc implements Runnable{
	
	private String ID;
	private Dispatcher disp;
	private WebAnalyzer analyzer = new WebAnalyzer();
	private GetPage page = new GetPage();
	private File file;
	private File afterfile;
	private BufferedWriter bfWriter;
	private BufferedWriter bfWriter2;
	

	
	public DealDoc(String ID, Dispatcher dis) {
		
		this.ID = ID;
		this.disp = dis;
		file = new File("OriginalDoc//htmlPage_"+ID+".txt");
		afterfile = new File("DocAfterAna//afterAna_"+ID+".txt");
		try {
			file.createNewFile();
			afterfile.createNewFile();
			bfWriter = new BufferedWriter(new FileWriter(file));
			bfWriter2 = new BufferedWriter(new FileWriter(afterfile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void Analyse(Dispatcher disp){
				
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int counter = 0;
		
		while(counter++ <= 2)		
		{
			URL url = disp.getURL();
			
			System.out.println("in running:  get url: " + url.toString());

			String htmlDoc = page.getDocumentAt(url);
			
			if(htmlDoc.length() != 0)
			{
				ArrayList<URL> newURL = analyzer.doAnalyzer(bfWriter, url, htmlDoc);
				analyzer.WriteAfterDoc(bfWriter2, url, htmlDoc);
				if(newURL.size() != 0)
					disp.insert(newURL);
			}
			
		}
	}
}
