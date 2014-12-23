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

/*import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
*/
import EDU.cmu.Atlas.owls1_1.parser.DefaultOWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;

/**
 * @author Naveen Srinivasan
 * @author Roman Vaculin
 *  
 */
public class TestProfileParser extends TestCase {
	
	private static final Logger logger = Logger.getLogger(TestProfileParser.class);

    public static void parseWithErrorHandler(String uri) {

        DefaultOWLSErrorHandler errHandler = new DefaultOWLSErrorHandler();
        OWLSProfileParser owlsProfileParser = new OWLSProfileParser();
        OWLSProfileModel owls = owlsProfileParser.read(uri, errHandler);
        logger.info(errHandler);
        logger.info(owls.getProfileList());

    }

    public static void _testParse() throws Exception {

    	OWLSProfileParser owlsProfileParser = new OWLSProfileParser();
    	
    	String uri = "http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl";
    	
    	OWLSProfileModel owls = owlsProfileParser.read(uri);
    	ProfileList pl = owls.getProfileList();
    	assertEquals(pl.size(), 1);
    	
    	Profile p = pl.getNthProfile(0);
    	assertEquals(p.getInputList().size(), 8);
    	assertEquals(p.getOutputList().size(), 3);
    	
    	assertEquals(p.getContactInformation().getNthActor(0).getEmail(), "Bravo@Bravoair.com");
    	
    	logger.info(owls.getProfileList());
    }

    public static void main(String[] str) throws IOException {

        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

/*		// example of how ontologies can be loaded from some local directory instead from the web   
 
        OntModel ontModel = ModelFactory.createOntologyModel();
        OntDocumentManager docMgr = ontModel.getDocumentManager();
        
        String base = "local directory";
        ClassLoader loader = OWLSProfileParser.class.getClassLoader();
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/BravoAirService.owl", loader.getResource(base + "BravoAirService.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl", loader.getResource(base + "BravoAirProfile.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl", loader.getResource(base + "BravoAirProcess.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.owl", loader.getResource(base + "BravoAirGrounding.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Concepts.owl", loader.getResource(base + "Concepts.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Country.owl", loader.getResource(base + "Country.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/ProfileHierarchy.owl", loader.getResource(base + "ProfileHierarchy.owl").toString());
        docMgr.addAltEntry("http://www.isi.edu/~pan/damltime/time-entry.owl", loader.getResource(base + "time-entry.owl").toString());
*/        
        
        
        try {
        	_testParse();
        } catch (Exception e) {
            e.printStackTrace();
        } 

    }

}