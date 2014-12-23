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
package EDU.cmu.Atlas.owls1_1.profile;

import java.util.Vector;

import com.hp.hpl.jena.ontology.OntClass;

import EDU.cmu.Atlas.owls1_1.exception.NotSubclassOfProfileException;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfile;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextList;
import EDU.pku.sj.rscasd.owls1_1.profile.LocationContextList;
import EDU.pku.sj.rscasd.owls1_1.profile.QoSContextList;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextRuleList;

/**
 * Interface that describes the result of parsing a OWL-S Profile object
 * @author Naveen Srinivasan
 */
public interface Profile extends ServiceProfile {

    /**
     * extract the serviceName
     * @return service name or null if undefined
     */
    public String getServiceName();

    /**
     * set the serviceName
     * @param the serviceName
     */
    public void setServiceName(String serviceName);

    /**
     * Extract Text Description of service
     * @return a text description or null if undefined
     */
    public String getTextDescription();

    /**
     * Set Text Description of service
     * @param textDescription
     */
    public void setTextDescription(String textDescription);

    /**
     * Extract Contact information of service
     * @return instance of ActorsList or null if undefined
     */
    public ActorsList getContactInformation();

    /**
     * Set contact information of service
     * @param instance of ActorsList
     */
    public void setContactInformation(ActorsList actor);

    /**
     * Extract list of service categories that define a service
     * @return an instance of ServiceCategoriesList or null if undefined
     */
    public ServiceCategoriesList getServiceCategory();

    /**
     * Set service categories list of service
     * @param an instance of ServiceCategoriesList
     */
    public void setServiceCategory(ServiceCategoriesList serviceCategory);

    /**
     * Extract list of service parameters
     * @return an instance of ServiceParametersList or null if undefined
     */
    public ServiceParametersList getServiceParameter();

    /**
     * Set name of service
     * @param an instance of ServiceParametersList
     */
    public void setServiceParameter(ServiceParametersList serviceParameterList);

    /**
     * Extract input list of service from profile
     * @return an instance of InputList or null if undefined
     */
    public InputList getInputList();

    /**
     * Set input list of a service
     * @param an instance of InputList
     */
    public void setInputList(InputList inputList);

    /**
     * Extract UnConditional output List of service from profile
     * @return an instance of UnConditionalOutputList or null if undefined
     */
    public OutputList getOutputList();

    /**
     * Set UnConditional output List of service from profile
     * @param an instance of UnConditionalOutputList or null if undefined
     */
    public void setOutputList(OutputList output);

    /**
     * Extract precondition list of service from profile
     * @return an instance of PreConditionList or null if undefined
     */
    public PreConditionList getPreconditionList();

    /**
     * Set precondition list of service
     * @param an instance of PreConditionList
     */
    public void setPreconditionList(PreConditionList preconditionList);

    /**
     * @param list
     */
    public ResultList getResultList();

    /**
     * @param list
     */
    public void setResultList(ResultList results);

    /**
     * Extract of service from profile
     * @return instance of process model described by profile or null if undefined
     */
    public Process getHasProcess();

    /**
     * Set name of service
     * @param hasProcess an instance of ProcessModel
     */
    public void setHasProcess(Process hasProcess);

    public Vector getServiceClassifications();

    public void setServiceClassifications(Vector classifications);

    public Vector getServiceProducts();

    public void setServiceProducts(Vector serviceProducts);
    
    /**
     * Sets the profile class (profiles can be organized in the profile hierarchy
     * to allow better classification of services). 
     * @param profileClass	The class of the profile. It has to be a superclass of the 
     * 						Profile class.
     */
    public void setProfileClass(OntClass profileClass) throws NotSubclassOfProfileException ;
    
    /**
     * Extracts the actual class of the profile.
     * @return the actual class of the profile.
     */
    public OntClass getProfileClass();

	public QoSContextList getQoSContext();

	public ContextList getContext();

	public LocationContextList getLocationContext();

	public ContextRuleList getContextRule();

	public void setQoSContext(QoSContextList qoSContext);

	public void setContext(ContextList context);

	public void setLocationContext(LocationContextList locationContext);

	public void setContextRule(ContextRuleList contextRule);
    	
}