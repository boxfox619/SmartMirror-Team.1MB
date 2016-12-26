package application;

import com.boxfox.dto.CommandJob;
import com.boxfox.reconizer.STTListener;

public abstract class MainReconizer implements STTListener{
	public CommandJob job,prevJob;
	
	public void yes(){
		if(job!=null){
			job.work();
			job = null;
		}
	}
	public void no(){
		if(job!=null){
			job.cancel();
			job = null;
		}
	}

	public void setJob(CommandJob commandJob) {
		
	}

}
