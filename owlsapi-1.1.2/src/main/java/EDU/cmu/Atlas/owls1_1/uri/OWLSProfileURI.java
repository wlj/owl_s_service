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
package EDU.cmu.Atlas.owls1_1.uri;

/**
 * @author Naveen Srinivasan
 *  
 */
public class OWLSProfileURI {

    //Profile.owl
    public static final String base = "http://localhost:8080/juddiv3/owl-s/1.1/";//http://www.daml.org/services/owl-s/1.1/

    public static final String profile = base + "Profile.owl";

    public static final String Profile = profile + "#Profile";

    public static final String serviceName = profile + "#serviceName";

    public static final String textDescription = profile + "#textDescription";

	public static final String has_process = profile + "#has_process";
	
    //Service Parameter
    public static final String ServiceParameter = profile + "#ServiceParameter";

    public static final String serviceParameter = profile + "#serviceParameter";

    public static final String serviceParameterName = profile + "#serviceParameterName";

    public static final String sParameter = profile + "#sParameter";

    //Quality Rating
    public static final String qualityRating = profile + "#qualityRating";

    public static final String QualityRating = profile + "#QualityRating";

    public static final String ratingName = profile + "#ratingName";

    public static final String rating = profile + "#rating";

    //Service Category
    public static final String serviceCategory = profile + "#serviceCategory";

    public static final String ServiceCategory = profile + "#ServiceCategory";

    public static String categoryName = profile + "#categoryName";

    public static String taxonomy = profile + "#taxonomy";

    public static final String code = profile + "#code";

    public static final String value = profile + "#value";

    //Service Classification and Service Product
    public static final String serviceClassification = profile + "#serviceClassification";

    public static final String serviceProduct = profile + "#serviceProduct";

    //Input, Output & Result
    public static final String hasInput = profile + "#hasInput";

    public static final String hasOutput = profile + "#hasOutput";

    public static final String hasResult = profile + "#hasResult";

    public static final String hasPrecondition = profile + "#hasPrecondition";

    //Actor
    public static final String contactInformation = profile + "#contactInformation";

    public static final String actorDefault = "http://localhost:8080/juddiv3/owl-s/1.1/ActorDefault.owl";//http://www.daml.org/services/owl-s/1.1/

    public static final String Actor = actorDefault + "#Actor";

    public static final String actor_name = actorDefault + "#name";

    public static final String actor_title = actorDefault + "#title";

    public static final String actor_phone = actorDefault + "#phone";

    public static final String actor_fax = actorDefault + "#fax";

    public static final String actor_email = actorDefault + "#email";

    public static final String actor_address = actorDefault + "#physicalAddress";

    public static final String actor_weburl = actorDefault + "#webURL";

    //ProfileAdditionalParameters
    public static final String additionalParameter = "http://localhost:8080/juddiv3/owl-s/1.1/ProfileAdditionalParameters.owl";//http://www.daml.org/services/owl-s/1.1/

    public static final String GeographicRadius = additionalParameter + "#GeographicRadius";

    public static final String NAICS = additionalParameter + "#NAICS";

    public static final String UNSPSC = additionalParameter + "#UNSPSC";

}