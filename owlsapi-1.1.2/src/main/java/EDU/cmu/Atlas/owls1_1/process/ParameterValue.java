/**
 * 
 */
package EDU.cmu.Atlas.owls1_1.process;

/**
 * A common superclass of parameter value. There are two subclasses:
 * StringParameterValue and OWLParameterValue representing a literal valued
 * objects and OWL class valued objects respectively.
 * 
 * @author Roman Vaculin
 * 
 */
public interface ParameterValue {
    
    
    /**
     * Constant used to distinguish literal object.
     */
    public static int TYPE_LITERAL = 0;

    /**
     * Constant used to distinguish OWL object.
     */
    public static int TYPE_OWL_INDIVIDUAL = 1;

    /**
     * Returns a string representation of the parameter value. 
     * For literals it returns its string value without xsd type information.
     * For OWL instances the OWL/XML serialization is returned. 
     * 
     */
    String toString();

    /**
     * @return returns the type of represented object (TYPE_LITERAL or TYPE_OWL_INDIVIDUAL)
     */
    int getType();

    /**
     * @return The uri of type of the represented object. It can be either the XSD type 
     * or uri refering to OWL class.
     */
    String getParameterTypeURI();
}
