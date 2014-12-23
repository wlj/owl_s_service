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
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMap;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * Implementation of this class was moved to @see EDU.cmu.Atlas.owls1_1.writer.OWLSGroundingWriterDynamic .
 * Now, it only delegates all call to this class.
 * @author Naveen Srinivasan
 */
public class OWLSGroundingWriter {
	
	private static OWLSGroundingWriterDynamic dw = new OWLSGroundingWriterDynamic();
	
    protected static OntModel init(String base) {
    	return dw.init(base);
    }

    protected static void setBaseURL(String base) {
        dw.setBaseURL(base);
    }

    public static void write(WsdlGroundingList gndList, String base, OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        dw.write(gndList, base, out);
    }

    public static void write(WsdlGroundingList gndList, String base, String imports[], OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {
        dw.write(gndList, base, imports, out);
    }

    public static void write(WsdlGroundingList gndList, String base, String imports[], OntModel submodel, OutputStream out)
    throws IndexOutOfBoundsException, OWLSWriterException {
    	
    	dw.write(gndList, base, imports, submodel, out);
    }


    public static OntModel writeModel(WsdlGroundingList gndList, String base, String imports[], OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {
    	
    	return dw.writeModel(gndList, base, imports, submodel, out);
    }

    public static void write(WsdlGrounding grounding, String base, OutputStream out) throws IndexOutOfBoundsException, OWLSWriterException {
        dw.write(grounding, base, out);
    }

    public static void write(WsdlGrounding grounding, String base, String imports[], OutputStream out) throws IndexOutOfBoundsException,
            OWLSWriterException {

    	dw.write(grounding, base, imports, out);
    }

    public static void write(WsdlGrounding grounding, String base, String imports[], OntModel submodel, OutputStream out)
            throws IndexOutOfBoundsException, OWLSWriterException {
        dw.write(grounding, base, imports, submodel, out);
    }

    public static Individual writeWsdlGrounding(WsdlGrounding grounding, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeWsdlGrounding(grounding, ontModel);
    }

    public static Individual writeWsdlAtomicProcessGrounding(WsdlAtomicProcessGrounding atomicGng, Individual wsdlGnd, OntModel model)
            throws OWLSWriterException {
        
    	return dw.writeWsdlAtomicProcessGrounding(atomicGng, wsdlGnd, model);
    }

    public static Individual writeWsdlAtomicProcessGrounding(WsdlAtomicProcessGrounding atomicGrounding, OntModel ontModel)
            throws OWLSWriterException {

        return dw.writeWsdlAtomicProcessGrounding(atomicGrounding, ontModel);
    }

    public static Individual writeWsdlOutputMessageMap(WsdlOutputMessageMap msgMap, Individual wsdlGnd, OntModel ontModel)
            throws OWLSWriterException {
        return dw.writeWsdlOutputMessageMap(msgMap, wsdlGnd, ontModel);
    }

    public static Individual writeWsdlOutputMessageMap(WsdlOutputMessageMap msgMap, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeWsdlOutputMessageMap(msgMap, ontModel);
    }

    public static Individual writeWsdlInputMessageMap(WsdlInputMessageMap msgMap, Individual wapg, OntModel ontModel)
            throws OWLSWriterException {
        
    	return dw.writeWsdlInputMessageMap(msgMap, wapg, ontModel);
    }

    public static Individual writeWsdlInputMessageMap(WsdlInputMessageMap msgMap, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeWsdlInputMessageMap(msgMap, ontModel);
    }

    public static Individual writeWsdlOperationRef(WsdlOperationRef wsdlOpRef, OntModel ontModel) throws OWLSWriterException {
    	return dw.writeWsdlOperationRef(wsdlOpRef, ontModel);
    }

}