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
 */
public class OWLSGroundingURI {
	
	public static String base = "http://localhost:8080/juddiv3/owl-s/1.1/";
	
    public static String Grounding = base + "Grounding.owl";//http://www.daml.org/services/owl-s/1.1/Grounding.owl

    public static String WsdlGrounding = Grounding + "#WsdlGrounding";

    public static String WsdlAtomicProcessGrounding = Grounding + "#WsdlAtomicProcessGrounding";

    public static String HasAtomicProcessGrounding = Grounding + "#hasAtomicProcessGrounding";

    public static String OwlsProcess = Grounding + "#owlsProcess";

    public static String WsdlOperation = Grounding + "#wsdlOperation";

    public static String WsdlInputMessage = Grounding + "#wsdlInputMessage";

    public static String WsdlInput = Grounding + "#wsdlInput";

    public static String WsdlOutputMessage = Grounding + "#wsdlOutputMessage";

    public static String WsdlOutputMessageMap = Grounding + "#WsdlOutputMessageMap";

    public static String WsdlInputMessageMap = Grounding + "#WsdlInputMessageMap";

    public static String WsdlOutput = Grounding + "#wsdlOutput";

    public static String WsdlDocument = Grounding + "#wsdlDocument";

    public static String WsdlVersion = Grounding + "#wsdlVersion";

    public static String WsdlOperationRef = Grounding + "#WsdlOperationRef";

    public static String Operation = Grounding + "#operation";

    public static String PortType = Grounding + "#portType";

    public static String OwlsParameter = Grounding + "#owlsParameter";

    public static String WsdlMessagePart = Grounding + "#wsdlMessagePart";

    public static String XSLTTransformationURI = Grounding + "#xsltTransformationURI";

    public static String XSLTTransformationString = Grounding + "#xsltTransformationString";

}