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
package EDU.cmu.Atlas.owls1_1.builder;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;
import EDU.cmu.Atlas.owls1_1.expression.ConditionList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMapList;
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
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParametersList;
import EDU.cmu.Atlas.owls1_1.service.ServiceGroundingList;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;

/**
 * Interface to create OWLS_Store
 * @author Naveen Srinivasan
 * @see EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory
 */
public interface OWLS_Store_Builder {

   
    public OWLS_Store createOWLS_Store();

    public ActorsList createActorsList();

    public BindingList createBindingList();

    public ConditionList createConditionList();
    
    public EffectList createEffectList();

    public ParameterList createParameterList();

    public InputList createInputList();

    public OutputList createOutputList();
    
    public LocalList createLocalList();
    
    public ResultVarList createResultVarList();

    public PreConditionList createPreConditionList();

    public ProcessList createProcessList();

    public ProfileList createProfileList();

    public ResultList createResultList();

    public ServiceCategoriesList createServiceCategoriesList();

    public ServiceGroundingList createServiceGroundingList();

    public WsdlGroundingList createWsdlGroundingList();

    public ServiceList createServiceList();

    public ServiceParametersList createServiceParametersList();

    public ServiceProfileList createServiceProfileList();

    public WsdlAtomicProcessGroundingList createWsdlAtomicProcessGroundingList();

    public WsdlInputMessageMapList createWsdlInputMessageMapList();

    public WsdlOutputMessageMapList createWsdlOutputMessageMapList();

}