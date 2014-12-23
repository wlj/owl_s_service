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

import EDU.cmu.Atlas.owls1_1.uri.OWLSProfileURI;
import EDU.pku.sj.rscasd.owls1_1.uri.OWLSServiceContextURI;

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
public class OWLSProfileProperties {

    private static OntModel model = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);

    public static final OntClass Profile = model.createClass(OWLSProfileURI.Profile);

    public static final DatatypeProperty serviceName = model.createDatatypeProperty(OWLSProfileURI.serviceName, true);

    public static final ObjectProperty textDescription = model.createObjectProperty(OWLSProfileURI.textDescription, true);

    public static final ObjectProperty has_process = model.createObjectProperty(OWLSProfileURI.has_process, true);
	// XXX Changed
	public static final ObjectProperty hasQoSContext = model.createObjectProperty(OWLSServiceContextURI.hasQoSContext,
			true);
	// XXX Changed
	public static final ObjectProperty hasLocationContext = model.createObjectProperty(
			OWLSServiceContextURI.hasLocationContext, true);
	// XXX Changed
	public static final ObjectProperty hasContext = model.createObjectProperty(OWLSServiceContextURI.hasContext, true);
	// XXX Changed
	public static final ObjectProperty hasContextRule = model.createObjectProperty(
			OWLSServiceContextURI.hasContextRule, true);

    public static final ObjectProperty hasInput = model.createObjectProperty(OWLSProfileURI.hasInput);

    public static final ObjectProperty hasOutput = model.createObjectProperty(OWLSProfileURI.hasOutput);

    public static final ObjectProperty hasPrecondition = model.createObjectProperty(OWLSProfileURI.hasPrecondition);

    public static final ObjectProperty hasResult = model.createObjectProperty(OWLSProfileURI.hasResult);

    public static final ObjectProperty contactInformation = model.createObjectProperty(OWLSProfileURI.contactInformation);

    public static final DatatypeProperty actor_name = model.createDatatypeProperty(OWLSProfileURI.actor_name);

    public static final DatatypeProperty actor_title = model.createDatatypeProperty(OWLSProfileURI.actor_title);

    public static final DatatypeProperty actor_phone = model.createDatatypeProperty(OWLSProfileURI.actor_phone);

    public static final DatatypeProperty actor_fax = model.createDatatypeProperty(OWLSProfileURI.actor_fax);

    public static final DatatypeProperty actor_email = model.createDatatypeProperty(OWLSProfileURI.actor_email);

    public static final DatatypeProperty actor_address = model.createDatatypeProperty(OWLSProfileURI.actor_address);

    public static final DatatypeProperty actor_weburl = model.createDatatypeProperty(OWLSProfileURI.actor_weburl);

    public static final Property serviceParameter = model.createProperty(OWLSProfileURI.serviceParameter);

    public static final DatatypeProperty serviceParameterName = model.createDatatypeProperty(OWLSProfileURI.serviceParameterName);

    public static final Property sParameter = model.createProperty(OWLSProfileURI.sParameter);

    //Service Category
    public static final OntClass ServiceCategory = model.createClass(OWLSProfileURI.ServiceCategory);

    public static final Property serviceCategory = model.createProperty(OWLSProfileURI.serviceCategory);

    public static final DatatypeProperty categoryName = model.createDatatypeProperty(OWLSProfileURI.categoryName);

    public static final DatatypeProperty taxonomy = model.createDatatypeProperty(OWLSProfileURI.taxonomy);

    public static final DatatypeProperty code = model.createDatatypeProperty(OWLSProfileURI.code);

    public static final DatatypeProperty value = model.createDatatypeProperty(OWLSProfileURI.value);

    //Service Classification
    public static final Property serviceClassfication = model.createDatatypeProperty(OWLSProfileURI.serviceClassification);

    //Service Product
    public static final Property serviceProduct = model.createDatatypeProperty(OWLSProfileURI.serviceProduct);
}