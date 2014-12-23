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
import java.net.URL;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProfileURI;
import EDU.cmu.Atlas.owls1_1.writer.OWLSWriterUtils;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * OWL-S Profile Parser
 * @author Naveen Srinivasan
 */

public class OWLSProfileParser {

    private OntModel ontModel;

    private OWLS_Object_Builder objectBuilder;

    private OWLS_Store_Builder storeBuilder;

    static Logger logger = Logger.getLogger(OWLSProfileParser.class);

    /**
     * OWLSProfileParser parsers the URI in to OWLSProfileModel
     */
    public OWLSProfileParser() {
        this(true);
    }

    /**
     * OWLSProfileParser parsers the URI in to OWLSProfileModel
     */
    public OWLSProfileParser(boolean imports) {
        this(OWLSWriterUtils.getNewOntModel());
        ontModel.setDynamicImports(imports);
    }

    /**
     * OWLSProfileParser parsers the URI in to OWLSProfileModel
     */
    public OWLSProfileParser(OntModel ontModel) {

        this.ontModel = OWLSWriterUtils.getNewOntModel(ontModel);

        //removing objects from the symbol table
        logger.debug("Reseting symbol table");
        objectBuilder = OWLS_Object_BuilderFactory.instance();
        objectBuilder.reset();
        storeBuilder = OWLS_Store_BuilderFactory.instance();

    }

    public OWLSProfileModel read(String URI) throws IOException, NotInstanceOfProfileException {
        return read(URI, "");
    }

    public OWLSProfileModel read(String URI, OWLSErrorHandler handler) {

        return read(URI, "", handler);
    }

    public OWLSProfileModel read(String URI, String base) throws IOException, NotInstanceOfProfileException {

        URL url = new URL(URI);
        InputStream is = url.openStream();
        //this is needed because we are converting URI to inputstream before loading into ontModel
        //hence ontModel has no knowledge that it is an URI
        ontModel.addLoadedImport(URI);
        return read(new InputStreamReader(is), base);
    }

    public OWLSProfileModel read(String URI, String base, OWLSErrorHandler handler) {

        InputStream is;
        try {
            is = new URL(URI).openStream();
        } catch (IOException e) {
            handler.fatalError(e);
            return null;
        }
        //this is needed because we are converting URI to inputstream before loading into ontModel
        //hence ontModel has no knowledge that it is an URI
        ontModel.addLoadedImport(URI);
        return read(new InputStreamReader(is), base, handler);
    }

    public OWLSProfileModel read(Reader reader) throws NotInstanceOfProfileException {
        return read(reader, "");
    }

    public OWLSProfileModel read(Reader reader, OWLSErrorHandler handler) {
        return read(reader, "", handler);
    }

    public OWLSProfileModel read(Reader reader, String base) throws NotInstanceOfProfileException {

        ontModel.read(reader, base);
        return read(ontModel);
    }

    public OWLSProfileModel read(Reader reader, String base, OWLSErrorHandler handler) {

        ontModel.read(reader, base);
        return read(ontModel, handler);
    }

    public OWLSProfileModel read(OntModel model) throws NotInstanceOfProfileException {

        OWLSProfileModel owlsModel = objectBuilder.createOWLSProfileModel();
        ontModel = OWLSWriterUtils.getNewOntModel(model);
        owlsModel.setOntModel(ontModel);
        logger.debug("Parsing the OWL-S Profile  ");
        extractProfiles(owlsModel);

        return owlsModel;

    }

    public OWLSProfileModel read(OntModel model, OWLSErrorHandler handler) {

        OWLSProfileModel owlsModel = objectBuilder.createOWLSProfileModel();
        ontModel = OWLSWriterUtils.getNewOntModel(model);
        owlsModel.setOntModel(ontModel);
        logger.debug("Parsing the OWL-S Profile  ");
        extractProfiles(owlsModel, handler);

        return owlsModel;

    }

    /**
     * Extracts the Profiles and creates Profile List
     * 
     * @param owlsProfileModel
     * @throws NotInstanceOfProfileException
     * @throws Exception
     */
    protected void extractProfiles(OWLSProfileModel owlsProfileModel) throws NotInstanceOfProfileException {

        logger.debug("Looking for Classes of type " + OWLSProfileURI.Profile);
        OntClass profileClass = owlsProfileModel.getOntModel().getOntClass(OWLSProfileURI.Profile);
        ExtendedIterator profileInstances = profileClass.listInstances();
        if (profileInstances.hasNext() == false) {
            logger.info("No Profile found in the given URL");
            return;
        }
        logger.debug("Profile found in the given URL");

        ProfileList profileList = storeBuilder.createProfileList();

        while (profileInstances.hasNext()) {
            logger.debug("Converting Profile");
            Profile profile;
            Individual profileInstance = (Individual) profileInstances.next();
            profile = objectBuilder.createProfile(profileInstance);
            //adding profile to profile list
            profileList.addProfile(profile);
        }

        owlsProfileModel.setProfileList(profileList);
    }

/*    protected static OntModel getNewOntModel() {
        OntModel ontModel = ModelFactory.createOntologyModel();
        OntDocumentManager docMgr = ontModel.getDocumentManager();

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
        return ontModel;
    }

    protected static OntModel getNewOntModel(OntModel ontModel) {

        OntDocumentManager docMgr = ontModel.getDocumentManager();

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
        return ontModel;
    }
*/
    protected void extractProfiles(OWLSProfileModel owlsProfileModel, OWLSErrorHandler handler) {

        logger.debug("Looking for Classes of type " + OWLSProfileURI.Profile);

        OntClass profileClass = owlsProfileModel.getOntModel().getOntClass(OWLSProfileURI.Profile);
        ExtendedIterator profileInstances = profileClass.listInstances();

        if (profileInstances.hasNext() == false) {
            logger.info("No Profile found in the given URL");
            return;
        }
        logger.debug("Profile found in the given URL");

        //creating a profile list
        ProfileList profileList = storeBuilder.createProfileList();

        //for each profile instance create a profile
        while (profileInstances.hasNext()) {
            logger.debug("Converting Profile");
            Profile profile;
            Individual profileInstance = (Individual) profileInstances.next();

            profile = objectBuilder.createProfile(profileInstance, handler);

            //adding profile to profile list
            if (profile != null)
                profileList.addProfile(profile);
        }

        owlsProfileModel.setProfileList(profileList);
    }
}