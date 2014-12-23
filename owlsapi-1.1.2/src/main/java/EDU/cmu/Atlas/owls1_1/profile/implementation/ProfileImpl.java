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
package EDU.cmu.Atlas.owls1_1.profile.implementation;

import java.util.Vector;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.exception.NotSubclassOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.parser.OWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.PreConditionListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultListImpl;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParametersList;
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceProfileImpl;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProfileProperties;
import EDU.cmu.Atlas.owls1_1.utils.OWLSServiceProperties;
import EDU.cmu.Atlas.owls1_1.utils.OWLS_StoreUtil;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextList;
import EDU.pku.sj.rscasd.owls1_1.profile.LocationContextList;
import EDU.pku.sj.rscasd.owls1_1.profile.QoSContextList;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextRuleList;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.ContextListImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.LocationContextListImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.QoSContextListImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.ContextRuleListImpl;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 *  
 */
public class ProfileImpl extends ServiceProfileImpl implements Profile {

    private OWLSProfileModel OWLS_Model;

    private String serviceName;

    private String textDescription;

    private ActorsList contactInformation;

    private Process hasProcess;

    private ServiceCategoriesList serviceCategoryList;

    private ServiceParametersList ServiceParameter;

    private InputList inputsList;

    private OutputList outputsList;

    private ResultList resultList;

    private PreConditionList preconditionsList;

    private Vector serviceClassifications;

    private Vector serviceProducts;
    
    private OntClass profileClass;
    
    // XXX Changed
    // START OF CHANGE
    public QoSContextList qoSContext; 
    
    public ContextList context; 
    
    public LocationContextList locationContext;
    
    public ContextRuleList contextRule; 
    // END OF CHANGE

    static Logger logger = Logger.getLogger(ProfileImpl.class);

    public ProfileImpl(Individual individual) throws NotInstanceOfProfileException {
        super(individual);
        extractProfile(individual);
    }

