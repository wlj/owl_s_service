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
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.parser.DefaultOWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;

/**
 * Process Model parser example
 * @author Naveen Srinivasan
 */
public class TestProcessParser extends TestCase {

	private static final Logger logger = Logger.getLogger(TestProcessParser.class);
	
    /*
     * Sample code for parsing the process model with an error handler
     */
    public static void parseWithErrorHandler(String uri) {

        OWLSProcessParser parser = new OWLSProcessParser();

        DefaultOWLSErrorHandler handler = new DefaultOWLSErrorHandler();
        OWLSProcessModel model;
        model = parser.read(uri, handler);

        logger.info(handler);
        logger.info(model.getProcessList());
    }

    /*
     * Sample code for parsing the process model
     */
    public static void _testParse() throws Exception {
    	
    	String uri = "http://www.daml.org/services/owl-s/1.1/CongoProcess.owl";
    	
    	OWLSProcessParser parser = new OWLSProcessParser();
    	
    	OWLSProcessModel model = parser.read(uri);

    	ProcessList pl = model.getProcessList();
    	assertEquals(pl.size(), 16);
    	
    	Process p = pl.getNthProcess(0);
    	assertEquals(p.getInputList().size(), 5);
    	assertEquals(p.getOutputList().size(), 1);
    	assertEquals(p.getName(), "http://www.daml.org/services/owl-s/1.1/CongoProcess.owl#ExpressCongoBuy");
    	logger.info(model.getProcessList());

    }

    public static void main(String[] str) throws MalformedURLException, NotInstanceOfProcessException, IOException {

        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        //String bravoURI = "http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl";
        String bravoURI = "http://www.daml.org/services/owl-s/1.1/CongoProcess.owl";

        try {
        	_testParse();
        	parseWithErrorHandler(bravoURI);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}