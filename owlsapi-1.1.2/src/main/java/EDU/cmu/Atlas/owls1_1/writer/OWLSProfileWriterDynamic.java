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

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParameter;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParametersList;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProfileURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProfileProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.RDFWriter;

/**
 * @author Naveen Srinivasan
 */
public class OWLSProfileWriterDynamic {

    String baseURL;

    private static Logger logger = Logger.getLogger(OWLSProfileWriter.class);

    protected OntModel init(String base) {

        //OntModel model = ModelFactory.createOntologyModel();
        OntModel model = OWLSWriterUtils.getNewOntModel();
        model.read("http://www.daml.org/services/owl-s/1.1/Service.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Profile.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/ActorDefault.owl");
//        model.read("http://www.daml.org/services/owl-s/1.1/ProfileAdditionalParameters.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Process.owl");
        model.read("http://www.daml.org/services/owl-s/1.1/Grounding.owl");

        model.setNsPrefix("grounding", "http://www.daml.org/services/owl-s/1.1/Grounding.owl#");
        
        //OntModel baseModel = ModelFactory.createOntologyModel();
        OntModel baseModel = OWLSWriterUtils.getNewOntModel();

        //create import statements
        Ontology ont = baseModel.createOntology(base);
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Service.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Profile.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/ActorDefault.owl"));
//        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/ProfileAdditionalParameters.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Process.owl"));
        ont.addImport(model.getOntology("http://www.daml.org/services/owl-s/1.1/Grounding.owl"));

        baseModel.addSubModel(model);

        return baseModel;
    }

    protected void setBaseURL(String base) {
        baseURL = base;
    }

    public void write(Profile profile, String base, OutputStream out) throws OWLSWriterException {
        write(profile, base, null, out);
    }

    public void write(Profile profile, String base, String[] imports, OutputStream out) throws OWLSWriterException {
        write(profile, base, imports, null, out);
    }

    public void write(Profile profile, String base, String[] imports, OntModel submodel, OutputStream out)
            throws OWLSWriterException {

        //checking if profile list
        if (profile == null)
            throw new OWLSWriterException("Profile is null");

        OWLS_Store_Builder storeBuilder = OWLS_Store_BuilderFactory.instance();
        ProfileList profileList = storeBuilder.createProfileList();
        profileList.add(profile);

        write(profileList, base, imports, submodel, out);
    }

    public void write(ProfileList profileList, String base, OutputStream out) throws IndexOutOfBoundsException, OWLSWriterException {
        write(profileList, base, null, out);
    }

    public void write(ProfileList profileList, String base, String[] imports, OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        write(profileList, base, imports, null, out);
    }

    public void write(ProfileList profileList, String base, String[] imports, OntModel submodel, OutputStream out)
    throws IndexOutOfBoundsException, OWLSWriterException {
    	writeModel(profileList, base, imports, submodel, out);
    }
    
