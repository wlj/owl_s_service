package com.pku.wlj;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlGroundingException;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.pku.ly.Grounding.Implementation.GroundingDeleteImpl;
import EDU.pku.ly.Grounding.Implementation.GroundingInquiryImpl;
import EDU.pku.ly.Grounding.Implementation.GroundingMainImpl;
import EDU.pku.ly.Grounding.Implementation.GroundingParserImpl;

@Path("/grounding")
public class Grounding {
	 @GET
	 @Path("/{service_id}")
	 @Produces({ MediaType.APPLICATION_JSON })
	public String get(@PathParam("service_id") int service_id){
		 System.out.println("get:"+service_id);
		GroundingInquiryImpl impl=new GroundingInquiryImpl();
		WsdlGroundingList ss = impl.GroundingInquiryEntry(service_id);
		Gson gson = new Gson();
		String str = gson.toJson(ss);
		return str;
	}
	 
	@DELETE
	@Path("/{service_id}")
	@Produces({MediaType.TEXT_PLAIN})
	 public void delete(@PathParam("service_id") int service_id){
		System.out.println("delete:"+service_id);
		GroundingDeleteImpl impl=new GroundingDeleteImpl();
		impl.GroundingDeleteEntry(service_id);
	 }
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void post(@FormParam("grounding_url") String grounding_url, @FormParam("service_id") int service_id) {
		System.out.println(String.format("post:grounding_url:%s; service_id:%s", grounding_url,service_id));
		
		OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
		try {
			WsdlGrounding grounding=builder.createWSDLGrounding(grounding_url);
			GroundingParserImpl impl=new GroundingParserImpl();
			impl.GroundingParserEntry(grounding, service_id);
		} catch (NotInstanceOfWsdlGroundingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
