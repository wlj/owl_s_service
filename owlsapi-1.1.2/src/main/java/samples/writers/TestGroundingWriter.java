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

import org.apache.log4j.PropertyConfigurator;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Builder_Util;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlGroundingException;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMapList;
import EDU.cmu.Atlas.owls1_1.parser.OWLSGroundingParser;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.writer.OWLSGroundingWriter;

/**
 * @author Naveen Srinivasan
 *  
 */
public class TestGroundingWriter {

    /**
     * The example create grounding programmatically.
     *  
     */
    public static void writerUsingOWLSObjects() {

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        OWLS_Store_Builder storeBuilder = OWLS_Store_BuilderFactory.instance();

        //Creating an atomic process with 3 inputs and 1 output

        Input input1;
        try {
            input1 = (Input) builder.createParameter("DepartureAirport", OWLS_Builder_Util.INPUT);
        } catch (NotInstanceOfParameterException e1) {
            e1.printStackTrace();
            return;
        }
        input1.setParameterType("http://www.daml.org/services/owl-s/1.1/Concepts.owl#Airport");

        //Creating input list
        InputList inputList = storeBuilder.createInputList();
        inputList.addInput(input1);

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
        AtomicProcess atomicProcess;
        try {
            atomicProcess = (AtomicProcess) builder.createProcess("BravoAirProcess", OWLS_Builder_Util.ATOMIC);
        } catch (NotInstanceOfProcessException e) {
            e.printStackTrace();
            return;
        }

        //adding inputs and outputs
        atomicProcess.setInputList(inputList);
        atomicProcess.setOutputList(outputList);

        //Creating Input message map
        WsdlInputMessageMap inputMessageMap = builder.createWsdlInputMessageMap();
        inputMessageMap.setOWLSParameter(input1);
        inputMessageMap.setWSDLMessagePart("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl#departureAirport");

        WsdlInputMessageMapList inputMessageMapList = storeBuilder.createWsdlInputMessageMapList();
        inputMessageMapList.addWsdlInputMessageMap(inputMessageMap);

        //Creating output message map
        WsdlOutputMessageMap outputMessageMap = builder.createWsdlOutputMessageMap();
        outputMessageMap.setOWLSParameter(output1);
        outputMessageMap.setWSDLMessagePart("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl#Itinerary");

        WsdlOutputMessageMapList outputMessageMapList = storeBuilder.createWsdlOutputMessageMapList();
        outputMessageMapList.addWsdlOutputMessageMap(outputMessageMap);

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
        wapg.setWsdlInputs(inputMessageMapList);
        wapg.setWsdlOutputMessage("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.wsdl#return");
        wapg.setWsdlOutputs(outputMessageMapList);
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

        //Writing to System.out
        try {
            OWLSGroundingWriter.write(wsdlGrounding, "http://www.daml.org/BravoAirGrounding.owl", System.out);
        } catch (IndexOutOfBoundsException e2) {
            e2.printStackTrace();
            return;
        } catch (OWLSWriterException e2) {
            e2.printStackTrace();
            return;
        }
    }

    public static void writerUsingURI() {

        //creating instance of parser
        OWLSGroundingParser owlsGroundingParser = new OWLSGroundingParser();

        //String url = "http://www.daml.ri.cmu.edu/owls/BravoAirGrounding.owl";
        String url = "http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.owl";

        OWLSGroundingModel gndModel;
        try {
            gndModel = owlsGroundingParser.read(url);
        } catch (IOException e2) {
            e2.printStackTrace();
            return;
        } catch (NotInstanceOfWsdlGroundingException e2) {
            e2.printStackTrace();
            return;
        }
        WsdlGroundingList gndList = gndModel.getWsdlGroundingList();

        //writing grounding
        try {
            OWLSGroundingWriter.write(gndList, "http://www.cs.cmu.edu/naveen.owls", System.out);
        } catch (IndexOutOfBoundsException e3) {
            e3.printStackTrace();
            return;
        } catch (OWLSWriterException e3) {
            e3.printStackTrace();
            return;
        }

    }

    public static void main(String[] str) throws Exception {

        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        //writerUsingURI();
        writerUsingOWLSObjects();
    }

}