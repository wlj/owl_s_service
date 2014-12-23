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

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Builder_Util;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfActorException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfBindingException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfControlConstructException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfControlConstructListException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfExpressionException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfInputBindingException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfLogicLanguage;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfOutputBindingException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfPerformException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfResultException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceCategoryException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceProviderException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfValueOfException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWSDLOperationRefException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlAtomicProcessGroundingException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlGroundingException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlMessageMapException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.Expression;
import EDU.cmu.Atlas.owls1_1.expression.LogicLanguage;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ExpressionImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.LogicLanguageImpl;
import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.OWLSGroundingModelImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlAtomicProcessGroundingImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlGroundingImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlInputMessageMapImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlOperationRefImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlOutputMessageMapImpl;
import EDU.cmu.Atlas.owls1_1.parser.OWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.process.Binding;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputBinding;
import EDU.cmu.Atlas.owls1_1.process.Local;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputBinding;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultVar;
import EDU.cmu.Atlas.owls1_1.process.ValueOf;
import EDU.cmu.Atlas.owls1_1.process.implementation.AnyOrderImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.AtomicProcessImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.BindingImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ChoiceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.CompositeProcessImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ControlConstructFactory;
import EDU.cmu.Atlas.owls1_1.process.implementation.ControlConstructListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.IfThenElseImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputBindingImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.LocalImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OWLSProcessModelImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputBindingImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ParameterFactory;
import EDU.cmu.Atlas.owls1_1.process.implementation.PerformImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ProcessFactoryDynamic;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultVarImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SequenceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SimpleProcessImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitJoinImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ValueOfImpl;
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParameter;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.OWLSProfileModelImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoryImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceParameterImpl;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.ServiceProvider;
import EDU.cmu.Atlas.owls1_1.service.implementation.OWLSServiceModelImpl;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceImpl;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceProviderImpl;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextObjectException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextRuleException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextTypeException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueDomainException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfLocationContextException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfPropertyValueException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfQoSContextException;
import EDU.pku.sj.rscasd.owls1_1.profile.Context;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextObject;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextRule;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextType;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValue;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValueDomain;
import EDU.pku.sj.rscasd.owls1_1.profile.LocationContext;
import EDU.pku.sj.rscasd.owls1_1.profile.PropertyValue;
import EDU.pku.sj.rscasd.owls1_1.profile.QoSContext;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.ContextImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.ContextObjectImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.ContextRuleImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.ContextTypeImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.ContextValueDomainImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.ContextValueImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.LocationContextImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.PropertyValueImpl;
import EDU.pku.sj.rscasd.owls1_1.profile.implementation.QoSContextImpl;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * @author Naveen Srinivasan
 */
public class OWLS_Object_BuilderImpl implements OWLS_Object_Builder {

	SymbolsTable symTable;
	ProcessFactoryDynamic processFactory;

	public OWLS_Object_BuilderImpl() {
		this(new ProcessFactoryDynamic());
	}

	protected OWLS_Object_BuilderImpl(ProcessFactoryDynamic processFactory) {
		symTable = SymbolsTable.instance();
		initSymbolTable();
		this.processFactory = processFactory;
	}

	public void initSymbolTable() {
		// Adding predefined LocialLanguage object
		symTable.addSymbol(OWLSProcessURI.SWRL, LogicLanguageImpl.SWRL);
		symTable.addSymbol(OWLSProcessURI.DRS, LogicLanguageImpl.DRS);
		symTable.addSymbol(OWLSProcessURI.KIF, LogicLanguageImpl.KIF);
	}

	public OWLS_Object createOWLS_Object(String uri) {
		return getOWLS_Object(uri);
	}

	public OWLSServiceModel createOWLSServiceModel() {
		return new OWLSServiceModelImpl();
	}

	public OWLSServiceModel createOWLSServiceModel(OntModel ontModel) {
		return new OWLSServiceModelImpl(ontModel);
	}

