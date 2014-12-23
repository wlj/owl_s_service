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

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Builder_Util;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.ConditionList;
import EDU.cmu.Atlas.owls1_1.expression.Expression;
import EDU.cmu.Atlas.owls1_1.expression.LogicLanguage;
import EDU.cmu.Atlas.owls1_1.process.AnyOrder;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.process.Binding;
import EDU.cmu.Atlas.owls1_1.process.BindingList;
import EDU.cmu.Atlas.owls1_1.process.Choice;
import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.IfThenElse;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Local;
import EDU.cmu.Atlas.owls1_1.process.LocalList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.process.ResultVar;
import EDU.cmu.Atlas.owls1_1.process.ResultVarList;
import EDU.cmu.Atlas.owls1_1.process.Sequence;
import EDU.cmu.Atlas.owls1_1.process.SimpleProcess;
import EDU.cmu.Atlas.owls1_1.process.Split;
import EDU.cmu.Atlas.owls1_1.process.SplitJoin;
import EDU.cmu.Atlas.owls1_1.process.ValueOf;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;

/**
 * @author Naveen Srinivasan
 */
public class OWLSProcessWriterDynamic {

    private String baseURL;
    private static Logger logger = Logger.getLogger(OWLSProcessWriter.class);
    
    protected OntModel init(String base) {
        //OntModel model = ModelFactory.createOntologyModel();
        OntModel model = OWLSWriterUtils.getNewOntModel();
        model.read("http://www.daml.org/services/owl-s/1.1/Service.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Profile.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Process.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Grounding.owl");
        
        model.setNsPrefix("grounding", "http://www.daml.org/services/owl-s/1.1/Grounding.owl#");

        OntModel baseModel = ModelFactory.createOntologyModel();
        baseModel.addSubModel(model);

        //create import statements
        Ontology ont = baseModel.createOntology(base);
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Service.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Profile.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Process.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Grounding.owl"));

        return baseModel;
    }

    protected void setBaseURL(String base) {
        baseURL = base;
    }
    
    protected String getBaseURL(){
    	return baseURL;
    }

    public void write(Process process, String base, OutputStream out) throws OWLSWriterException {
        write(process, base, null, out);
    }

    public void write(Process process, String base, String imports[], OutputStream out) throws OWLSWriterException {
        write(process, base, imports, null, out);
    }

    public void write(Process process, String base, String imports[], OntModel submodel, OutputStream out)
            throws OWLSWriterException {

        if (process == null) {
            throw new OWLSWriterException("Process is null");
        }

        OWLS_Store_Builder builder = OWLS_Store_BuilderFactory.instance();

        ProcessList list = builder.createProcessList();
        list.add(process);

        write(list, base, imports, submodel, out);
    }

    public void write(ProcessList processList, String base, OutputStream out) throws IndexOutOfBoundsException, OWLSWriterException {
        write(processList, base, null, out);
    }

    public void write(ProcessList processList, String base, String[] imports, OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        write(processList, base, imports, null, out);
    }

    public void write(ProcessList processList, String base, String[] imports, OntModel submodel, OutputStream out) 
    		throws IndexOutOfBoundsException, OWLSWriterException {
    	writeModel(processList, base, imports, submodel, out);    	
    }
    
