package com.pku.wlj.service;

import EDU.cmu.Atlas.owls1_1.process.Process;

public interface ProcessService {
	public Process ProcessInquiryEntry(int process_id, String flag);
	public Process ProcessInquiryEntry(int service_id);
}
