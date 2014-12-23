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
package EDU.cmu.Atlas.owls1_1.process.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfControlConstructException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.parser.OWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.LocalList;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.process.SimpleProcess;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;
import EDU.cmu.Atlas.owls1_1.utils.OWLS_StoreUtil;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 */
public class ProcessFactoryDynamic {

    private static Logger logger = Logger.getLogger(ProcessFactory.class);

    /**
     * extract and parses a process from the Jena model
     * @param processInd the instance of the process in the a-box
     * @return an instance of Process
     * @throws NotInstanceOfProcessException
     * @throws SyntacticError when the instance is defined with the wrong type of process, or some
     *             parts for the process are ill defined
     */
    public Process parseProcess(Individual processInd) throws NotInstanceOfProcessException {

        // check that the individual exists
        if (processInd == null) {
            throw new NotInstanceOfProcessException("Process instance is null");
        }
        Process process;
        // check whether it is extracting an atomic or a composite process
        if (isAtomicProcess(processInd)) {
            process = extractAtomicProcess(processInd);
        } // else: the process is composite
        else if (isCompositeProcess(processInd)) {
            process = extractCompositeProcess(processInd);
        } // else it should be a simple process
        else if (isSimpleProcess(processInd)) {
            process = extractSimpleProcess(processInd);
        } else {
            throw new NotInstanceOfProcessException("found a process that is not declared of type " + "Atomc or Composite or Simple \n");
        }
        // parse the name of the process
        Literal name;
        try {
            name = OWLUtil.getLiteralFromFunctionalProperty(processInd, OWLSProcessProperties.processName);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfProcessException(exp);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfProcessException(e);
        }
        if (name != null) {
            process.setName(name.toString());
        } else {
            // the name has not been declared, use uri instead
            process.setName(processInd.getURI());
        }

        logger.debug("found Process process.name= " + process.getName());

        //Extracting inputs
        logger.debug("Extracting the inputs of process " + process.getName());
        try {
            process.setInputList((InputList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd, OWLSProcessProperties.hasInput,
                    "createInput", InputListImpl.class.getName()));

        } catch (OWLS_Store_Exception e1) {
            throw new NotInstanceOfProcessException("InputList", e1);
        }

        //Extracting outputs
        logger.debug("Extracting the outputs of process " + process.getName());
        try {
            process.setOutputList((OutputList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd, OWLSProcessProperties.hasOutput,
                    "createOutput", OutputListImpl.class.getName()));
        } catch (OWLS_Store_Exception e2) {
            throw new NotInstanceOfProcessException("OutputList", e2);
        }

        //Extracting pre-condition
        logger.debug("Extracting the pre-conditions of process " + process.getName());
        try {
            process.setPreConditionList((PreConditionList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd,
                    OWLSProcessProperties.hasPrecondition, "createCondition", PreConditionListImpl.class.getName()));
        } catch (OWLS_Store_Exception e3) {
            throw new NotInstanceOfProcessException("PreConditionList", e3);
        }

        //Extracting results
        logger.debug("Extracting the results of process " + process.getName());
        try {
            process.setResultList((ResultList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd, OWLSProcessProperties.hasResult,
                    "createResult", ResultListImpl.class.getName()));
        } catch (OWLS_Store_Exception e4) {
            throw new NotInstanceOfProcessException("ResultList", e4);
        }

        //Extracting local varialbes
        logger.debug("Extracting the locals of process " + process.getName());
        try {
            process.setLocalList((LocalList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd, OWLSProcessProperties.hasLocal,
                    "createLocal", LocalListImpl.class.getName()));
        } catch (OWLS_Store_Exception e3) {
            throw new NotInstanceOfProcessException("LocalList", e3);
        }

        return process;
    }

    /**
     * check whether an individual is of type atomic process
     * @author Massimo
     * @param processIndividual
     * @return true if the individual is an atomic process false otherwise
     */
    public final boolean isAtomicProcess(Individual processIndividual) {
        // check whether the type of the individual is "atomic process"
        return OWLUtil.typeOf(processIndividual, OWLSProcessURI.AtomicProcess);
    }

    /**
     * check whether an individual is of type composite process
     * @author Massimo
     * @param processIndividual
     * @return true if the individual is an composite process false otherwise
     */
    public final boolean isCompositeProcess(Individual processIndividual) {
        // check whether the type of the individual is "composite process"
        return OWLUtil.typeOf(processIndividual, OWLSProcessURI.CompositeProcess);
    }

    /**
     * check whether an individual is of type composite process
     * @author Massimo
     * @param processIndividual
     * @return true if the individual is an composite process false otherwise
     */
    public final boolean isSimpleProcess(Individual processIndividual) {
        // check whether the type of the individual is "atomic process"
        return OWLUtil.typeOf(processIndividual, OWLSProcessURI.SimpleProcess);
    }

