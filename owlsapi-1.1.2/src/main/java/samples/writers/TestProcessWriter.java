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
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.process.implementation.ProcessListImpl;
import EDU.cmu.Atlas.owls1_1.writer.OWLSProcessWriter;

/**
 * @author Naveen Srinivasan
 */
public class TestProcessWriter {

    /**
     * The example create processes programmatically.
     *  
     */
    public static void writerUsingOWLSObjects() {

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        OWLS_Store_Builder storeBuilder = OWLS_Store_BuilderFactory.instance();

        //Creating atomic processes
        AtomicProcess atomic1;
        try {
            atomic1 = (AtomicProcess) builder.createProcess("DollarToEuroConvProcess", OWLS_Builder_Util.ATOMIC);
        } catch (NotInstanceOfProcessException e) {
            e.printStackTrace();
            return;
        }


        //inputs
        Input input1;
        try {
            input1 = (Input) builder.createParameter("Dollar_In", OWLS_Builder_Util.INPUT);
        } catch (NotInstanceOfParameterException e1) {
            e1.printStackTrace();
            return;
        }
        input1.setParameterType("http://www.daml.org/services/owl-s/1.1/Concepts.owl#Dollar");

        //Creating input list
        InputList inputList = storeBuilder.createInputList();
        inputList.addInput(input1);
        
        //outputs
        Output output;
        try {
            output = (Output) builder.createParameter("Euro_Out", OWLS_Builder_Util.OUTPUT);
        } catch (NotInstanceOfParameterException e1) {
            e1.printStackTrace();
            return;
        }
        output.setParameterType("http://www.daml.org/services/owl-s/1.1/Concepts.owl#Euro");

        //Creating outputs list
        OutputList outputList = storeBuilder.createOutputList();
        outputList.addOutput(output);
        
        
        
        //Creating atomic processes
        AtomicProcess atomic2;
        try {
            atomic2 = (AtomicProcess) builder.createProcess("Atomic2", OWLS_Builder_Util.ATOMIC);
        } catch (NotInstanceOfProcessException e) {
            e.printStackTrace();
            return;
        }

        //Creating performs for atomic process 'atomic2'
        Perform perfAtomic2 = builder.createPerform();
        perfAtomic2.setProcess(atomic2);

        //Creating control construct list and adding perform
        ControlConstructList ccList2 = builder.createControlConstructList();
        ccList2.setFirst(perfAtomic2);
        ccList2.setRest(null);

        //Creating performs for atomic process 'atomic2'
        Perform perfAtomic1 = builder.createPerform();
        perfAtomic1.setProcess(atomic1);

        //Creating control construct list and adding perform
        ControlConstructList ccList1 = builder.createControlConstructList();
        ccList1.setFirst(perfAtomic1);
        ccList1.setRest(ccList2);

        //Creating control construct of type sequence
        ControlConstruct cc = builder.createControlConstruct(OWLS_Builder_Util.SEQUENCE);
        cc.setComponents(ccList1);

        //Creating composite process 'composite1' and setting hte control construct
        CompositeProcess composite1;
        try {
            composite1 = (CompositeProcess) builder.createProcess("Composite1", OWLS_Builder_Util.COMPOSITE);
        } catch (NotInstanceOfProcessException e) {
            e.printStackTrace();
            return;
        }
        composite1.setComposedOf(cc);

        //Creating process list to adding composite
        ProcessList list = storeBuilder.createProcessList();
        list.add(composite1);

        try {
            //writing the composite process
            OWLSProcessWriter.write(list, "http://www.cs.cmu.edu/naveen.owls", System.out);
        } catch (IndexOutOfBoundsException e1) {
            e1.printStackTrace();
        } catch (OWLSWriterException e1) {
            e1.printStackTrace();
        }

    }

    public static void writerUsingURI() {
        //String bravoURI = "http://www.daml.ri.cmu.edu/owls/BravoAirProcess.owl";
        String bravoURI = "http://www.daml.org/services/owl-s/1.1/CongoProcess.owl";

        //creating instance of parser
        OWLSProcessParser parser = new OWLSProcessParser();

        OWLSProcessModel model;
        try {
            //parsing process uri
            model = parser.read(bravoURI);
        } catch (NotInstanceOfProcessException e1) {
            e1.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        
        ProcessList pl = new ProcessListImpl();
        pl.addProcess(model.getProcessList().getProcess("http://www.daml.org/services/owl-s/1.1/CongoProcess.owl#ExpressCongoBuy"));
        //writing process
        try {
//            OWLSProcessWriter.write(model.getProcessList(), "http://www.cs.cmu.edu/naveen.owls", System.out);
            OWLSProcessWriter.write(pl, "http://www.cs.cmu.edu/naveen.owls", System.out);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        } catch (OWLSWriterException e) {
            e.printStackTrace();
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