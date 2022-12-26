package net.winborne.net.winborne.maven.dtunes;

import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.progress.EncoderProgressListener;

public class ConvertProgressListener implements EncoderProgressListener {
    
	   public ConvertProgressListener() {                                    
	    //code                                                               
	   }                                                                     
	                                                                         
	   public void message(String m) {                                       
	     //code                                                              
	   }                                                                     
	                                                                         
	   public void progress(int p) {                                         
		                                                                     
	     //Find %100 progress                                                
	     double progress = p / 1000.00;              
	     // TODO: pipe this back to the main class so it can be displayed as a progress bar
	     System.out.println(progress);                                            
	   }                                                                     
	                                                                         
	    public void sourceInfo(MultimediaInfo m) {                           
	       //code                                                            
	    }                                                                    
	 }              