    public OntModel writeModel(ProfileList profileList, String base, String[] imports, OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {

        //setting base url
        baseURL = base;

        //setting the base of other writer, incase other writer are used in this one.
        OWLSProcessWriter.setBaseURL(base);
        OWLSServiceWriter.setBaseURL(base);
        OWLSGroundingWriter.setBaseURL(base);

        //checking if profile list
        if (profileList == null)
            throw new OWLSWriterException("ProfileList is null");

        //initializing ontmodel for writing the profile
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

        for (int i = 0; i < profileList.size(); i++) {
            writeProfile(profileList.getNthProfile(i), baseModel);
        }

        RDFWriter writer = OWLSWriterUtils.getWriter(baseModel, base);
        writer.write(baseModel.getBaseModel(), out, base);
        return baseModel;
    }

    public Individual writeProfile(Profile profile, OntModel ontModel) throws OWLSWriterException {

        if (profile == null)
            throw new OWLSWriterException("profile is null");

        //check if the profile with that uri already exist in the ontModel
        Individual profileInst = OWLSWriterUtils.checkIfIndividualExist(profile, ontModel);
        if (profileInst != null)
            return profileInst;

        // added by Roman
        OntClass profileClass;
        if (profile.getProfileClass()!=null){ 
        	profileClass = profile.getProfileClass();
        	profileInst = ontModel.createIndividual(profile.getURI(), profileClass);
        } else {
        	profileClass = ontModel.getOntClass(OWLSProfileURI.Profile);
        	profileInst = OWLSWriterUtils.createIndividual(profileClass, profile, baseURL);
        }
        
        //Service Name
        String srvName = profile.getServiceName();
        if (srvName != null)
            profileInst.addProperty(OWLSProfileProperties.serviceName, srvName);
        else
            throw new OWLSWriterException(profile.getURI() + " : service name is missing");

        //Text Description
        String txtDescription = profile.getTextDescription();
        if (txtDescription != null)
            profileInst.addProperty(OWLSProfileProperties.textDescription, txtDescription);
        else
            throw new OWLSWriterException(profile.getURI() + " : textDescription is missing");

        //Has Process
        Process hasProcess = profile.getHasProcess();
        if (hasProcess != null) {
            Individual processInd = OWLSProcessWriter.writeProcess(hasProcess, ontModel);
            profileInst.addProperty(OWLSProfileProperties.has_process, processInd);
        } else {
            logger.debug(profile.getURI() + " : hasProcess is missing");
        }

        //Contact Information
        ActorsList actors = profile.getContactInformation();
        if (actors != null) {
            for (int i = 0; i < actors.size(); i++) {
                Actor actor = actors.getNthActor(i);
                writeActor(actor, profileInst, ontModel);
            }
        }

        //Service Category
        ServiceCategoriesList srvCatList = profile.getServiceCategory();
        if (srvCatList != null) {
            for (int i = 0; i < srvCatList.size(); i++) {
                ServiceCategory srvCat = srvCatList.getServiceCategoryAt(i);
                writeServiceCatgeory(srvCat, profileInst, ontModel);
            }
        }

        //Service Parameter
        ServiceParametersList srvParamList = profile.getServiceParameter();
        if (srvParamList != null) {
            for (int i = 0; i < srvParamList.size(); i++) {
                ServiceParameter srvParam = srvParamList.getServiceParameterAt(i);
                writeServiceParameter(srvParam, profileInst, ontModel);
            }
        }

        //Inputs
        InputList ipList = profile.getInputList();
        if (ipList != null) {
            for (int i = 0; i < ipList.size(); i++) {
                Input input = ipList.getNthInput(i);
                writeInput(input, profileInst, ontModel);
            }
        }

        //Outputs
        OutputList opList = profile.getOutputList();
        if (opList != null) {
            for (int i = 0; i < opList.size(); i++) {
                Output output = opList.getNthOutput(i);
                writeOutput(output, profileInst, ontModel);
            }
        }

        //Preconditions
        PreConditionList pcList = profile.getPreconditionList();
        if (pcList != null) {
            for (int i = 0; i < pcList.size(); i++) {
                Condition cond = pcList.getNthPreCondition(i);
                writeCondition(cond, profileInst, ontModel);
            }
        }

        //Result
        ResultList resultList = profile.getResultList();
        if (resultList != null) {
            for (int i = 0; i < resultList.size(); i++) {
                Result result = resultList.getNthResult(i);
                writeResult(result, profileInst, ontModel);
            }
        }

        return profileInst;
    }

    public Individual writeResult(Result result, Individual profileInst, OntModel ontModel) throws OWLSWriterException {
        Individual individual = OWLSProcessWriter.writeResult(result, ontModel);
        profileInst.addProperty(OWLSProfileProperties.hasResult, individual);
        return individual;

    }

    public Individual writeCondition(Condition cond, Individual profileInst, OntModel ontModel) throws OWLSWriterException {
        Individual individual = OWLSProcessWriter.writeCondition(cond, ontModel);
        profileInst.addProperty(OWLSProfileProperties.hasPrecondition, individual);
        return individual;
    }

    public Individual writeOutput(Output output, Individual profileInst, OntModel ontModel) throws OWLSWriterException {
        Individual individual = OWLSProcessWriter.writeOutput(output, ontModel);
        profileInst.addProperty(OWLSProfileProperties.hasOutput, individual);
        return individual;
    }

    public Individual writeInput(Input input, Individual profileInst, OntModel ontModel) throws OWLSWriterException {

        Individual individual = OWLSProcessWriter.writeInput(input, ontModel);
        profileInst.addProperty(OWLSProfileProperties.hasInput, individual);
        return individual;

    }

    public Individual writeServiceParameter(ServiceParameter srvParam, Individual profileInst, OntModel ontModel)
            throws OWLSWriterException {
        Individual individual = writeServiceParameter(srvParam, ontModel);
        profileInst.addProperty(OWLSProfileProperties.serviceParameter, individual);
        return individual;
    }

    public Individual writeServiceParameter(ServiceParameter srvParam, OntModel ontModel) throws OWLSWriterException {

        if (srvParam == null)
            throw new OWLSWriterException("ServiceParameter is null");

        //check if the srvParam with that uri already exist in the ontModel
        Individual srvParamInd = OWLSWriterUtils.checkIfIndividualExist(srvParam, ontModel);
        if (srvParamInd != null)
            return srvParamInd;

        //Creating a New Service Category Instances
        OntClass srvParamClass = ontModel.getOntClass(OWLSProfileURI.ServiceParameter);

        srvParamInd = OWLSWriterUtils.createIndividual(srvParamClass, srvParam, baseURL);

        //Adding Properties to the instance
        if (srvParam.getServiceParameterName() != null)
            srvParamInd.addProperty(OWLSProfileProperties.serviceParameterName, srvParam.getServiceParameterName());
        else
            throw new OWLSWriterException(srvParam.getURI() + " : service parameter name is missing");

        if (srvParam.getsParameter() != null)
            srvParamInd.addProperty(OWLSProfileProperties.sParameter, srvParam.getsParameter());
        else
            throw new OWLSWriterException(srvParam.getURI() + " : sParmeter name is missing");

        return srvParamInd;
    }

    public Individual writeServiceCatgeory(ServiceCategory srvCat, Individual profileInst, OntModel ontModel)
            throws OWLSWriterException {

        Individual individual = writeServiceCatgeory(srvCat, ontModel);
        profileInst.addProperty(OWLSProfileProperties.serviceCategory, individual);
        return individual;
    }

    public Individual writeServiceCatgeory(ServiceCategory srvCat, OntModel ontModel) throws OWLSWriterException {

        if (srvCat == null)
            throw new OWLSWriterException("ServiceCategory is null");

        //check if the srvCat with that uri already exist in the ontModel
        Individual srvCatInd = OWLSWriterUtils.checkIfIndividualExist(srvCat, ontModel);
        if (srvCatInd != null)
            return srvCatInd;

        //Creating a New Service Category Instances
        OntClass srvCatClass = ontModel.getOntClass(OWLSProfileURI.ServiceCategory);

        srvCatInd = OWLSWriterUtils.createIndividual(srvCatClass, srvCat, baseURL);

        //Adding Properties to the instance
        if (srvCat.getCategoryName() != null)
            srvCatInd.addProperty(OWLSProfileProperties.categoryName, srvCat.getCategoryName());
        else
            throw new OWLSWriterException(srvCat.getURI() + " : categoryname is missing");

        if (srvCat.getTaxonomy() != null)
            srvCatInd.addProperty(OWLSProfileProperties.taxonomy, srvCat.getTaxonomy());
        else
            throw new OWLSWriterException(srvCat.getURI() + " : taxonomy is missing");

        if (srvCat.getValue() != null)
            srvCatInd.addProperty(OWLSProfileProperties.value, srvCat.getValue());
        else
            throw new OWLSWriterException(srvCat.getURI() + " : value is missing");

        if (srvCat.getCode() != null)
            srvCatInd.addProperty(OWLSProfileProperties.code, srvCat.getCode());
        else
            throw new OWLSWriterException(srvCat.getURI() + " : code is missing");

        return srvCatInd;
    }

    public Individual writeActor(Actor actor, Individual profileInst, OntModel ontModel) throws OWLSWriterException {

        Individual individual = writeActor(actor, ontModel);
        profileInst.addProperty(OWLSProfileProperties.contactInformation, individual);
        return individual;

    }

    public Individual writeActor(Actor actor, OntModel ontModel) throws OWLSWriterException {

        if (actor == null)
            throw new OWLSWriterException("actor is null");

        //check if the actor with that uri already exist in the ontModel
        Individual actorInd = OWLSWriterUtils.checkIfIndividualExist(actor, ontModel);
        if (actorInd != null)
            return actorInd;

        OntClass actorClass = ontModel.getOntClass(OWLSProfileURI.Actor);

        actorInd = OWLSWriterUtils.createIndividual(actorClass, actor, baseURL);

        if (actor.getName() != null)
            actorInd.addProperty(OWLSProfileProperties.actor_name, actor.getName());

        if (actor.getTitle() != null)
            actorInd.addProperty(OWLSProfileProperties.actor_title, actor.getTitle());

        if (actor.getPhone() != null)
            actorInd.addProperty(OWLSProfileProperties.actor_phone, actor.getPhone());

        if (actor.getEmail() != null)
            actorInd.addProperty(OWLSProfileProperties.actor_email, actor.getEmail());

        if (actor.getFax() != null)
            actorInd.addProperty(OWLSProfileProperties.actor_fax, actor.getFax());

        if (actor.getAddress() != null)
            actorInd.addProperty(OWLSProfileProperties.actor_address, actor.getAddress());

        if (actor.getWebURL() != null)
            actorInd.addProperty(OWLSProfileProperties.actor_weburl, actor.getWebURL());

        return actorInd;
    }

}
