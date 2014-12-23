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

import com.hp.hpl.jena.ontology.Individual;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.LocalList;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.ParameterList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceModelImpl;

/**
 * @author Naveen Srinivasan
 */
public class ProcessImpl extends ServiceModelImpl implements Process {

    protected String name;

    protected InputList inputList;

    protected PreConditionList preConditionList;

    protected ParameterList ParameterList;

    protected OutputList Outputs;

    protected ResultList ResultList;

    protected EffectList Effects;

    protected LocalList localList;
    
    protected boolean isAtomic = false;

    protected boolean isComposite = false;

    protected boolean isSimple = false;

    /**
     * constructor
     * @param instance the instance of process in the abox
     */
    public ProcessImpl(Individual instance) {
        super(instance);
    }

    public ProcessImpl() {
        super();
    }
    public ProcessImpl(String uri) {
        super(uri);
    }
    /**
     * @return
     */
    public InputList getInputList() {
        return inputList;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public PreConditionList getPreConditionList() {
        return preConditionList;
    }

    /**
     * @param list
     */
    public void setInputList(InputList list) {
        inputList = list;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param list
     */
    public void setPreConditionList(PreConditionList list) {
        preConditionList = list;
    }

    /**
     * @return the corresponding ParameterList
     * @see EDU.cmu.Atlas.owls1_1.process.Process#getParameterList()
     */
    public ParameterList getParameterList() {
        return ParameterList;
    }

    /**
     * @param parameterList a ParameterList
     * @see EDU.cmu.Atlas.owls1_1.process.Process#setParameterList(EDU.cmu.Atlas.owls1_1.process.ParameterList)
     */
    public void setParameterList(ParameterList parameterList) {
        ParameterList = parameterList;
    }

    /**
     * @result the process list of results
     * @see EDU.cmu.Atlas.owls1_1.process.Process#getResultList()
     */
    public ResultList getResultList() {
        return ResultList;
    }

    /**
     * @param resultList the result list to store
     * @see EDU.cmu.Atlas.owls1_1.process.Process#setResultList(EDU.cmu.Atlas.owls1_1.process.ResultList)
     */
    public void setResultList(ResultList resultList) {
        ResultList = resultList;
    }

    /**
     * @see EDU.cmu.Atlas.owls1_1.process.Process#setOutputList(java.lang.Object)
     */
    public void setOutputList(OutputList outputs) {
        Outputs = outputs;
    }

    /**
     * @see EDU.cmu.Atlas.owls1_1.process.Process#getOutputList()
     */
    public OutputList getOutputList() {
        return Outputs;
    }
           
    public LocalList getLocalList() {
        return localList;
    }
    
    public void setLocalList(LocalList localList) {
        this.localList = localList;
    }
    
    public boolean isAtomic() {
        return isAtomic;
    }

    public boolean isComposite() {
        return isComposite;
    }

    public boolean isSimple() {
        return isSimple;
    }

	public void setIsAtomic(boolean flag) {
		// TODO Auto-generated method stub
		isAtomic = flag;
	}

	public void setIsComposite(boolean flag) {
		isComposite = flag;
		
	}

	public void setIsSimple(boolean flag) {
		// TODO Auto-generated method stub
		isSimple = flag;
	}
}