    /**
     * extract the definition of a composite process
     * @param process the instance of the process in the a-box
     * @return an instance of Process that compiled from the a-box
     * @throws NotInstanceOfProcessException
     * @throws SyntacticError when either the composite process is ill defined, or its parts are ill
     *             defined
     */
    protected Process extractCompositeProcess(Individual process) throws NotInstanceOfProcessException {

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        logger.debug("CompositeProcess " + process.getURI());
        // create a new instance of composite process
        CompositeProcess compositeProcess = new CompositeProcessImpl(process);
        // extract ComposedOf
        try {
            // find the instance of control construct
            Individual controlConstructInd = OWLUtil.getInstanceFromFunctionalProperty(process, OWLSProcessProperties.composedOf);
            if(controlConstructInd != null)
                logger.debug("ControlConstruct= " + controlConstructInd.toString());
            else
                logger.debug("Composite Process " + process.getURI() + " has no control construct");
            // extract the control construct
            ControlConstruct controlConstruct = builder.createControlConstruct(controlConstructInd);
            compositeProcess.setComposedOf(controlConstruct);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfProcessException("the Composite Process " + process.getURI() + " has more than one control construct \n");
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfProcessException(e);
        } catch (NotInstanceOfControlConstructException e) {
            throw new NotInstanceOfProcessException(e);
        }

        // extract Collapse To
        try {
            // find the instance of control construct
            Individual collapsesInd = OWLUtil.getInstanceFromFunctionalProperty(process, OWLSProcessProperties.collapsesTo);
            if (collapsesInd != null) {
                System.out.println("OWLSProcessParser.extractCompositeProcess: ControlConstruct= " + collapsesInd.getURI());
            }
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfProcessException("the Composite Process " + process.getURI()
                    + " has more than one collapsesTo properties \n");
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfProcessException(e);
        }

        return compositeProcess;
    }

    /**
     * @param hasProcess
     * @return
     */
    protected Process extractAtomicProcess(Individual process) {
        logger.debug("AtomicProcess " + process.getURI());
        AtomicProcess atomicProcess = new AtomicProcessImpl(process);
        return atomicProcess;
    }

    /**
     * parser for simple processes
     * @param processInd an instance of simple process in the a-box
     * @return null for now because the function is not implemented
     * @throws NotInstanceOfProcessException
     */
    protected Process extractSimpleProcess(Individual processInd) throws NotInstanceOfProcessException {

        logger.debug("SimpleProcess " + processInd.getURI());
        SimpleProcess simpleProcess = new SimpleProcessImpl(processInd);

        //realizedBy
        Individual atomicProcessInd;
        try {
            atomicProcessInd = OWLUtil.getInstanceFromProperty(processInd, OWLSProcessProperties.realizedBy);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfProcessException("Simple Process " + processInd.getURI(), e);
        }
        if (atomicProcessInd != null)
            simpleProcess.setRealizedBy((AtomicProcess) parseProcess(atomicProcessInd));

        // parse expandsTo
        Individual compositeProcessInd;
        try {
            compositeProcessInd = OWLUtil.getInstanceFromProperty(processInd, OWLSProcessProperties.expandsTo);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfProcessException("Simple Process " + processInd.getURI(), e);
        }
        if (compositeProcessInd != null)
            simpleProcess.setExpandsTo((CompositeProcess) parseProcess(compositeProcessInd));

        return simpleProcess;

    }

    public Process parseProcess(Individual processInd, OWLSErrorHandler handler) {

        // check that the individual exists
        if (processInd == null) {
            handler.error(new NotInstanceOfProcessException("\nProcess instance is null"));
            return null;
        }
        Process process;
        // check whether it is extracting an atomic or a composite process
        if (isAtomicProcess(processInd)) {
            process = extractAtomicProcess(processInd);
        } // else: the process is composite
        else if (isCompositeProcess(processInd)) {
            try {
                process = extractCompositeProcess(processInd);
            } catch (NotInstanceOfProcessException e5) {
                handler.error(e5);
                return null;
            }
        } // else it should be a simple process
        else if (isSimpleProcess(processInd)) {
            try {
                process = extractSimpleProcess(processInd);
            } catch (NotInstanceOfProcessException e1) {
                handler.error(e1);
                return null;
            }
        } else {
            handler.error(new NotInstanceOfProcessException("found a process that is not declared of type "
                    + "Atomc or Composite or Simple \n"));
            return null;
        }
        // parse the name of the process
        Literal name;
        try {
            name = OWLUtil.getLiteralFromFunctionalProperty(processInd, OWLSProcessProperties.processName);
        } catch (PropertyNotFunctional exp) {
            handler.error(new NotInstanceOfProcessException(exp));
            name = null;
        } catch (NotAnLiteralException e) {
            handler.error(new NotInstanceOfProcessException(e));
            name = null;
        }
        if (name != null) {
            process.setName(name.toString());
        } else {
            // the name has not been declared, use uri instead
            process.setName(processInd.getURI());
        }
        logger.debug("found Process process.name= " + process.getName());

        process.setInputList((InputList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd, OWLSProcessProperties.hasInput,
                "createInput", InputListImpl.class.getName(), handler));

        process.setOutputList((OutputList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd, OWLSProcessProperties.hasOutput,
                "createOutput", OutputListImpl.class.getName(), handler));

        process.setPreConditionList((PreConditionList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd,
                OWLSProcessProperties.hasPrecondition, "createCondition", PreConditionListImpl.class.getName(), handler));

        process.setLocalList((LocalList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd, OWLSProcessProperties.hasLocal,
                "createLocal", LocalListImpl.class.getName(), handler));

        process.setResultList((ResultList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(processInd, OWLSProcessProperties.hasResult,
                "createResult", ResultListImpl.class.getName(), handler));

        return process;
    }

}