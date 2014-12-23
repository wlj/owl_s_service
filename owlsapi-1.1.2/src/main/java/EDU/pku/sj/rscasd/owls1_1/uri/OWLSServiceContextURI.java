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
package EDU.pku.sj.rscasd.owls1_1.uri;

/**
 * XXX Changed
 * 
 */
public class OWLSServiceContextURI {

	public static final String base = "http://localhost:8080/juddiv3/owl-s/1.1/";

	public static final String profileExt = base + "ProfileExt.owl";
	
	public static final String hasQoSContext = profileExt + "#hasQoSContext";

	public static final String hasLocationContext = profileExt + "#hasLocationContext";

	public static final String hasContext = profileExt + "#hasContext";
	
	public static final String hasContextRule = profileExt + "#hasContextRule";

	public static final String hasRuleContent = profileExt + "#hasRuleContent";
	
	public static final String ContextRule = profileExt + "#ContextRule";
	
	public static final String serviceContext = base + "ServiceContext.owl";

	public static final String Context = serviceContext + "#Context";

	public static final String serviceContextName = serviceContext + "#contextName";

	public static final String hasContextValue = serviceContext + "#hasContextValue";

	public static final String hasContextObject = serviceContext + "#hasContextObject";

	public static final String hasContextType = serviceContext + "#hasContextType";

	public static final String hasPropertyContextValue = serviceContext + "#hasPropertyContextValue";

	public static final String hasPropertyType = serviceContext + "#hasPropertyType";
	
	public static final String hasPropertyValue = serviceContext + "#hasPropertyValue";

	public static final String hasDomain = serviceContext + "#hasDomain";

	public static final String hasDomainValue = serviceContext + "#hasDomainValue";

	public static final String LocationContext = serviceContext + "#LocationContext";

	public static final String QoSContext = serviceContext + "#QoSContext";

	public static final String ContextType = serviceContext + "#ContextType";

	public static final String ContextObject = serviceContext + "#ContextObject";

	public static final String PropertyValue = serviceContext + "#PropertyValue";

	public static final String ContextValue = serviceContext + "#ContextValue";
	
	public static final String ContextValueDomain = serviceContext + "#ContextValueDomain";

}