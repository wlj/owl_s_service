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
package EDU.pku.sj.rscasd.owls1_1.utils;

import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OWLSServiceContextProperties {

	private static OntModel model = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);

	public static final OntClass Context = model.createClass(OWLSServiceContextURI.Context);

	public static final OntClass ContextRule = model.createClass(OWLSServiceContextURI.ContextRule);

	public static final OntClass LocationContext = model.createClass(OWLSServiceContextURI.LocationContext);

	public static final OntClass QoSContext = model.createClass(OWLSServiceContextURI.QoSContext);

	public static final OntClass ContextType = model.createClass(OWLSServiceContextURI.ContextType);

	public static final OntClass ContextValueDomain = model.createClass(OWLSServiceContextURI.ContextValueDomain);

	public static final OntClass ContextObject = model.createClass(OWLSServiceContextURI.ContextObject);

	public static final OntClass PropertyValue = model.createClass(OWLSServiceContextURI.PropertyValue);

	public static final ObjectProperty hasRuleContent = model.createObjectProperty(
			OWLSServiceContextURI.hasRuleContent, true);

	public static final DatatypeProperty serviceContextName = model
			.createDatatypeProperty(OWLSServiceContextURI.serviceContextName);

	public static final ObjectProperty hasContextValue = model.createObjectProperty(
			OWLSServiceContextURI.hasContextValue, true);

	public static final ObjectProperty hasContextObject = model.createObjectProperty(
			OWLSServiceContextURI.hasContextObject, true);

	public static final ObjectProperty hasContextType = model.createObjectProperty(
			OWLSServiceContextURI.hasContextType, true);

	public static final ObjectProperty hasPropertyType = model.createObjectProperty(
			OWLSServiceContextURI.hasPropertyType, true);

	public static final ObjectProperty hasPropertyValue = model.createObjectProperty(
			OWLSServiceContextURI.hasPropertyValue, true);

	public static final ObjectProperty hasPropertyContextValue = model.createObjectProperty(
			OWLSServiceContextURI.hasPropertyContextValue, true);

	public static final DatatypeProperty hasDomain = model.createDatatypeProperty(OWLSServiceContextURI.hasDomain);

	public static final DatatypeProperty hasDomainValue = model
			.createDatatypeProperty(OWLSServiceContextURI.hasDomainValue);
}