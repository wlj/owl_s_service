package EDU.pku.ly.Process;

import EDU.cmu.Atlas.owls1_1.process.Process;

public interface ProcessMainNew {
	
	public void ProcessPublishEntry(String service_url);
	
	//public void ProcessPublishEntryWithProcess(Process process, int service_id);
	
	public String ProcessInquiryEntry(int service_id);
	
	public int ProcessDeleteEntry(int service_id);
}
