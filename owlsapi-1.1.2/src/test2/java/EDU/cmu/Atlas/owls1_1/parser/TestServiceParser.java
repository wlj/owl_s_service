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
package EDU.cmu.Atlas.owls1_1.parser;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.parser.DefaultOWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.parser.OWLSServiceParser;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;

/**
 * @author Naveen Srinivasan
 *  
 */
public class TestServiceParser extends TestCase {
	
	private static final Logger logger = Logger.getLogger(TestServiceParser.class);

    public static void parseWithErrorHandler(String uri) {

        DefaultOWLSErrorHandler errHandler = new DefaultOWLSErrorHandler();
        OWLSServiceParser parser = new OWLSServiceParser();
        OWLSServiceModel owls = parser.read(uri, errHandler);

        logger.info(errHandler);
        logger.info(owls.getServiceList());

    }

    public static void _testParse() throws Exception {
    	
        String uri = "http://www.daml.ri.cmu.edu/owls/BravoAirService.owl";
        
        OWLSServiceParser parser = new OWLSServiceParser();
        
        OWLSServiceModel owls = parser.read(uri);
        
    	ServiceList pl = owls.getServiceList();
    	assertEquals(pl.size(), 1);
    	
    	Service s = pl.getNthService(0);
    	assertEquals(s.getURI(), "http://www.daml.org/services/owl-s/1.1/BravoAirService.owl#BravoAir_ReservationAgent");
    	assertEquals(s.getProvidedBy(), null);
    	assertEquals(s.getDescribes().getURI(), "http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl#BravoAir_Process");
    	assertEquals(s.getPresents().getNthServiceProfile(0).getURI(), "http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl#Profile_BravoAir_ReservationAgent");
    	
    	assertEquals(s.getSupports().getNthServiceGrounding(0).getURI(), "http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.owl#Grounding_BravoAir_ReservationAgent");
        
        logger.info(owls.getServiceList());
        
    }

    public static void main(String[] str) throws IOException, NotInstanceOfServiceException {

        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        String bravoURI = "http://www.daml.ri.cmu.edu/owls/BravoAirService.owl";
        //String bravoURI = "file:///F:/CMUWork/owls-api/test.owl";
        try {
        	_testParse();
        	parseWithErrorHandler(bravoURI);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}