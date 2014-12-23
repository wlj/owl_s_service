package EDU.pku.ly.owlsservice;

import java.util.List;
import java.util.Vector;

public interface OwlsServiceMain {
	
	public void OwlsServicePublishEntry(String service_url);
	
	public List<ExtendedService> OwlsServiceInquiryEntry();
}
