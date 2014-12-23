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
package samples.parsers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.parser.DefaultOWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;

/**
 * Demonstrate - usage of process parser
 * @author Naveen Srinivasan
 */
public class TestProcessParser {
	private static Logger logger = Logger.getLogger(TestProcessParser.class);

    /**
     * This method parse the given URI to generate process object
     * @param uri
     */
    public static void parse(String uri) {

        //creating instance of parser
        OWLSProcessParser parser = new OWLSProcessParser();

        try {

            //parsing the process uri
            OWLSProcessModel model = parser.read(uri);

            //printing the OWL-S process
            logger.debug(model.getProcessList());

            
        } catch (NotInstanceOfProcessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method parse the given URI using a error handler. ErrorHandler stores the warnings and
     * errors generated while parsing the URI.
     * @param uri
     */
    public static void parseWithErrorHandler(String uri) {

        //Creating an instance of default error handler.
        DefaultOWLSErrorHandler errHandler = new DefaultOWLSErrorHandler();

        //creating instance of parser
        OWLSProcessParser parser = new OWLSProcessParser();

        //parsing the process uri
        OWLSProcessModel model = parser.read(uri, errHandler);

        //printing the OWL-S process
        logger.debug(model.getProcessList());

        //Print the warning and errors generated while parsing
        logger.debug(errHandler);
    }

    public static void main(String[] str) throws MalformedURLException, NotInstanceOfProcessException, IOException {

        //configuring the logger
        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        //process urls
        //String url= "http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl";
        String url = "http://www.daml.org/services/owl-s/1.1/CongoProcess.owl";

        parse(url);

        //parseWithErrorHandler(url);
    }
}