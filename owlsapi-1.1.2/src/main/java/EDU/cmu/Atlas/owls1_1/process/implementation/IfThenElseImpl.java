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
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfControlConstructException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfExpressionException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.IfThenElse;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

import com.hp.hpl.jena.ontology.Individual;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Massimo Paolucci
 *  
 */
public class IfThenElseImpl extends ControlConstructImpl implements IfThenElse {

    private Condition ifCondition;

    private ControlConstruct thenControlConstruct;

    private ControlConstruct elseControlConstruct;

    public IfThenElseImpl(Individual instance)
            throws NotInstanceOfControlConstructException {

        super(instance);

        Individual ifcond;
        try {
            ifcond = OWLUtil.getInstanceFromFunctionalProperty(instance, OWLSProcessProperties.ifCondition);
        } catch (PropertyNotFunctional e) {
            throw new NotInstanceOfControlConstructException(e);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfControlConstructException(getURI() + " : If condition should of type condition", e);
        }

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        if (ifcond != null)
            try {
                ifCondition = builder.createCondition(ifcond);
            } catch (NotInstanceOfExpressionException e1) {
                throw new NotInstanceOfControlConstructException(getURI(), e1);
            }
        else
            throw new NotInstanceOfControlConstructException(getURI() + " : condition part should not be null");

        //Then part
        Individual thenInd;
        try {
            thenInd = OWLUtil.getInstanceFromFunctionalProperty(instance, OWLSProcessProperties.then);
        } catch (PropertyNotFunctional e) {
            throw new NotInstanceOfControlConstructException(e);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfControlConstructException(
                    getURI() + " : Then part should of type control construct", e);
        }

        if (thenInd != null)
            thenControlConstruct = builder.createControlConstruct(thenInd);
        else
            throw new NotInstanceOfControlConstructException(getURI() + " : then part should not be null");

        //Else part
        Individual elseInd;
        try {
            elseInd = OWLUtil.getInstanceFromProperty(instance, OWLSProcessProperties.process_else);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfControlConstructException(
                    getURI() + " : else part should of type control construct", e);
        }

        if (elseInd != null)
            elseControlConstruct = builder.createControlConstruct(elseInd);

    }

    public IfThenElseImpl() {
        super();
    }
    public IfThenElseImpl(String uri) {
        super(uri);
    }
    public Condition getIfCondition() {
        return ifCondition;
    }

    public void setIfCondition(Condition condition) {
        ifCondition = condition;
    }

    public ControlConstruct getThen() {
        return thenControlConstruct;
    }

    public void setThen(ControlConstruct controlConstruct) {
        thenControlConstruct = controlConstruct;
    }

    public ControlConstruct getElse() {
        return elseControlConstruct;
    }

    public void setElse(ControlConstruct controlConstruct) {
        elseControlConstruct = controlConstruct;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nIf-Then-Else Process : ");
        sb.append(getURI());
        sb.append("\nCondition : ");
        sb.append(ifCondition);
        sb.append("\nThen : ");
        sb.append(thenControlConstruct);
        sb.append("\nElse : ");
        sb.append(elseControlConstruct);
        return sb.toString();
    }

    public String toString(String indent) {
        return toString();
    }
}