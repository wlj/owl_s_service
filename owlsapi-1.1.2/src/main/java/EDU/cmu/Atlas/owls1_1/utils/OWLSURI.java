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
package EDU.cmu.Atlas.owls1_1.utils;


/**
 * @author Naveen Srinivasan
 *
 */
public class OWLSURI {
	
	

	public static final String base = "http://www.daml.org/services/owl-s/1.1/";
	public static final String profile = base + "Profile.owl";

	public static final String Profile= profile + "#Profile";
	public static final String serviceName = profile + "#serviceName";
	public static final String textDescription = profile + "#textDescription";
	public static final String has_process = profile + "#has_process";
	
	public static final String ServiceParameter =  	profile + "#ServiceParameter";
	public static final String serviceParameter =  	profile + "#serviceParameter";
	public static final String serviceParameterName =  	profile + "#serviceParameterName";
	public static final String sParameter =  	profile + "#sParameter";

	public static final String qualityRating = profile + "#qualityRating";
	public static final String QualityRating = profile + "#QualityRating";
	public static final String ratingName = profile + "#ratingName";
	public static final String rating = profile + "#rating";
	
	
	public static final String serviceCategory = profile + "#serviceCategory";
	public static final String ServiceCategory = profile + "#ServiceCategory";	
	public static String categoryName = profile + "#categoryName";
	public static String taxonomy = profile + "#taxonomy";
	public static final String code = profile + "#code";
	public static final String value = profile + "#value";
	
	
	
	public static final String hasInput = profile + "#hasInput";
	public static final String hasOutput = profile + "#hasOutput";
	public static final String hasEffect = profile + "#hasEffect";
	public static final String hasPrecondition = profile + "#hasPrecondition";

	public static final String contactInformation = profile + "#contactInformation";
	
	
	public static final String actorDefault = "http://www.daml.org/services/owl-s/1.1/ActorDefault.owl";
	public static final String Actor = actorDefault + "#Actor";
	public static final String actor_name = actorDefault + "#name";
	public static final String actor_title = actorDefault +"#title";
	public static final String actor_phone = actorDefault +"#phone";
	public static final String actor_fax = actorDefault +"#fax";
	public static final String actor_email = actorDefault +"#email";
	public static final String actor_address= actorDefault +"#physicalAddress";
	public static final String actor_weburl= actorDefault +"#webURL";	


	public static final String additionalParameter = "http://www.daml.org/services/owl-s/1.1/ProfileAdditionalParameters.owl";
	public static final String GeographicRadius = additionalParameter + "#GeographicRadius";
	public static final String NAICS =  	additionalParameter + "#NAICS";
	public static final String UNSPSC =  	additionalParameter + "#UNSPSC";


	public static final String process = base + "Process.owl";	

	public static final String processHasParameter = process + "#hasParameter";
	public static final String processHasInput = process + "#hasInput";
	public static final String processHasOutput = process + "#hasOutput";
	public static final String processHasEffect = process + "#hasEffect";
	public static final String processHasPrecondition = process + "#hasPrecondition";
	public static final String processHasResult = process + "#hasResult";

	public static final String Input = process + "#Input";
	public static final String Output = process + "#Output";
	public static final String Effect = process + "#Effect";
	
//	public static final String ConditionalOutput = process + "#ConditionalOutput";
//	public static final String coCondition = process + "#coCondition";
//	public static final String UnConditionalOutput = process + "#UnConditionalOutput";
//	public static final String ConditionalEffect = process + "#ConditionalEffect";
//	public static final String UnConditionalEffect = process + "#UnConditionalEffect";
	
	public static final String PreCondition = process + "#PreCondition";
	public static final String parameterType = process + "#parameterType";
	public static final String ceeffect = process + "#ceEffect";
	public static final String ceCondition = process + "#ceCondition"; 
	
	
	public static final String Process = process + "#Process";
	public static final String processName = process + "#name";
	
	public static final String AtomicProcess = process + "#AtomicProcess";
	public static final String SimpleProcess = process + "#SimpleProcess";
	public static final String CompositeProcess = process + "#CompositeProcess";
	
	public static final String ProcessModel = process + "#ProcessModel";
	public static final String hasProcess = process + "#hasProcess";

	public static final String  composedOf = process + "#composedOf";
	public static final String  ControlConstruct = process + "#ControlConstruct";
	
	public static final String  components = process + "#components";
	public static final String ProcessComponent = process + "#ProcessComponent";

	public static final String ProcessComponentList = process + "#ProcessComponentList";
	public static final String ProcessComponentBag = process + "#ProcessComponentBag";

	public static final String Sequence = process + "#Sequence";
	public static final String Split = process + "#Split";
	public static final String Split_JOIN = process + "#Split-Join";
	public static final String Unordered = process + "#Unordered";
	public static final String Any_Order = process + "#Any-Order";
	public static final String Choice = process + "#Choice";
	public static final String If_Then_Else = process + "#If-Then-Else";
	public static final String Iterate = process + "#Iterate";
	
	public static final String ifCondition = process + "#ifCondition";
	public static final String then = process + "#then";
	public static final String process_else = process + "#else";

	public static final String collapsesTo = process + "#collapsesTo";
	public static final String invocable = process + "#invocable";
	
	public static final String first= "http://www.w3.org/1999/02/22-rdf-syntax-ns#first";
	
	public static final String rest= "http://www.w3.org/1999/02/22-rdf-syntax-ns#rest";
	
	public static final String rdfType = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	
	// shadow RDF list
	public static final String shadow_rdf= "http://www.daml.org/services/owl-s/1.1/generic/ObjectList.owl";
	
	public static final String shadow_rdf_first= shadow_rdf+"#first";
	
	public static final String shadow_rdf_rest= shadow_rdf+"#rest";
	
	public static final String shadow_rdf_nil= shadow_rdf+"#nil";
	
	public static String Perform = process + "#Perform";
	
	public static String hasBinding = process + "#hasBinding";
	
	public static String processPerformProperty = process + "#process";
	
	public static String theParam = process + "#theParam";
	
	public static String valueForm = process + "#valueForm";
}
