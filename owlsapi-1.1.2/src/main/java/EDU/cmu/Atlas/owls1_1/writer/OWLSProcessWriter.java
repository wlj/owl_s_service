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

import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.Expression;
import EDU.cmu.Atlas.owls1_1.expression.LogicLanguage;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.process.Binding;
import EDU.cmu.Atlas.owls1_1.process.CompositeProcess;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.process.IfThenElse;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.Local;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.SimpleProcess;
import EDU.cmu.Atlas.owls1_1.process.ValueOf;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * Implementation of this class was moved to @see EDU.cmu.Atlas.owls1_1.writer.OWLSProcessWriterDynamic .
 * Now, it only delegates all call to this class.
 * @author Naveen Srinivasan
 */
public class OWLSProcessWriter {

    private static OWLSProcessWriterDynamic dw = new OWLSProcessWriterDynamic(); 
    
    protected static OntModel init(String base) {
         return dw.init(base);
    }

    protected static void setBaseURL(String base) {
        dw.setBaseURL(base);
    }

    public static void write(Process process, String base, OutputStream out) throws OWLSWriterException {
        dw.write(process, base, out);
    }

    public static void write(Process process, String base, String imports[], OutputStream out) throws OWLSWriterException {
        dw.write(process, base, imports, out);
    }

    public static void write(Process process, String base, String imports[], OntModel submodel, OutputStream out)
            throws OWLSWriterException {
    	dw.write(process, base, imports, submodel,  out);
    }

    public static void write(ProcessList processList, String base, OutputStream out) throws IndexOutOfBoundsException, OWLSWriterException {
        dw.write(processList, base, null, out);
    }

    public static void write(ProcessList processList, String base, String[] imports, OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        dw.write(processList, base, imports, null, out);
    }

    public static void write(ProcessList processList, String base, String[] imports, OntModel submodel, OutputStream out) 
    		throws IndexOutOfBoundsException, OWLSWriterException {
    	dw.writeModel(processList, base, imports, submodel, out);    	
    }
    
    public static OntModel writeModel(ProcessList processList, String base, String[] imports, OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {
    	return dw.writeModel(processList, base, imports, submodel, out);
    }

    public static Individual writeProcess(Process process, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeProcess(process, ontModel);
    }

    public static Individual writeSimpleProcess(SimpleProcess process, OntModel ontModel) throws OWLSWriterException {

        return dw.writeSimpleProcess(process, ontModel);
    }

    public static Individual writeCompositeProcess(CompositeProcess process, Individual simpleProcesInst, OntModel ontModel)
            throws OWLSWriterException {
        return dw.writeCompositeProcess(process, simpleProcesInst, ontModel);
    }

    public static Individual writeCompositeProcess(CompositeProcess process, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeCompositeProcess(process, ontModel);
    }

    public static Individual writeControlConstruct(ControlConstruct control, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeControlConstruct(control, ontModel);
    }

    public static Individual writeIfThenElse(IfThenElse ifthenelse, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeIfThenElse(ifthenelse, ontModel);
    }

    public static Individual writeControlConstructList(ControlConstructList controlList, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeControlConstructList(controlList, ontModel);
    }

    public static Individual writeAtomicProcess(AtomicProcess process, Individual simpleProcesInst, OntModel ontModel)
            throws OWLSWriterException {
    	return dw.writeAtomicProcess(process, simpleProcesInst, ontModel);
    }

    public static Individual writeAtomicProcess(AtomicProcess process, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeAtomicProcess(process, ontModel);
    }

    public static Individual writeInput(Input input, Individual processInst, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeInput(input, processInst, ontModel);
    }

    public static Individual writeInput(Input input, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeInput(input, ontModel);
    }

    public static Individual writeOutput(Output output, Individual processInst, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeOutput(output, processInst, ontModel);
    }

    public static Individual writeOutput(Output output, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeOutput(output, ontModel);
    }

    public static Individual writePreCondition(Condition cond, Individual processInd, OntModel ontModel) throws OWLSWriterException {
    	return dw.writePreCondition(cond, processInd, ontModel);
    }

    public static Individual writeLocal(Local local, Individual processInd, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeLocal(local, processInd, ontModel);
    }
    
    public static Individual writeLocal(Local local, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeLocal(local, ontModel);
    }

    public static Individual writeParameter(Parameter parameter, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeParameter(parameter, ontModel);
    }

    
    public static Individual writeInCondition(Condition cond, Individual resultInd, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeInCondition(cond, resultInd, ontModel);
    }

    public static Individual writeCondition(Condition cond, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeCondition(cond, ontModel);
    }

    public static Individual writeLogicLanguage(LogicLanguage language, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeLogicLanguage(language, ontModel);
    }

    public static Individual writeResult(Result result, Individual processInst, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeResult(result, processInst, ontModel);
    }

    public static Individual writeResult(Result result, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeResult(result, ontModel);
    }


    public static Individual writeHasDataFrom(Binding binding, Individual performInd, OntModel ontModel) throws OWLSWriterException {
        return dw.writeHasDataFrom(binding, performInd, ontModel);
    }

    public static Individual writeWithOutput(Binding binding, Individual resultInd, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeWithOutput(binding, resultInd, ontModel);
    }

    public static Individual writeBinding(Binding binding, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeBinding(binding, ontModel);
    }

    public static Individual writeInputBinding(Binding binding, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeInputBinding(binding, ontModel);
    }

    public static Individual writeOutputBinding(Binding binding, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeOutputBinding(binding, ontModel);
    }

    public static Individual writeValueOf(ValueOf valueOf, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeValueOf(valueOf, ontModel);
    }

    public static Individual writePerform(Perform perform, OntModel ontModel) throws OWLSWriterException {
    	return dw.writePerform(perform, ontModel);
    }

    public static Individual writeHasEffect(Expression exp, Individual resultInd, OntModel model) throws OWLSWriterException {
    	return dw.writeHasEffect(exp, resultInd, model);
    }

    public static Individual writeExpression(Expression exp, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeExpression(exp, ontModel);

    }

}