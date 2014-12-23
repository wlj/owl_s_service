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
package EDU.cmu.Atlas.owls1_1.builder.implementation;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.cmu.Atlas.owls1_1.expression.ConditionList;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionListImpl;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlAtomicProcessGroundingListImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlGroundingListImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlInputMessageMapListImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlOutputMessageMapListImpl;
import EDU.cmu.Atlas.owls1_1.process.BindingList;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.LocalList;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.ParameterList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.process.ResultVarList;
import EDU.cmu.Atlas.owls1_1.process.implementation.BindingListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.EffectListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.LocalListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ParameterListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.PreConditionListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ProcessListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultVarListImpl;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParametersList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorsListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoriesListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceParametersListImpl;
import EDU.cmu.Atlas.owls1_1.service.ServiceGroundingList;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceGroundingListImpl;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceListImpl;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceProfileListImpl;

/**
 * @author Naveen Srinivasan
 */
public class OWLS_Store_BuilderImpl implements OWLS_Store_Builder {

    public OWLS_Store createOWLS_Store() {
        return new OWLS_StoreImpl();
    }

    public ActorsList createActorsList() {
        return new ActorsListImpl();
    }

    public BindingList createBindingList() {
        return new BindingListImpl();
    }

    public EffectList createEffectList() {
        return new EffectListImpl();
    }

    public ParameterList createParameterList() {
        return new ParameterListImpl();
    }

    public InputList createInputList() {
        return new InputListImpl();
    }

    public OutputList createOutputList() {
        return new OutputListImpl();
    }

    public LocalList createLocalList() {
        return new LocalListImpl();
    }
    
    
    public ResultVarList createResultVarList() {
        return new ResultVarListImpl();
    }
    
    public PreConditionList createPreConditionList() {
        return new PreConditionListImpl();
    }

    public ProcessList createProcessList() {
        return new ProcessListImpl();
    }

    public ProfileList createProfileList() {
        return new ProfileListImpl();
    }

    public ResultList createResultList() {
        return new ResultListImpl();
    }

    public ServiceCategoriesList createServiceCategoriesList() {
        return new ServiceCategoriesListImpl();
    }

    public ServiceGroundingList createServiceGroundingList() {
        return new ServiceGroundingListImpl();
    }

    public WsdlGroundingList createWsdlGroundingList() {
        return new WsdlGroundingListImpl();
    }

    public ServiceList createServiceList() {
        return new ServiceListImpl();
    }

    public ServiceParametersList createServiceParametersList() {
        return new ServiceParametersListImpl();
    }

    public ServiceProfileList createServiceProfileList() {
        return new ServiceProfileListImpl();
    }

    public WsdlAtomicProcessGroundingList createWsdlAtomicProcessGroundingList() {
        return new WsdlAtomicProcessGroundingListImpl();
    }

    public WsdlInputMessageMapList createWsdlInputMessageMapList() {
        return new WsdlInputMessageMapListImpl();
    }

    public WsdlOutputMessageMapList createWsdlOutputMessageMapList() {
        return new WsdlOutputMessageMapListImpl();
    }

    public ConditionList createConditionList() {
        return new ConditionListImpl();
    }


}