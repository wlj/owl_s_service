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

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfPerformException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfValueOfException;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.ValueOf;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

import com.hp.hpl.jena.ontology.Individual;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Massimo Paolucci
 */
public class ValueOfImpl extends OWLS_ObjectImpl implements ValueOf {

    /** store for the variable that records the incoming values */
    private Parameter theVar;

    private Perform fromProcess;

    /**
     * @param individual
     * @throws NotInstanceOfValueOfException
     */
    public ValueOfImpl(Individual individual) throws NotInstanceOfValueOfException {
        super(individual);

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        //Extracting the Var
        Individual theVarInd;
        try {
            theVarInd = OWLUtil.getInstanceFromFunctionalProperty(individual, OWLSProcessProperties.theVar);
        } catch (NotAnIndividualException e1) {
            throw new NotInstanceOfValueOfException(e1);
        } catch (PropertyNotFunctional e1) {
            throw new NotInstanceOfValueOfException(e1);
        }
        if (theVarInd != null) {
            try {
                setTheVar(builder.createParameter(theVarInd));
            } catch (NotInstanceOfParameterException e) {
                throw new NotInstanceOfValueOfException(individual.getURI() + " :Property 'theVar' is not an instance of Parameter", e);
            }
        } else
            throw new NotInstanceOfValueOfException(individual.getURI() + " : Property 'theVar' is missing");

        //Extracting parameterValue
        Individual fromProcInd;
        try {
            fromProcInd = OWLUtil.getInstanceFromFunctionalProperty(individual, OWLSProcessProperties.fromProcess);
        } catch (NotAnIndividualException e2) {
            throw new NotInstanceOfValueOfException(e2);
        } catch (PropertyNotFunctional e2) {
            throw new NotInstanceOfValueOfException(e2);
        }
        if (fromProcInd != null) {
            try {
                //logger.debug("XXXXXXXXX" + fromProcInd.getURI());
                setFromProcess(builder.createPerform(fromProcInd));
            } catch (NotInstanceOfPerformException e) {
                throw new NotInstanceOfValueOfException(individual.getURI() + " :Property 'fromProcess' is not an instance of Perform", e);
            }
        } else
            throw new NotInstanceOfValueOfException(individual.getURI() + " : Property 'fromProcess' is missing");

    }

    public ValueOfImpl(String uri) {
        super(uri);
    }

    public ValueOfImpl() {
    }

    /**
     * @return
     */
    public Parameter getTheVar() {
        return theVar;
    }

    /**
     * @param parameter
     */
    public void setTheVar(Parameter parameter) {
        theVar = parameter;
    }

    /**
     * @return
     */
    public Perform getFromProcess() {
        return fromProcess;
    }

    /**
     * @param perform
     */
    public void setFromProcess(Perform perform) {
        fromProcess = perform;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        StringBuffer output = new StringBuffer(indent);
        output.append("\nValueOf");
        output.append(indent);
        output.append("\nProcess");
        output.append(indent + "\t");
        output.append(fromProcess.getURI());
        output.append(indent);
        output.append("\nParameter");
        output.append(indent + "\t");
        output.append(theVar);
        return output.toString();
    }

}