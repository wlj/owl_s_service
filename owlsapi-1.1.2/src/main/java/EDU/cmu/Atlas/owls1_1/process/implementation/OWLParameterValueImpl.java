package EDU.cmu.Atlas.owls1_1.process.implementation;

import java.util.Iterator;
import java.util.LinkedList;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotDefinedException;
import edu.cmu.atlas.owl.utils.OWLUtil;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.process.OWLParameterValue;
import EDU.cmu.Atlas.owls1_1.process.ParameterValue;

/**
 * @author Roman Vaculin
 *
 */
public class OWLParameterValueImpl extends OWLS_ObjectImpl implements
	OWLParameterValue {

    OntModel model;

    public OWLParameterValueImpl(Individual individual) {
	super(individual);
	if (individual == null) {
	    throw new IllegalArgumentException(
		    "Parameter individual cannot be null");
	}
	model = individual.getOntModel();
    }

    /* ********************************************************************* */
    /* Setters */
    /* ********************************************************************* */

    public void setPropertyValue(Property property, String value) {
	getIndividual().removeAll(property);
	getIndividual().addProperty(property, value);
    }

    public void setPropertyValue(String propertyURI, String value) {
	Property p = model.getProperty(propertyURI);
	setPropertyValue(p, value);
    }

    public void setPropertyValue(Property property, OWLParameterValue value) {
	getIndividual().removeAll(property);
	getIndividual().addProperty(property, value.getIndividual());
    }

    public void setPropertyValue(String propertyURI, OWLParameterValue value) {
	Property p = model.getProperty(propertyURI);
	setPropertyValue(p, value);
    }

    public void addPropertyValue(Property property, String value) {
	getIndividual().addProperty(property, value);
    }

    public void addPropertyValue(String propertyURI, String value) {
	Property p = model.getProperty(propertyURI);
	setPropertyValue(p, value);
    }

    public void addPropertyValue(Property property, OWLParameterValue value) {
	getIndividual().addProperty(property, value.getIndividual());
    }

    public void addPropertyValue(String propertyURI, OWLParameterValue value) {
	Property p = model.getProperty(propertyURI);
	setPropertyValue(p, value);
    }

    /* ********************************************************************* */
    /* Getters */
    /* ********************************************************************* */

    public ParameterValue getPropertyValue(Property property)
	    throws PropertyNotDefinedException {
	RDFNode val = getIndividual().getPropertyValue(property);
	if (val == null) {
	    throw new PropertyNotDefinedException("The value of property "
		    + property.getURI() + " was not defined");
	}
	if (val.canAs(Literal.class)) {
	    // return new
	    // StringParameterValue(((Literal)val.as(Literal.class)).getValue().toString());
	    return new StringParameterValue(((Literal) val.as(Literal.class)));
	} else if (val.canAs(Individual.class)) {
	    return new OWLParameterValueImpl((Individual) val
		    .as(Individual.class));
	} else {
	    throw new RuntimeException(
		    "Unexpected value of the property "
			    + property.getURI()
			    + ". The value should be either literal or the owl individual which is not the case for "
			    + val.toString());
	}
    }

    public ParameterValue getPropertyValue(String propertyURI)
	    throws PropertyNotDefinedException {
	Property p = model.getProperty(propertyURI);
	return getPropertyValue(p);
    }

    public String getDatatypePropertyValue(Property property)
	    throws NotAnLiteralException, PropertyNotDefinedException {
	Literal l = OWLUtil.getLiteralFromProperty(getIndividual(), property);
	if (l == null) {
	    throw new PropertyNotDefinedException("The value of property "
		    + property.getURI() + " was not defined");
	}
	return l.getValue().toString();
    }

    public String getDatatypePropertyValue(String propertyURI)
	    throws NotAnLiteralException, PropertyNotDefinedException {
	Property p = model.getProperty(propertyURI);
	return getDatatypePropertyValue(p);
    }

    public OWLParameterValue getObjectPropertyValue(Property property)
	    throws PropertyNotDefinedException, NotAnIndividualException {
	RDFNode val = getIndividual().getPropertyValue(property);
	if (val == null) {
	    throw new PropertyNotDefinedException("The value of property "
		    + property.getURI() + " was not defined");
	}
	if (val.canAs(Individual.class)) {
	    return new OWLParameterValueImpl((Individual) val
		    .as(Individual.class));
	} else {
	    throw new NotAnIndividualException("The value of property "
		    + property.getURI() + " is not an OWL instance");
	}
    }

    public Iterator<ParameterValue> listPropertyValues(String propertyURI)
	    throws PropertyNotDefinedException {
	Property p = model.getProperty(propertyURI);
	return listPropertyValues(p);
    }

    public Iterator<ParameterValue> listPropertyValues(Property property)
	    throws PropertyNotDefinedException {
	ExtendedIterator it = getIndividual().listPropertyValues(property);
	LinkedList<ParameterValue> list = new LinkedList<ParameterValue>();

	while (it.hasNext()) {
	    RDFNode val = (RDFNode) it.next();
	    if (val == null) {
		throw new PropertyNotDefinedException("The value of property "
			+ property.getURI() + " was not defined");
	    }
	    if (val.canAs(Individual.class)) {
		list.add(new OWLParameterValueImpl((Individual) val
			.as(Individual.class)));
	    } else if (val.canAs(Literal.class)) {
		// list.add(new
		// StringParameterValue(((Literal)val.as(Literal.class)).getValue().toString()));
		list.add(new StringParameterValue(((Literal) val
			.as(Literal.class))));
	    } else {
		throw new RuntimeException(
			"Unexpected value of the property "
				+ property.getURI()
				+ ". The value should be either literal or the owl individual which is not the case for "
				+ val.toString());
	    }
	}

	return list.iterator();
    }

    public OWLParameterValue getObjectPropertyValue(String propertyURI)
	    throws PropertyNotDefinedException, NotAnIndividualException {
	Property p = model.getProperty(propertyURI);
	return getObjectPropertyValue(p);
    }

    public String toString(String indent) {
	return OWLUtil.serializeIndividual(getIndividual());
    }

    public String toString() {
	return OWLUtil.serializeIndividual(getIndividual());
    }
    
    public int getType() {
	return ParameterValue.TYPE_OWL_INDIVIDUAL;
    }

    public String getParameterTypeURI() {
	return getIndividual().getRDFType().getURI();
    }

    public Iterator<String> listAllPropertyURIs() {
	ExtendedIterator it = getIndividual().listProperties();
	LinkedList<String> list = new LinkedList<String>();
	while (it.hasNext()) {
	    Statement statement = (Statement) it.next();
	    Property p = statement.getPredicate();
	    list.add(p.getURI());
	}
	return list.iterator();
    }
}
