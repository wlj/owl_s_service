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

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Builder_Util;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author Naveen Srinivasan
 *  
 */
public class TestProfileWriter  extends TestCase {
	
	private static final Logger logger = Logger.getLogger(TestProfileWriter.class);

    public static void _testConstructProfile() throws Exception {

        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        OWLS_Store_Builder storeBuilder = OWLS_Store_BuilderFactory.instance();

        //Creating Actor
        Actor naveen = builder.createActor("Naveen");
        naveen.setName("Naveen Srinivasan");
        naveen.setAddress("5000 Forbes");
        naveen.setTitle("Programmer");
        naveen.setPhone("412-000-0000");
        naveen.setEmail("naveen@naveen.com");

        ActorsList actorList = storeBuilder.createActorsList();
        actorList.addActor(naveen);

        //Creating ServiceCategory
        ServiceCategory srvCat = builder.createServiceCategory("MySrvCat");
        srvCat.setCategoryName("NAVN");
        srvCat.setCode("5000");
        srvCat.setTaxonomy("10.223");
        srvCat.setValue("50");

        ServiceCategoriesList srvCatList = storeBuilder.createServiceCategoriesList();
        srvCatList.addServiceCategory(srvCat);

        //Process
        AtomicProcess hasProcess = (AtomicProcess) builder.createProcess("MyProcess", OWLS_Builder_Util.ATOMIC);

        Profile profile = builder.createProfile("MyProfile");
        profile.setServiceName("MyProfile");
        profile.setTextDescription("This is description of my profile");

        profile.setHasProcess(hasProcess);
        profile.setContactInformation(actorList);

        profile.setServiceCategory(srvCatList);

        ProfileList list = storeBuilder.createProfileList();
        list.addProfile(profile);

        String pf = writeProfileList(list, "http://www.cs.cmu.edu/naveen.owl",null, null);
        
        // tests
        assertTrue(pf.contains("Naveen Srinivasan"));
        assertTrue(pf.contains("5000 Forbes"));
        assertTrue(pf.contains("Programmer"));
        assertTrue(pf.contains("412-000-0000"));
        assertTrue(pf.contains("naveen@naveen.com"));
        assertTrue(pf.contains("MySrvCat"));
        assertTrue(pf.contains("NAVN"));
        assertTrue(pf.contains("5000"));
        assertTrue(pf.contains("10.223"));
        assertTrue(pf.contains("50"));
        
    }

    public static void _testWriteFromURL() throws OWLSWriterException {

        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        OntModel m = ModelFactory.createOntologyModel();
        m.setDynamicImports(true);

        OWLSProfileParser owlsProfileParser = new OWLSProfileParser(m);
        String url;
        //url = "http://www.daml.ri.cmu.edu/owls/BravoAirProfile.owl";
        url = "http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl";
        OWLSProfileModel owls = null;
        try {
            owls = owlsProfileParser.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotInstanceOfProfileException e) {
            e.printStackTrace();
        }
        
        String pf = writeProfileList(owls.getProfileList(), "http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl",
                new String[] { "http://www.daml.org/services/owl-s/1.1/Concepts.owl" }, null);
        
        // tests
        assertTrue(pf.contains("BravoAir-reservation"));
        assertTrue(pf.contains("BravoAir Reservation department"));
        assertTrue(pf.contains("Bravo@Bravoair.com"));
        assertTrue(pf.contains("BravoAir-information"));
        assertTrue(pf.contains("BravoAir-geographicRadiu"));
        assertTrue(pf.contains("qualityRating_Good"));
        assertTrue(pf.contains("Airline reservation services"));
        assertTrue(pf.contains("DepartureAirport"));
        assertTrue(pf.contains("AcctName"));
        assertTrue(pf.contains("PreferredFlightItinerary"));

    }

    public static void _testConstructProfileUsingExternal() throws OWLSWriterException {
        
        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        OWLS_Store_Builder storeBuilder = OWLS_Store_BuilderFactory.instance();

        OntModel submodel = ModelFactory.createOntologyModel();
        submodel.read("http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl");

        //Using existing Process definition
        Individual indBAProcess = submodel.getIndividual("http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl#BravoAir_Process");
        Process baProcess;
        try {
            baProcess = builder.createProcess(indBAProcess);
        } catch (NotInstanceOfProcessException e1) {
            e1.printStackTrace();
            return;
        }

        //Using existing input definitions
        Individual indInput1 = submodel.getIndividual("http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl#DepartureAirport");
        Input input;
        try {
            input = builder.createInput(indInput1);
        } catch (NotInstanceOfParameterException e2) {
            e2.printStackTrace();
            return;
        }
        InputList inputlist = storeBuilder.createInputList();
        inputlist.add(input);

        //Using existing output definitions

        Individual indOutput1 = submodel
                .getIndividual("http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl#PreferredFlightItinerary");
        Output output;
        try {
            output = builder.createOutput(indOutput1);
        } catch (NotInstanceOfParameterException e2) {
            e2.printStackTrace();
            return;
        }
        OutputList outputlist = storeBuilder.createOutputList();
        outputlist.add(output);

        //Profile
        Profile profile;
        try {
            profile = builder.createProfile("MyProfile");
        } catch (NotInstanceOfProfileException e) {
            e.printStackTrace();
            return;
        }
        profile.setServiceName("MyProfile");
        profile.setTextDescription("This is description of my profile");
        profile.setHasProcess(baProcess);
        profile.setInputList(inputlist);
        profile.setOutputList(outputlist);

        ProfileList list = storeBuilder.createProfileList();
        list.addProfile(profile);
        
        // tests
        
        String pf = writeProfileList(list, "http://www.cs.cmu.edu/naveen.owl",
                new String[] { "http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl" }, submodel);
        
        assertTrue(pf.contains("DepartureAirport"));
        assertTrue(pf.contains("PreferredFlightItinerary"));
        assertTrue(pf.contains("MyProfile"));
        assertTrue(pf.contains("http://www.cs.cmu.edu/naveen.owl"));
        assertTrue(pf.contains("http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl"));
        assertTrue(pf.contains("This is description of my profile"));
    }
    
    static protected String writeProfileList(ProfileList pl, String base, String [] importsList, OntModel submodel) throws OWLSWriterException {

	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	
    	OWLSProfileWriter.write(pl, base, importsList, submodel, bos);
        logger.info(bos.toString());
    	return bos.toString();
	}

    public static void main(String[] str) throws Exception {

        _testConstructProfileUsingExternal();
        _testWriteFromURL();
        _testConstructProfile();
    }

}