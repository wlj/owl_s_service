package EDU.pku.ly.Process;

import java.io.Serializable;

import EDU.cmu.Atlas.owls1_1.process.Process;

public interface ProcessMain extends Serializable{
	
	public void ProcessPublishEntry(String service_url);
	
	public void ProcessPublishEntry(Process process, int service_id);
	
	public Process ProcessInquiryEntry(int service_id);
	
	public int ProcessDeleteEntry(int service_id);
}
