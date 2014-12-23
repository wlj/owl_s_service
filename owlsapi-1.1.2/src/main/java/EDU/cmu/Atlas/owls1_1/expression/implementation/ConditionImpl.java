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
package EDU.cmu.Atlas.owls1_1.expression.implementation;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfExpressionException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;

import com.hp.hpl.jena.ontology.Individual;

/**
 * @author Naveen Srinivasan
 */
public class ConditionImpl extends ExpressionImpl implements Condition {

    public ConditionImpl(Individual individual) throws NotInstanceOfExpressionException {
        super(individual);
    }

    public ConditionImpl(String uri) {
        super(uri);
    }

    public ConditionImpl() {
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nCondition :");
        sb.append(getURI());
        sb.append("\nexpressionLanguage :");
        sb.append(expressionLanguage);
        sb.append("\nexpressionBody : \n");
        sb.append(expressionBody);
        return sb.toString();
    }

    public String toString(String indent) {
        return toString();
    }
}