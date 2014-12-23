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
package EDU.cmu.Atlas.owls1_1.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.uri.OWLSServiceURI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * @author Naveen Srinivasan
 */
public class OWLSServiceParser {

    private OntModel ontModel;

    private OWLS_Object_Builder objectBuilder;

    private OWLS_Store_Builder storeBuilder;

    static Logger logger = Logger.getLogger(OWLSProfileParser.class);

    public OWLSServiceParser() {
        this(true);
    }

    public OWLSServiceParser(boolean imports) {
        this(ModelFactory.createOntologyModel());
        ontModel.setDynamicImports(imports);

        
/*        OntDocumentManager docMgr = ontModel.getDocumentManager();
        String base = "EDU/cmu/Atlas/owls1_1/owlsfiles/";
        ClassLoader loader = OWLSProfileParser.class.getClassLoader();
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Service.owl", loader.getResource(base + "Service.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Profile.owl", loader.getResource(base + "Profile.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Process.owl", loader.getResource(base + "Process.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/Grounding.owl", loader.getResource(base + "Grounding.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/ProfileAdditionalParameters.owl", loader.getResource(
                base + "ProfileAdditionalParameters.owl").toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/ActorDefault.owl", loader.getResource(base + "ActorDefault.owl")
                .toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/generic/Expression.owl", loader.getResource(base + "Expression.owl")
                .toString());
        docMgr.addAltEntry("http://www.daml.org/services/owl-s/1.1/generic/ObjectList.owl", loader.getResource(base + "ObjectList.owl")
                .toString());

*/        
    }

    public OWLSServiceParser(OntModel ontModel) {
        this.ontModel = ontModel;
        //removing objects from the symbol table
        objectBuilder = OWLS_Object_BuilderFactory.instance();
        objectBuilder.reset();
        storeBuilder = OWLS_Store_BuilderFactory.instance();

    }

    public OWLSServiceModel read(String URI) throws IOException, NotInstanceOfServiceException {
        return read(URI, "");
    }

    public OWLSServiceModel read(Reader reader) throws NotInstanceOfServiceException {
        return read(reader, "");
    }

    public OWLSServiceModel read(String URI, String base) throws IOException, NotInstanceOfServiceException {

        URL url = new URL(URI);
        InputStream is = url.openStream();
        ontModel.addLoadedImport(URI);
        return read(new InputStreamReader(is), base);
    }

    public OWLSServiceModel read(Reader reader, String base) throws NotInstanceOfServiceException {

        OWLSServiceModel srvModel = objectBuilder.createOWLSServiceModel(ontModel);
        try {
        	ontModel.read(reader, base);
        } catch (Exception e) {
        	throw new NotInstanceOfServiceException("Error while parsing the service files", e);
        }
        ServiceList list = extractServiceList(ontModel);
        if (list==null) {
        	throw new NotInstanceOfServiceException("There was no service instance found at the given URL: " + base);
        }
        srvModel.setServiceList(list);
        return srvModel;

    }

    public OWLSServiceModel read(String URI, OWLSErrorHandler errHandler) {
        return read(URI, "", errHandler);
    }

    public OWLSServiceModel read(Reader reader, OWLSErrorHandler errHandler) {
        return read(reader, "", errHandler);
    }

    public OWLSServiceModel read(String URI, String base, OWLSErrorHandler errHandler) {

        URL url;
        try {
            url = new URL(URI);
        } catch (MalformedURLException e) {
            errHandler.fatalError(e);
            return null;
        }
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e1) {
            errHandler.fatalError(e1);
            return null;
        }
        ontModel.addLoadedImport(URI);
        return read(new InputStreamReader(is), base, errHandler);
    }

    public OWLSServiceModel read(Reader reader, String base, OWLSErrorHandler errHandler) {

        OWLSServiceModel srvModel = objectBuilder.createOWLSServiceModel(ontModel);
        ontModel.read(reader, base);
        ServiceList list = extractServiceList(ontModel, errHandler);
        srvModel.setServiceList(list);
        return srvModel;

    }

    public OWLSServiceModel read(OntModel model) throws NotInstanceOfServiceException {

        OWLSServiceModel owlsModel = objectBuilder.createOWLSServiceModel(model);
        ontModel = model;
        ServiceList list = extractServiceList(ontModel);
        if (list==null) {
        	new NotInstanceOfServiceException("There was no service instance found in the given model." );
        }
        owlsModel.setServiceList(list);
        return owlsModel;

    }

    public OWLSServiceModel read(OntModel model, OWLSErrorHandler errHandler) {

        OWLSServiceModel owlsModel = objectBuilder.createOWLSServiceModel(model);
        ontModel = model;
        ServiceList list = extractServiceList(ontModel, errHandler);
        owlsModel.setServiceList(list);
        return owlsModel;

    }

    private ServiceList extractServiceList(OntModel ontModel, OWLSErrorHandler errHandler) {

        ServiceList serviceList = storeBuilder.createServiceList();
        OntClass srvClass = ontModel.getOntClass(OWLSServiceURI.Service);

        ExtendedIterator iter = srvClass.listInstances();
        if (iter.hasNext() == false) {
            logger.debug("No Service found");
            return null;
        }
        Service service;
        while (iter.hasNext()) {
            service = objectBuilder.createService((Individual) iter.next(), errHandler);
            serviceList.add(service);
        }
        return serviceList;
    }

    private ServiceList extractServiceList(OntModel ontModel) throws NotInstanceOfServiceException {

    	logger.debug("extractServiceList called");
    	
        ServiceList serviceList = storeBuilder.createServiceList();
        OntClass srvClass = ontModel.getOntClass(OWLSServiceURI.Service);
        ExtendedIterator iter = srvClass.listInstances();
        if (iter.hasNext() == false) {
            logger.debug("No Service found");
            return null;
        }
        Service service;
        while (iter.hasNext()) {
            service = objectBuilder.createService((Individual) iter.next());
            logger.debug("Service found: " + service.getURI());
            serviceList.add(service);
        }
        return serviceList;
    }

}