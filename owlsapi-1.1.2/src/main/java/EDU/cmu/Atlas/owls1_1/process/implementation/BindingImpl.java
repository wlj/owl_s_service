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

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfBindingException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfValueOfException;
import EDU.cmu.Atlas.owls1_1.process.Binding;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.ValueOf;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Massimo Paolucci
 * @author Naveen Srinivasan
 */
public class BindingImpl extends OWLS_ObjectImpl implements Binding {

    /** the parameter to retrieve */
    public Parameter toParam;

    /** relation to the object storing the value of the parameter */
    public ValueOf valueForm;

    public String valueData;
    public String valueType;

    Logger logger = Logger.getLogger(BindingImpl.class);

    /**
     * constructor
     * @param individual
     * @throws NotAnIndividualException
     * @throws NotInstanceOfBindingException
     */
    public BindingImpl(Individual individual) throws NotInstanceOfBindingException {
        super(individual);
        logger.debug("Extracting Binding " + individual.getURI());

        // extract parameter
        Individual parameterInd;
        try {
            parameterInd = OWLUtil.getInstanceFromFunctionalProperty(individual, OWLSProcessProperties.toParam);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfBindingException(individual.getURI(), exp);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfBindingException(individual.getURI(), e);
        }

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();

        try {
            setToParam(builder.createParameter(parameterInd));
        } catch (NotInstanceOfParameterException e) {
            throw new NotInstanceOfBindingException(individual.getURI(), e);
        }

        // extract value source property
        Individual valueOfInd;
        try {
            valueOfInd = OWLUtil.getInstanceFromFunctionalProperty(individual, OWLSProcessProperties.valueSource);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfBindingException(individual.getURI(), exp);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfBindingException(individual.getURI(), e);
        }

        // check if indivudial is not null and extract the ValueOf object
        if (valueOfInd != null) {
            try {
                setValueSource(builder.createValueOf(valueOfInd));
            } catch (NotInstanceOfValueOfException e) {
                throw new NotInstanceOfBindingException(individual.getURI(), e);
            }
            return;
        }

        //extract valueData property
        Literal valueDataLit;
        try {
            valueDataLit = OWLUtil.getLiteralFromFunctionalProperty(individual, OWLSProcessProperties.valueData);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfBindingException(individual.getURI(), exp);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfBindingException(individual.getURI(), e);
        }
        if (valueDataLit != null) {
            setValueData(valueDataLit.getString().trim());
            return;
        }

        //extract valueData property
        Literal valueTypeLit;
        try {
            valueTypeLit = OWLUtil.getLiteralFromFunctionalProperty(individual, OWLSProcessProperties.valueType);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfBindingException(individual.getURI(), exp);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfBindingException(individual.getURI(), e);
        }
        if (valueTypeLit != null) {
            setValueType(valueTypeLit.getString().trim());
            return;
        }

        //extract valueData property
        Literal valueSpecifierLit;
        try {
            valueSpecifierLit = OWLUtil.getLiteralFromFunctionalProperty(individual, OWLSProcessProperties.valueSpecifier);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfBindingException(individual.getURI(), exp);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfBindingException(individual.getURI(), e);
        }
        if (valueSpecifierLit != null) {
            logger.info("Value Specifier not handled by this parser");
            return;
        }

        throw new NotInstanceOfBindingException(individual.getURI() + " :should have valueSource, valueData or valueSpecifier property");

    }

    public BindingImpl(String uri) {
        super(uri);
    }

    public BindingImpl() {
    }

    /**
     * @return the value of the parameter to return
     */
    public Parameter getToParam() {
        return toParam;
    }

    /**
     * @param parameter to record
     */
    public void setToParam(Parameter parameter) {
        toParam = parameter;
    }

    /**
     * @return the valueForm used to retrieve the value of the parameter
     */
    public ValueOf getValueSource() {
        return valueForm;
    }

    /**
     * @param link the link to the conteiner of the value of theParam
     */
    public void setValueSource(ValueOf link) {
        valueForm = link;
    }

    public String getValueData() {
        return valueData;
    }

    public void setValueData(String valueData) {
        this.valueData = valueData;
    }

    public void setValueType(String valueType){
    	this.valueType = valueType;
    }
    
    public String getValueType(){
    	return this.valueType;
    }

    
    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        StringBuffer output = new StringBuffer(indent);
        output.append("\nBinding");
        output.append("\nToParameter : ");
        output.append(toParam);
        if (valueData != null) {
            output.append("\nvalueData: ");
            output.append(valueData);
        }
        if (valueForm != null) {
            output.append("\nValue Source: ");
            output.append(valueForm.toString(indent + "\t"));
        } else {
            output.append("\nInfo : No value data or value source");
        }
        return output.toString();
    }
}