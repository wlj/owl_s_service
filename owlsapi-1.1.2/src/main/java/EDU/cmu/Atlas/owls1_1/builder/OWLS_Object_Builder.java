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

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfActorException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfBindingException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfControlConstructException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfControlConstructListException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfExpressionException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfLogicLanguage;
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
import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMap;
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
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParameter;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.ServiceProvider;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextObjectException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextTypeException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueDomainException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfContextValueException;
import EDU.pku.sj.rscasd.owls1_1.exception.NotInstanceOfPropertyValueException;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextObject;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextType;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValue;
import EDU.pku.sj.rscasd.owls1_1.profile.ContextValueDomain;
import EDU.pku.sj.rscasd.owls1_1.profile.PropertyValue;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * Interface to create OWLS_Objects. OWLS_Object_Builder should be used to create all OWLS_Object
 * instead of using the OWL-S object implementation directly. The OWLS_Object_Builder maintains a
 * cache of OWLS_Objects with URI generated using the builder and reuses them whenever possible. To
 * clear the cache use the reset function.
 * @author Naveen Srinivasan
 * @see EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory
 */
public interface OWLS_Object_Builder {

    public void reset();

    /**
     * This methods dosenot create new OWL-S Object, instead returns a previously created OWL-S
     * object with given the URI or null if no object with the given URI is found.
     * @param URI
     * @return
     */
    public OWLS_Object createOWLS_Object(String URI);

    //Service

    public OWLSServiceModel createOWLSServiceModel();

    public OWLSServiceModel createOWLSServiceModel(OntModel ontModel);

    public Service createService(Individual instance) throws NotInstanceOfServiceException;

    public Service createService(String URI) throws NotInstanceOfServiceException;

    public Service createService();

    public Service createService(Individual individual, OWLSErrorHandler errHandler);
    
    public ServiceProvider createServiceProvider(String URI, OntClass providerClass)  throws NotInstanceOfServiceProviderException ;
    
    //Profile builder

    public OWLSProfileModel createOWLSProfileModel();

    public OWLSProfileModel createOWLSProfileModel(OntModel ontModel);

    //Actor
    public Actor createActor(Individual instance) throws NotInstanceOfActorException;

    public Actor createActor(String URI) throws NotInstanceOfActorException;

    public Actor createActor();

    //ServiceCategory
    public ServiceCategory createServiceCategory(Individual instance) throws NotInstanceOfServiceCategoryException;

    public ServiceCategory createServiceCategory(String URI) throws NotInstanceOfServiceCategoryException;

    public ServiceCategory createServiceCategory();

    //Service Parameter
    public ServiceParameter createServiceParameter(Individual instance) throws NotInstanceOfServiceParameterException;

    public ServiceParameter createServiceParameter(String URI) throws NotInstanceOfServiceParameterException;

    public ServiceParameter createServiceParameter();

    //Profile
    public Profile createProfile(Individual instance) throws NotInstanceOfProfileException;

    public Profile createProfile(String URI) throws NotInstanceOfProfileException;

    public Profile createProfile();

    public Profile createProfile(Individual instance, OWLSErrorHandler handler);

    //Process Builder

    public OWLSProcessModel createOWLSProcessModel();

    public OWLSProcessModel createOWLSProcessModel(OntModel ontModel);

    //Parameter
    public Parameter createParameter(Individual instance) throws NotInstanceOfParameterException;

    public Parameter createParameter(Individual instance, int type) throws NotInstanceOfParameterException;

    public Parameter createParameter(String uri, int type) throws NotInstanceOfParameterException;

    public Parameter createParameter(String uri) throws NotInstanceOfParameterException;

    public Parameter createParameter(int type);

    public Input createInput(Individual instance) throws NotInstanceOfParameterException;

    public Output createOutput(Individual instance) throws NotInstanceOfParameterException;

    public Local createLocal(Individual instance) throws NotInstanceOfParameterException;
    
    public ResultVar createResultVar(Individual instance) throws NotInstanceOfParameterException;
    

    //Condition
    public Condition createCondition(Individual instance) throws NotInstanceOfExpressionException;

    public Condition createCondition(String uri) throws NotInstanceOfExpressionException;

    public Condition createCondition();

    //Expression
    public Expression createExpression(Individual instance) throws NotInstanceOfExpressionException;

    public Expression createExpression(String uri) throws NotInstanceOfExpressionException;

    public Expression createExpression();

    //LogicLanguage
    public LogicLanguage createLogicLanguage(Individual instance) throws NotInstanceOfLogicLanguage;

    public LogicLanguage createLogicLanguage(String uri) throws NotInstanceOfLogicLanguage;

    public LogicLanguage createLogicLanguage();

    //Result
    public Result createResult(Individual instance) throws NotInstanceOfResultException;

    public Result createResult(String uri) throws NotInstanceOfResultException;

    public Result createResult();

    //ValueOf
    public ValueOf createValueOf(Individual instance) throws NotInstanceOfValueOfException;