    public void extractProfile(Individual individual) throws NotInstanceOfProfileException {
	
	logger.debug("Extracting Profile " + individual.getURI());
	
	setIndividual(individual);
	
	OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
	
	// Extract Has Process
	logger.debug("Extracting has Process");
	Individual hasProcess;
	try {
	    hasProcess = OWLUtil.getInstanceFromProperty(individual, OWLSProfileProperties.has_process);
	} catch (NotAnIndividualException e) {
	    throw new NotInstanceOfProfileException("has_process", e);
	}
	if (hasProcess != null) {
	    try {
		setHasProcess(builder.createProcess(hasProcess));
	    } catch (NotInstanceOfProcessException e) {
		throw new NotInstanceOfProfileException("Property 'has_process' not an instance of process ", e);
	    }
	    logger.debug("has_process " + hasProcess.getURI());
	} else
	    logger.debug("Profile " + getURI() + " has no has_process property");
	
	// Extracting presentedBy 
	Individual parentService;
	try {
	    parentService = OWLUtil.getInstanceFromProperty(individual, OWLSServiceProperties.presentedBy);
	} catch (NotAnIndividualException e1) {
	    throw new NotInstanceOfProfileException("has_process", e1);
	}
	if (parentService != null) {
	    logger.debug("Profile " + getURI() + " HAVE");
	    try {
		Service service = builder.createService(parentService); 
		logger.debug("Service created " + service.getURI());
		setPresentedBy(service);
	    } catch (NotInstanceOfServiceException e1) {
		throw new NotInstanceOfProfileException("Property 'presentedBy' not an instance of process ", e1);
	    }
	} else {
	    logger.debug("Profile " + getURI() + " has no presentedBy property");
	}
	
	// Extract serviceName
	logger.debug("Extracting has service name");
	Literal serviceName;
	try {
	    serviceName = OWLUtil.getLiteralFromFunctionalProperty(individual, OWLSProfileProperties.serviceName);
	} catch (PropertyNotFunctional e6) {
	    throw new NotInstanceOfProfileException("service name", e6);
	} catch (NotAnLiteralException e6) {
	    throw new NotInstanceOfProfileException("service name", e6);
	}
	if (serviceName != null)
	    setServiceName(serviceName.getString().trim());
	else
	    throw new NotInstanceOfProfileException("Profile " + getURI() + " has no serviceName property");
	logger.debug("serviceName " + serviceName);
	
	// Extract Text Description
	logger.debug("Extracting has text Description");
	Literal txtDesc;
	try {
	    txtDesc = OWLUtil.getLiteralFromFunctionalProperty(individual, OWLSProfileProperties.textDescription);
	} catch (PropertyNotFunctional e7) {
	    throw new NotInstanceOfProfileException("text Description", e7);
	} catch (NotAnLiteralException e7) {
	    throw new NotInstanceOfProfileException("text Description", e7);
	}
	if (txtDesc != null)
	    setTextDescription(txtDesc.getString().trim());
	else
	    throw new NotInstanceOfProfileException("Profile " + individual.getURI() + " has no textDescription property");
	logger.debug("Text Description " + txtDesc);
	
	//Extract Service Classification
	Literal srvClassification;
	Vector tempVector = OWLUtil.extractPropertyValues(individual, OWLSProfileProperties.serviceClassfication);
	
	if (tempVector != null && tempVector.size() > 0) {
	    Vector tempSrvClass = new Vector();
	    for (int i = 0; i < tempVector.size(); i++) {
		srvClassification = (Literal) tempVector.get(i);
		tempSrvClass.add(srvClassification.getString().trim());
	    }
	    setServiceClassifications(tempSrvClass);
	    logger.debug("Service Classification " + tempSrvClass);
	} else {
	    logger.warn("Profile " + individual.getURI() + " has no serviceClassification property");
	}
	
	//Extract Service Product
	
	Literal srvProduct;
	tempVector = OWLUtil.extractPropertyValues(individual, OWLSProfileProperties.serviceProduct);
	
	if (tempVector != null && tempVector.size() > 0) {
	    Vector tempSrvProduct = new Vector();
	    for (int i = 0; i < tempVector.size(); i++) {
		srvProduct = (Literal) tempVector.get(i);
		tempSrvProduct.add(srvProduct.getString().trim());
	    }
	    setServiceProducts(tempSrvProduct);
	    logger.debug("Service Product" + serviceProducts);
	} else {
	    logger.warn("Profile " + individual.getURI() + " has no serviceProduct property");
	}
	
	//Extract Contact Information, Service Category, Input, Output,
	// Proconditions, Effects, ServiceParameter
	
	try {
	    setContactInformation((ActorsList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
		    OWLSProfileProperties.contactInformation, "createActor", ActorsListImpl.class.getName()));
	} catch (OWLS_Store_Exception e) {
	    throw new NotInstanceOfProfileException("Actor List in " + individual.getURI(), e);
	}
	
	try {
	    setServiceCategory((ServiceCategoriesList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
		    OWLSProfileProperties.serviceCategory, "createServiceCategory", ServiceCategoriesListImpl.class.getName()));
	} catch (OWLS_Store_Exception e) {
	    throw new NotInstanceOfProfileException("Service Categories List in " + individual.getURI(), e);
	}
	
	try {
	    setServiceParameter((ServiceParametersList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
		    OWLSProfileProperties.serviceParameter, "createServiceParameter", ServiceParametersListImpl.class.getName()));
	} catch (OWLS_Store_Exception e1) {
	    throw new NotInstanceOfProfileException("Service Parameter List in " + individual.getURI(), e1);
	}
	
		// XXX Changed
		// START OF CHANGE
		// Extract Context
		try {
			setContext(((ContextList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
					OWLSProfileProperties.hasContext, "createContext", ContextListImpl.class.getName())));
		} catch (OWLS_Store_Exception e1) {
			logger.error(e1.getMessage(), new NotInstanceOfProfileException("Context List in " + individual.getURI(),
					e1));
		}
		// Extract QoSContext
		try {
			setQoSContext(((QoSContextList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
					OWLSProfileProperties.hasQoSContext, "createQoSContext", QoSContextListImpl.class.getName())));
		} catch (OWLS_Store_Exception e1) {
			logger.error(e1.getMessage(), new NotInstanceOfProfileException("QoS Context List in " + individual.getURI(), e1));
		}
		// Extract LocationContext
		try {
			setLocationContext(((LocationContextList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
					OWLSProfileProperties.hasLocationContext, "createLocationContext", LocationContextListImpl.class
							.getName())));
		} catch (OWLS_Store_Exception e1) {
			logger.error(e1.getMessage(), new NotInstanceOfProfileException("Location Context List in " + individual.getURI(), e1));
		}
		// Extract ContextRule
		try {
			setContextRule(((ContextRuleList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
					OWLSProfileProperties.hasContextRule, "createContextRule", ContextRuleListImpl.class
							.getName())));
		} catch (OWLS_Store_Exception e1) {
			logger.error(e1.getMessage(), new NotInstanceOfProfileException("Context Rule List in " + individual.getURI(), e1));
		}
		// END OF CHANGE
	
	try {
	    setInputList((InputList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasInput,
		    "createInput", InputListImpl.class.getName()));
	} catch (OWLS_Store_Exception e2) {
	    throw new NotInstanceOfProfileException("Input List in " + individual.getURI(), e2);
	}
	
	try {
	    setOutputList((OutputList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasOutput,
		    "createOutput", OutputListImpl.class.getName()));
	} catch (OWLS_Store_Exception e3) {
	    throw new NotInstanceOfProfileException("Output List in " + individual.getURI(), e3);
	}
	
	try {
	    setPreconditionList((PreConditionList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
		    OWLSProfileProperties.hasPrecondition, "createCondition", PreConditionListImpl.class.getName()));
	} catch (OWLS_Store_Exception e4) {
	    throw new NotInstanceOfProfileException("PreCondition List in " + individual.getURI(), e4);
	}
	
	try {
	    setResultList((ResultList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasResult,
		    "createResult", ResultListImpl.class.getName()));
	} catch (OWLS_Store_Exception e5) {
	    throw new NotInstanceOfProfileException("Result List in " + individual.getURI(), e5);
	}
    }
    
