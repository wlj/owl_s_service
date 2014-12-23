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
package EDU.cmu.Atlas.owls1_1.process;

import java.util.Iterator;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Property;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotDefinedException;

/**
 * The wrapper for OWL instances. Facilitates creation and access
 * to properties of the represented instance.
 * 
 * Setters maintain an inveriant that there is allways at most one property
 * whith the given URI, i.e., existing property values are removed first.
 * 
 * The addPropertyValue methods can be used if there are more values for a given
 * property, as, e.g., in the case of Sets and Lists.
 * 
 * 
 * @author Roman Vaculin
 * 
 */
public interface OWLParameterValue extends ParameterValue {
    /**
     * @return Returns the underlying OWL individual.
     */
    Individual getIndividual();

    void setPropertyValue(String propertyURI, String value);

    void setPropertyValue(String propertyURI, OWLParameterValue value);

    void setPropertyValue(Property property, String value);

    void setPropertyValue(Property property, OWLParameterValue value);

    void addPropertyValue(String propertyURI, String value);

    void addPropertyValue(String propertyURI, OWLParameterValue value);

    void addPropertyValue(Property property, String value);

    void addPropertyValue(Property property, OWLParameterValue value);

    /**
     * @param propertyURI uri of the property
     * @return a Returns StringParameterValue for datatype properties, OWLParameterProperty for 
     * object properties.  
     * @throws PropertyNotDefinedException if the property has no value
     */
    ParameterValue getPropertyValue(String propertyURI)
	    throws PropertyNotDefinedException;

    /**
     * 
     * @param property
     * @return a Returns StringParameterValue for datatype properties, OWLParameterProperty for 
     * object properties.  
     * @throws PropertyNotDefinedException if the property has no value
     */
    ParameterValue getPropertyValue(Property property)
	    throws PropertyNotDefinedException;

    /**
     * @param propertyURI
     * @return Returns the string representation of value of datatype property.
     * @throws NotAnLiteralException
     * @throws PropertyNotDefinedException
     */
    String getDatatypePropertyValue(String propertyURI)
	    throws NotAnLiteralException, PropertyNotDefinedException;

    /**
     * @param propertyURI
     * @return Returns the string representation of value of datatype property.
     * @throws NotAnLiteralException
     * @throws PropertyNotDefinedException
     */
    String getDatatypePropertyValue(Property property)
	    throws NotAnLiteralException, PropertyNotDefinedException;

    /**
     * @param propertyURI
     * @return Returns an instance of the OWLParameterValue which represent the value of given 
     * object property. 
     * 
     * @throws PropertyNotDefinedException
     * @throws NotAnIndividualException thrown if value is not an individual but a literal or 
     * something else
     */
    OWLParameterValue getObjectPropertyValue(String propertyURI)
	    throws PropertyNotDefinedException, NotAnIndividualException;

    /**
     * @param property
     * @return Returns an instance of the OWLParameterValue which represent the value of given 
     * object property. 
     * 
     * @throws PropertyNotDefinedException
     * @throws NotAnIndividualException thrown if value is not an individual but a literal or 
     * something else
     */
    OWLParameterValue getObjectPropertyValue(Property property)
	    throws PropertyNotDefinedException, NotAnIndividualException;

    /**
     * @param propertyURI
     * @return Returns an iterator over all property values for given property.
     * @throws PropertyNotDefinedException
     */
    Iterator<ParameterValue> listPropertyValues(String propertyURI)
	    throws PropertyNotDefinedException;

    /**
     * @param property
     * @return Returns an iterator over all property values for given property.
     * @throws PropertyNotDefinedException
     */
    Iterator<ParameterValue> listPropertyValues(Property property)
	    throws PropertyNotDefinedException;

    /**
     * @return Returns an iterator over all properties URLs that are defined for the 
     * given object.
     */
    public Iterator<String> listAllPropertyURIs();

}
