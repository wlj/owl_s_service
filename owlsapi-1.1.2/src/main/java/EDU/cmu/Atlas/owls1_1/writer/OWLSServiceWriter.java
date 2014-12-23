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
import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.ServiceGrounding;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.service.ServiceModel;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfile;
import EDU.cmu.Atlas.owls1_1.service.ServiceProvider;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * Implementation of this class was moved to @see EDU.cmu.Atlas.owls1_1.writer.OWLSServiceWriterDynamic .
 * Now, it only delegates all call to this class.
 * @author Naveen Srinivasan
 */
public class OWLSServiceWriter {

    private static OWLSServiceWriterDynamic dw = new OWLSServiceWriterDynamic();

    protected static void setBaseURL(String base) {
        dw.setBaseURL(base);
    }

    public static void write(Service service, String base, OutputStream out) throws OWLSWriterException {
        dw.write(service, base, out);
    }

    public static void write(Service service, String base, String[] imports, OutputStream out) throws OWLSWriterException {
        dw.write(service, base, imports, out);
    }

    public static void write(Service service, String base, String[] imports, OntModel submodel, OutputStream out)
            throws OWLSWriterException {

        dw.write(service, base, imports, submodel, out);
    }

    public static void write(ServiceList serviceList, String base, OutputStream out) throws IndexOutOfBoundsException, OWLSWriterException {
        dw.write(serviceList, base, out);
    }

    public static void write(ServiceList serviceList, String base, String[] imports, OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        dw.write(serviceList, base, imports, out);
    }

    public static void write(ServiceList serviceList, String base, String[] imports, OntModel submodel, OutputStream out)
    throws IndexOutOfBoundsException, OWLSWriterException {
    	dw.write(serviceList, base, imports, submodel, out);
    }

    public static OntModel writeModel(ServiceList serviceList, String base, String[] imports, OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {
    	
    	return dw.writeModel(serviceList, base, imports, submodel, out);
    }

    public static Individual writeService(Service service, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeService(service, ontModel);
    }

    public static void writeServiceGrounding(ServiceGrounding gnd, Individual serviceInd, OntModel ontModel)
            throws IndexOutOfBoundsException, OWLSWriterException {
    	dw.writeServiceGrounding(gnd, serviceInd, ontModel);
    }

    public static void writeServiceModel(ServiceModel serviceModel, Individual serviceInd, OntModel ontModel) throws OWLSWriterException {
    	dw.writeServiceModel(serviceModel, serviceInd, ontModel);
    }

    public static void writeServiceProvider(ServiceProvider serviceProvider, Individual serviceInd, OntModel ontModel) throws OWLSWriterException {
    	dw.writeServiceProvider(serviceProvider, serviceInd, ontModel);    
    }

    public static void writeServiceProfile(ServiceProfile profile, Individual serviceInd, OntModel ontModel) throws OWLSWriterException {
        dw.writeServiceProfile(profile, serviceInd, ontModel);
    }

}