    public ProfileImpl(Individual individual, OWLSErrorHandler errorHandler) {
        super(individual);
        extractProfile(individual, errorHandler);
    }

    public void extractProfile(Individual individual, OWLSErrorHandler errorHandler) {
	
	logger.debug("Extracting Profile " + individual.getURI());
	
	setIndividual(individual);
	
	OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
	// Extract Has Process
	logger.debug("Extracting has Process");
	Individual hasProcessInd;
	try {
	    hasProcessInd = OWLUtil.getInstanceFromProperty(individual, OWLSProfileProperties.has_process);
	} catch (NotAnIndividualException e) {
	    errorHandler.error(new NotInstanceOfProfileException("has Process", e));
	    hasProcessInd = null;
	}
	if (hasProcessInd != null)
	    try {
		setHasProcess(builder.createProcess(hasProcessInd));
	    } catch (NotInstanceOfProcessException e) {
		errorHandler.error(new NotInstanceOfProfileException("has Process", e));
	    }
	    else
		logger.debug("Profile " + getURI() + " has no hasProcess property");
	
	logger.debug("has_process " + hasProcess);
	
	//Extracting presentedBy 
        Individual parentService;
        try {
            parentService = OWLUtil.getInstanceFromProperty(individual, OWLSServiceProperties.presentedBy);
        } catch (NotAnIndividualException e1) {
            errorHandler.error(new NotInstanceOfProfileException("has Process", e1));
            parentService = null;
        }
        if (parentService != null) {
            try {
        	Service service = builder.createService(parentService); 
        	logger.debug("Service created " + service.getURI());
                setPresentedBy(service);
            } catch (NotInstanceOfServiceException e) {
                errorHandler.error(new NotInstanceOfProfileException("presentedBy", e));
            }
        } else {
            logger.debug("Profile " + getURI() + " has no presentedBy property");
        }
	
	
	// Extract serviceName
	logger.debug("Extracting has service name");
	Literal serviceName;
	try {
	    serviceName = OWLUtil.getLiteralFromProperty(individual, OWLSProfileProperties.serviceName);
	} catch (NotAnLiteralException e) {
	    errorHandler.error(new NotInstanceOfProfileException("service name", e));
	    serviceName = null;
	}
	if (serviceName != null)
	    setServiceName(serviceName.getString().trim());
	else
	    errorHandler.error(new NotInstanceOfProfileException("Profile " + getURI() + " has no serviceName property"));
	logger.debug("serviceName " + serviceName);
	
	// Extract Text Description
	logger.debug("Extracting has text Description");
	Literal txtDesc;
	try {
	    txtDesc = OWLUtil.getLiteralFromProperty(individual, OWLSProfileProperties.textDescription);
	} catch (NotAnLiteralException e) {
	    errorHandler.error(new NotInstanceOfProfileException("text Description", e));
	    txtDesc = null;
	}
	if (txtDesc != null)
	    setTextDescription(txtDesc.getString().trim());
	else
	    errorHandler.error(new NotInstanceOfProfileException("Profile " + individual.getURI() + " has no textDescription property"));
	logger.debug("Text Description " + txtDesc);
	
	//Extract Service
	// TODO - should code extract services
	Service service = null;
	logger.debug("presentedBy " + service);
	
	//Extract Service Classification
	Literal srvClassification;
	Vector tempVector = OWLUtil.extractPropertyValues(individual, OWLSProfileProperties.serviceClassfication);
	
	if (tempVector != null && tempVector.size() > 0) {
	    Vector tempSrvClass = new Vector();
	    for (int i = 0; i < tempVector.size(); i++) {
		srvClassification = (Literal) tempVector.get(i);
		tempSrvClass.add(srvClassification.getString().trim());
	    }
	    setServiceClassifications(tempSrvClass);
	    logger.debug("Service Classification " + tempSrvClass);
	} else {
	    errorHandler.warning(new NotInstanceOfProfileException("Profile " + individual.getURI()
		    + " has no serviceClassification property"));
	}
	
	//Extract Service Product
	
	Literal srvProduct;
	tempVector = OWLUtil.extractPropertyValues(individual, OWLSProfileProperties.serviceProduct);
	
	if (tempVector != null && tempVector.size() > 0) {
	    Vector tempSrvProduct = new Vector();
	    for (int i = 0; i < tempVector.size(); i++) {
		srvProduct = (Literal) tempVector.get(i);
		tempSrvProduct.add(srvProduct.getString().trim());
	    }
	    setServiceProducts(tempSrvProduct);
	    logger.debug("Service Product" + serviceProducts);
	} else {
	    errorHandler.warning(new NotInstanceOfProfileException("Profile " + individual.getURI() + " has no serviceProduct property"));
	}
	
	//Extract Contact Information, Service Category, Input, Output,
	// Proconditions, Effects, ServiceParameter
	
	//Extract Contact Information
	Object tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.contactInformation, "createActor",
		ActorsListImpl.class.getName(), errorHandler);
	if (tempObj != null)
	    setContactInformation((ActorsList) tempObj);
	
