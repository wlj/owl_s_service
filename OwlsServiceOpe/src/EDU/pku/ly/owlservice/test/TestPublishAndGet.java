package EDU.pku.ly.owlservice.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceListImpl;
import EDU.cmu.Atlas.owls1_1.writer.OWLSGroundingWriter;
import EDU.cmu.Atlas.owls1_1.writer.OWLSProcessWriter;
import EDU.cmu.Atlas.owls1_1.writer.OWLSProfileWriter;
import EDU.cmu.Atlas.owls1_1.writer.OWLSServiceWriter;
import EDU.pku.ly.Grounding.util.GroundingResource;
import EDU.pku.ly.Process.util.ProcessResource;
import EDU.pku.ly.Profile.util.ProfileResource;
import EDU.pku.ly.owlsservice.ExtendedService;
import EDU.pku.ly.owlsservice.OwlsServiceMain;
import EDU.pku.ly.owlsservice.implementation.OwlsServiceMainImpl;
import EDU.pku.ly.owlsservice.util.ServiceResource;

public class TestPublishAndGet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//publish();
		
		Inquiry();
		
		System.out.println("");
		
		OwlsServiceMain owlsservicemain = new OwlsServiceMainImpl();
		List<ExtendedService> lst_services = owlsservicemain.OwlsServiceInquiryEntry();
		
		
		String url = "C:\\luoshan\\SourceCode\\juddi\\running\\juddi-portal-bundle-3.0.2\\webapps\\juddiv3\\owl-s\\1.1\\BravoAirService.owl";
		//String url = "C:\\testservice.owl";
		
		//OWLSServiceModel servicemodel = ServiceResource.GetOWLSServiceModel(url);
		//OWLSProfileModel profilemodel = ProfileResource.GetOWLSProfileModel(url);
		//OWLSGroundingModel groundingmodel = GroundingResource.GetOWLSGroundingModel(url);
		//OWLSProcessModel processmodel = ProcessResource.GetOWLSProcessModel(url);
		
		try {
			//FileOutputStream fos = new FileOutputStream("c:\\testservice.owl");
			FileOutputStream fos_profile = new FileOutputStream("c:\\testprofile.owl");
			//FileOutputStream fos_grounding = new FileOutputStream("c:\\testgrounding.owl");
			//FileOutputStream fos_process = new FileOutputStream("c:\\testprocess.owl");
			
			//OWLSServiceWriter.write(servicemodel.getServiceList(), "http://localhost:8080/testservice.owl", fos);
			OWLSProfileWriter.write((ProfileList)lst_services.get(1).getServiceProfileList(), "http://localhost:8080/juddiv3/owl-s/1.1/Profile.owl#Profile", fos_profile);
			//OWLSGroundingWriter.write(groundingmodel.getWsdlGroundingList(), "http://localhost:8080/testgrounding.owl", fos_grounding);
			//OWLSProcessWriter.write(processmodel.getProcessList(), "http://localhost:8080/testprocess.owl", fos_process);
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLSWriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("");
		//Publish();
		
		//Inquiry(); 
		 
	}

	private static void Inquiry() {
		
		OwlsServiceMain owlsservicemain = new OwlsServiceMainImpl();
		List<ExtendedService> lst_services = owlsservicemain.OwlsServiceInquiryEntry();

		//FileOutputStream fos_profile = new FileOutputStream("c:\\testprofile.owl");
		//FileOutputStream fos_grounding = new FileOutputStream("c:\\testgrounding.owl");
		//FileOutputStream fos_process = new FileOutputStream("c:\\testprocess.owl");

	
		try {
			
			FileOutputStream fos = new FileOutputStream("c:\\testservice.owl");
			
			OWLSServiceWriter.write(lst_services.get(0).getServiceList(), "http://localhost:8080/testservice.owl", fos);
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLSWriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\ninquiry over...");
	}
	
	public List<ExtendedService> getInquiryResults() {
		OwlsServiceMain owlsservicemain = new OwlsServiceMainImpl();
		return owlsservicemain.OwlsServiceInquiryEntry();
	}

	private static void Publish() {
		
		OWLSProcessModel model = ProcessResource.GetOWLSProcessModel("C:\\luoshan\\SourceCode\\juddi\\running\\juddi-portal-bundle-3.0.2\\webapps\\juddiv3\\owl-s\\1.1\\BravoAirProcess.owl");
		
		/*
		OwlsServiceMain owlsservicemain = new OwlsServiceMainImpl();
		owlsservicemain.OwlsServicePublishEntry("C:\\luoshan\\SourceCode\\juddi\\running\\juddi-portal-bundle-3.0.2\\webapps\\juddiv3\\owl-s\\1.1\\BravoAirService.owl");
		*/
		System.out.println("\npublish over...");
		
	}
}
