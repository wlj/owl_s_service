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

import java.io.OutputStream;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMapList;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.uri.OWLSGroundingURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSGroundingProperties;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;

/**
 * OWLSGroundingWriter
 * @author Naveen Srinivasan
 */
public class OWLSGroundingWriterDynamic {

    private String baseURL;

    private static Logger logger = Logger.getLogger(OWLSGroundingWriterDynamic.class);
    
    protected OntModel init(String base) {

        //OntModel model = ModelFactory.createOntologyModel();
        OntModel model = OWLSWriterUtils.getNewOntModel();

        model.read("http://www.daml.org/services/owl-s/1.1/Service.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Process.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Grounding.owl");

        model.setNsPrefix("grounding", "http://www.daml.org/services/owl-s/1.1/Grounding.owl#");

        OntModel baseModel = ModelFactory.createOntologyModel();
        baseModel.addSubModel(model);

        //create import statements
        Ontology ont = baseModel.createOntology(base);
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Service.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Process.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Grounding.owl"));

        return baseModel;
    }

    protected void setBaseURL(String base) {
        baseURL = base;
    }

    public void write(WsdlGroundingList gndList, String base, OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        write(gndList, base, null, out);
    }

    public void write(WsdlGroundingList gndList, String base, String imports[], OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        write(gndList, base, imports, null, out);
    }

    public void write(WsdlGroundingList gndList, String base, String imports[], OntModel submodel, OutputStream out)
    throws IndexOutOfBoundsException, OWLSWriterException {
    	writeModel(gndList, base, imports, submodel, out);
    }


