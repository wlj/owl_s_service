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
package EDU.cmu.Atlas.owls1_1.grounding.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWSDLOperationRefException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlAtomicProcessGroundingException;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMapList;
import EDU.cmu.Atlas.owls1_1.parser.OWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.utils.OWLSGroundingProperties;
import EDU.cmu.Atlas.owls1_1.utils.OWLS_StoreUtil;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 *  
 */
public class WsdlAtomicProcessGroundingImpl extends OWLS_ObjectImpl implements WsdlAtomicProcessGrounding {

    Process owlsProcess;

    WsdlOperationRef wsdlOperation;

    String wsdlInputMessage;

    WsdlInputMessageMapList wsdlInputs;

    String wsdlOutputMessage;

    WsdlOutputMessageMapList wsdlOutputs;

    String wsdlDocument;

    String wsdlVersion;

    static Logger logger = Logger.getLogger(WsdlAtomicProcessGroundingImpl.class);

    public WsdlAtomicProcessGroundingImpl() {
        super();
    }

    public WsdlAtomicProcessGroundingImpl(String uri) {
        super(uri);
    }

    public WsdlAtomicProcessGroundingImpl(Individual individual)
            throws NotInstanceOfWsdlAtomicProcessGroundingException {
        super(individual);

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();

        logger.debug("Extracting wsdl atomic process grounding instance : " + individual.getURI());
        //extracting OWL-S Process
        Individual owlsProcess;

        try {
            owlsProcess = OWLUtil.getInstanceFromFunctionalProperty(individual, OWLSGroundingProperties.OwlsProcess);
        } catch (NotAnIndividualException e1) {
            throw new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                    + " : Property 'owlsProcess' not an instance", e1);
        } catch (PropertyNotFunctional e1) {
            throw new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI(), e1);
        }
        if (owlsProcess != null) {
            try {
                setOwlsProcess(builder.createProcess(owlsProcess));
            } catch (NotInstanceOfProcessException e2) {
                throw new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                        + " : Property 'owlsProcess' not an process", e2);
            }
        } else {
            throw new NotInstanceOfWsdlAtomicProcessGroundingException("WSDLAtomicProcessGrouding "
                    + individual.getURI() + " has not OWL-S process");
        }

        //extracting WSDL Operation Prop
        Individual wsdlOpRefInd;
        try {
            wsdlOpRefInd = OWLUtil.getInstanceFromProperty(individual, OWLSGroundingProperties.WsdlOperation);
        } catch (NotAnIndividualException e3) {
            throw new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                    + " : Property 'wsdlOperation' not an instance", e3);
        }
        if (wsdlOpRefInd != null) {
            try {
                setWsdlOperation(builder.createWSDLOperationRef(wsdlOpRefInd));
            } catch (NotInstanceOfWSDLOperationRefException e2) {
                throw new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                        + " : Property 'wsdlOperation' not an WSDL Operation", e2);
            }
        } else {
            logger.info("WSDLAtomicProcessGrouding " + individual.getURI() + " has not WSDL Operation");
        }

        //extracting WSDL Input Message Prop

        Literal wsdlIpMsg;
        try {
            wsdlIpMsg = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.WsdlInputMessage);
        } catch (NotAnLiteralException e4) {
            throw new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                    + " : Property 'wsdlInputMessage' not a Literal", e4);
        }

        if (wsdlIpMsg != null) {
            setWsdlInputMessage(wsdlIpMsg.getString().trim());
            logger.debug("extracting WSDL Input Message part " + wsdlIpMsg.getString().trim());
        }
        //else
        //    throw new NotInstanceOfWsdlAtomicProcessGroundingException("No Input
        // Message Part");

        //extracting WSDL Inputs
        try {
            setWsdlInputs((WsdlInputMessageMapList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                    OWLSGroundingProperties.WsdlInput, "createWsdlInputMessageMap", WsdlInputMessageMapListImpl.class
                            .getName()));
        } catch (OWLS_Store_Exception e) {
            throw new NotInstanceOfWsdlAtomicProcessGroundingException("Problems in Input Message List of Atomic Process Grounding " + individual.getURI(), e);
        }

        //extracting WSDL Output Message Prop
        Literal wsdlOpMsg;
        try {
            wsdlOpMsg = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.WsdlOutputMessage);
        } catch (NotAnLiteralException e5) {
            throw new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI(), e5);
        }

        if (wsdlOpMsg != null) {
            setWsdlOutputMessage(wsdlOpMsg.getString().trim());
            logger.debug("extracting WSDL Output Message part " + wsdlOpMsg.getString().trim());
        }
        //else
        //    throw new NotInstanceOfWsdlAtomicProcessGroundingException("No Output
        // Message Part");

        //extracting WSDL Output
        //setWsdlOutputs(extractWsdlOutputs(individual));
        try {
            setWsdlOutputs((WsdlOutputMessageMapList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                    OWLSGroundingProperties.WsdlOutput, "createWsdlOutputMessageMap",
                    WsdlOutputMessageMapListImpl.class.getName()));
        } catch (OWLS_Store_Exception e) {
            throw new NotInstanceOfWsdlAtomicProcessGroundingException("Problems in Output Message List of Atomic Process Grounding " + individual.getURI(), e);
        }

        //extracting WSDL document
        Literal tempLiteral;
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.WsdlDocument);
            if (tempLiteral != null) {
                setWsdlDocument(tempLiteral.getString().trim());
                logger.debug("extracting WSDL " + tempLiteral.getString().trim());
            }
        } catch (NotAnLiteralException e6) {
            logger.warn("WSDL Document is not a Literal");

        }

        //extracting WSDL version
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.WsdlVersion);
            if (tempLiteral != null) {
                setWsdlVersion(tempLiteral.getString().trim());
                logger.debug("extracting WSDL version " + tempLiteral.getString().trim());
            }
        } catch (NotAnLiteralException e7) {
            logger.warn("WSDL version is not a Literal");
        }

    }

    public WsdlAtomicProcessGroundingImpl(Individual individual, OWLSErrorHandler errHandler) {
        super(individual);

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();

        logger.debug("Extracting wsdl atomic process grounding instance : " + individual.getURI());

        //extracting OWL-S Process - Cardinality 1
        Individual owlsProcess = null;
        try {
            owlsProcess = OWLUtil.getInstanceFromFunctionalProperty(individual, OWLSGroundingProperties.OwlsProcess);
        } catch (NotAnIndividualException e1) {
            errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                    + " : Property 'owlsProcess' not an instance", e1));
        } catch (PropertyNotFunctional e1) {
            errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException(e1));
        }

        if (owlsProcess != null) {
            try {
                setOwlsProcess(builder.createProcess(owlsProcess));
            } catch (NotInstanceOfProcessException e2) {
                errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                        + " : Property 'owlsProcess' not an process", e2));
            }
        } else {
            errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException("WSDLAtomicProcessGrouding "
                    + individual.getURI() + " has not OWL-S process"));
        }

        //extracting WSDL Operation Prop
        Individual wsdlOpRefInd = null;
        try {
            wsdlOpRefInd = OWLUtil.getInstanceFromProperty(individual, OWLSGroundingProperties.WsdlOperation);
        } catch (NotAnIndividualException e3) {
            errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                    + " : Property 'wsdlOperation' not an instance", e3));
        }
        if (wsdlOpRefInd != null) {
            try {
                setWsdlOperation(builder.createWSDLOperationRef(wsdlOpRefInd));
            } catch (NotInstanceOfWSDLOperationRefException e2) {
                errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                        + " : Property 'wsdlOperation' not an WSDL Operation", e2));
            }
        } else
            logger.info("WSDLAtomicProcessGrouding " + individual.getURI() + " has not WSDL Operation");

        //extracting WSDL Input Message Prop
        Literal wsdlIpMsg = null;
        try {
            wsdlIpMsg = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.WsdlInputMessage);
        } catch (NotAnLiteralException e4) {
            errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException(individual.getURI()
                    + " : Property 'wsdlInputMessage' not a Literal", e4));
        }

        if (wsdlIpMsg != null) {
            setWsdlInputMessage(wsdlIpMsg.getString().trim());
            logger.debug("extracting WSDL Input Message part " + wsdlIpMsg.getString().trim());
        }
        //else
        //    throw new NotInstanceOfWsdlAtomicProcessGroundingException("No Input
        // Message Part");

        //extracting WSDL Inputs
        //setWsdlInputs(extractWsdlInputs(individual));
        try {
            setWsdlInputs((WsdlInputMessageMapList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                    OWLSGroundingProperties.WsdlInput, "createWsdlInputMessageMap", WsdlInputMessageMapListImpl.class
                            .getName()));
        } catch (OWLS_Store_Exception e) {
            errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException("Input Message List", e));
        }

        //extracting WSDL Output Message Prop
        Literal wsdlOpMsg = null;
        try {
            wsdlOpMsg = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.WsdlOutputMessage);
        } catch (NotAnLiteralException e5) {
            errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException(e5));
        }

        if (wsdlOpMsg != null) {
            setWsdlOutputMessage(wsdlOpMsg.getString().trim());
            logger.debug("extracting WSDL Output Message part " + wsdlOpMsg.getString().trim());
        }
        //else
        //    throw new NotInstanceOfWsdlAtomicProcessGroundingException("No Output
        // Message Part");

        //extracting WSDL Output
        //setWsdlOutputs(extractWsdlOutputs(individual));
        try {
            setWsdlOutputs((WsdlOutputMessageMapList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                    OWLSGroundingProperties.WsdlOutput, "createWsdlOutputMessageMap",
                    WsdlOutputMessageMapListImpl.class.getName()));
        } catch (OWLS_Store_Exception e) {
            errHandler.error(new NotInstanceOfWsdlAtomicProcessGroundingException("Output Message List", e));
        }

        //extracting WSDL document
        Literal tempLiteral;
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.WsdlDocument);
            if (tempLiteral != null) {
                setWsdlDocument(tempLiteral.getString().trim());
                logger.debug("extracting WSDL " + tempLiteral.getString().trim());
            }
        } catch (NotAnLiteralException e6) {
            logger.warn("WSDL Document is not a Literal");

        }

        //extracting WSDL version
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.WsdlVersion);
            if (tempLiteral != null) {
                setWsdlVersion(tempLiteral.getString().trim());
                logger.debug("extracting WSDL version " + tempLiteral.getString().trim());
            }
        } catch (NotAnLiteralException e7) {
            logger.warn("WSDL version is not a Literal");
        }

    }

    public Process getOwlsProcess() {
        return owlsProcess;
    }

    public String getWsdlDocument() {
        return wsdlDocument;
    }

    public String getWsdlInputMessage() {
        return wsdlInputMessage;
    }

    public WsdlInputMessageMapList getWsdlInputs() {
        return wsdlInputs;
    }

    public WsdlOperationRef getWsdlOperation() {
        return wsdlOperation;
    }

    public String getWsdlOutputMessage() {
        return wsdlOutputMessage;
    }

    public WsdlOutputMessageMapList getWsdlOutputs() {
        return wsdlOutputs;
    }

    public String getWsdlVersion() {
        return wsdlVersion;
    }

    public void setOwlsProcess(Process string) {
        owlsProcess = string;
    }

    public void setWsdlDocument(String string) {
        wsdlDocument = string;
    }

    public void setWsdlInputMessage(String string) {
        wsdlInputMessage = string;
    }

    public void setWsdlInputs(WsdlInputMessageMapList list) {
        wsdlInputs = list;
    }

    public void setWsdlOperation(WsdlOperationRef ref) {
        wsdlOperation = ref;
    }

    public void setWsdlOutputMessage(String string) {
        wsdlOutputMessage = string;
    }

    public void setWsdlOutputs(WsdlOutputMessageMapList list) {
        wsdlOutputs = list;
    }

    public void setWsdlVersion(String string) {
        wsdlVersion = string;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nWSDLAtomicProcessGrounding");
        sb.append("\n");
        sb.append("\nRDF:ID : ");
        sb.append(getURI());
        sb.append("\nOWL-S Process :");
        sb.append(owlsProcess.getURI());
        sb.append("\nWsdlOperationRef :");
        sb.append(wsdlOperation.toString("    "));
        sb.append("\nWsdl Input Message :");
        sb.append(wsdlInputMessage);
        sb.append("\nWsdl Input Message Map List :");
        sb.append(wsdlInputs);
        sb.append("\nWsdl Output Message :");
        sb.append(wsdlOutputMessage);
        sb.append("\nWsdl Output Message Map List :");
        sb.append(wsdlOutputs);
        sb.append("\nWSDL Document :");
        sb.append(wsdlDocument);
        sb.append("\nWSDL Version :");
        sb.append(wsdlVersion);
        return sb.toString();
    }

    public String toString(String indent) {
        return toString();
    }
}