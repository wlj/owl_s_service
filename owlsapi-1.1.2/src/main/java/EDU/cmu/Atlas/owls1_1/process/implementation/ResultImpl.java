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
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfResultException;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.expression.ConditionList;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionListImpl;
import EDU.cmu.Atlas.owls1_1.process.BindingList;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultVarList;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;
import EDU.cmu.Atlas.owls1_1.utils.OWLS_StoreUtil;

import com.hp.hpl.jena.ontology.Individual;

/**
 * @author Massimo Paolucci and Naveen Srinivasan
 */
public class ResultImpl extends OWLS_ObjectImpl implements Result {

    private ConditionList inCondition;

    /** store for the effects */
    private EffectList effects;

    /** store for the list of outputs */
    private BindingList outputs;
    
    private ResultVarList resultVars;

    static Logger logger = Logger.getLogger(ResultImpl.class);

    /**
     * constructor
     * @param individual the individual corresponding to the result in the abox
     * @throws NotInstanceOfResultException
     */
    public ResultImpl(Individual individual) throws NotInstanceOfResultException {
        super(individual);

        //Extracting InCondition

        if (individual.getCardinality(OWLSProcessProperties.inCondition) > 0) {
            try {
                inCondition = (ConditionList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                        OWLSProcessProperties.inCondition, "createCondition", ConditionListImpl.class.getName());
            } catch (OWLS_Store_Exception e) {
                throw new NotInstanceOfResultException(individual.getURI(), e);
            }
        } else
            logger.debug("Result " + individual.getURI() + " has no Condition");

        //Extracting ResultVar
        if (individual.getCardinality(OWLSProcessProperties.hasResultVar) > 0) {
            try {
                resultVars = (ResultVarList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                        OWLSProcessProperties.hasResultVar, "createResultVar", ResultVarListImpl.class.getName());
            } catch (OWLS_Store_Exception e) {
                throw new NotInstanceOfResultException(individual.getURI(), e);
            }
        } else
            logger.debug("Result " + individual.getURI() + " has no ResultVar");
        
        
        //Extracting hasEffect.

        try {
            effects = (EffectList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                    OWLSProcessProperties.hasEffect, "createExpression", EffectListImpl.class.getName());
        } catch (OWLS_Store_Exception e) {
            throw new NotInstanceOfResultException(individual.getURI(), e);
        }

        try {
            //Extracting withOutput
            outputs = (BindingList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                    OWLSProcessProperties.withOutput, "createOutputBinding", BindingListImpl.class.getName());
        } catch (OWLS_Store_Exception e) {
            throw new NotInstanceOfResultException(individual.getURI(), e);
        }

    }

    public ResultImpl(String uri) {
        super(uri);
    }

    public ResultImpl() {
    }

    /**
     * fetch the condition 
     * @see EDU.cmu.Atlas.owls1_1.process.Result#getInCondition()
     */
    public ConditionList getInCondition() {
        return inCondition;
    }

    /**
     * sets the condition
     * @param the resource with the condition
     * @see EDU.cmu.Atlas.owls1_1.process.Result#setInCondition(com.hp.hpl.jena.rdf.model.Resource)
     */
    public void setInCondition(ConditionList condition) {
        inCondition = condition;
    }

    /**
     * @see EDU.cmu.Atlas.owls1_1.process.Result#getHasEffects()
     */
    public EffectList getHasEffects() {
        return effects;
    }

    /**
     * store effects
     * @param effects the effects to store
     * @see EDU.cmu.Atlas.owls1_1.process.Result#setHasEffects(EDU.cmu.Atlas.owls1_1.process.EffectList)
     */
    public void setHasEffects(EffectList effects) {
        this.effects = effects;
    }

    /*
     * (non-Javadoc)
     * @see EDU.cmu.Atlas.owls1_1.process.Result#getWithOutputs()
     */
    public BindingList getWithOutputs() {
        return outputs;
    }

    /*
     * (non-Javadoc)
     * @see EDU.cmu.Atlas.owls1_1.process.Result#setWithOutputs(EDU.cmu.Atlas.owls1_1.process.BindingList)
     */
    public void setWithOutputs(BindingList outputs) {
        this.outputs = outputs;
    }
        
    
    public ResultVarList getResultVarList() {
        return resultVars;
    }
    
    public void setResultVarList(ResultVarList resultVarList) {
        this.resultVars = resultVarList;
    }
    
    public String toString() {
        String str = "";

        str += "\nResult:";
        str += "\nresultVar:";
        str += resultVars;
        str += "\nInCondition:";
        str += inCondition;
        str += "\nhasEffects:";
        str += effects;
        str += "\nwithOutputs:";
        str += outputs;

        return str;
    }

    /*
     * (non-Javadoc)
     * @see EDU.cmu.Atlas.owls1_1.core.OWLS_Object#toString(java.lang.String)
     */
    public String toString(String indent) {
        return toString();
    }

}