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
package EDU.cmu.Atlas.owls1_1.service.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.parser.OWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.ServiceGroundingList;
import EDU.cmu.Atlas.owls1_1.service.ServiceModel;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;
import EDU.cmu.Atlas.owls1_1.service.ServiceProvider;
import EDU.cmu.Atlas.owls1_1.utils.OWLSServiceProperties;
import EDU.cmu.Atlas.owls1_1.utils.OWLS_StoreUtil;

import com.hp.hpl.jena.ontology.Individual;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * Implementation of the Servce interface. It implements methods to parse the service
 * @author Naveen Srinivasan
 */

public class ServiceImpl extends OWLS_ObjectImpl implements Service {

    private ServiceProfileList presents;

    private ServiceModel describes;

    private ServiceGroundingList supports;
    
    private ServiceProvider serviceProvider;

    static Logger logger = Logger.getLogger(ServiceImpl.class);

    /**
     * @param individual
     * @throws NotInstanceOfServiceException
     */
    public ServiceImpl(Individual individual) throws NotInstanceOfServiceException {
        super(individual);

        extractService(individual);
    }

    public void extractService(Individual individual) throws NotInstanceOfServiceException {
	setIndividual(individual);
	// Extract presents profile
	try {
	    setPresents((ServiceProfileList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
		    OWLSServiceProperties.presents, "createProfile", ProfileListImpl.class.getName()));
	} catch (OWLS_Store_Exception e) {
	    throw new NotInstanceOfServiceException("Service Profile List", e);
	}
	
	OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
	
	//Extracting describedBy Service Model
	Individual processModel;
	try {
	    processModel = OWLUtil.getInstanceFromFunctionalProperty(individual, OWLSServiceProperties.describedBy);
	} catch (NotAnIndividualException e1) {
	    throw new NotInstanceOfServiceException("Service Model", e1);
	} catch (PropertyNotFunctional e1) {
	    throw new NotInstanceOfServiceException(e1);
	}
	
	try {
	    if (processModel != null)
		setDescribes(builder.createProcess(processModel));
	    else
		logger.info("Service " + individual.getURI() + " has no Service Model");
	} catch (NotInstanceOfProcessException e) {
	    throw new NotInstanceOfServiceException("Service Model", e);
	}
	
	//Extract Service Grounding List
	try {
	    setSupports((ServiceGroundingList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
		    OWLSServiceProperties.supports, "createWSDLGrounding", ServiceGroundingListImpl.class.getName()));
	} catch (OWLS_Store_Exception e) {
	    throw new NotInstanceOfServiceException("Service Grounding List", e);
	}
	
    }
    
    public ServiceImpl(Individual individual, OWLSErrorHandler errHandler) {
        super(individual);
        extractService(individual, errHandler);
    }

    public void extractService(Individual individual, OWLSErrorHandler errHandler) {
	setIndividual(individual);
	
	// Extract presents profile
	setPresents((ServiceProfileList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
		OWLSServiceProperties.presents, "createProfile", ProfileListImpl.class.getName(), errHandler));
	
	OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
	
	//Extracting describedBy Service Model
	Individual processModel = null;
	try {
	    processModel = OWLUtil.getInstanceFromFunctionalProperty(individual, OWLSServiceProperties.describedBy);
	} catch (NotAnIndividualException e1) {
	    errHandler.error(e1);
	} catch (PropertyNotFunctional e1) {
	    errHandler.error(e1);
	}
	
	if (processModel != null)
	    setDescribes(builder.createProcess(processModel, errHandler));
	else
	    logger.info("Service " + individual.getURI() + " has no Service Model");
	
	//Extract Service Grounding List
	setSupports((ServiceGroundingList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
		OWLSServiceProperties.supports, "createWSDLGrounding", ServiceGroundingListImpl.class.getName(),
		errHandler));
	
    }
    
    public ServiceImpl(String uri) {
        super(uri);
    }

    public ServiceImpl() {
    }

    /**
     * Extract the Service Profile of a Service
     * @return a string or null if undefined
     */
    public ServiceProfileList getPresents() {
        return presents;
    }

    /**
     * Set the parameter of a service
     * @param serviceProfile an instance of ServiceProfile
     */
    public void setPresents(ServiceProfileList serviceProfile) {
        presents = serviceProfile;
    }

    /**
     * Extract the Service Profile of a Service
     * @return a string or null if undefined
     */
    public ServiceModel getDescribes() {
        return describes;
    }

    /**
     * Record the service model
     * @param serviceModel an instance of ServiceProfile
     */
    public void setDescribes(ServiceModel serviceModel) {
        describes = serviceModel;
    }

    /**
     * Extract the Service Profile of a Service
     * @return a string or null if undefined
     */
    public ServiceGroundingList getSupports() {
        return supports;
    }

    /**
     * Record the Service Grounding
     * @param serviceGrounding an instance of ServiceProfile
     */
    public void setSupports(ServiceGroundingList serviceGroundingList) {
        supports = serviceGroundingList;
    }
    
    /**
     * Record the Service Provider  
     * @param serviceProvider an instance of the service provider 
     */
    public void setProvidedBy(ServiceProvider serviceProvider){
    	this.serviceProvider = serviceProvider;
    }
    
    /** (non-Javadoc)
     * Extract the Service provider
     * @return the instance of the service provider
     */
    public ServiceProvider getProvidedBy(){
    	return serviceProvider;
    }


    // ========================================================== ToString

    /**
     * format a service into a string
     * @return a string that describes the service
     */
    public String toString() {
        // create a store for the string
        StringBuffer sb = new StringBuffer("");
        sb.append("\nService: ");
        sb.append(getURI());
        sb.append("\npresents: null");
        sb.append(getPresents());
        sb.append("\ndescribes: ");
        sb.append(getDescribes());
        sb.append("\nSupports: ");
        sb.append(getSupports());

        return sb.toString();
    }

    public String toString(String indent) {
        return toString();
    }

}

