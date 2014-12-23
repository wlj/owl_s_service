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

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.OWLSParameterNotIndividual;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 */
public class ParameterImpl extends OWLS_ObjectImpl implements Parameter {

    private String parameterType;

    private Object parameterValue;

    protected boolean isInput = false;

    protected boolean isOutput = false;

    protected boolean isLocal = false;

    protected boolean isResultVar = false;

    static Logger logger = Logger.getLogger(ParameterImpl.class);

    /**
     * constructor
     * @param instance the Jena instace of the parameter
     */
    protected ParameterImpl(Individual instance) throws NotInstanceOfParameterException {
        super(instance);
        if (instance != null && !instance.hasRDFType(OWLSProcessURI.Parameter)) {
        	logger.debug("rdftype " + instance.getRDFType());
            throw new NotInstanceOfParameterException("Instance " + instance.getURI() + " not an instance of Parameter");
        }

        //Extracting parameterType
        Literal litParameterType;
        try {
            litParameterType = OWLUtil.getLiteralFromFunctionalProperty(instance, OWLSProcessProperties.parameterType);
        } catch (PropertyNotFunctional e1) {
            throw new NotInstanceOfParameterException(e1);
        } catch (NotAnLiteralException e1) {
            throw new NotInstanceOfParameterException(e1);
        }

        if (litParameterType != null) {
            setParameterType(litParameterType.getString().trim());
        } else
            throw new NotInstanceOfParameterException(instance.getURI() + " : Property 'parameterType' missing");

        //Extracting parameterValue
        Literal litParameterValue;
        try {
            litParameterValue = OWLUtil.getLiteralFromProperty(instance, OWLSProcessProperties.parameterValue);
        } catch (NotAnLiteralException e) {
            litParameterValue = null;
        }
        if (litParameterValue != null)
            setParameterValue(litParameterValue.getString().trim());
        else
            logger.debug("owl-s-api-warning : Property 'parameterValue' missing in Parameter Instance " + instance.getURI());

    }

    public ParameterImpl(String uri) {
        super(uri);
    }

    public ParameterImpl() {
        super();
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String name) {
        parameterType = name;
    }

    public String getParameterValue() {
        return getParameterValueAsString();
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    
    public Individual getParameterValueAsIndividual() throws OWLSParameterNotIndividual {
    	if (parameterValue == null){
			return null;
    	} else if (parameterValue instanceof Individual) {
			return (Individual) parameterValue;
    	} else {
			throw new OWLSParameterNotIndividual("The value of parameter is not class of Individual");
		}
	}

	public String getParameterValueAsString() {
		if (parameterValue == null) {
			return null;
		} else if (parameterValue instanceof String) {
			return (String) parameterValue;
		} else if (parameterValue instanceof Individual) {
			return OWLUtil.serializeIndividual((Individual) parameterValue);
		} else {
			return parameterValue.toString();
		}
	}

	public boolean isValueIndividual() {
		if (parameterValue instanceof Individual) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValueLiteral() {
		if (parameterValue instanceof String) {
			return true;
		} else {
			return false;
		}
	}

	public void setParameterValue(Individual parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        StringBuffer sb = new StringBuffer(indent);
        sb.append("\nParameter Description:");
        sb.append(indent);
        sb.append("\t URI : ");
        sb.append(getURI());
        sb.append("\n");
        sb.append(indent);
        sb.append("\t Parameter Name: ");
        sb.append(parameterType);
        sb.append("\n");
        sb.append(indent);
        sb.append("\t Parameter Value: ");
        sb.append(parameterValue);

        return sb.toString();
    }

    public boolean isInput() {
        return isInput;
    }

    public boolean isOutput() {
        return isOutput;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public boolean isResultVar() {
        return isResultVar;
    }

}