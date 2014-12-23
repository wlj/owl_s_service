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

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
/**
 * @author Naveen Srinivasan
 *
 */
public class OWLSProperties {


	private static OntModel model = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);

	public static final OntClass Profile = model.createClass(OWLSURI.Profile);

	public static final DatatypeProperty serviceName = model.createDatatypeProperty(OWLSURI.serviceName, true);

	public static final ObjectProperty textDescription = model.createObjectProperty(OWLSURI.textDescription, true);

	public static final ObjectProperty has_process = model.createObjectProperty(OWLSURI.has_process, true);

	

	public static final ObjectProperty hasInput = model.createObjectProperty(OWLSURI.hasInput);
	public static final ObjectProperty hasOutput = model.createObjectProperty(OWLSURI.hasOutput);
	public static final ObjectProperty hasEffect = model.createObjectProperty(OWLSURI.hasEffect);
	public static final ObjectProperty hasPrecondition = model.createObjectProperty(OWLSURI.hasPrecondition);


	public static final ObjectProperty contactInformation = model.createObjectProperty(OWLSURI.contactInformation);


	public static final DatatypeProperty actor_name = model.createDatatypeProperty(OWLSURI.actor_name);
	public static final DatatypeProperty actor_title = model.createDatatypeProperty(OWLSURI.actor_title);
	public static final DatatypeProperty actor_phone = model.createDatatypeProperty(OWLSURI.actor_phone);
	public static final DatatypeProperty actor_fax = model.createDatatypeProperty(OWLSURI.actor_fax);
	public static final DatatypeProperty actor_email = model.createDatatypeProperty(OWLSURI.actor_email);
	public static final DatatypeProperty actor_address = model.createDatatypeProperty(OWLSURI.actor_address);
	public static final DatatypeProperty actor_weburl = model.createDatatypeProperty(OWLSURI.actor_weburl);


	public static final Property  serviceParameter =  	model.createProperty(OWLSURI.serviceParameter);
	public static final DatatypeProperty  serviceParameterName =   	model.createDatatypeProperty(OWLSURI.serviceParameterName);
	public static final Property sParameter =  	model.createProperty(OWLSURI.sParameter);


	
	public static final OntClass ServiceCategory = model.createClass(OWLSURI.ServiceCategory);	
	public static final Property serviceCategory = model.createProperty(OWLSURI.serviceCategory);
	public static final DatatypeProperty  categoryName = model.createDatatypeProperty(OWLSURI.categoryName);
	public static final DatatypeProperty  taxonomy = model.createDatatypeProperty(OWLSURI.taxonomy);
	public static final DatatypeProperty  code = model.createDatatypeProperty(OWLSURI.code);
	public static final DatatypeProperty  value = model.createDatatypeProperty(OWLSURI.value);
	

	public static final Property parameterType = 	model.createProperty(OWLSURI.parameterType);
	//public static final ObjectProperty coCondition = model.createObjectProperty(OWLSURI.coCondition);
	public static final ObjectProperty ceEffect = model.createObjectProperty(OWLSURI.ceeffect);
	public static final ObjectProperty ceCondition = model.createObjectProperty(OWLSURI.ceCondition);


}
