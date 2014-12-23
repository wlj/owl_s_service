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
package samples.writers;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.apache.log4j.PropertyConfigurator;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Builder_Util;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfActorException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceCategoryException;
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
import EDU.cmu.Atlas.owls1_1.uri.OWLSProfileURI;
import EDU.cmu.Atlas.owls1_1.writer.OWLSProfileWriter;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author Naveen Srinivasan
 *  
 */
public class TestProfileWriter {

    /**
     * The example create the bravo air profile programmatically.
     *  
     */
    public static void writerUsingOWLSObjects() {

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        OWLS_Store_Builder storeBuilder = OWLS_Store_BuilderFactory.instance();

        //Creating Actor - BravoAir-reservation
        Actor actor1;
        try {
            actor1 = builder.createActor("BravoAir-reservation");
        } catch (NotInstanceOfActorException e) {
            e.printStackTrace();
            return;
        }
        actor1.setName("BravoAir Reservation department");
        actor1.setAddress("Airstrip 2,Teetering Cliff Hights,Florida 12321,USA");
        actor1.setTitle("Reservation Representative");
        actor1.setPhone("412 268 8780");
        actor1.setFax("412 268 5569");
        actor1.setEmail("Bravo@Bravoair.com");
        actor1.setWebURL("http://www.daml.org/services/daml-s/2001/05/BravoAir.html");

        //Creating Actor - BravoAir-information
        Actor actor2;
        try {
            actor2 = builder.createActor("BravoAir-information");
        } catch (NotInstanceOfActorException e) {
            e.printStackTrace();
            return;
        }
        actor2.setName("John Doe");
        actor2.setAddress("Airstrip 2,Teetering Cliff Hights,Florida 12321,USA");
        actor2.setTitle("Sale Representativ");
        actor2.setPhone("412 268 8789");
        actor2.setFax("412 268 5569");
        actor2.setEmail("John_Doe@Bravoair.com");
        actor2.setWebURL("http://www.daml.org/services/daml-s/2001/05/BravoAir.html");

        //Creating ActorsList and adding actor1 and actor2
        ActorsList actorList = storeBuilder.createActorsList();
        actorList.addActor(actor1);
        actorList.addActor(actor2);

        //Creating ServiceCategory
        ServiceCategory srvCat1;
        try {
            srvCat1 = builder.createServiceCategory("NAICS-category");
        } catch (NotInstanceOfServiceCategoryException e) {
            e.printStackTrace();
            return;
        }
        srvCat1.setCategoryName("NAICS");
        srvCat1.setTaxonomy(OWLSProfileURI.NAICS);
        srvCat1.setCode("561599");
        srvCat1.setValue("Airline reservation services");

        //Creating ServiceCategory
        ServiceCategory srvCat2;
        try {
            srvCat2 = builder.createServiceCategory("UNSPSC-category");
        } catch (NotInstanceOfServiceCategoryException e) {
            e.printStackTrace();
            return;
        }
        srvCat2.setCategoryName("UNSPSC");
        srvCat2.setTaxonomy(OWLSProfileURI.UNSPSC);
        srvCat2.setCode("90121500");
        srvCat2.setValue("Travel Agent");

        ServiceCategoriesList srvCatList = storeBuilder.createServiceCategoriesList();
        srvCatList.addServiceCategory(srvCat1);
        srvCatList.addServiceCategory(srvCat2);

        //Creating an atomic process with 3 inputs and 1 output

        Input input1;
        try {
            input1 = (Input) builder.createParameter("DepartureAirport", OWLS_Builder_Util.INPUT);
        } catch (NotInstanceOfParameterException e1) {
            e1.printStackTrace();
            return;
        }
        input1.setParameterType("http://www.daml.org/services/owl-s/1.1/Concepts.owl#Airport");

        Input input2;
        try {
            input2 = (Input) builder.createParameter("ArrivalAirport", OWLS_Builder_Util.INPUT);
        } catch (NotInstanceOfParameterException e1) {
            e1.printStackTrace();
            return;
        }
        input2.setParameterType("http://www.daml.org/services/owl-s/1.1/Concepts.owl#Airport");

        Input input3;
        try {
            input3 = (Input) builder.createParameter("OutboundDate", OWLS_Builder_Util.INPUT);
        } catch (NotInstanceOfParameterException e1) {
            e1.printStackTrace();
            return;
        }
        input3.setParameterType("http://www.daml.org/services/owl-s/1.1/Concepts.owl#FlightDate");

        //Creating input list
        InputList inputList = storeBuilder.createInputList();
        inputList.addInput(input1);
        inputList.addInput(input2);
        inputList.addInput(input3);

        //Creating output
        Output output1;
        try {
            output1 = (Output) builder.createParameter("FlightItinerary", OWLS_Builder_Util.OUTPUT);
        } catch (NotInstanceOfParameterException e1) {
            e1.printStackTrace();
            return;
        }
        output1.setParameterType("http://www.daml.org/services/owl-s/1.1/Concepts.owl#FlightItinerary");

        //Creating output list
        OutputList outputList = storeBuilder.createOutputList();
        outputList.addOutput(output1);

        //Creating atomic process
        AtomicProcess hasProcess;
        try {
            hasProcess = (AtomicProcess) builder.createProcess("BravoAirProcess", OWLS_Builder_Util.ATOMIC);
        } catch (NotInstanceOfProcessException e) {
            e.printStackTrace();
            return;
        }

        //adding inputs and outputs
        hasProcess.setInputList(inputList);
        hasProcess.setOutputList(outputList);

        //Creating profile object
        Profile profile;
        try {
            profile = builder.createProfile("Profile_BravoAir_ReservationAgent");
        } catch (NotInstanceOfProfileException e) {
            e.printStackTrace();
            return;
        }

        //adding service name
        profile.setServiceName("BravoAir_ReservationAgent");

        //adding text description
        profile.setTextDescription("This  service provide flight reservations");

        //adding has process
        profile.setHasProcess(hasProcess);

        //adding contact information using actor list
        profile.setContactInformation(actorList);

        //adding service categeory
        profile.setServiceCategory(srvCatList);

        //adding inputs - reusing the list created for atomic process
        profile.setInputList(inputList);

        //adding outputs - reusing the list created for atomic process
        profile.setOutputList(outputList);

        //Creating a profile list and adding the profile
        ProfileList list = storeBuilder.createProfileList();
        list.addProfile(profile);

        //imports

        //Writing the profile list to System.out
        try {
            OWLSProfileWriter.write(list, "http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl", System.out);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (OWLSWriterException e) {
            e.printStackTrace();
        }

    }

    public static void writerUsingExternalObjects() {

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
        
        Vector cl = new Vector();
        cl.add("pokus");
        
        profile.setServiceClassifications(cl);

        try {
            OWLSProfileWriter.write(profile, "http://www.cs.cmu.edu/naveen.owl",
                    new String[] { "http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl" }, submodel, System.out);
        } catch (OWLSWriterException e3) {
            e3.printStackTrace();
            return;
        }

    }

    public static void writerUsingURI() {

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

        try {
            OWLSProfileWriter.write(owls.getProfileList(), "http://www.cs.cmu.edu/naveen.owl", System.out);
        } catch (IndexOutOfBoundsException e1) {
            e1.printStackTrace();
        } catch (OWLSWriterException e1) {
            e1.printStackTrace();
        }

    }

    public static void main(String[] str) throws IOException, OWLSWriterException {

        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        writerUsingOWLSObjects();
//        writerUsingExternalObjects();
        //writerUsingURI();

    }

}