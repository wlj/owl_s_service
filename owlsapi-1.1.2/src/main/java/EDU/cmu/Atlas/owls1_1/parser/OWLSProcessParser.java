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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;
import EDU.cmu.Atlas.owls1_1.writer.OWLSWriterUtils;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;

/**
 * OWL-S Process Parser
 * @author Massimo Paolucci
 * @author Naveen Srinivasan
 */

public class OWLSProcessParser {

    private OntModel ontModel;

    private OWLSProcessModel owlsModel;

    private OWLS_Object_Builder objectBuilder;

    private OWLS_Store_Builder storeBuilder;

    static Logger logger = Logger.getLogger(OWLSProcessParser.class);

    /**
     * OWLSProcessParser parsers the URI in to DAML_S_Profile_Model
     */
    public OWLSProcessParser() {
        this(true);
    }

    /**
     * constructor instantiate the model and specifies whether other ontologies should be imported
     * 
     * @param imports a boolean to specify whether additional ontologies should be imported
     * @author Naveen
     */
    public OWLSProcessParser(boolean imports) {
        //this(ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF,null));
        this(OWLSWriterUtils.getNewOntModel());

        ontModel.setDynamicImports(imports);

    }

    /**
     * constructor instantiate the model and specifies whether other ontologies should be imported
     * 
     * @param imports a boolean to specify whether additional ontologies should be imported
     * @author Naveen
     */

    public OWLSProcessParser(OntModel ontModel) {
        this.ontModel = OWLSWriterUtils.getNewOntModel(ontModel);
        objectBuilder = OWLS_Object_BuilderFactory.instance();
        objectBuilder.reset();
        storeBuilder = OWLS_Store_BuilderFactory.instance();

    }

    /*
     * Comment (Naveen) Intially the parser can take only an URI as input i added code to take
     * reader as a input (requirement for the editor)
     */
    public OWLSProcessModel read(String processURI) throws MalformedURLException, NotInstanceOfProcessException, IOException {
        return read(processURI, "");
    }

    public OWLSProcessModel read(String processURI, OWLSErrorHandler handler) {
        return read(processURI, "", handler);
    }

    public OWLSProcessModel read(String URI, String base) throws MalformedURLException, NotInstanceOfProcessException, IOException {
        ontModel.addLoadedImport(URI);
        return read(new InputStreamReader(new URL(URI).openStream()), base);
    }

    public OWLSProcessModel read(String URI, String base, OWLSErrorHandler handler) {
        try {
            ontModel.addLoadedImport(URI);
            return read(new InputStreamReader(new URL(URI).openStream()), base, handler);
        } catch (IOException e) {
            handler.fatalError(e);
            return null;
        }
    }

    public OWLSProcessModel read(Reader reader) throws NotInstanceOfProcessException {
        return read(reader, "");
    }

    public OWLSProcessModel read(Reader reader, OWLSErrorHandler handler) {
        return read(reader, "", handler);
    }

    public OWLSProcessModel read(Reader reader, String base) throws NotInstanceOfProcessException {

        // read process model loading ontologies if processURI is true
        ontModel.read(reader, base);

        return read(ontModel);

    }

    public OWLSProcessModel read(OntModel model) throws NotInstanceOfProcessException {

        owlsModel = objectBuilder.createOWLSProcessModel();

        ontModel = OWLSWriterUtils.getNewOntModel(model);

        owlsModel.setOntModel(ontModel);

        // extract process model from the owl-s model
        ProcessList processList = parseAllProcess(owlsModel);
        owlsModel.setProcessList(processList);

        return owlsModel;

    }

    public OWLSProcessModel read(OntModel model, OWLSErrorHandler errorHandler) {

        owlsModel = objectBuilder.createOWLSProcessModel();

        ontModel = OWLSWriterUtils.getNewOntModel(model);

        owlsModel.setOntModel(ontModel);

        // extract process model from the owl-s model
        ProcessList processList = parseAllProcess(owlsModel, errorHandler);
        owlsModel.setProcessList(processList);

        return owlsModel;

    }

    public OWLSProcessModel read(Reader reader, String base, OWLSErrorHandler handler) {

        // read process model loading ontologies if processURI is true
        ontModel.read(reader, base);

        return read(ontModel, handler);

    }

    /**
     * extract process model from the OWL-s model by 1. looping through al the Process models found
     * 2. constructing an instance of that process model 3. extracting the process model processes
     * 
     * @param owlsModel the owl-s model
     * @return @throws NotInstanceOfProcessException
     */
    public ProcessList parseAllProcess(OWLSProcessModel owlsModel) throws NotInstanceOfProcessException {

        ProcessList processList = storeBuilder.createProcessList();
        // extract the ontModel from the owlsModel
        OntModel ontModel = owlsModel.getOntModel();

        // extract the process model class
        OntClass processClass = ontModel.getOntClass(OWLSProcessURI.Process);
        if (processClass == null) {
            //cheap hack.. if a non-process model file is parsed then above
            //getOntClass file returns null
            logger.debug("No Process Instance found");
            return null;
        }

        // extract the instances of the process model class
        ExtendedIterator processIterator = processClass.listInstances();
        // check whether any process model class has been found
        if (!processIterator.hasNext()) {
            // no process model class --- TODO is this an error? a warning?
            logger.debug("No Process  Instance found");
            return processList;
        }

        // process have been found, look through them
        while (processIterator.hasNext()) {
            // extract the next process
            Individual processInd = (Individual) processIterator.next();
            Process process = objectBuilder.createProcess(processInd);
            processList.add(process);
        }
        logger.debug("DONE");
        // return the list of process models
        return processList;
    }

