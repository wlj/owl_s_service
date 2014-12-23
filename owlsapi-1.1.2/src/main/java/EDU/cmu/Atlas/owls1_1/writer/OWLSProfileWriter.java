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

import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParameter;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * Implementation of this class was moved to @see EDU.cmu.Atlas.owls1_1.writer.OWLSProfileWriterDynamic .
 * Now, it only delegates all call to this class.
 * @author Naveen Srinivasan
 */
public class OWLSProfileWriter {

    private static OWLSProfileWriterDynamic dw = new OWLSProfileWriterDynamic();

    protected static void setBaseURL(String base) {
    	dw.setBaseURL(base);
    }

    public static void write(Profile profile, String base, OutputStream out) throws OWLSWriterException {
        dw.write(profile, base, out);
    }

    public static void write(Profile profile, String base, String[] imports, OutputStream out) throws OWLSWriterException {
        dw.write(profile, base, imports, out);
    }

    public static void write(Profile profile, String base, String[] imports, OntModel submodel, OutputStream out)
            throws OWLSWriterException {

        dw.write(profile, base, imports, submodel, out);
    }

    public static void write(ProfileList profileList, String base, OutputStream out) throws IndexOutOfBoundsException, OWLSWriterException {
        dw.write(profileList, base, out);
    }

    public static void write(ProfileList profileList, String base, String[] imports, OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        dw.write(profileList, base, imports, out);
    }

    public static void write(ProfileList profileList, String base, String[] imports, OntModel submodel, OutputStream out)
    throws IndexOutOfBoundsException, OWLSWriterException {
    	dw.write(profileList, base, imports, submodel, out);
    }
    
    public static OntModel writeModel(ProfileList profileList, String base, String[] imports, OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {

    	return dw.writeModel(profileList, base, imports, submodel, out);    
    }

    public static Individual writeProfile(Profile profile, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeProfile(profile, ontModel);    
    }

    public static Individual writeResult(Result result, Individual profileInst, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeResult(result, profileInst, ontModel);    
    }

    public static Individual writeCondition(Condition cond, Individual profileInst, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeCondition(cond, profileInst, ontModel);
    }

    public static Individual writeOutput(Output output, Individual profileInst, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeOutput(output, profileInst, ontModel);
    }

    public static Individual writeInput(Input input, Individual profileInst, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeInput(input, profileInst, ontModel);
    }

    public static Individual writeServiceParameter(ServiceParameter srvParam, Individual profileInst, OntModel ontModel)
            throws OWLSWriterException {
    	return dw.writeServiceParameter(srvParam, profileInst, ontModel);
    }

    public static Individual writeServiceParameter(ServiceParameter srvParam, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeServiceParameter(srvParam, ontModel);
    }

    public static Individual writeServiceCatgeory(ServiceCategory srvCat, Individual profileInst, OntModel ontModel)
            throws OWLSWriterException {
    	return dw.writeServiceCatgeory(srvCat, profileInst, ontModel);

    }

    public static Individual writeServiceCatgeory(ServiceCategory srvCat, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeServiceCatgeory(srvCat, ontModel);
    }

    public static Individual writeActor(Actor actor, Individual profileInst, OntModel ontModel) throws OWLSWriterException {
        return dw.writeActor(actor, profileInst, ontModel);

    }

    public static Individual writeActor(Actor actor, OntModel ontModel) throws OWLSWriterException {
        return dw.writeActor(actor, ontModel);
    }

}