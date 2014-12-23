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

import java.io.OutputStream;

//import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.ServiceGrounding;
import EDU.cmu.Atlas.owls1_1.service.ServiceGroundingList;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.service.ServiceModel;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfile;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;
import EDU.cmu.Atlas.owls1_1.service.ServiceProvider;
import EDU.cmu.Atlas.owls1_1.uri.OWLSServiceURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSServiceProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;

/**
 * Writes Service in the OWL-S.
 * @author Naveen Srinivasan
 */
public class OWLSServiceWriterDynamic {

    String baseURL;

//    private static Logger logger = Logger.getLogger(OWLSServiceWriter.class);

    protected OntModel init(String base) {

//        OntModel model = ModelFactory.createOntologyModel();
        OntModel model = OWLSWriterUtils.getNewOntModel();

        model.read("http://www.daml.org/services/owl-s/1.1/Service.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Profile.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Process.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/ActorDefault.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Grounding.owl");

        model.setNsPrefix("grounding", "http://www.daml.org/services/owl-s/1.1/Grounding.owl#");

        OntModel baseModel = ModelFactory.createOntologyModel();
        baseModel.addSubModel(model);

        //create import statements
        Ontology ont = baseModel.createOntology(base);
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Service.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Profile.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Process.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Grounding.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/ActorDefault.owl"));

        return baseModel;

    }

    protected void setBaseURL(String base) {
        baseURL = base;
    }

    public void write(Service service, String base, OutputStream out) throws OWLSWriterException {
        write(service, base, null, out);
    }

    public void write(Service service, String base, String[] imports, OutputStream out) throws OWLSWriterException {
        write(service, base, imports, null, out);
    }

    public void write(Service service, String base, String[] imports, OntModel submodel, OutputStream out)
            throws OWLSWriterException {
        if (service == null)
            throw new OWLSWriterException("service is null");

        OWLS_Store_Builder builder = OWLS_Store_BuilderFactory.instance();

        ServiceList srvlist = builder.createServiceList();
        srvlist.addService(service);

        write(srvlist, base, imports, submodel, out);

    }

    public void write(ServiceList serviceList, String base, OutputStream out) throws IndexOutOfBoundsException, OWLSWriterException {
        write(serviceList, base, null, out);
    }

    public void write(ServiceList serviceList, String base, String[] imports, OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        write(serviceList, base, imports, null, out);
    }

    public void write(ServiceList serviceList, String base, String[] imports, OntModel submodel, OutputStream out)
    throws IndexOutOfBoundsException, OWLSWriterException {
    	writeModel(serviceList, base, imports, submodel, out);
    }

    public OntModel writeModel(ServiceList serviceList, String base, String[] imports, OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {

        baseURL = base;
        //setting the base of other writer, incase other writer are used in this one.
        OWLSGroundingWriter.setBaseURL(base);
        OWLSProcessWriter.setBaseURL(base);
        OWLSProfileWriter.setBaseURL(base);

        if (serviceList == null)
            throw new OWLSWriterException("serviceList is null");

        OntModel baseModel = init(base);
        baseModel.setNsPrefix("", base);

        //adding submodel
        if (submodel != null)
            baseModel.addSubModel(submodel);

        //create import statements
        if (imports != null) {
            OntModel tmpModel = OWLSWriterUtils.getNewOntModel();
            Ontology ont = baseModel.getOntology(base);
            boolean readNewSubmodel = false;
            for (int i = 0; i < imports.length; i++) {
                if ((submodel!=null) && (submodel.getOntology(imports[i]) != null)) {
                    ont.addImport(submodel.getOntology(imports[i]));
                } else {
                    tmpModel.read(imports[i]);
                    ont.addImport(tmpModel.getOntology(imports[i]));
                    readNewSubmodel = true;
                }
            }
            if (readNewSubmodel) {
            	baseModel.addSubModel(tmpModel);
            }
        }

        for (int i = 0; i < serviceList.size(); i++) {
            writeService(serviceList.getNthService(i), baseModel);
        }

        System.gc();
        RDFWriter writer = OWLSWriterUtils.getWriter(baseModel, base);
        writer.write(baseModel.getBaseModel(), out, base);
        
        return baseModel;
    }

    public Individual writeService(Service service, OntModel ontModel) throws OWLSWriterException {

        if (service == null)
            throw new OWLSWriterException("service is null");

        //check if the service with that uri already exist in the ontModel
        Individual serviceInst = OWLSWriterUtils.checkIfIndividualExist(service, ontModel);
        if (serviceInst != null)
            return serviceInst;

        OntClass serviceClass = ontModel.getOntClass(OWLSServiceURI.Service);
        serviceInst = OWLSWriterUtils.createIndividual(serviceClass, service, baseURL);

        ServiceProfileList profileList = service.getPresents();
        if (profileList != null) {
            for (int i = 0; i < profileList.size(); i++) {
                ServiceProfile profile = profileList.getNthServiceProfile(i);
                writeServiceProfile(profile, serviceInst, ontModel);
            }
        }

        ServiceModel serviceModel = service.getDescribes();
        if (serviceModel != null)
            writeServiceModel(serviceModel, serviceInst, ontModel);

        ServiceGroundingList gndList = service.getSupports();
        if (gndList != null) {
            for (int i = 0; i < gndList.size(); i++) {
                ServiceGrounding gnd = gndList.getNthServiceGrounding(i);
                writeServiceGrounding(gnd, serviceInst, ontModel);
            }
        }

        ServiceProvider serviceProvider = service.getProvidedBy();
        if (serviceProvider != null)
            writeServiceProvider(serviceProvider, serviceInst, ontModel);

        
        return serviceInst;
    }

    public void writeServiceGrounding(ServiceGrounding gnd, Individual serviceInd, OntModel ontModel)
            throws IndexOutOfBoundsException, OWLSWriterException {
        Individual gndInd = OWLSGroundingWriter.writeWsdlGrounding((WsdlGrounding) gnd, ontModel);
        serviceInd.addProperty(OWLSServiceProperties.supports, gndInd);
    }

    public void writeServiceModel(ServiceModel serviceModel, Individual serviceInd, OntModel ontModel) throws OWLSWriterException {
        Individual processInd = OWLSProcessWriter.writeProcess((Process) serviceModel, ontModel);
        serviceInd.addProperty(OWLSServiceProperties.describedBy, processInd);
    }

    public void writeServiceProvider(ServiceProvider serviceProvider, Individual serviceInd, OntModel ontModel) throws OWLSWriterException {
        
    	OntClass providerClass  = serviceProvider.getProviderClass();
    	if (providerClass==null) {
    		throw new OWLSWriterException("Provider OWL class was not specified. Cannot creat an instance of the service provider.");
    	}
    	
    	Individual providerInd = OWLSWriterUtils.checkIfIndividualExist(serviceProvider, ontModel);
        
        if (providerInd == null) {
        	providerInd = ontModel.createIndividual(serviceProvider.getURI(), providerClass);
        }
        if (providerInd==null) {
        	throw new OWLSWriterException("Instance of service provider cannot be created. Probably the relevat ontology was not added to th import of the service model.");
        }
    	
        serviceInd.addProperty(OWLSServiceProperties.providedBy, providerInd);
    }

    public void writeServiceProfile(ServiceProfile profile, Individual serviceInd, OntModel ontModel) throws OWLSWriterException {

        Individual profileInd = OWLSProfileWriter.writeProfile((Profile) profile, ontModel);
        serviceInd.addProperty(OWLSServiceProperties.presents, profileInd);
    }

}
