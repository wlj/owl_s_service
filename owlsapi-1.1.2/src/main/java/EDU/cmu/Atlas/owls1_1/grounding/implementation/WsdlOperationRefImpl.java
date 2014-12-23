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

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWSDLOperationRefException;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.utils.OWLSGroundingProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 *  
 */
public class WsdlOperationRefImpl extends OWLS_ObjectImpl implements WsdlOperationRef {

    String operation;

    String portType;

    
    public WsdlOperationRefImpl() {
        super();
    }
    public WsdlOperationRefImpl(String uri) {
        super(uri);
    }
    /**
     * @param individual
     * @throws NotInstanceOfWSDLOperationRefException
     */
    public WsdlOperationRefImpl(Individual individual) throws NotInstanceOfWSDLOperationRefException {
        super(individual);

        //extracting operation - cardinality 1
        Literal operationNode = null;
        try {
            operationNode = OWLUtil.getLiteralFromFunctionalProperty(individual, OWLSGroundingProperties.Operation);
        } catch (PropertyNotFunctional e1) {
            throw new NotInstanceOfWSDLOperationRefException(e1);
        } catch (NotAnLiteralException e1) {
            throw new NotInstanceOfWSDLOperationRefException(individual.getURI()
                    + " : Property 'operation' is not a literal", e1);
        }

        if (operationNode != null)
            setOperation(operationNode.getString().trim());
        else
            throw new NotInstanceOfWSDLOperationRefException(individual.getURI() + " : Property 'operation' is missing");

        //extracting portType - cardinality 1
        Literal portTypeNode = null;

        try {
            portTypeNode = OWLUtil.getLiteralFromFunctionalProperty(individual, OWLSGroundingProperties.PortType);
        } catch (PropertyNotFunctional e) {
            throw new NotInstanceOfWSDLOperationRefException(e);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfWSDLOperationRefException(individual.getURI()
                    + " : Property 'portType' is not a Literal", e);
        }

        if (portTypeNode != null)
            setPortType(portTypeNode.getString().trim());
        else
            throw new NotInstanceOfWSDLOperationRefException(individual.getURI() + " : Property 'portType' is missing");

    }

    public String getOperation() {
        return operation;
    }

    public String getPortType() {
        return portType;
    }

    public void setOperation(String string) {
        operation = string;
    }

    public void setPortType(String string) {
        portType = string;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append(indent);
        sb.append("Operation :");
        sb.append(operation);
        sb.append("\n");
        sb.append(indent);
        sb.append("Port Type :");
        sb.append(portType);
        return sb.toString();
    }
}