    public ProcessList parseAllProcess(OWLSProcessModel owlsModel, OWLSErrorHandler handler) {

        ProcessList processList = storeBuilder.createProcessList();
        // extract the ontModel from the owlsModel
        OntModel ontModel = owlsModel.getOntModel();

        // extract the process model class
        OntClass processClass = ontModel.getOntClass(OWLSProcessURI.Process);
        if (processClass == null) {
            logger.debug("No Process Instance found");
            return null;
        }

        // extract the instances of the process model class
        ExtendedIterator processIterator = processClass.listInstances();
        // check whether any process model class has been found
        if (!processIterator.hasNext()) {
            // no process model class --- TODO is this an error? a warning?
            logger.debug("No Process  Instance found");
            return processList;
        }

        // process have been found, look through them
        while (processIterator.hasNext()) {
            // extract the next process
            Individual processInd = (Individual) processIterator.next();
            Process process = objectBuilder.createProcess(processInd, handler);
            if (process != null)
                processList.add(process);
        }
        logger.debug("DONE");
        // return the list of process models
        return processList;
    }

    /**
     * extract the list of atomic processes in a process model
     * 
     * @return the list of atomic processes
     * @throws NotInstanceOfProcessException
     */
    public OWLS_Store listAtomicProcesses() throws NotInstanceOfProcessException {
        return listAtomicProcesses(ontModel);

    }

    /**
     * extract the list of atomic processes in an ontology model
     * 
     * @param ontologyModel the ontology model to use
     * @return the list of atomic processes
     * @throws NotInstanceOfProcessException
     */
    public OWLS_Store listAtomicProcesses(OntModel ontologyModel) throws NotInstanceOfProcessException {

        OWLS_Store list = storeBuilder.createOWLS_Store();

        OntClass apClass = ontologyModel.getOntClass(OWLSProcessURI.AtomicProcess);

        if (apClass == null)
            return list;

        Iterator iter = apClass.listInstances();
        while (iter.hasNext()) {

            Individual apIndividual = (Individual) iter.next();
            AtomicProcess atomicProcess = (AtomicProcess) objectBuilder.createProcess(apIndividual);
            list.add(atomicProcess);

        }
        return list;

    }

    public OWLS_Store listCompositeProcesses() throws NotInstanceOfProcessException {
        return listCompositeProcesses(ontModel);
    }

    public OWLS_Store listCompositeProcesses(OntModel ontologyModel) throws NotInstanceOfProcessException {

        OWLS_Store list = storeBuilder.createOWLS_Store();

        OntClass cpClass = ontologyModel.getOntClass(OWLSProcessURI.CompositeProcess);

        if (cpClass == null)
            return list;

        Iterator iter = cpClass.listInstances();
        while (iter.hasNext()) {

            Individual cpIndividual = (Individual) iter.next();
            CompositeProcess compositeProcess = (CompositeProcess) objectBuilder.createProcess(cpIndividual);
            list.add(compositeProcess);

        }
        return list;

    }

    public OWLS_Store listIO(OntModel ontologyModel) throws NotAnIndividualException {

        OWLS_Store list = storeBuilder.createOWLS_Store();

        OntClass ipClass = ontologyModel.getOntClass(OWLSProcessURI.Input);

        if (ipClass == null)
            return list;

        Iterator iter = ipClass.listInstances();
        while (iter.hasNext()) {

            Individual ipIndividual = (Individual) iter.next();
            try {
                Input input = (Input) objectBuilder.createInput(ipIndividual);
                list.add(input);
            } catch (NotInstanceOfParameterException e) {
                e.printStackTrace();
            }
        }

        OntClass opClass = ontologyModel.getOntClass(OWLSProcessURI.Output);
        iter = opClass.listInstances();

        while (iter.hasNext()) {

            Individual opIndividual = (Individual) iter.next();
            //TODO OutputBinding's hasOutput has to changed to withOutput
            //otherwise OutputBinding are treated as output instances
            if (opIndividual.hasRDFType(OWLSProcessURI.process + "#OutputBinding"))
                continue;
            try {
                Output output = (Output) objectBuilder.createOutput(opIndividual);
                list.add(output);
            } catch (NotInstanceOfParameterException e) {
                e.printStackTrace();
            }
        }

        return list;

    }

}