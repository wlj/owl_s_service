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

import EDU.cmu.Atlas.owls1_1.uri.OWLSGroundingURI;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author Naveen Srinivasan
 *  
 */
public class OWLSGroundingProperties {

    private static OntModel model = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);

    public static final DatatypeProperty Operation = model.createDatatypeProperty(OWLSGroundingURI.Operation);

    public static final DatatypeProperty PortType = model.createDatatypeProperty(OWLSGroundingURI.PortType);

    public static final DatatypeProperty WsdlMessagePart = model
            .createDatatypeProperty(OWLSGroundingURI.WsdlMessagePart);

    public static final DatatypeProperty XSLTTransformationString = model
            .createDatatypeProperty(OWLSGroundingURI.XSLTTransformationString);

    public static final DatatypeProperty XSLTTransformationURI = model
            .createDatatypeProperty(OWLSGroundingURI.XSLTTransformationURI);

    public static final DatatypeProperty WsdlOutputMessage = model
            .createDatatypeProperty(OWLSGroundingURI.WsdlOutputMessage);

    public static final DatatypeProperty WsdlInputMessage = model
            .createDatatypeProperty(OWLSGroundingURI.WsdlInputMessage);

    public static final DatatypeProperty WsdlDocument = model.createDatatypeProperty(OWLSGroundingURI.WsdlDocument);

    public static final ObjectProperty OwlsParameter = model.createObjectProperty(OWLSGroundingURI.OwlsParameter);

    public static final ObjectProperty HasAtomicProcessGrounding = model
            .createObjectProperty(OWLSGroundingURI.HasAtomicProcessGrounding);

    public static final DatatypeProperty WsdlVersion = model.createDatatypeProperty(OWLSGroundingURI.WsdlVersion);

    public static final ObjectProperty WsdlOutput = model.createObjectProperty(OWLSGroundingURI.WsdlOutput);

    public static final ObjectProperty WsdlInput = model.createObjectProperty(OWLSGroundingURI.WsdlInput);

    public static final ObjectProperty OwlsProcess = model.createObjectProperty(OWLSGroundingURI.OwlsProcess);

    public static final ObjectProperty WsdlOperation = model.createObjectProperty(OWLSGroundingURI.WsdlOperation);

}