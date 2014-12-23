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
package EDU.cmu.Atlas.owls1_1.writer;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProfileParser;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFSyntax;

/**
 * @author Naveen Srinivasan
 */
public class OWLSWriterUtils {

    public static Individual checkIfIndividualExist(OWLS_Object object, OntModel ontModel) {


// NOTE:	following code caused problems with writing of existing models    	
/*        if(object.getIndividual() != null)
            return object.getIndividual();
*/        
        if (object.getURI() != null) {
            Individual objectInd = ontModel.getIndividual(object.getURI());
            if (objectInd != null)
                return objectInd;
        }
        return null;
    }

    public static Individual createIndividual(OntClass ontClass, OWLS_Object object, String base) {
        OntModel ontModel = (OntModel) ontClass.getModel();
        if (object.getURI() == null)
            return ontModel.createIndividual(ontClass);
        else {
            String uri = object.getURI();
            if (uri.indexOf("#") == -1)
                uri = base + "#" + uri;
            return ontModel.createIndividual(uri, ontClass);
        }

    }

    public static RDFWriter getWriter(OntModel ontModel, String xmlbase) {

        RDFWriter writer = ontModel.getWriter("RDF/XML-ABBREV");
        //RDFWriter writer = baseModel.getWriter("RDF/XML");
        writer.setProperty("showXmlDeclaration", "true");
        writer.setProperty("tab", "4");
        writer.setProperty("xmlbase", xmlbase);
        writer.setProperty("relativeURIs", "same-document");
        writer.setProperty("blockRules", new Resource[] { RDFSyntax.propertyAttr });

        return writer;
    }

    public static OntModel getNewOntModel() {
        OntModel ontModel = ModelFactory.createOntologyModel();
        return getNewOntModel(ontModel);
    }
    
    public static OntModel getNewOntModel(OntModel ontModel) {

        OntDocumentManager docMgr = ontModel.getDocumentManager();
        
        // following code caused problems when reading alternative entries 
        // from the jar package

//        String base1 = "file:EDU/cmu/Atlas/owls1_1/owlsfiles/";
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Service.owl", base1 + "Service.owl");
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Profile.owl", base1 + "Profile.owl");
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Process.owl", base1 + "Process.owl");
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Grounding.owl", base1 + "Grounding.owl");
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/ProfileAdditionalParameters.owl", base1 + "ProfileAdditionalParameters.owl");
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/ActorDefault.owl", base1 + "ActorDefault.owl");
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/generic/Expression.owl", base1 + "Expression.owl");
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/generic/ObjectList.owl", base1 + "ObjectList.owl");
        
        
//            String base = "EDU/cmu/Atlas/owls1_1/owlsfiles/";
            
//            ClassLoader loader = OWLSProfileParser.class.getClassLoader();
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Service.owl", loader.getResource(base + "Service.owl").toString());
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Profile.owl", loader.getResource(base + "Profile.owl").toString());
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Process.owl", loader.getResource(base + "Process.owl").toString());
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Grounding.owl", loader.getResource(base + "Grounding.owl").toString());
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/ProfileAdditionalParameters.owl", loader.getResource(
//        	    base + "ProfileAdditionalParameters.owl").toString());
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/ActorDefault.owl", loader.getResource(base + "ActorDefault.owl")
//        	    .toString());
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/generic/Expression.owl", loader.getResource(base + "Expression.owl")
//        	    .toString());
//            docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/generic/ObjectList.owl", loader.getResource(base + "ObjectList.owl")
//        	    .toString());
            
//            ClassLoader loader = OWLSProfileParser.class.getClassLoader();
//            OntModel m = ModelFactory.createOntologyModel();
//            m.read(loader.getResourceAsStream(base + "Service.owl"),"http://www.daml.org/services/owl-s/1.1/Service.owl"); 
//            docMgr.addModel("http://www.daml.org/services/owl-s/1.1/Service.owl", m);
//            addAltModelFromLocalSrc("http://www.daml.org/services/owl-s/1.1/Service.owl", loader.getResourceAsStream(base + "Service.owl"), docMgr);
//            addAltModelFromLocalSrc("http://www.daml.org/services/owl-s/1.1/Profile.owl", loader.getResourceAsStream(base + "Profile.owl"), docMgr);
//            addAltModelFromLocalSrc("http://www.daml.org/services/owl-s/1.1/Process.owl", loader.getResourceAsStream(base + "Process.owl"), docMgr);
//            addAltModelFromLocalSrc("http://www.daml.org/services/owl-s/1.1/Grounding.owl", loader.getResourceAsStream(base + "Grounding.owl"), docMgr);
//            addAltModelFromLocalSrc("http://www.daml.org/services/owl-s/1.1/ProfileAdditionalParameters.owl", loader.getResourceAsStream(
//        	    base + "ProfileAdditionalParameters.owl"), docMgr);
//            addAltModelFromLocalSrc("http://www.daml.org/services/owl-s/1.1/ActorDefault.owl", loader.getResourceAsStream(base + "ActorDefault.owl")
//        	    , docMgr);
//            addAltModelFromLocalSrc("http://www.daml.org/services/owl-s/1.1/generic/Expression.owl", loader.getResourceAsStream(base + "Expression.owl")
//        	    , docMgr);
//            addAltModelFromLocalSrc("http://www.daml.org/services/owl-s/1.1/generic/ObjectList.owl", loader.getResourceAsStream(base + "ObjectList.owl")
//        	    , docMgr);
            
            
        return ontModel;
    }
    
    private static void addAltModelFromLocalSrc(String base, InputStream in, OntDocumentManager docMgr) {
        OntModel m;
        if (docMgr.getModel(base) == null) {
            m = ModelFactory.createOntologyModel();
            m.read(in,base); 
            docMgr.addModel(base, m);
        }
    }
}