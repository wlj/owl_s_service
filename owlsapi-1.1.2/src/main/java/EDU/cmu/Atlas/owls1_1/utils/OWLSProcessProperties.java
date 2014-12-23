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

import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * @author Naveen Srinivasan
 *  
 */
public class OWLSProcessProperties {

    private static OntModel model = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);

    public static final ObjectProperty hasInput = model.createObjectProperty(OWLSProcessURI.hasInput);

    public static final ObjectProperty hasOutput = model.createObjectProperty(OWLSProcessURI.hasOutput);

    public static final ObjectProperty hasPrecondition = model.createObjectProperty(OWLSProcessURI.hasPrecondition);

    public static final ObjectProperty  hasLocal = model.createObjectProperty(OWLSProcessURI.hasLocal);
    
    public static final ObjectProperty hasResult = model.createObjectProperty(OWLSProcessURI.hasResult);

    public static final Property parameterType = model.createProperty(OWLSProcessURI.parameterType);

    public static final Property parameterValue = model.createProperty(OWLSProcessURI.parameterValue);

    public static final ObjectProperty hasResultVar = model.createObjectProperty(OWLSProcessURI.hasResultVar);

    public static final ObjectProperty inCondition = model.createObjectProperty(OWLSProcessURI.inCondition);

    public static final ObjectProperty hasEffect = model.createObjectProperty(OWLSProcessURI.processHasEffect);

    public static final ObjectProperty withOutput = model.createObjectProperty(OWLSProcessURI.withOutput);

    public static final ObjectProperty theVar = model.createObjectProperty(OWLSProcessURI.theVar);

    public static final ObjectProperty fromProcess = model.createObjectProperty(OWLSProcessURI.fromProcess);

    public static final ObjectProperty toParam = model.createObjectProperty(OWLSProcessURI.toParam);

    public static final ObjectProperty valueSource = model.createObjectProperty(OWLSProcessURI.valueSource);

    public static final DatatypeProperty valueData = model.createDatatypeProperty(OWLSProcessURI.valueData);

    public static final DatatypeProperty valueSpecifier = model.createDatatypeProperty(OWLSProcessURI.valueSpecifier);
    
    public static final DatatypeProperty valueType = model.createDatatypeProperty(OWLSProcessURI.valueType);

    public static final ObjectProperty components = model.createObjectProperty(OWLSProcessURI.components);

    public static final ObjectProperty processPerformProperty = model.createObjectProperty(OWLSProcessURI.processPerformProperty);

    public static final ObjectProperty composedOf = model.createObjectProperty(OWLSProcessURI.composedOf);

    //shadow rdf list
    public static final ObjectProperty shadow_rdf_first = model.createObjectProperty(OWLSProcessURI.shadow_rdf_first);

    public static final ObjectProperty shadow_rdf_rest = model.createObjectProperty(OWLSProcessURI.shadow_rdf_rest);

    //Simple Process
    public static final ObjectProperty collapsesTo = model.createObjectProperty(OWLSProcessURI.collapsesTo);

    public static final ObjectProperty expandsTo = model.createObjectProperty(OWLSProcessURI.expandsTo);

    public static final ObjectProperty realizedBy = model.createObjectProperty(OWLSProcessURI.realizedBy);

    public static final ObjectProperty realizes = model.createObjectProperty(OWLSProcessURI.realizes);

    public static final DatatypeProperty processName = model.createDatatypeProperty(OWLSProcessURI.processName);

    public static final ObjectProperty hasDataFrom = model.createObjectProperty(OWLSProcessURI.hasDataFrom);

    //If-then-else
    public static final ObjectProperty then = model.createObjectProperty(OWLSProcessURI.then);

    public static final ObjectProperty ifCondition = model.createObjectProperty(OWLSProcessURI.ifCondition);;

    public static final ObjectProperty process_else = model.createObjectProperty(OWLSProcessURI.process_else);

    //Expression
    public static final DatatypeProperty refURI = model.createDatatypeProperty(OWLSProcessURI.refURI);

    public static final DatatypeProperty expressionBody = model.createDatatypeProperty(OWLSProcessURI.expressionBody);

    public static final ObjectProperty expressionLanguage = model.createObjectProperty(OWLSProcessURI.expressionLanguage);
    
    //Repeat-Until
    public static final ObjectProperty untilCondition = model.createObjectProperty(OWLSProcessURI.untilcondition);
    
    public static final ObjectProperty untilProcess = model.createObjectProperty(OWLSProcessURI.untilprocess);
    
    public static final ObjectProperty whileCondition = model.createObjectProperty(OWLSProcessURI.whilecondition);
    
    public static final ObjectProperty whileProcess = model.createObjectProperty(OWLSProcessURI.whileprocess);

}