	public Service createService(Individual individual, OWLSErrorHandler errHandler) {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(individual)) != null) {
			if (obj instanceof Service)
				return (Service) obj;
			else {
				errHandler
						.error(new NotInstanceOfServiceException("Instance " + obj.getURI() + " not of type Service"));
				return null;
			}
		}

		ServiceImpl service = new ServiceImpl(individual.getURI());
		service.setIndividual(individual);
		storeOWLS_Object(service);
		service.extractService(individual, errHandler);
		return service;

		// ServiceImpl service = new ServiceImpl(individual, errHandler);
		// storeOWLS_Object(service);
		// return service;
	}

	public Service createService(Individual instance) throws NotInstanceOfServiceException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Service)
				return (Service) obj;
			else
				throw new NotInstanceOfServiceException("Instance " + obj.getURI() + " not of type Service");
		}

		ServiceImpl service = new ServiceImpl(instance.getURI());
		service.setIndividual(instance);
		storeOWLS_Object(service);
		service.extractService(instance);
		return service;
		// ServiceImpl service = new ServiceImpl(instance);
		// storeOWLS_Object(service);
		// return service;
	}

	public Service createService(String URI) throws NotInstanceOfServiceException {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(URI)) != null) {
			if (obj instanceof Service)
				return (Service) obj;
			else
				throw new NotInstanceOfServiceException("Instance " + obj.getURI() + " not of type Service");
		}

		Service service = new ServiceImpl(URI);
		storeOWLS_Object(service);
		return service;
	}

	public Service createService() {
		return new ServiceImpl();
	}

	public ServiceProvider createServiceProvider(String URI, OntClass providerClass)
			throws NotInstanceOfServiceProviderException {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(URI)) != null) {
			if (obj instanceof ServiceProvider)
				return (ServiceProvider) obj;
			else
				throw new NotInstanceOfServiceProviderException("Instance " + obj.getURI()
						+ " not of type ServiceProvider");
		}

		ServiceProvider serviceProvider = new ServiceProviderImpl(URI, providerClass);
		storeOWLS_Object(serviceProvider);
		return serviceProvider;
	}

	public OWLSProfileModel createOWLSProfileModel() {
		return new OWLSProfileModelImpl();
	}

	public OWLSProfileModel createOWLSProfileModel(OntModel ontModel) {
		return new OWLSProfileModelImpl(ontModel);
	}

	public Profile createProfile(Individual instance) throws NotInstanceOfProfileException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Profile)
				return (Profile) obj;
			else
				throw new NotInstanceOfProfileException("Instance " + obj.getURI() + " not of type Process");
		}
		ProfileImpl profile = new ProfileImpl(instance.getURI());
		profile.setIndividual(instance);
		storeOWLS_Object(profile);
		profile.extractProfile(instance);
		return profile;
	}

	public Profile createProfile(String URI) throws NotInstanceOfProfileException {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(URI)) != null) {
			if (obj instanceof Profile)
				return (Profile) obj;
			else
				throw new NotInstanceOfProfileException("Instance " + obj.getURI() + " not of type Process");
		}

		Profile profile = new ProfileImpl(URI);
		storeOWLS_Object(profile);
		return profile;
	}

	public Profile createProfile() {
		return new ProfileImpl();
	}

	public Profile createProfile(Individual instance, OWLSErrorHandler handler) {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Profile)
				return (Profile) obj;
			else {
				handler.equals(new NotInstanceOfProfileException("Instance " + obj.getURI() + " not of type Process"));
				return null;
			}
		}

		ProfileImpl profile = new ProfileImpl(instance.getURI());
		profile.setIndividual(instance);
		storeOWLS_Object(profile);
		profile.extractProfile(instance, handler);
		return profile;
	}

	public Actor createActor(Individual instance) throws NotInstanceOfActorException {

		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Actor)
				return (Actor) obj;
			else
				throw new NotInstanceOfActorException("Instance " + obj.getURI() + " not of type Actor");
		}
		Actor actor = new ActorImpl(instance);
		storeOWLS_Object(actor);
		return actor;
	}

	public Actor createActor(String uri) throws NotInstanceOfActorException {

		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Actor)
				return (Actor) obj;
			else
				throw new NotInstanceOfActorException("Instance " + obj.getURI() + " not of type Actor");
		}

		Actor actor = new ActorImpl(uri);
		storeOWLS_Object(actor);
		return actor;
	}

	public Actor createActor() {

		return new ActorImpl();
	}

	public ServiceCategory createServiceCategory(Individual instance) throws NotInstanceOfServiceCategoryException {

		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ServiceCategory)
				return (ServiceCategory) obj;
			else
				throw new NotInstanceOfServiceCategoryException("Instance " + obj.getURI()
						+ " not of type Service Category");
		}

		ServiceCategory serviceCategory = new ServiceCategoryImpl(instance);
		storeOWLS_Object(serviceCategory);
		return serviceCategory;
	}

	public ServiceCategory createServiceCategory(String uri) throws NotInstanceOfServiceCategoryException {

		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof ServiceCategory)
				return (ServiceCategory) obj;
			else
				throw new NotInstanceOfServiceCategoryException("Instance " + obj.getURI()
						+ " not of type Service Category");
		}

		ServiceCategory serviceCategory = new ServiceCategoryImpl(uri);
		storeOWLS_Object(serviceCategory);
		return serviceCategory;
	}

	public ServiceCategory createServiceCategory() {
		return new ServiceCategoryImpl();
	}

	// //Process Model

	// Service Parameter
	public ServiceParameter createServiceParameter(Individual instance) throws NotInstanceOfServiceParameterException {

		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ServiceParameter)
				return (ServiceParameter) obj;
			else
				throw new NotInstanceOfServiceParameterException("Instance " + obj.getURI()
						+ " not of type ServiceParameter");
		}
		ServiceParameter serviceParameter = new ServiceParameterImpl(instance);
		storeOWLS_Object(serviceParameter);
		return serviceParameter;
	}

	public ServiceParameter createServiceParameter(String uri) throws NotInstanceOfServiceParameterException {

		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof ServiceParameter)
				return (ServiceParameter) obj;
			else
				throw new NotInstanceOfServiceParameterException("Instance " + obj.getURI()
						+ " not of type ServiceParameter");
		}

		ServiceParameter serviceParameter = new ServiceParameterImpl(uri);
		storeOWLS_Object(serviceParameter);
		return serviceParameter;
	}

	public ServiceParameter createServiceParameter() {
		return new ServiceParameterImpl();
	}

	public OWLSProcessModel createOWLSProcessModel() {
		return new OWLSProcessModelImpl();
	}

	public OWLSProcessModel createOWLSProcessModel(OntModel ontModel) {
		return new OWLSProcessModelImpl(ontModel);
	}

	// Parameter
	public Parameter createParameter(Individual instance) throws NotInstanceOfParameterException {

		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Parameter)
				return (Parameter) obj;
			else
				throw new NotInstanceOfParameterException("Instance " + obj.getURI() + " not of type Parameter");
		}

		Parameter parameter = ParameterFactory.parseParameter(instance);

		storeOWLS_Object(parameter);
		return parameter;
	}

	public Parameter createParameter(Individual instance, int type) throws NotInstanceOfParameterException {

		return createParameter(instance);
	}

	public Parameter createParameter(String uri) throws NotInstanceOfParameterException {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Parameter)
				return (Parameter) obj;
			else
				throw new NotInstanceOfParameterException("Instance " + obj.getURI() + " not of type Parameter");
		}
		return null;
	}

	public Parameter createParameter(String uri, int type) throws NotInstanceOfParameterException {

		OWLS_Object obj;

		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Parameter)
				return (Parameter) obj;
			else
				throw new NotInstanceOfParameterException("Instance " + obj.getURI() + " not of type Parameter");
		}

		Parameter parameter = null;

		switch (type) {
		case OWLS_Builder_Util.INPUT:
			parameter = new InputImpl(uri);
			break;
		case OWLS_Builder_Util.OUTPUT:
			parameter = new OutputImpl(uri);
			break;
		case OWLS_Builder_Util.LOCAL:
			parameter = new LocalImpl(uri);
			break;
		case OWLS_Builder_Util.RESULT_VAR:
			parameter = new ResultVarImpl(uri);
			break;
		}
		storeOWLS_Object(parameter);

		return parameter;

	}

	public Parameter createParameter(int type) {

		Parameter parameter = null;

		switch (type) {
		case OWLS_Builder_Util.INPUT:
			parameter = new InputImpl();
			break;
		case OWLS_Builder_Util.OUTPUT:
			parameter = new OutputImpl();
			break;
		case OWLS_Builder_Util.LOCAL:
			parameter = new LocalImpl();
			break;
		case OWLS_Builder_Util.RESULT_VAR:
			parameter = new ResultVarImpl();
			break;
		}

		return parameter;

	}

	public Input createInput(Individual instance) throws NotInstanceOfParameterException {
		return (Input) createParameter(instance);
	}

	public Output createOutput(Individual instance) throws NotInstanceOfParameterException {
		return (Output) createParameter(instance);
	}

	public Local createLocal(Individual instance) throws NotInstanceOfParameterException {
		return (Local) createParameter(instance);
	}

	public ResultVar createResultVar(Individual instance) throws NotInstanceOfParameterException {
		return (ResultVar) createParameter(instance);
	}

	// Condition
	public Condition createCondition(Individual instance) throws NotInstanceOfExpressionException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Condition)
				return (Condition) obj;
			else
				throw new NotInstanceOfExpressionException("Instance " + obj.getURI() + " not of type Condition");
		}
		Condition condition = new ConditionImpl(instance);
		storeOWLS_Object(condition);
		return condition;

	}

	public Condition createCondition(String uri) throws NotInstanceOfExpressionException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Condition)
				return (Condition) obj;
			else
				throw new NotInstanceOfExpressionException("Instance " + obj.getURI() + " not of type Condition");
		}

		Condition condition = new ConditionImpl(uri);
		storeOWLS_Object(condition);
		return condition;

	}

	public Condition createCondition() {
		return new ConditionImpl();
	}

	// XXX Changed
	// START OF CHANGE
	// Service Context Related
	public Context createContext(Individual instance) throws NotInstanceOfContextException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Context) {
				return (Context) obj;
			} else {
				throw new NotInstanceOfContextException("Instance " + obj.getURI()
						+ " not of type Context");
			}
		}

		Context context = null;
		if (instance.hasRDFType(OWLSServiceContextURI.Context)) {
			context = new ContextImpl(instance);
		} else if (instance.hasRDFType(OWLSServiceContextURI.LocationContext)) {
			context = new LocationContextImpl(instance);
		} else if (instance.hasRDFType(OWLSServiceContextURI.QoSContext)) {
			context = new QoSContextImpl(instance);
		}
		else{
			context = new ContextImpl(instance);
		}
		storeOWLS_Object(context);
		return context;
	}
	
	public LocationContext createLocationContext(Individual instance) throws NotInstanceOfContextException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof LocationContext) {
				return (LocationContext) obj;
			} else {
				throw new NotInstanceOfLocationContextException("Instance " + obj.getURI()
						+ " not of type LocationContext");
			}
		}

		LocationContext locationContext = new LocationContextImpl(instance);
		storeOWLS_Object(locationContext);
		return locationContext;
	}
	
	public QoSContext createQoSContext(Individual instance) throws NotInstanceOfContextException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof QoSContext) {
				return (QoSContext) obj;
			} else {
				throw new NotInstanceOfQoSContextException("Instance " + obj.getURI() + " not of type QoSContext");
			}
		}

		QoSContext qoSContext = new QoSContextImpl(instance);
		storeOWLS_Object(qoSContext);
		return qoSContext;
	}
	
	public ContextRule createContextRule(Individual instance) throws NotInstanceOfContextRuleException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ContextRule) {
				return (ContextRule) obj;
			} else {
				throw new NotInstanceOfContextRuleException("Instance " + obj.getURI()
						+ " not of type ServiceContextRule");
			}
		}

		ContextRule contextRule = new ContextRuleImpl(instance);
		storeOWLS_Object(contextRule);
		return contextRule;
	}
	
	public ContextObject createContextObject(Individual instance)
			throws NotInstanceOfContextObjectException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ContextObject) {
				return (ContextObject) obj;
			} else {
				throw new NotInstanceOfContextObjectException("Instance " + obj.getURI()
						+ " not of type ContextObject");
			}
		}

		ContextObject contextObject = new ContextObjectImpl(instance);
		storeOWLS_Object(contextObject);
		return contextObject;
	}

	public ContextValue createContextValue(Individual instance)
			throws NotInstanceOfContextValueException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ContextValue) {
				return (ContextValue) obj;
			} else {
				throw new NotInstanceOfContextValueException("Instance " + obj.getURI()
						+ " not of type ContextValue");
			}
		}

		ContextValue contextValue = new ContextValueImpl(instance);
		storeOWLS_Object(contextValue);
		return contextValue;
	}

	public ContextType createContextType(Individual instance)
			throws NotInstanceOfContextTypeException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ContextType) {
				return (ContextType) obj;
			} else {
				throw new NotInstanceOfContextTypeException("Instance " + obj.getURI()
						+ " not of type ContextType");
			}
		}

		ContextType contextType = new ContextTypeImpl(instance);
		storeOWLS_Object(contextType);
		return contextType;
	}
	
	public ContextValueDomain createContextValueDomain(Individual instance)
			throws NotInstanceOfContextValueDomainException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ContextType) {
				return (ContextValueDomain) obj;
			} else {
				throw new NotInstanceOfContextValueDomainException("Instance " + obj.getURI()
						+ " not of type ContextValueDomain");
			}
		}

		ContextValueDomain contextValueDomain = new ContextValueDomainImpl(instance);
		storeOWLS_Object(contextValueDomain);
		return contextValueDomain;
	}

	public PropertyValue createPropertyValue(Individual instance)
			throws NotInstanceOfPropertyValueException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof PropertyValue) {
				return (PropertyValue) obj;
			} else {
				throw new NotInstanceOfPropertyValueException("Instance " + obj.getURI()
						+ " not of type PropertyValue");
			}
		}

		PropertyValue propertyValue = new PropertyValueImpl(instance);
		storeOWLS_Object(propertyValue);
		return propertyValue;
	}

	// END OF CHANGE

	// Expression
	public Expression createExpression(Individual instance) throws NotInstanceOfExpressionException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Expression)
				return (Expression) obj;
			else
				throw new NotInstanceOfExpressionException("Instance " + obj.getURI() + " not of type Expression");
		}

		Expression expression = new ExpressionImpl(instance);
		storeOWLS_Object(expression);
		return expression;

	}

	public Expression createExpression(String uri) throws NotInstanceOfExpressionException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Expression)
				return (Expression) obj;
			else
				throw new NotInstanceOfExpressionException("Instance " + obj.getURI() + " not of type Expression");
		}

		Expression expression = new ExpressionImpl(uri);
		storeOWLS_Object(expression);
		return expression;

	}

	public Expression createExpression() {
		return new ExpressionImpl();
	}

	// Expression
	public LogicLanguage createLogicLanguage(Individual instance) throws NotInstanceOfLogicLanguage {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof LogicLanguage)
				return (LogicLanguage) obj;
			else
				throw new NotInstanceOfLogicLanguage("Instance " + obj.getURI() + " not of type Logic Language");
		}
		LogicLanguage logicLanguage = new LogicLanguageImpl(instance);
		storeOWLS_Object(logicLanguage);
		return logicLanguage;

	}

	public LogicLanguage createLogicLanguage(String uri) throws NotInstanceOfLogicLanguage {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof LogicLanguage)
				return (LogicLanguage) obj;
			else
				throw new NotInstanceOfLogicLanguage("Instance " + obj.getURI() + " not of type Logic Language");
		}

		LogicLanguage logicLanguage = new LogicLanguageImpl(uri);
		storeOWLS_Object(logicLanguage);
		return logicLanguage;

	}

	public LogicLanguage createLogicLanguage() {
		return new LogicLanguageImpl();
	}

	// Result
	public Result createResult(Individual instance) throws NotInstanceOfResultException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Result)
				return (Result) obj;
			else
				throw new NotInstanceOfResultException("Instance " + obj.getURI() + " not of type Result");
		}
		Result result = new ResultImpl(instance);
		storeOWLS_Object(result);
		return result;

	}

	public Result createResult(String uri) throws NotInstanceOfResultException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Result)
				return (Result) obj;
			else
				throw new NotInstanceOfResultException("Instance " + obj.getURI() + " not of type Result");
		}
		Result result = new ResultImpl(uri);
		storeOWLS_Object(result);
		return result;

	}

	public Result createResult() {
		return new ResultImpl();

	}

	// ValueOf
	public ValueOf createValueOf(Individual instance) throws NotInstanceOfValueOfException {

		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ValueOf)
				return (ValueOf) obj;
			else
				throw new NotInstanceOfValueOfException("Instance " + obj.getURI() + " not of type ValueOf");
		}
		ValueOf valueOf = new ValueOfImpl(instance);
		storeOWLS_Object(valueOf);
		return valueOf;
	}

	public ValueOf createValueOf(String uri) throws NotInstanceOfValueOfException {

		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof ValueOf)
				return (ValueOf) obj;
			else
				throw new NotInstanceOfValueOfException("Instance " + obj.getURI() + " not of type ValueOf");
		}

		ValueOf valueOf = new ValueOfImpl(uri);
		storeOWLS_Object(valueOf);
		return valueOf;
	}

	public ValueOf createValueOf() {
		return new ValueOfImpl();
	}

	// Binding
	public Binding createBinding(Individual instance) throws NotInstanceOfBindingException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Binding)
				return (Binding) obj;
			else
				throw new NotInstanceOfBindingException("Instance " + obj.getURI() + " not of type Binding");
		}

		Binding binding = new BindingImpl(instance);
		storeOWLS_Object(binding);
		return binding;
	}

	public Binding createBinding(String uri) throws NotInstanceOfBindingException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Binding)
				return (Binding) obj;
			else
				throw new NotInstanceOfBindingException("Instance " + obj.getURI() + " not of type Binding");
		}

		Binding binding = new BindingImpl(uri);
		storeOWLS_Object(binding);
		return binding;
	}

	public Binding createBinding() {
		return new BindingImpl();
	}

	// InputBinding
	public InputBinding createInputBinding(Individual instance) throws NotInstanceOfBindingException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Binding)
				return (InputBinding) obj;
			else
				throw new NotInstanceOfInputBindingException("Instance " + obj.getURI() + " not of type InputBinding");
		}

		InputBinding binding = new InputBindingImpl(instance);
		storeOWLS_Object(binding);
		return binding;
	}

	public InputBinding createInputBinding(String uri) throws NotInstanceOfBindingException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Binding)
				return (InputBinding) obj;
			else
				throw new NotInstanceOfInputBindingException("Instance " + obj.getURI() + " not of type InputBinding");
		}

		InputBinding binding = new InputBindingImpl(uri);
		storeOWLS_Object(binding);
		return binding;
	}

	public InputBinding createInputBinding() {
		return new InputBindingImpl();
	}

	// OutputBinding
	public OutputBinding createOutputBinding(Individual instance) throws NotInstanceOfBindingException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Binding)
				return (OutputBinding) obj;
			else
				throw new NotInstanceOfOutputBindingException("Instance " + obj.getURI() + " not of type OutputBinding");
		}

		OutputBinding binding = new OutputBindingImpl(instance);
		storeOWLS_Object(binding);
		return binding;
	}

	public OutputBinding createOutputBinding(String uri) throws NotInstanceOfBindingException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Binding)
				return (OutputBinding) obj;
			else
				throw new NotInstanceOfOutputBindingException("Instance " + obj.getURI() + " not of type OutputBinding");
		}

		OutputBinding binding = new OutputBindingImpl(uri);
		storeOWLS_Object(binding);
		return binding;
	}

	public OutputBinding createOutputBinding() {
		return new OutputBindingImpl();
	}

	// process
	public Process createProcess(Individual instance) throws NotInstanceOfProcessException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Process)
				return (Process) obj;
			else
				throw new NotInstanceOfProcessException("Instance " + obj.getURI() + " not of type Process");
		}
		Process process;
		process = processFactory.parseProcess(instance);
		storeOWLS_Object(process);
		return process;
	}

	public Process createProcess(Individual instance, int type) throws NotInstanceOfProcessException {
		return createProcess(instance);
	}

	public Process createProcess(Individual instance, OWLSErrorHandler handler) {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Process)
				return (Process) obj;
			else
				handler.error(new NotInstanceOfProcessException("Instance " + obj.getURI() + " not of type Process"));
		}
		Process process;
		process = processFactory.parseProcess(instance, handler);
		storeOWLS_Object(process);
		return process;
	}

	public Process createProcess(String uri) throws NotInstanceOfProcessException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Process)
				return (Process) obj;
			else
				throw new NotInstanceOfProcessException("Instance " + obj.getURI() + " not of type Process");
		}
		return null;
	}

	public Process createProcess(String uri, int type) throws NotInstanceOfProcessException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Process)
				return (Process) obj;
			else
				throw new NotInstanceOfProcessException("Instance " + obj.getURI() + " not of type Process");
		}
		Process process = null;

		switch (type) {
		case OWLS_Builder_Util.ATOMIC:
			process = new AtomicProcessImpl(uri);
			break;
		case OWLS_Builder_Util.COMPOSITE:
			process = new CompositeProcessImpl(uri);
			break;
		case OWLS_Builder_Util.SIMPLE:
			process = new SimpleProcessImpl(uri);
			break;
		}
		return process;
	}

	public Process createProcess(int type) {

		Process process = null;

		switch (type) {
		case OWLS_Builder_Util.ATOMIC:
			process = new AtomicProcessImpl();
			break;
		case OWLS_Builder_Util.COMPOSITE:
			process = new CompositeProcessImpl();
			break;
		case OWLS_Builder_Util.SIMPLE:
			process = new SimpleProcessImpl();
			break;
		}
		return process;
	}

	// perform
	public Perform createPerform(Individual instance) throws NotInstanceOfPerformException {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof Perform)
				return (Perform) obj;
			else
				throw new NotInstanceOfPerformException("Instance " + obj.getURI() + " not of type Perform");
		}

		if (instance.getURI() != null) {
			if (instance.getURI().equals(OWLSProcessURI.TheParentPerform))
				return PerformImpl.PARENT_PERFORM;

			if (instance.getURI().equals(OWLSProcessURI.ThisPerform))
				return PerformImpl.THIS_PERFORM;
		}
		Perform perform = new PerformImpl(instance);
		storeOWLS_Object(perform);
		return perform;
	}

	public Perform createPerform(String uri) throws NotInstanceOfPerformException {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof Perform)
				return (Perform) obj;
			else
				throw new NotInstanceOfPerformException("Instance " + obj.getURI() + " not of type Perform");
		}
		if (uri != null) {
			if (uri.equals(OWLSProcessURI.TheParentPerform))
				return PerformImpl.PARENT_PERFORM;

			if (uri.equals(OWLSProcessURI.ThisPerform))
				return PerformImpl.THIS_PERFORM;
		}
		Perform perform = new PerformImpl(uri);
		storeOWLS_Object(perform);
		return perform;
	}

	public Perform createPerform() {
		return new PerformImpl();
	}

	// Control Construct
	public ControlConstruct createControlConstruct(Individual instance) throws NotInstanceOfControlConstructException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ControlConstruct)
				return (ControlConstruct) obj;
			else
				throw new NotInstanceOfControlConstructException("Instance " + obj.getURI()
						+ " not of type ControlConstruct");
		}
		ControlConstruct controlConstruct;

		controlConstruct = ControlConstructFactory.extractControlConstruct(instance);

		storeOWLS_Object(controlConstruct);
		return controlConstruct;
	}

	public ControlConstruct createControlConstruct(Individual instance, int type)
			throws NotInstanceOfControlConstructException {
		return createControlConstruct(instance);
	}

	public ControlConstruct createControlConstruct(int type) {
		ControlConstruct control = null;

		switch (type) {
		case OWLS_Builder_Util.SEQUENCE:
			control = new SequenceImpl();
			break;
		case OWLS_Builder_Util.SPLIT:
			control = new SplitImpl();
			break;
		case OWLS_Builder_Util.SPLIT_JOIN:
			control = new SplitJoinImpl();
			break;
		case OWLS_Builder_Util.ANY_ORDER:
			control = new AnyOrderImpl();
			break;
		case OWLS_Builder_Util.CHOICE:
			control = new ChoiceImpl();
			break;
		case OWLS_Builder_Util.IFTHENELSE:
			control = new IfThenElseImpl();
			break;
		}
		return control;
	}

	public ControlConstruct createControlConstruct(String uri, int type) throws NotInstanceOfControlConstructException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof ControlConstruct)
				return (ControlConstruct) obj;
			else
				throw new NotInstanceOfControlConstructException("Instance " + obj.getURI()
						+ " not of type ControlConstruct");
		}

		ControlConstruct control = null;

		switch (type) {
		case OWLS_Builder_Util.SEQUENCE:
			control = new SequenceImpl(uri);
			break;
		case OWLS_Builder_Util.SPLIT:
			control = new SplitImpl(uri);
			break;
		case OWLS_Builder_Util.SPLIT_JOIN:
			control = new SplitJoinImpl(uri);
			break;
		case OWLS_Builder_Util.ANY_ORDER:
			control = new AnyOrderImpl(uri);
			break;
		case OWLS_Builder_Util.CHOICE:
			control = new ChoiceImpl(uri);
			break;
		case OWLS_Builder_Util.IFTHENELSE:
			control = new IfThenElseImpl(uri);
			break;
		}
		storeOWLS_Object(control);
		return control;
	}

	public ControlConstructList createControlConstructList() {
		return new ControlConstructListImpl();
	}

	public ControlConstructList createControlConstructList(Individual instance)
			throws NotInstanceOfControlConstructListException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof ControlConstructList)
				return (ControlConstructList) obj;
			else
				throw new NotInstanceOfControlConstructListException("Instance " + obj.getURI()
						+ " not of type ControlConstructList");
		}
		ControlConstructList controlConstructList;

		controlConstructList = ControlConstructFactory.extractControlConstructList(instance);

		storeOWLS_Object(controlConstructList);
		return controlConstructList;
	}

	public OWLSGroundingModel createOWLSGroundingModel() {
		return new OWLSGroundingModelImpl();
	}

	public OWLSGroundingModel createOWLSGroundingModel(OntModel ontModel) {
		return new OWLSGroundingModelImpl(ontModel);
	}

	public WsdlOperationRef createWSDLOperationRef(Individual instance) throws NotInstanceOfWSDLOperationRefException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof WsdlOperationRef)
				return (WsdlOperationRef) obj;
			else
				throw new NotInstanceOfWSDLOperationRefException("Instance " + obj.getURI()
						+ " not of type WSDL Operation Ref");
		}
		WsdlOperationRef wsdlOperationRef;

		wsdlOperationRef = new WsdlOperationRefImpl(instance);

		storeOWLS_Object(wsdlOperationRef);
		return wsdlOperationRef;
	}

	public WsdlOperationRef createWSDLOperationRef(String uri) throws NotInstanceOfWSDLOperationRefException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof WsdlOperationRef)
				return (WsdlOperationRef) obj;
			else
				throw new NotInstanceOfWSDLOperationRefException("Instance " + obj.getURI()
						+ " not of type WSDL Operation Ref");
		}

		WsdlOperationRef wsdlOperationRef;

		wsdlOperationRef = new WsdlOperationRefImpl(uri);

		storeOWLS_Object(wsdlOperationRef);
		return wsdlOperationRef;
	}

	public WsdlOperationRef createWSDLOperationRef() {
		return new WsdlOperationRefImpl();
	}

	public WsdlAtomicProcessGrounding createWsdlAtomicProcessGrounding(Individual instance)
			throws NotInstanceOfWsdlAtomicProcessGroundingException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null)
			return (WsdlAtomicProcessGrounding) obj;

		WsdlAtomicProcessGrounding wsdlAtomicProcessGrounding;

		wsdlAtomicProcessGrounding = new WsdlAtomicProcessGroundingImpl(instance);

		storeOWLS_Object(wsdlAtomicProcessGrounding);
		return wsdlAtomicProcessGrounding;
	}

	public WsdlAtomicProcessGrounding createWsdlAtomicProcessGrounding(Individual instance, OWLSErrorHandler errHandler) {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null)
			return (WsdlAtomicProcessGrounding) obj;

		WsdlAtomicProcessGrounding wsdlAtomicProcessGrounding;

		wsdlAtomicProcessGrounding = new WsdlAtomicProcessGroundingImpl(instance, errHandler);

		storeOWLS_Object(wsdlAtomicProcessGrounding);
		return wsdlAtomicProcessGrounding;
	}

	public WsdlAtomicProcessGrounding createWsdlAtomicProcessGrounding(String uri)
			throws NotInstanceOfWsdlAtomicProcessGrounding {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof WsdlAtomicProcessGrounding)
				return (WsdlAtomicProcessGrounding) obj;
			else
				throw new NotInstanceOfWsdlAtomicProcessGrounding("Instance " + obj.getURI()
						+ " not of type Wsdl AtomicProcessGrounding");
		}

		WsdlAtomicProcessGrounding wsdlAtomicProcessGrounding;

		wsdlAtomicProcessGrounding = new WsdlAtomicProcessGroundingImpl(uri);

		storeOWLS_Object(wsdlAtomicProcessGrounding);
		return wsdlAtomicProcessGrounding;
	}

	public WsdlAtomicProcessGrounding createWsdlAtomicProcessGrounding() {
		return new WsdlAtomicProcessGroundingImpl();
	}

	public WsdlGrounding createWSDLGrounding(Individual wsdlOpRefInd) throws NotInstanceOfWsdlGroundingException {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(wsdlOpRefInd)) != null) {
			if (obj instanceof WsdlGrounding)
				return (WsdlGrounding) obj;
			else
				throw new NotInstanceOfWsdlGroundingException("Instance " + obj.getURI()
						+ " not of type WSDL Grounding");
		}

		WsdlGrounding wsdlGrounding;

		wsdlGrounding = new WsdlGroundingImpl(wsdlOpRefInd);

		storeOWLS_Object(wsdlGrounding);
		return wsdlGrounding;
	}

	public WsdlGrounding createWSDLGrounding(Individual individual, OWLSErrorHandler errHandler) {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(individual)) != null) {
			if (obj instanceof WsdlGrounding)
				return (WsdlGrounding) obj;
			else {
				errHandler.fatalError(new NotInstanceOfWsdlGroundingException("Instance " + obj.getURI()
						+ " not of type WSDL Grounding"));
			}
		}

		WsdlGrounding wsdlGrounding;

		wsdlGrounding = new WsdlGroundingImpl(individual, errHandler);

		storeOWLS_Object(wsdlGrounding);
		return wsdlGrounding;
	}

	public WsdlGrounding createWSDLGrounding(String uri) throws NotInstanceOfWsdlGroundingException {
		OWLS_Object obj;

		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof WsdlGrounding)
				return (WsdlGrounding) obj;
			else
				throw new NotInstanceOfWsdlGroundingException("Instance " + obj.getURI()
						+ " not of type WSDL Grounding");
		}

		WsdlGrounding wsdlGrounding;

		wsdlGrounding = new WsdlGroundingImpl(uri);

		storeOWLS_Object(wsdlGrounding);
		return wsdlGrounding;
	}

	public WsdlGrounding createWSDLGrounding() {
		return new WsdlGroundingImpl();
	}

	public WsdlInputMessageMap createWsdlInputMessageMap(Individual instance)
			throws NotInstanceOfWsdlMessageMapException {

		OWLS_Object obj;

		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof WsdlInputMessageMap)
				return (WsdlInputMessageMap) obj;
			else
				throw new NotInstanceOfWsdlMessageMapException("Instance " + obj.getURI()
						+ " not of type WSDL Input MessageMap");
		}

		WsdlInputMessageMap wsdlInputMessageMap;

		wsdlInputMessageMap = new WsdlInputMessageMapImpl(instance);

		storeOWLS_Object(wsdlInputMessageMap);
		return wsdlInputMessageMap;
	}

	public WsdlInputMessageMap createWsdlInputMessageMap(String uri) throws NotInstanceOfWsdlMessageMapException {

		OWLS_Object obj;

		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof WsdlInputMessageMap)
				return (WsdlInputMessageMap) obj;
			else
				throw new NotInstanceOfWsdlMessageMapException("Instance " + obj.getURI()
						+ " not of type WSDL Input MessageMap");
		}

		WsdlInputMessageMap wsdlInputMessageMap;

		wsdlInputMessageMap = new WsdlInputMessageMapImpl(uri);

		storeOWLS_Object(wsdlInputMessageMap);
		return wsdlInputMessageMap;
	}

	public WsdlInputMessageMap createWsdlInputMessageMap() {
		return new WsdlInputMessageMapImpl();
	}

	public WsdlOutputMessageMap createWsdlOutputMessageMap(Individual instance)
			throws NotInstanceOfWsdlMessageMapException {
		OWLS_Object obj;
		if ((obj = getOWLS_Object(instance)) != null) {
			if (obj instanceof WsdlOutputMessageMap)
				return (WsdlOutputMessageMap) obj;
			else
				throw new NotInstanceOfWsdlMessageMapException("Instance " + obj.getURI()
						+ " not of type WSDLOutputMessageMap");
		}

		WsdlOutputMessageMap wsdlOutputMessageMap;

		wsdlOutputMessageMap = new WsdlOutputMessageMapImpl(instance);

		storeOWLS_Object(wsdlOutputMessageMap);
		return wsdlOutputMessageMap;
	}

	public WsdlOutputMessageMap createWsdlOutputMessageMap(String uri) throws NotInstanceOfWsdlMessageMapException {

		OWLS_Object obj;

		if ((obj = getOWLS_Object(uri)) != null) {
			if (obj instanceof WsdlOutputMessageMap)
				return (WsdlOutputMessageMap) obj;
			else
				throw new NotInstanceOfWsdlMessageMapException("Instance " + obj.getURI()
						+ " not of type WSDLOutputMessageMap");
		}
		WsdlOutputMessageMap wsdlOutputMessageMap;

		wsdlOutputMessageMap = new WsdlOutputMessageMapImpl(uri);

		storeOWLS_Object(wsdlOutputMessageMap);
		return wsdlOutputMessageMap;
	}

	public WsdlOutputMessageMap createWsdlOutputMessageMap() {

		return new WsdlOutputMessageMapImpl();
	}

	/**
	 * Checks if an object of the given URI already exists in the cache.
	 * 
	 * @param individual
	 * @return
	 */
	public OWLS_Object getOWLS_Object(Individual individual) {

		if (individual == null)
			return null;

		// object id is individual's uri or anonid if the individual is
		// anonymous
		String objID = individual.getURI() != null ? individual.getURI() : individual.getId().toString();
		if (objID == null)
			return null;
		if (symTable.containsKey(objID)) {
			OWLS_Object obj = (OWLS_Object) symTable.getSymbol(objID);
			// if an object of the given URI, but if it doesnot have the
			// individual then return null
			// the above case man occur when you use a owls builder to create a
			// new OWL-S object.
			if (obj.getIndividual() == null)
				return null;
			return (OWLS_Object) obj;
		}
		return null;
	}

	public OWLS_Object getOWLS_Object(String URIorAnonID) {
		if (URIorAnonID == null)
			return null;
		if (symTable.containsKey(URIorAnonID)) {
			return (OWLS_Object) symTable.getSymbol(URIorAnonID);
		}
		return null;
	}

	public void storeOWLS_Object(OWLS_Object owlsObj) {
		// check if the owlsobject's indiviual exist
		Individual individual = owlsObj.getIndividual();
		if (individual == null)
			return;

		String objID = individual.getURI() != null ? individual.getURI() : individual.getId().toString();
		if (objID == null)
			return;

		if (owlsObj.getIndividual().getURI() != null)
			symTable.addSymbol(owlsObj.getIndividual().getURI(), owlsObj);
		else
			symTable.addSymbol(owlsObj.getIndividual().getId().toString(), owlsObj);

	}

	public void reset() {
		symTable = SymbolsTable.newInstance();
		initSymbolTable();
	}

}