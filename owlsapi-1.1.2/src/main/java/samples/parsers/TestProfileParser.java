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
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.parser.DefaultOWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.writer.OWLSProfileWriter;

/**
 * Demonstrate - usage of profile parser
 * @author Naveen Srinivasan
 */
public class TestProfileParser {

	private  static Logger logger = Logger.getLogger(TestProfileParser.class);
    /**
     * This method parse the given URI to generate Profile object
     * @param uri
     */
    public static void parse(String uri) {

        //creating instance of parser
        OWLSProfileParser owlsProfileParser = new OWLSProfileParser();
        
        OWLSProfileModel owls = null; 
        
        try {

            //parsing the profile
            owls = owlsProfileParser.read(uri);

            //println the OWL-S profiles
            logger.debug(owls.getProfileList());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotInstanceOfProfileException e) {
            e.printStackTrace();
        }
        
        OntModel submodel = ModelFactory.createOntologyModel();
        submodel.read("http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl");

        
        ProfileList list = owls.getProfileList();
        
        try {
            OWLSProfileWriter.write(list, "http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl", 
            		new String[] { "http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl" },submodel, System.out);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (OWLSWriterException e) {
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
        OWLSProfileParser owlsProfileParser = new OWLSProfileParser();

        //parsing the profile
        OWLSProfileModel owls = owlsProfileParser.read(uri, errHandler);

        //println the OWL-S profiles
        logger.debug("\n\n\nPRINTING:\n\n\n" + owls.getProfileList());

        //Print the warning and errors generated while parsing
        logger.debug(errHandler);
        
        ProfileList list = owls.getProfileList();
        
        try {
            OWLSProfileWriter.write(list, "http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl", null, System.out);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (OWLSWriterException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] str) throws IOException {

        //configuring the logger
        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        //Profile urls
        //String url= "http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl";
        String url = "http://www.daml.ri.cmu.edu/owls/BravoAirProfile.owl";

        parse(url);

        parseWithErrorHandler(url);
    }

}