    public ValueOf createValueOf(String uri) throws NotInstanceOfValueOfException;

    public ValueOf createValueOf();

    //Binding
    public Binding createBinding(Individual instance) throws NotInstanceOfBindingException;

    public Binding createBinding(String uri) throws NotInstanceOfBindingException;

    public Binding createBinding();
    
    // OutputBinding
    public OutputBinding createOutputBinding(Individual instance) throws NotInstanceOfBindingException;

    public OutputBinding createOutputBinding(String uri) throws NotInstanceOfBindingException;

    public OutputBinding createOutputBinding();

    // InputBinding
    public InputBinding createInputBinding(Individual instance) throws NotInstanceOfBindingException;

    public InputBinding createInputBinding(String uri) throws NotInstanceOfBindingException;

    public InputBinding createInputBinding();

    //Perform
    public Perform createPerform(Individual tempInd) throws NotInstanceOfPerformException;

    public Perform createPerform(String uri) throws NotInstanceOfPerformException;

    public Perform createPerform();

    //Process
    public Process createProcess(Individual processRes) throws NotInstanceOfProcessException;

    public Process createProcess(Individual processRes, OWLSErrorHandler handler);

    public Process createProcess(Individual processRes, int type) throws NotInstanceOfProcessException;

    public Process createProcess(String uri) throws NotInstanceOfProcessException;

    public Process createProcess(String uri, int type) throws NotInstanceOfProcessException;

    public Process createProcess(int type);

    //ControlConstruct
    public ControlConstruct createControlConstruct(Individual controlConstructInd) throws NotInstanceOfControlConstructException;

    public ControlConstruct createControlConstruct(Individual controlConstructInd, int type) throws NotInstanceOfControlConstructException;
    
    public ControlConstruct createControlConstruct(String uri, int type) throws NotInstanceOfControlConstructException;

    public ControlConstruct createControlConstruct(int type);

    public ControlConstructList createControlConstructList();

    public ControlConstructList createControlConstructList(Individual controlConstructInd)
            throws NotInstanceOfControlConstructListException;
    
    //Grounding

    public OWLSGroundingModel createOWLSGroundingModel();

    public OWLSGroundingModel createOWLSGroundingModel(OntModel ontModel);

    //wsdl operation ref
    public WsdlOperationRef createWSDLOperationRef(Individual wsdlOpRefInd) throws NotInstanceOfWSDLOperationRefException;

    public WsdlOperationRef createWSDLOperationRef(String uri) throws NotInstanceOfWSDLOperationRefException;

    public WsdlOperationRef createWSDLOperationRef();

    //wsdl atomice process grounding
    public WsdlAtomicProcessGrounding createWsdlAtomicProcessGrounding(Individual instance, OWLSErrorHandler errHandler);

    public WsdlAtomicProcessGrounding createWsdlAtomicProcessGrounding(Individual wsdlOpRefInd)
            throws NotInstanceOfWsdlAtomicProcessGroundingException;

    public WsdlAtomicProcessGrounding createWsdlAtomicProcessGrounding(String uri) throws NotInstanceOfWsdlAtomicProcessGrounding;

    public WsdlAtomicProcessGrounding createWsdlAtomicProcessGrounding();

    //wsdl grounding
    public WsdlGrounding createWSDLGrounding(Individual individual, OWLSErrorHandler errHandler);

    public WsdlGrounding createWSDLGrounding(Individual wsdlOpRefInd) throws NotInstanceOfWsdlGroundingException;

    public WsdlGrounding createWSDLGrounding(String uri) throws NotInstanceOfWsdlGroundingException;

    public WsdlGrounding createWSDLGrounding();

    //WSDL Input message map
    public WsdlInputMessageMap createWsdlInputMessageMap(Individual wsdlOpRefInd) throws NotInstanceOfWsdlMessageMapException;

    public WsdlInputMessageMap createWsdlInputMessageMap(String uri) throws NotInstanceOfWsdlMessageMapException;

    public WsdlInputMessageMap createWsdlInputMessageMap();

    //WSDL Output message map
    public WsdlOutputMessageMap createWsdlOutputMessageMap(Individual wsdlOpRefInd) throws NotInstanceOfWsdlMessageMapException;

    public WsdlOutputMessageMap createWsdlOutputMessageMap(String uri) throws NotInstanceOfWsdlMessageMapException;

    public WsdlOutputMessageMap createWsdlOutputMessageMap();

	public ContextObject createContextObject(Individual instance) throws NotInstanceOfContextObjectException;
	
	public ContextValueDomain createContextValueDomain(Individual instance) throws NotInstanceOfContextValueDomainException;

	public ContextValue createContextValue(Individual instance) throws NotInstanceOfContextValueException;

	public ContextType createContextType(Individual instance) throws NotInstanceOfContextTypeException;

	public PropertyValue createPropertyValue(Individual instance) throws NotInstanceOfPropertyValueException;

}