    public OntModel writeModel(WsdlGroundingList gndList, String base, String imports[], OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {

        baseURL = base;
        //setting the base of other writer, incase other writer are used in this one.
        OWLSProcessWriter.setBaseURL(base);
        OWLSServiceWriter.setBaseURL(base);
        OWLSProfileWriter.setBaseURL(base);

        if (gndList == null)
            throw new OWLSWriterException("WsdlGroundingList is null");

        OntModel baseModel = init(base);
        baseModel.setNsPrefix("", base);

        //adding submodel
        if (submodel != null)
            baseModel.addSubModel(submodel);

        //create import statements
        if (imports != null) {
            OntModel tmpModel = OWLSWriterUtils.getNewOntModel();
            Ontology ont = baseModel.getOntology(base);
            boolean readNewSubmodel = false;
            for (int i = 0; i < imports.length; i++) {
                if ((submodel!=null) && (submodel.getOntology(imports[i]) != null)) {
                    ont.addImport(submodel.getOntology(imports[i]));
                } else {
                    tmpModel.read(imports[i]);
                    ont.addImport(tmpModel.getOntology(imports[i]));
                    readNewSubmodel = true;
                }
            }
            if (readNewSubmodel) {
            	baseModel.addSubModel(tmpModel);
            }
        }
        

//        Individual ind = null;
        for (int i = 0; i < gndList.size(); i++) {
            writeWsdlGrounding(gndList.getNthWsdlGrounding(i), baseModel);
        }

//        baseModel = (OntModel) ind.getModel();
        RDFWriter writer = OWLSWriterUtils.getWriter(baseModel, base);
        writer.write(baseModel.getBaseModel(), out, base);
        
        return baseModel;

    }

    public void write(WsdlGrounding grounding, String base, OutputStream out) throws IndexOutOfBoundsException, OWLSWriterException {
        write(grounding, base, null, out);
    }

    public void write(WsdlGrounding grounding, String base, String imports[], OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        write(grounding, base, imports, null, out);
    }

    public void write(WsdlGrounding grounding, String base, String imports[], OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {

        if (grounding == null)
            throw new OWLSWriterException("Grounding is null");

        OWLS_Store_Builder builder = OWLS_Store_BuilderFactory.instance();

        WsdlGroundingList list = builder.createWsdlGroundingList();
        list.addWsdlGrounding(grounding);

        write(list, base, imports, submodel, out);
    }

    public Individual writeWsdlGrounding(WsdlGrounding grounding, OntModel ontModel) throws OWLSWriterException {

        if (grounding == null)
            throw new OWLSWriterException("WsdlGrounding is null");

        //check if the wsdlGndInst with that uri already exist in the ontModel
        Individual wsdlGndInst = OWLSWriterUtils.checkIfIndividualExist(grounding, ontModel);
        if (wsdlGndInst != null)
            return wsdlGndInst;

        OntClass wsdlGndClass = ontModel.getOntClass(OWLSGroundingURI.WsdlGrounding);
        wsdlGndInst = OWLSWriterUtils.createIndividual(wsdlGndClass, grounding, baseURL);

        WsdlAtomicProcessGroundingList apList = grounding.getWsdlAtomicProcessGroundingList();
        if (apList != null) {
            for (int i = 0; i < apList.size(); i++) {
                writeWsdlAtomicProcessGrounding(apList.getNthWsdlAtomicProcessGrounding(i), wsdlGndInst, ontModel);
            }
        }
        return wsdlGndInst;
    }

    public Individual writeWsdlAtomicProcessGrounding(WsdlAtomicProcessGrounding atomicGng, Individual wsdlGnd, OntModel model)
            throws OWLSWriterException {

        Individual wapgInd = writeWsdlAtomicProcessGrounding(atomicGng, model);
        wsdlGnd.addProperty(OWLSGroundingProperties.HasAtomicProcessGrounding, wapgInd);
        return wapgInd;

    }

    public Individual writeWsdlAtomicProcessGrounding(WsdlAtomicProcessGrounding atomicGrounding, OntModel ontModel)
            throws OWLSWriterException {

        if (atomicGrounding == null)
            throw new OWLSWriterException("WsdlAtomicProcessGrounding is null");

        //check if the atomicGrounding with that uri already exist in the ontModel
        Individual atomicGroundingInst = OWLSWriterUtils.checkIfIndividualExist(atomicGrounding, ontModel);
        if (atomicGroundingInst != null)
            return atomicGroundingInst;

        OntClass atomicGroundingClass = ontModel.getOntClass(OWLSGroundingURI.WsdlAtomicProcessGrounding);
        atomicGroundingInst = OWLSWriterUtils.createIndividual(atomicGroundingClass, atomicGrounding, baseURL);

        //OWLS Process
        Process owlsProcess = atomicGrounding.getOwlsProcess();
        if (owlsProcess != null) {
            Individual owlsProcessInd = OWLSProcessWriter.writeProcess(owlsProcess, ontModel);
            atomicGroundingInst.addProperty(OWLSGroundingProperties.OwlsProcess, owlsProcessInd);
        } else
            throw new OWLSWriterException("owlsProcess is null");

        //wsdlInputMessage
        String wsdlIpMsg = atomicGrounding.getWsdlInputMessage();
        if (wsdlIpMsg != null) {
            Literal dt = ontModel.createTypedLiteral(wsdlIpMsg, XSDDatatype.XSDanyURI);
            atomicGroundingInst.addProperty(OWLSGroundingProperties.WsdlInputMessage, dt);
        }
        //WsdlInputMessageMapList
        WsdlInputMessageMapList ipList = atomicGrounding.getWsdlInputs();
        if (ipList != null) {
            for (int i = 0; i < ipList.size(); i++) {
                writeWsdlInputMessageMap(ipList.getNthWsdlInputMessageMap(i), atomicGroundingInst, ontModel);
            }
        }

        //wsdlOutputMessage
        String wsdlOpMsg = atomicGrounding.getWsdlOutputMessage();
        if (wsdlOpMsg != null) {
            Literal dt = ontModel.createTypedLiteral(wsdlOpMsg, XSDDatatype.XSDanyURI);
            atomicGroundingInst.addProperty(OWLSGroundingProperties.WsdlOutputMessage, dt);
        }

        //WsdlOutputMessageMapList
        WsdlOutputMessageMapList opList = atomicGrounding.getWsdlOutputs();
        if (opList != null) {
            for (int i = 0; i < opList.size(); i++) {
                writeWsdlOutputMessageMap(opList.getNthWsdlOutputMessageMap(i), atomicGroundingInst, ontModel);
            }
        }
        //WsdlOperationRef
        WsdlOperationRef wsdlOpRef = atomicGrounding.getWsdlOperation();
        Individual wsdlOpRefInd = writeWsdlOperationRef(wsdlOpRef, ontModel);
        atomicGroundingInst.addProperty(OWLSGroundingProperties.WsdlOperation, wsdlOpRefInd);

        //WsdlDocument
        String wsdlDoc = atomicGrounding.getWsdlDocument();
        if (wsdlDoc != null) {
            Literal dt = ontModel.createTypedLiteral(wsdlDoc, XSDDatatype.XSDanyURI);
            atomicGroundingInst.addProperty(OWLSGroundingProperties.WsdlDocument, dt);
        } else
        	logger.warn("warning : WSDL Doc is missing");

        //WsdlVersion
        String wsdlVer = atomicGrounding.getWsdlVersion();
        if (wsdlVer != null) {
            Literal dt = ontModel.createTypedLiteral(wsdlVer, XSDDatatype.XSDanyURI);
            atomicGroundingInst.addProperty(OWLSGroundingProperties.WsdlVersion, dt);
        } else
        	logger.warn("warning : Wsdl Version is missing");

        return atomicGroundingInst;
    }

    public Individual writeWsdlOutputMessageMap(WsdlOutputMessageMap msgMap, Individual wsdlGnd, OntModel ontModel)
            throws OWLSWriterException {
        Individual opmap = writeWsdlOutputMessageMap(msgMap, ontModel);
        wsdlGnd.addProperty(OWLSGroundingProperties.WsdlOutput, opmap);
        return opmap;
    }

    public Individual writeWsdlOutputMessageMap(WsdlOutputMessageMap msgMap, OntModel ontModel) throws OWLSWriterException {

        if (msgMap == null)
            throw new OWLSWriterException("WsdlOutputMessageMap is null");

        //check if the msgMapInst with that uri already exist in the ontModel
        Individual msgMapInst = OWLSWriterUtils.checkIfIndividualExist(msgMap, ontModel);
        if (msgMapInst != null)
            return msgMapInst;

        OntClass msgMapClass = ontModel.getOntClass(OWLSGroundingURI.WsdlOutputMessageMap);
        msgMapInst = OWLSWriterUtils.createIndividual(msgMapClass, msgMap, baseURL);

        //Parameter
        Parameter param = msgMap.getOWLSParameter();
        if (param != null) {
            Individual paramInd = OWLSProcessWriter.writeParameter(param, ontModel);
            msgMapInst.addProperty(OWLSGroundingProperties.OwlsParameter, paramInd);
        } else
            throw new OWLSWriterException("WsdlOutputMessageMap :" + msgMap.getURI() + " : OwlsParameter is null");

        //message part
        String msgPart = msgMap.getWSDLMessagePart();
        if (msgPart != null) {
            Literal dt = ontModel.createTypedLiteral(msgPart, XSDDatatype.XSDanyURI);
            msgMapInst.addProperty(OWLSGroundingProperties.WsdlMessagePart, dt);
        } else {
            //XSLTTransformationString
            String xsltStr = msgMap.getXSLTTransformationString();
            if (xsltStr != null) {
                Literal dt = ontModel.createLiteral(xsltStr, true);
                msgMapInst.addProperty(OWLSGroundingProperties.XSLTTransformationString, dt);
            } else {
                //XSLTTransformationURI
                String xsltURI = msgMap.getXSLTTransformationURI();
                if (xsltURI != null) {
                    Literal dt = ontModel.createTypedLiteral(xsltURI, XSDDatatype.XSDanyURI);
                    msgMapInst.addProperty(OWLSGroundingProperties.XSLTTransformationURI, dt);
                } else
                    throw new OWLSWriterException("WsdlOutputMessageMap: should have message part or xstlstring or xslt uri");
            }
        }
        return msgMapInst;
    }

    public Individual writeWsdlInputMessageMap(WsdlInputMessageMap msgMap, Individual wapg, OntModel ontModel)
            throws OWLSWriterException {
        Individual ipmap = writeWsdlInputMessageMap(msgMap, ontModel);
        wapg.addProperty(OWLSGroundingProperties.WsdlInput, ipmap);
        return ipmap;

    }

    public Individual writeWsdlInputMessageMap(WsdlInputMessageMap msgMap, OntModel ontModel) throws OWLSWriterException {

        if (msgMap == null)
            throw new OWLSWriterException("WsdlInputMessageMap is null");

        //check if the msgMapInst with that uri already exist in the ontModel
        Individual msgMapInst = OWLSWriterUtils.checkIfIndividualExist(msgMap, ontModel);
        if (msgMapInst != null)
            return msgMapInst;

        OntClass msgMapClass = ontModel.getOntClass(OWLSGroundingURI.WsdlInputMessageMap);
        msgMapInst = OWLSWriterUtils.createIndividual(msgMapClass, msgMap, baseURL);

        //message part
        String msgPart = msgMap.getWSDLMessagePart();

        if (msgPart != null) {
            Literal dt = ontModel.createTypedLiteral(msgPart, XSDDatatype.XSDanyURI);
            msgMapInst.addProperty(OWLSGroundingProperties.WsdlMessagePart, dt);
        } else
            throw new OWLSWriterException("WsdlInputMessageMap : messagePart is null");

        //Parameter
        Parameter param = msgMap.getOWLSParameter();
        if (param != null) {
            Individual paramInd = OWLSProcessWriter.writeParameter(param, ontModel);
            msgMapInst.addProperty(OWLSGroundingProperties.OwlsParameter, paramInd);
        } else {
            //XSLTTransformationString
            String xsltStr = msgMap.getXSLTTransformationString();
            if (xsltStr != null) {
                Literal dt = ontModel.createLiteral(xsltStr, true);
                msgMapInst.addProperty(OWLSGroundingProperties.XSLTTransformationString, dt);
            } else {
                //XSLTTransformationURI
                String xsltURI = msgMap.getXSLTTransformationURI();
                if (xsltURI != null) {
                    msgMapInst.addProperty(OWLSGroundingProperties.XSLTTransformationURI, xsltURI);
                } else {
                    throw new OWLSWriterException("WsdlInputMessageMap : should have owlsparameter or xsltstring or xslt uri");
                }
            }
        }
        return msgMapInst;
    }

    public Individual writeWsdlOperationRef(WsdlOperationRef wsdlOpRef, OntModel ontModel) throws OWLSWriterException {

        if (wsdlOpRef == null)
            throw new OWLSWriterException("WsdlOperationRef is null");

        //check if the wsdlOpRefInst with that uri already exist in the ontModel
        Individual wsdlOpRefInst = OWLSWriterUtils.checkIfIndividualExist(wsdlOpRef, ontModel);
        if (wsdlOpRefInst != null)
            return wsdlOpRefInst;

        OntClass wsdlOpRefClass = ontModel.getOntClass(OWLSGroundingURI.WsdlOperationRef);
        wsdlOpRefInst = OWLSWriterUtils.createIndividual(wsdlOpRefClass, wsdlOpRef, baseURL);

        //WSDL Operation
        String wsdlOp = wsdlOpRef.getOperation();
        if (wsdlOp != null) {
            Literal dt = ontModel.createTypedLiteral(wsdlOp, XSDDatatype.XSDanyURI);
            wsdlOpRefInst.addProperty(OWLSGroundingProperties.Operation, dt);
        } else
            throw new OWLSWriterException("WsdlOperationRef is null");

        //WSDL Port
        String wsdlPort = wsdlOpRef.getPortType();
        if (wsdlPort != null) {
            Literal dt = ontModel.createTypedLiteral(wsdlPort, XSDDatatype.XSDanyURI);
            wsdlOpRefInst.addProperty(OWLSGroundingProperties.PortType, dt);
        } else
            throw new OWLSWriterException("WsdlOperationRef is null");

        return wsdlOpRefInst;
    }

}