    public OntModel writeModel(ProcessList processList, String base, String[] imports, OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {

        baseURL = base;
        //setting the base of other writer, incase other writer are used in this one.
        OWLSGroundingWriter.setBaseURL(base);
        OWLSServiceWriter.setBaseURL(base);
        OWLSProfileWriter.setBaseURL(base);

        if (processList == null)
            throw new OWLSWriterException("Process list is null");

        //init ontmodel to write process model
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

        for (int i = 0; i < processList.size(); i++) {
            writeProcess(processList.getNthProcess(i), baseModel);
        }
        System.gc();
        RDFWriter writer = OWLSWriterUtils.getWriter(baseModel, base);
        writer.write(baseModel.getBaseModel(), out, base);
        return baseModel;
    }

    public Individual writeProcess(Process process, OntModel ontModel) throws OWLSWriterException {

        if (process == null)
            throw new OWLSWriterException("Process is null");

        //check if the process with that uri already exist in the ontModel
        Individual processInd = OWLSWriterUtils.checkIfIndividualExist(process, ontModel);
        if (processInd != null)
            return processInd;

        processInd = null;
        if (process instanceof AtomicProcess)
            processInd = writeAtomicProcess((AtomicProcess) process, ontModel);
        else if (process instanceof CompositeProcess)
            processInd = writeCompositeProcess((CompositeProcess) process, ontModel);
        else if (process instanceof SimpleProcess)
            processInd = writeSimpleProcess((SimpleProcess) process, ontModel);
        else
            logger.debug("process type " + process.getClass());
        //Inputs
        InputList ipList = process.getInputList();
        if (ipList != null) {
            for (int i = 0; i < ipList.size(); i++) {
                Input input = ipList.getNthInput(i);
                writeInput(input, processInd, ontModel);
            }
        }

        //Outputs
        OutputList opList = process.getOutputList();
        if (opList != null) {
            for (int i = 0; i < opList.size(); i++) {
                Output output = opList.getNthOutput(i);
                writeOutput(output, processInd, ontModel);
            }
        }

        //Preconditions
        PreConditionList pcList = process.getPreConditionList();
        if (pcList != null) {
            for (int i = 0; i < pcList.size(); i++) {
                Condition cond = pcList.getNthPreCondition(i);
                writePreCondition(cond, processInd, ontModel);
            }
        }

        //Locals
        LocalList localList = process.getLocalList();
        logger.debug("XXXXXXXXXXXXXXXXX");
        if (localList != null) {
        	logger.debug("XXXXXXXXXXXXXXXXX");
            for (int i = 0; i < localList.size(); i++) {
                Local local = localList.getNthLocal(i);
                writeLocal(local, processInd, ontModel);
            }
        }
        
        //Result
        ResultList resultList = process.getResultList();
        if (resultList != null) {
            for (int i = 0; i < resultList.size(); i++) {
                Result result = resultList.getNthResult(i);
                writeResult(result, processInd, ontModel);
            }
        }
        return processInd;
    }

    public Individual writeSimpleProcess(SimpleProcess process, OntModel ontModel) throws OWLSWriterException {

        if (process == null)
            throw new OWLSWriterException("SimpleProcess is null");

        //check if the process with that uri already exist in the ontModel
        Individual processInd = OWLSWriterUtils.checkIfIndividualExist(process, ontModel);
        if (processInd != null)
            return processInd;

        OntClass simpleClass = ontModel.getOntClass(OWLSProcessURI.SimpleProcess);
        Individual ind = OWLSWriterUtils.createIndividual(simpleClass, process, baseURL);

        if (process.getRealizedBy() != null)
            writeAtomicProcess(process.getRealizedBy(), ind, ontModel);

        if (process.getExpandsTo() != null)
            writeCompositeProcess(process.getExpandsTo(), ind, ontModel);

        return ind;
    }

    public Individual writeCompositeProcess(CompositeProcess process, Individual simpleProcesInst, OntModel ontModel)
            throws OWLSWriterException {
        Individual individual = writeCompositeProcess(process, ontModel);
        simpleProcesInst.addProperty(OWLSProcessProperties.expandsTo, individual);
        return individual;
    }

    public Individual writeCompositeProcess(CompositeProcess process, OntModel ontModel) throws OWLSWriterException {

        if (process == null)
            throw new OWLSWriterException("CompositeProcess is null");

        //check if the CompositeProcess with that uri already exist in the
        // ontModel
        Individual processInd = OWLSWriterUtils.checkIfIndividualExist(process, ontModel);
        if (processInd != null)
            return processInd;

        OntClass compositeClass = ontModel.getOntClass(OWLSProcessURI.CompositeProcess);
        processInd = OWLSWriterUtils.createIndividual(compositeClass, process, baseURL);

        //ComposedOf
        ControlConstruct control = process.getComposedOf();
        Individual ind = writeControlConstruct(control, ontModel);
        processInd.addProperty(OWLSProcessProperties.composedOf, ind);

        return processInd;
    }

    public Individual writeControlConstruct(ControlConstruct control, OntModel ontModel) throws OWLSWriterException {

        if (control == null)
            throw new OWLSWriterException("ControlConstruct is null");

        //check if the ControlConstruct already exist in the ontModel
        Individual controlInd = OWLSWriterUtils.checkIfIndividualExist(control, ontModel);
        if (controlInd != null)
            return controlInd;

        if (control instanceof Perform) {
            controlInd = writePerform((Perform) control, ontModel);
        } else if (control instanceof Sequence) {
            OntClass seqClass = ontModel.getOntClass(OWLSProcessURI.Sequence);
            controlInd = OWLSWriterUtils.createIndividual(seqClass, control, baseURL);
        } else if (control instanceof Choice) {
            OntClass choiceClass = ontModel.getOntClass(OWLSProcessURI.Choice);
            controlInd = OWLSWriterUtils.createIndividual(choiceClass, control, baseURL);
        } else if (control instanceof Split) {
            OntClass splitClass = ontModel.getOntClass(OWLSProcessURI.Split);
            controlInd = OWLSWriterUtils.createIndividual(splitClass, control, baseURL);
        } else if (control instanceof SplitJoin) {
            OntClass splitJoinClass = ontModel.getOntClass(OWLSProcessURI.Split_JOIN);
            controlInd = OWLSWriterUtils.createIndividual(splitJoinClass, control, baseURL);
        }else if (control instanceof AnyOrder) {
            OntClass anyOrderClass = ontModel.getOntClass(OWLSProcessURI.AnyOrder);
            controlInd = OWLSWriterUtils.createIndividual(anyOrderClass, control, baseURL);
        }else if (control instanceof IfThenElse) {
            controlInd = writeIfThenElse((IfThenElse) control, ontModel);
        } else {
        	logger.debug("UnHandled Class  " + control.getClass());
        }
        ControlConstructList ccList = control.getComponents();

        if (ccList != null) {
            Individual controlListInd = writeControlConstructList(ccList, ontModel);
            controlInd.addProperty(OWLSProcessProperties.components, controlListInd);
        }
        return controlInd;
    }

    public Individual writeIfThenElse(IfThenElse ifthenelse, OntModel ontModel) throws OWLSWriterException {

        if (ifthenelse == null)
            throw new OWLSWriterException("ifthenelse is null");

        //check if the Expression with that uri already exist in the ontModel
        Individual ifInd = OWLSWriterUtils.checkIfIndividualExist(ifthenelse, ontModel);
        if (ifInd != null)
            return ifInd;

        OntClass ifThenElseClass = ontModel.getOntClass(OWLSProcessURI.If_Then_Else);
        ifInd = OWLSWriterUtils.createIndividual(ifThenElseClass, ifthenelse, baseURL);

        //If condition
        Condition ifCond = ifthenelse.getIfCondition();
        Individual ifCondInd = writeCondition(ifCond, ontModel);

        ifInd.addProperty(OWLSProcessProperties.ifCondition, ifCondInd);

        //Then part
        ControlConstruct thenCC = ifthenelse.getThen();
        Individual thenCCInd = writeControlConstruct(thenCC, ontModel);
        ifInd.addProperty(OWLSProcessProperties.then, thenCCInd);

        //Else part
        ControlConstruct elseCC = ifthenelse.getElse();
        if (elseCC != null) {
            Individual elseCCInd = writeControlConstruct(elseCC, ontModel);
            ifInd.addProperty(OWLSProcessProperties.process_else, elseCCInd);
        }
        return ifInd;
    }

    public Individual writeControlConstructList(ControlConstructList controlList, OntModel ontModel) throws OWLSWriterException {

        if (controlList == null)
            throw new OWLSWriterException("ControlConstructList is null");

        //check if the controlList with that uri already exist in the ontModel
        Individual controlListInd = OWLSWriterUtils.checkIfIndividualExist(controlList, ontModel);
        if (controlListInd != null)
            return controlListInd;
        
        
        OntClass ccListClass = ontModel.getOntClass(OWLSProcessURI.ControlConstructList);
        controlListInd = OWLSWriterUtils.createIndividual(ccListClass, controlList, baseURL);

        OWLS_Object object = controlList.getFirst();

        if (object instanceof ControlConstruct) {
            Individual ccInd = writeControlConstruct((ControlConstruct) object, ontModel);
            controlListInd.addProperty(OWLSProcessProperties.shadow_rdf_first, ccInd);
        } else {
            throw new OWLSWriterException("OWLS_Object " + object.getURI() + " not an instance of control construct");
        }

        ControlConstructList restList = (ControlConstructList) controlList.getRest();

        if (restList != null) {
            Individual ccListInd = writeControlConstructList(restList, ontModel);
            controlListInd.addProperty(OWLSProcessProperties.shadow_rdf_rest, ccListInd);
        } else {
            Individual nilList = ontModel.getIndividual(OWLSProcessURI.shadow_rdf_nil);
            controlListInd.addProperty(OWLSProcessProperties.shadow_rdf_rest, nilList);
        }

        return controlListInd;
    }

    public Individual writeAtomicProcess(AtomicProcess process, Individual simpleProcesInst, OntModel ontModel)
            throws OWLSWriterException {
        Individual individual = writeAtomicProcess(process, ontModel);
        simpleProcesInst.addProperty(OWLSProcessProperties.realizedBy, individual);
        return individual;
    }

    public Individual writeAtomicProcess(AtomicProcess process, OntModel ontModel) throws OWLSWriterException {

        if (process == null)
            throw new OWLSWriterException("AtomicProcess is null");

        //check if the AtomicProcess with that uri already exist in the
        // ontModel
        Individual processInd = OWLSWriterUtils.checkIfIndividualExist(process, ontModel);
        if (processInd != null)
            return processInd;

        OntClass atomicClass = ontModel.getOntClass(OWLSProcessURI.AtomicProcess);

        processInd = OWLSWriterUtils.createIndividual(atomicClass, process, baseURL);

        if (process.getName() != null)
            processInd.addProperty(OWLSProcessProperties.processName, process.getName());

        return processInd;
    }

    public Individual writeInput(Input input, Individual processInst, OntModel ontModel) throws OWLSWriterException {

        Individual individual = writeInput(input, ontModel);
        processInst.addProperty(OWLSProcessProperties.hasInput, individual);
        return individual;

    }

    public Individual writeInput(Input input, OntModel ontModel) throws OWLSWriterException {
    	return writeParameter(input,ontModel);
        /*

        if (input == null)
            throw new OWLSWriterException("input is null");

        //check if the input with that uri already exist in the ontModel
        Individual inputInd = OWLSWriterUtils.checkIfIndividualExist(input, ontModel);
        if (inputInd != null)
            return inputInd;

        OntClass inputClass = ontModel.getOntClass(OWLSProcessURI.Input);

        inputInd = OWLSWriterUtils.createIndividual(inputClass, input, baseURL);

        if (input.getParameterType() != null) {
            Literal dt = ontModel.createTypedLiteral(input.getParameterType(), XSDDatatype.XSDanyURI);
            inputInd.addProperty(OWLSProcessProperties.parameterType, dt);
        } else
            throw new OWLSWriterException(input.getURI() + " : parameterType is missing");

        if (input.getParameterValue() != null)
            inputInd.addProperty(OWLSProcessProperties.parameterValue, input.getParameterValue());
        else
            logger.warn"Warning: No Parameter Value");

        return inputInd;
        */

    }

    public Individual writeOutput(Output output, Individual processInst, OntModel ontModel) throws OWLSWriterException {
        Individual individual = writeOutput(output, ontModel);
        processInst.addProperty(OWLSProcessProperties.hasOutput, individual);
        return individual;
    }

    public Individual writeOutput(Output output, OntModel ontModel) throws OWLSWriterException {
        	return writeParameter(output,ontModel);
        /*
        if (output == null)
            throw new OWLSWriterException("Output is null");

        //check if the cond with that uri already exist in the ontModel
        Individual outputInd = OWLSWriterUtils.checkIfIndividualExist(output, ontModel);
        if (outputInd != null)
            return outputInd;

        OntClass outputClass = ontModel.getOntClass(OWLSProcessURI.Output);

        outputInd = OWLSWriterUtils.createIndividual(outputClass, output, baseURL);

        if (output.getParameterType() != null) {
            Literal dt = ontModel.createTypedLiteral(output.getParameterType(), XSDDatatype.XSDanyURI);
            outputInd.addProperty(OWLSProcessProperties.parameterType, dt);
        } else
            throw new OWLSWriterException(output.getURI() + " : parameterType is missing");

        if (output.getParameterValue() != null)
            outputInd.addProperty(OWLSProcessProperties.parameterValue, output.getParameterValue());
        else
            logger.warn("Warning: No Parameter Value");

        return outputInd;
        */
    }

    public Individual writePreCondition(Condition cond, Individual processInd, OntModel ontModel) throws OWLSWriterException {
        Individual individual = writeCondition(cond, ontModel);
        processInd.addProperty(OWLSProcessProperties.hasPrecondition, individual);
        return individual;
    }

    public Individual writeLocal(Local local, Individual processInd, OntModel ontModel) throws OWLSWriterException {
        Individual individual = writeLocal(local, ontModel);
        processInd.addProperty(OWLSProcessProperties.hasLocal, individual);
        return individual;
    }
    
    public Individual writeLocal(Local local, OntModel ontModel) throws OWLSWriterException {
        return writeParameter(local,ontModel);
    }

    public Individual writeParameter(Parameter parameter, OntModel ontModel) throws OWLSWriterException {

        if (parameter == null)
            throw new OWLSWriterException(parameter.getClass().getName() + " is null");

        //check if the parameter with that uri already exist in the ontModel
        Individual parameterInd = OWLSWriterUtils.checkIfIndividualExist(parameter, ontModel);
        if (parameterInd != null)
            return parameterInd;

        String parameterType=null;
        if(parameter instanceof Input){
            parameterType = OWLSProcessURI.Input;
        }else if(parameter instanceof Output){
            parameterType = OWLSProcessURI.Output;
        }else if(parameter instanceof Local){
            parameterType = OWLSProcessURI.Local;
        }else if(parameter instanceof ResultVar){
            parameterType = OWLSProcessURI.ResultVar;
        }else
            throw new OWLSWriterException("Found unknown Paramater Type " + parameter.getClass().getName());               
        
        OntClass parameterClass = ontModel.getOntClass(parameterType);

        parameterInd = OWLSWriterUtils.createIndividual(parameterClass, parameter, baseURL);

        if (parameter.getParameterType() != null) {
            Literal dt = ontModel.createTypedLiteral(parameter.getParameterType(), XSDDatatype.XSDanyURI);
            parameterInd.addProperty(OWLSProcessProperties.parameterType, dt);
        } else
            throw new OWLSWriterException(parameter.getURI() + " : parameterType is missing");

        if (parameter.getParameterValue() != null)
            parameterInd.addProperty(OWLSProcessProperties.parameterValue, parameter.getParameterValue());
        else
        	logger.debug("Warning: No Parameter Value");

        return parameterInd;
    }

    
    public Individual writeInCondition(Condition cond, Individual resultInd, OntModel ontModel) throws OWLSWriterException {
        Individual individual = writeCondition(cond, ontModel);
        resultInd.addProperty(OWLSProcessProperties.inCondition, individual);
        return individual;
    }

    public Individual writeCondition(Condition cond, OntModel ontModel) throws OWLSWriterException {

        if (cond == null)
            throw new OWLSWriterException("Condition is null");

        //check if the cond with that uri already exist in the ontModel
        Individual condInd = OWLSWriterUtils.checkIfIndividualExist(cond, ontModel);
        if (condInd != null)
            return condInd;

        OntClass condClass = ontModel.getOntClass(OWLSProcessURI.Condition);
        condInd = OWLSWriterUtils.createIndividual(condClass, cond, baseURL);

        //dealing with expression language
        Individual logicLang = writeLogicLanguage(cond.getExpressionLanguage(), ontModel);
        condInd.addProperty(OWLSProcessProperties.expressionLanguage, logicLang);

        //dealing with expression body
        Literal literal = ontModel.createLiteral(cond.getExpressionBody());
        condInd.addProperty(OWLSProcessProperties.expressionBody, literal);

        return condInd;
    }

    public Individual writeLogicLanguage(LogicLanguage language, OntModel ontModel) throws OWLSWriterException {

        if (language == null)
            throw new OWLSWriterException("LogicLanguage is null");

        //logger.debug("ll ind " + language.getURI());
        //check if the input with that uri already exist in the ontModel
        Individual languageInd = OWLSWriterUtils.checkIfIndividualExist(language, ontModel);
        if (languageInd != null)
            return languageInd;

        OntClass llClass = ontModel.getOntClass(OWLSProcessURI.LogicLanguage);
        languageInd = OWLSWriterUtils.createIndividual(llClass, language, baseURL);
        languageInd.addProperty(OWLSProcessProperties.refURI, language.getRefURI());
        return languageInd;

    }

    public Individual writeResult(Result result, Individual processInst, OntModel ontModel) throws OWLSWriterException {
        Individual individual = writeResult(result, ontModel);
        processInst.addProperty(OWLSProcessProperties.hasResult, individual);
        return individual;

    }

    public Individual writeResult(Result result, OntModel ontModel) throws OWLSWriterException {

        if (result == null)
            throw new OWLSWriterException("Result is null");

        //check if the result with that uri already exist in the ontModel
        Individual resultInd = OWLSWriterUtils.checkIfIndividualExist(result, ontModel);
        if (resultInd != null)
            return resultInd;

        OntClass resultClass = ontModel.getOntClass(OWLSProcessURI.Result);
        resultInd = OWLSWriterUtils.createIndividual(resultClass, result, baseURL);

        //fixed the property inCondition, now a result can have more than one
        // inCondition property
        //Individual individual = writeCondition(condition, ontModel);
        //resultInd.addProperty(OWLSProcessProperties.inCondition, individual);
        //incondition
        ConditionList conditions = result.getInCondition();
        if (conditions != null) {
            for (int i = 0; i < conditions.size(); i++) {
                Condition cond = conditions.getNthCondition(i);
                writeInCondition(cond, resultInd, ontModel);
            }
        }

        //ResultVar
        ResultVarList resultVarList = result.getResultVarList();
        if(resultVarList != null){
            for(int i=0;i<resultVarList.size();i++){
                ResultVar resultVar = resultVarList.getNthResultVar(i);
                writeResultVar(resultVar,resultInd,ontModel);
            }
        }
        

        //Effects
        EffectList list = result.getHasEffects();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Expression exp = list.getNthEffect(i);
                writeHasEffect(exp, resultInd, ontModel);
            }
        }
        //outputs
        BindingList bindingList = result.getWithOutputs();
        if (bindingList != null) {
            for (int i = 0; i < bindingList.size(); i++) {
                Binding binding = bindingList.getNthBindingDescription(i);
                Individual bind = writeOutputBinding(binding, ontModel);
//                Individual bind = writeBinding(binding, ontModel);
                resultInd.addProperty(OWLSProcessProperties.withOutput, bind);
            }
        }
        return resultInd;
    }

    private Individual writeResultVar(ResultVar resultVar, Individual resultInd, OntModel ontModel) throws OWLSWriterException {
   
        Individual resultVarInd = writeResultVar(resultVar, ontModel);
        resultInd.addProperty(OWLSProcessProperties.hasResultVar, resultVarInd);
        return resultVarInd;
    
    }

    private Individual writeResultVar(ResultVar resultVar, OntModel ontModel) throws OWLSWriterException {
        return writeParameter(resultVar,ontModel);
    }

    public Individual writeHasDataFrom(Binding binding, Individual performInd, OntModel ontModel) throws OWLSWriterException {
        Individual bind = writeBinding(binding, ontModel);
        performInd.addProperty(OWLSProcessProperties.hasDataFrom, bind);
        return bind;
    }

    public Individual writeWithOutput(Binding binding, Individual resultInd, OntModel ontModel) throws OWLSWriterException {

//        Individual bindingInd = writeBinding(binding, ontModel);
        Individual bindingInd = writeOutputBinding(binding, ontModel);
        resultInd.addProperty(OWLSProcessProperties.withOutput, bindingInd);
        return bindingInd;
    }
    
    public Individual writeBinding(Binding binding, OntModel ontModel) throws OWLSWriterException {
    	return writeBinding(binding, OWLS_Builder_Util.BINDING, ontModel);
    }

    public Individual writeInputBinding(Binding binding, OntModel ontModel) throws OWLSWriterException {
    	return writeBinding(binding, OWLS_Builder_Util.INPUT_BINDING, ontModel);
    }

    public Individual writeOutputBinding(Binding binding, OntModel ontModel) throws OWLSWriterException {
    	return writeBinding(binding, OWLS_Builder_Util.OUTPUT_BINDING, ontModel);
    }

    protected Individual writeBinding(Binding binding, int type, OntModel ontModel) throws OWLSWriterException {

        if (binding == null)
            throw new OWLSWriterException("Binding is null");

        //check if the binding with that uri already exist in the ontModel
        Individual bindingInd = OWLSWriterUtils.checkIfIndividualExist(binding, ontModel);
        if (bindingInd != null)
            return bindingInd;

        OntClass bindClass = null;
        switch (type) {
        case OWLS_Builder_Util.INPUT_BINDING:
        	bindClass = ontModel.getOntClass(OWLSProcessURI.InputBinding);
        	break;
        case OWLS_Builder_Util.OUTPUT_BINDING:
        	bindClass = ontModel.getOntClass(OWLSProcessURI.OutputBinding);
        	break;
        default:
        	bindClass = ontModel.getOntClass(OWLSProcessURI.Binding);
        	break;
        }
        
        Individual bindInd = OWLSWriterUtils.createIndividual(bindClass, binding, baseURL);

        //toParam
        Parameter parameter = binding.getToParam();

        Individual paramInd = writeParameter(parameter, ontModel);
        bindInd.addProperty(OWLSProcessProperties.toParam, paramInd);

        //valueSource
        ValueOf valueOf = binding.getValueSource();
        if (valueOf != null) {
            Individual valueOfInd = writeValueOf(valueOf, ontModel);
            bindInd.addProperty(OWLSProcessProperties.valueSource, valueOfInd);
            return bindInd;
        }
        
        // valueData
        String valueData = binding.getValueData();
        if (valueData != null) {
            bindInd.addProperty(OWLSProcessProperties.valueData, valueData);
            return bindInd;
        }

        // valueType
        String valueType = binding.getValueType();
        if (valueType != null) {
            bindInd.addProperty(OWLSProcessProperties.valueType, valueType);
            return bindInd;
        }

        return bindInd;
    }

    public Individual writeValueOf(ValueOf valueOf, OntModel ontModel) throws OWLSWriterException {

        if (valueOf == null)
            throw new OWLSWriterException("valueOf is null");

        //check if the valueOf with that uri already exist in the ontModel
        Individual valueOfInd = OWLSWriterUtils.checkIfIndividualExist(valueOf, ontModel);
        if (valueOfInd != null)
            return valueOfInd;

        OntClass valueOfClass = ontModel.getOntClass(OWLSProcessURI.ValueOf);

        valueOfInd = OWLSWriterUtils.createIndividual(valueOfClass, valueOf, baseURL);

        Perform perform = valueOf.getFromProcess();
        Individual performInd;

        //Check if the perform is ParentPerform, ThisPerform or regular process
        if (perform.getURI().equals(OWLSProcessURI.TheParentPerform)) {
            performInd = ontModel.getIndividual(OWLSProcessURI.TheParentPerform);
        } else if (perform.getURI().equals(OWLSProcessURI.ThisPerform)) {
            performInd = ontModel.getIndividual(OWLSProcessURI.ThisPerform);
        } else {
            performInd = OWLSProcessWriter.writePerform(perform, ontModel);
        }

        valueOfInd.addProperty(OWLSProcessProperties.fromProcess, performInd);

        Parameter theVar = valueOf.getTheVar();
        Individual theVarInd = writeParameter(theVar, ontModel);
        valueOfInd.addProperty(OWLSProcessProperties.theVar, theVarInd);

        return valueOfInd;
    }

    public Individual writePerform(Perform perform, OntModel ontModel) throws OWLSWriterException {

        if (perform == null)
            throw new OWLSWriterException("perform is null");

        //check if the Perform with that uri already exist in the ontModel
        Individual performInd = OWLSWriterUtils.checkIfIndividualExist(perform, ontModel);
        if (performInd != null) {
            logger.debug("Reusing - Individual " + performInd.getURI() + " already exist.");
            return performInd;
        }

        OntClass performClass = ontModel.getOntClass(OWLSProcessURI.Perform);
        performInd = OWLSWriterUtils.createIndividual(performClass, perform, baseURL);

        Process process = perform.getProcess();
        Individual processInd = OWLSProcessWriter.writeProcess(process, ontModel);
        performInd.addProperty(OWLSProcessProperties.processPerformProperty, processInd);

        BindingList bindingList = perform.getBinding();
        if (bindingList != null) {
            for (int i = 0; i < bindingList.size(); i++) {
                Binding binding = bindingList.getNthBindingDescription(i);
                writeHasDataFrom(binding, performInd, ontModel);
            }
        }
        return performInd;
    }

    /*
    public Individual writeParameter(Parameter parameter, OntModel ontModel) throws OWLSWriterException {

        if (parameter == null)
            throw new OWLSWriterException("parameter is null");

        //check if the parameter with that uri already exist in the ontModel
        Individual parameterInd = OWLSWriterUtils.checkIfIndividualExist(parameter, ontModel);
        if (parameterInd != null)
            return parameterInd;

        if (parameter instanceof Input)
            return OWLSProcessWriter.writeInput((Input) parameter, ontModel);

        if (parameter instanceof Output)
            return OWLSProcessWriter.writeOutput((Output) parameter, ontModel);

        return null;
    }
*/
    public Individual writeHasEffect(Expression exp, Individual resultInd, OntModel model) throws OWLSWriterException {

        Individual expInd = writeExpression(exp, model);
        resultInd.addProperty(OWLSProcessProperties.hasEffect, expInd);
        return expInd;
    }

    public Individual writeExpression(Expression exp, OntModel ontModel) throws OWLSWriterException {

        if (exp == null)
            throw new OWLSWriterException("Expression is null");

        //check if the Expression with that uri already exist in the ontModel
        Individual expInd = OWLSWriterUtils.checkIfIndividualExist(exp, ontModel);
        if (expInd != null)
            return expInd;

        OntClass expClass = ontModel.getOntClass(OWLSProcessURI.Expression);
        expInd = OWLSWriterUtils.createIndividual(expClass, exp, baseURL);

        //dealing with expression language
        Individual logicLang = writeLogicLanguage(exp.getExpressionLanguage(), ontModel);
        expInd.addProperty(OWLSProcessProperties.expressionLanguage, logicLang);

        //dealing with expression body
        Literal literal = ontModel.createLiteral(exp.getExpressionBody());
        expInd.addProperty(OWLSProcessProperties.expressionBody, literal);

        return expInd;

    }

}