	//Extract Service Category
	tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.serviceCategory, "createServiceCategory",
		ServiceCategoriesListImpl.class.getName(), errorHandler);
	if (tempObj != null)
	    setServiceCategory((ServiceCategoriesList) tempObj);
	
	//Extract Service Parameter
	tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.serviceParameter,
		"createServiceParameter", ServiceParametersListImpl.class.getName(), errorHandler);
	if (tempObj != null)
	    setServiceParameter((ServiceParametersList) tempObj);
	
		// XXX Changed
		// START OF CHANGE
		// Extract Context
		tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasContext,
				"createContext", ContextListImpl.class.getName(), errorHandler);
		if (tempObj != null) {
			setContext((ContextList) tempObj);
		}
		// Extract QoSContext
		tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasQoSContext,
				"createQoSContext", QoSContextListImpl.class.getName(), errorHandler);
		if (tempObj != null) {
			setQoSContext((QoSContextList) tempObj);
		}
		// Extract LocationContext
		tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasLocationContext,
				"createLocationContext", LocationContextListImpl.class.getName(), errorHandler);
		if (tempObj != null) {
			setLocationContext((LocationContextList) tempObj);
		}
		// Extract ServiceContextRule
		tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasContextRule,
				"createContextRule", ContextRuleListImpl.class.getName(), errorHandler);
		if (tempObj != null) {
			setContextRule((ContextRuleList) tempObj);
		}

		// END OF CHANGE
	
	//Extract Input
	tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasInput, "createInput",
		InputListImpl.class.getName(), errorHandler);
	if (tempObj != null)
	    setInputList((InputList) tempObj);
	
	//Extract Output
	tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasOutput, "createOutput",
		OutputListImpl.class.getName(), errorHandler);
	if (tempObj != null)
	    setOutputList((OutputList) tempObj);
	
	//Extract Precondition
	tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasPrecondition, "createCondition",
		PreConditionListImpl.class.getName(), errorHandler);
	if (tempObj != null)
	    setPreconditionList((PreConditionList) tempObj);
	
	//Extract Result
	tempObj = OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual, OWLSProfileProperties.hasResult, "createResult",
		ResultListImpl.class.getName(), errorHandler);
	if (tempObj != null)
	    setResultList((ResultList) tempObj);
	
    }
    
    public ProfileImpl(String uri) {
        super(uri);
    }

    public ProfileImpl() {
    }

    /**
     * @return
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * @return
     */
    public ActorsList getContactInformation() {
        return contactInformation;
    }

    /**
     * @return
     */
    public OWLSProfileModel getOWLS_Model() {
        return OWLS_Model;
    }

    /**
     * @return
     */
    public ResultList getResultList() {
        return resultList;
    }

    /**
     * @return
     */
    public Process getHasProcess() {
        return hasProcess;
    }

    /**
     * @return
     */
    public InputList getInputList() {
        return inputsList;
    }

    /**
     * @return
     */
    public OutputList getOutputList() {
        return outputsList;
    }

    /**
     * @return
     */
    public PreConditionList getPreconditionList() {
        return preconditionsList;
    }

    /**
     * @return
     */
    public ServiceCategoriesList getServiceCategory() {
        return serviceCategoryList;
    }

    /**
     * @return
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @return
     */
    public ServiceParametersList getServiceParameter() {
        return ServiceParameter;
    }

    /**
     * @return
     */
    public String getTextDescription() {
        return textDescription;
    }

    /**
     * @param logger
     */
    public static void setLogger(Logger logger) {
        ProfileImpl.logger = logger;
    }

    /**
     * @param list
     */
    public void setContactInformation(ActorsList list) {
        contactInformation = list;
    }

    /**
     * @param model
     */
    public void setOWLS_Model(OWLSProfileModel model) {
        OWLS_Model = model;
    }

    /**
     * @param list
     */
    public void setResultList(ResultList list) {
        resultList = list;
    }

    /**
     * @param string
     */
    public void setHasProcess(Process process) {
        hasProcess = process;
    }

    /**
     * @param list
     */
    public void setInputList(InputList list) {
        inputsList = list;
    }

    /**
     * @param list
     */
    public void setOutputList(OutputList list) {
        outputsList = list;
    }

    /**
     * @param list
     */
    public void setPreconditionList(PreConditionList list) {
        preconditionsList = list;
    }

    /**
     * @param list
     */
    public void setServiceCategory(ServiceCategoriesList list) {
        serviceCategoryList = list;
    }

    /**
     * @param string
     */
    public void setServiceName(String string) {
        serviceName = string;
    }

    /**
     * @param list
     */
    public void setServiceParameter(ServiceParametersList list) {
        ServiceParameter = list;
    }

    /**
     * @param string
     */
    public void setTextDescription(String string) {
        textDescription = string;
    }

    public Vector getServiceClassifications() {
        return serviceClassifications;
    }

    public void setServiceClassifications(Vector serviceClassifications) {
        this.serviceClassifications = serviceClassifications;
    }

    public Vector getServiceProducts() {
        return serviceProducts;
    }

    public void setServiceProducts(Vector serviceProducts) {
        this.serviceProducts = serviceProducts;
    }
    
    // XXX Changed
	// START OF CHANGE
	public QoSContextList getQoSContext() {
		return qoSContext;
	}

	public void setQoSContext(QoSContextList qoSContext) {
		this.qoSContext = qoSContext;
	}

	public ContextList getContext() {
		return context;
	}

	public void setContext(ContextList context) {
		this.context = context;
	}

	public LocationContextList getLocationContext() {
		return locationContext;
	}

	public void setLocationContext(LocationContextList locationContext) {
		this.locationContext = locationContext;
	}

	public ContextRuleList getContextRule() {
		return contextRule;
	}

	public void setContextRule(ContextRuleList contextRule) {
		this.contextRule = contextRule;
	}

	// END OF CHANGE

    public OntClass getProfileClass() {
		return profileClass;
	}

	public void setProfileClass(OntClass profileClass) throws NotSubclassOfProfileException {
		if (profileClass!=null){
			if (profileClass.hasSuperClass(OWLSProfileProperties.Profile)){
				this.profileClass = profileClass;
			} else {
				throw new NotSubclassOfProfileException("the profileClass parameter is not a subclass of the profile class.");
			}
		}
	}

	/**
     * cast the instance into a string
     * 
     * @return a formatted string with information about the instance
     */
    public String toString() {
        // create String Buffer to contain the string

        StringBuffer sb = new StringBuffer("Profile: \n");
        sb.append("\n\nURI :");
        sb.append(getURI());
        sb.append("\n\nServiceName: ");
        sb.append(getServiceName());
        sb.append("\n\nTextDescription: ");
        sb.append("\n----------------------");
        sb.append(getTextDescription());
        sb.append("\n\nHasProcess: ");
        sb.append(getHasProcess());

        sb.append("\n\nContact Information: ");
        sb.append("\n-----------------------");
        if (getContactInformation() != null)
            sb.append(getContactInformation().toString());
        else
            sb.append("null");

        sb.append("\n\nServiceCategory: ");
        sb.append("\n-------------------");
        if (getServiceCategory() != null)
            sb.append(getServiceCategory().toString());
        else
            sb.append("null");

        sb.append("\n\nServiceParameter: ");
        sb.append("\n--------------------");
        if (getServiceParameter() != null)
            sb.append(getServiceParameter().toString());
        else
            sb.append("null");

        sb.append("\n\nService Classfication : ");
        sb.append(serviceClassifications);

        sb.append("\n\nService Product");
        sb.append(serviceProducts);

        sb.append("\n\nQoS Context: ");
        sb.append("\n----------\n");
        sb.append(getQoSContext());
        sb.append("\n\nLocation Context: ");
        sb.append("\n----------\n");
        sb.append(getLocationContext());
        sb.append("\n\nDetailed Service Context: ");
        sb.append("\n----------\n");
        sb.append(getContext());
        sb.append("\n\nInputs: ");
        sb.append("\n----------\n");
        sb.append(getInputList());
        sb.append("\n\nOutputs: ");
        sb.append("\n----------------------\n");
        sb.append(getOutputList());
        sb.append("\n\nPreconditions: ");
        sb.append("\n----------------\n");
        sb.append(getPreconditionList());
        sb.append("\n\nResults: ");
        sb.append("\n------------\n");
        sb.append(getResultList());
        return sb.toString();
    }

}