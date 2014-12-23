/*
 * OWL-S API provides functionalities to create and to manipulate OWL-S files. Copyright
 * (C) 2005 Katia Sycara, Softagents Lab, Carnegie Mellon University
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the
 * 
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * The Intelligent Software Agents Lab The Robotics Institute Carnegie Mellon University 5000 Forbes
 * Avenue Pittsburgh PA 15213
 * 
 * More information available at http://www.cs.cmu.edu/~softagents/
 */
package EDU.cmu.Atlas.owls1_1.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.hp.hpl.jena.ontology.OntModel;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Builder_Util;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlGroundingException;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.parser.OWLSServiceParser;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.writer.OWLSServiceWriter;

/**
 * @author Naveen Srinivasan
 *  
 */
public class TestServiceWriter extends TestCase {

	private static final Logger logger = Logger.getLogger(TestServiceWriter.class);
	
    /**
     * The example create processes programmatically.
     *  
     */
    public static void _testWriterUsingOWLSObjects() throws IndexOutOfBoundsException, OWLSWriterException, IOException, NotInstanceOfServiceException {

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        OWLS_Store_Builder storeBuilder = OWLS_Store_BuilderFactory.instance();

        //Process
        AtomicProcess atomicProcess;
        try {
            atomicProcess = (AtomicProcess) builder.createProcess("BravoAirProcess", OWLS_Builder_Util.ATOMIC);
        } catch (NotInstanceOfProcessException e) {
            e.printStackTrace();
            return;
        }

        //////////Profile
        Profile profile;
        try {
            profile = builder.createProfile("MyProfile");
        } catch (NotInstanceOfProfileException e1) {
            e1.printStackTrace();
            return;
        }
        profile.setServiceName("MyService");
        profile.setTextDescription("This is description of my profile");

        profile.setHasProcess(atomicProcess);

        ProfileList list = storeBuilder.createProfileList();
        list.addProfile(profile);

        /////////Grounding

        //Creating wsdl operation ref
        WsdlOperationRef opRef = builder.createWSDLOperationRef();
        opRef.setOperation("GetDesiredFlightDetails_operation");
        opRef.setPortType("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl#GetDesiredFlightDetails_PortType");

        //Creating WSDL Atomic Process Grounding
        WsdlAtomicProcessGrounding wapg;
        try {
            wapg = builder.createWsdlAtomicProcessGrounding("WSDLGnd_BravoAirProcess");
        } catch (NotInstanceOfWsdlAtomicProcessGrounding e4) {
            e4.printStackTrace();
            return;
        }
        wapg.setOwlsProcess(atomicProcess);
        wapg.setWsdlOperation(opRef);
        wapg.setWsdlInputMessage("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl#GetDesiredFlightDetails_Input");
        wapg.setWsdlOutputMessage("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl#return");
        wapg.setWsdlDocument("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl");
        wapg.setWsdlVersion("http://www.w3.org/TR/2001/NOTE-wsdl-20010315");

        WsdlAtomicProcessGroundingList wapgList = storeBuilder.createWsdlAtomicProcessGroundingList();
        wapgList.addWsdlAtomicProcessGrounding(wapg);

        //Creating WSDL Grounding
        WsdlGrounding wsdlGrounding;
        try {
            wsdlGrounding = builder.createWSDLGrounding("BravoGrounding");
        } catch (NotInstanceOfWsdlGroundingException e3) {
            e3.printStackTrace();
            return;
        }
        wsdlGrounding.setWsdlAtomicProcessGroundingList(wapgList);

        WsdlGroundingList gndList = storeBuilder.createWsdlGroundingList();
        gndList.addWsdlGrounding(wsdlGrounding);

        //////////Service

        Service service;
        try {
            service = builder.createService("BravoAirService");
        } catch (NotInstanceOfServiceException e5) {
            e5.printStackTrace();
            return;
        }
        service.setDescribes(atomicProcess);
        service.setPresents(list);
        service.setSupports(gndList);

//        OWLSServiceWriter.write(service, "http://www.daml.org/BravoAirService.owl", System.out);
        
        ServiceList srvList = storeBuilder.createServiceList();
        srvList.addService(service);
        
        
        String pf = writeServiceList(srvList, "http://www.daml.org/BravoAirService.owl", null, null);
        
        // tests

        assertTrue(pf.contains("<owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/ActorDefault.owl\"/>"));
        assertTrue(pf.contains("MyService"));
        assertTrue(pf.contains("MyProfile"));
        assertTrue(pf.contains("BravoAirProcess"));
        assertTrue(pf.contains("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl#GetDesiredFlightDetails_Input"));
        assertTrue(pf.contains("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl#return"));
        assertTrue(pf.contains("BravoGrounding"));
        assertTrue(pf.contains("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl#return"));
        assertTrue(pf.contains("http://www.daml.org/BravoAirService.owl"));
        
    }

    public static void _testWriterUsingURI() throws IndexOutOfBoundsException, OWLSWriterException, IOException, NotInstanceOfServiceException {

	URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        //String url = "http://www.daml.ri.cmu.edu/owls/BravoAirService.owl";
        String url = "http://www.daml.org/services/owl-s/1.1/BravoAirService.owl";

        OWLSServiceParser parser = new OWLSServiceParser();

        OWLSServiceModel srvModel;
        srvModel = parser.read(url);
        ServiceList srvList = srvModel.getServiceList();

        logger.info("#################################");
        
        String pf = writeServiceList(srvList, url, null, null);
        
        // tests
        
        assertTrue(pf.contains("rdf:ID=\"BravoAir_ReservationAgent\""));
        assertTrue(pf.contains("http://www.daml.org/services/owl-s/1.1/BravoAirService.owl"));
        assertTrue(pf.contains("#Profile_BravoAir_ReservationAgent"));
        assertTrue(pf.contains("#BravoAir_Process"));
        assertTrue(pf.contains("#Grounding_BravoAir_ReservationAgent"));
    }

    static protected String writeServiceList(ServiceList sl, String base, String [] importsList, OntModel submodel) throws OWLSWriterException {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	
    	OWLSServiceWriter.write(sl, base, importsList, submodel, bos);
        logger.info(bos.toString());
    	return bos.toString();
	}

    public static void main(String[] str) throws IndexOutOfBoundsException, OWLSWriterException, IOException, NotInstanceOfServiceException {

        //increase jvm memory
        _testWriterUsingURI();
        _testWriterUsingOWLSObjects();
    }

}