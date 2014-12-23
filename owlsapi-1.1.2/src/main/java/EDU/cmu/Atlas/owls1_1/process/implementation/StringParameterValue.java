/**
 * 
 */
package EDU.cmu.Atlas.owls1_1.process.implementation;

import com.hp.hpl.jena.rdf.model.Literal;

import EDU.cmu.Atlas.owls1_1.process.ParameterValue;

/**
 * Represents a literal valued parameter value.
 * 
 * @author Roman Vaculin
 * 
 */
public class StringParameterValue implements ParameterValue {

    protected String value;

    protected Literal literalValue;

    /**
     * @param value string representation of the literal without the xsd type
     */
    public StringParameterValue(String value) {
	this.value = value;
    }

    /**
     * @param value a literal representation of the parameter value
     */
    public StringParameterValue(Literal value) {
	this.literalValue = value;
    }

    /**
     * Returns a string value of the parameter.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
	if (literalValue!=null) {
	    return literalValue.getValue().toString();
	} else {
	    return value;
	}
    }

    /* (non-Javadoc)
     * @see EDU.cmu.Atlas.owls1_1.process.ParameterValue#getType()
     */
    public int getType() {
	return ParameterValue.TYPE_LITERAL;
    }

    /** 
     * @return Returns URI of the XSD type of the parameter. Null is returned in the case 
     * of the plain literal.
     *   
     * @see EDU.cmu.Atlas.owls1_1.process.ParameterValue#getParameterTypeURI()
     */
    public String getParameterTypeURI() {
	if (literalValue!=null) {
	    return literalValue.getDatatypeURI();
	} else {
	    return null;
	}
    }

}
