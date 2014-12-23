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

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlMessageMapException;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlMessageMap;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.utils.OWLSGroundingProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 *  
 */
public class WsdlMessageMapImpl extends OWLS_ObjectImpl implements WsdlMessageMap {

    Parameter owlsParameter;

    String wsdlMessagePart;

    String xsltTransformationURI;

    String xsltTransformationString;

    static Logger logger = Logger.getLogger(WsdlMessageMapImpl.class);

    /**
     * @param individual
     * @throws NotInstanceOfWsdlMessageMapException
     */
    public WsdlMessageMapImpl(Individual individual, int type) throws NotInstanceOfWsdlMessageMapException {
        super(individual);

        boolean owlsparameter = false;
        boolean msgPartFlag = false;
        boolean xsltStringFlag = false;

        //extracting owls parameter - cardinality restriction 1
        Individual parameter = null;

        try {
            parameter = OWLUtil.getInstanceFromProperty(individual, OWLSGroundingProperties.OwlsParameter);
        } catch (NotAnIndividualException e4) {
            logger.info("Owls Parameter not present");
        }

        if (parameter == null) {
            if (type == 1) {
                throw new NotInstanceOfWsdlMessageMapException(individual.getURI()
                        + " : Property 'owlsParameter' is missing in WslOutputMessageMap");
            }
        } else {
            owlsparameter = true;
            OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
            try {
                setOWLSParameter(builder.createParameter(parameter));
            } catch (NotInstanceOfParameterException e1) {
                throw new NotInstanceOfWsdlMessageMapException(individual.getURI()
                        + " : Property 'owlsParameter' is not instance of parameter", e1);
            }
        }
        //extracting WSDL message part
        Literal literal = null;
        try {
            literal = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.WsdlMessagePart);
        } catch (NotAnLiteralException e3) {
            literal = null;
        }

        if (literal == null) {
            if (type == 0) {
                throw new NotInstanceOfWsdlMessageMapException(individual.getURI()
                        + " : Property 'msgPart' is missing in WslInputMessageMap");
            }
        } else {
            msgPartFlag = true;
            setWSDLMessagePart(literal.getString().trim());
            logger.debug("WSDL Message Part " + literal.getString().trim());
        }

        //extracting xslt transformation string
        try {
            literal = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.XSLTTransformationString);
        } catch (NotAnLiteralException e2) {
            literal = null;
        }
        if (literal != null) {
            xsltStringFlag = true;
            if (type == 1) {
                if (msgPartFlag == true) {
                    logger.info("wsdl message part already present");
                    setXSLTTransformationString(literal.getString().trim());//owlsvm	// FIXME: uncommented by rom - transformation did not work
                } else
                    setXSLTTransformationString(literal.getString().trim());
            }
            if (type == 0) {
                if (owlsparameter == true) {
                    logger.info("owls parameter already present");
                    setXSLTTransformationString(literal.getString().trim());//owlsvm	// FIXME: uncommented by rom - transformation did not work
                } else
                    setXSLTTransformationString(literal.getString().trim());
            }
        }

        //extracting xslt transformation uri
        try {
            literal = OWLUtil.getLiteralFromProperty(individual, OWLSGroundingProperties.XSLTTransformationURI);
        } catch (NotAnLiteralException e) {
            literal = null;
        }

        if (literal != null) {

        	// FIXME: Roman: it is not clear why XSLT URL should not be extracted when owlsparameter or msgPart is presented
        	// the implementation of OVM did not work correctly, therefore the following code was modified so that 
        	// XSLT is extracted (and applied) allways
        	
/*            if (type == 0 && owlsparameter == true) {
                logger.info("owlsparameter already present");
            }
            if (type == 1 && msgPartFlag == true) {
                logger.info("wsdl message part already present");
            }
*/            if (xsltStringFlag == true) {
                logger.info("xslt string already present");
            }

//            if (type == 0 && owlsparameter == false && xsltStringFlag == false)		// rom
           	if (type == 0 && xsltStringFlag == false)
                setXSLTTransformationURI(literal.getString().trim());

//            if (type == 1 && msgPartFlag == false && xsltStringFlag == false)			// rom
           	if (type == 1 && msgPartFlag == false && xsltStringFlag == false)
                setXSLTTransformationURI(literal.getString().trim());

        } else {

            if (type == 0 && owlsparameter == false && xsltStringFlag == false)
                throw new NotInstanceOfWsdlMessageMapException(
                        "WSDLInputMessageMap : Either OWLS Parameter or XSLT String or XSLT URI should be present");

            if (type == 1 && msgPartFlag == false && xsltStringFlag == false)
                throw new NotInstanceOfWsdlMessageMapException(
                        "WSDLOutputMessageMap : Either Message Part or XSLT String or XSLT URI should be present");

        }
    }

    public WsdlMessageMapImpl() {
        super();
    }

    public WsdlMessageMapImpl(String uri) {
        super(uri);
    }

    public String getWSDLMessagePart() {
        return wsdlMessagePart;
    }

    public String getXSLTTransformationURI() {
        return xsltTransformationURI;
    }

    public String getXSLTTransformationString() {
        return xsltTransformationString;
    }

    public void setWSDLMessagePart(String string) {
        wsdlMessagePart = string;
    }

    public void setXSLTTransformationURI(String string) {
        xsltTransformationURI = string;
    }

    public void setXSLTTransformationString(String string) {
        xsltTransformationString = string;
    }

    public Parameter getOWLSParameter() {
        return owlsParameter;
    }

    public void setOWLSParameter(Parameter string) {
        owlsParameter = string;
    }

    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("\nWsdlMessagePart :");
        sb.append(wsdlMessagePart);
        sb.append("\nOwlsParameter");
        sb.append(owlsParameter);
        sb.append("\nxsltTransformationString");
        sb.append(xsltTransformationString);
        sb.append("\nxsltTransformationURI");
        sb.append(xsltTransformationURI);

        return sb.toString();
    }

    public String toString(String indent) {
        return toString();
    }
}