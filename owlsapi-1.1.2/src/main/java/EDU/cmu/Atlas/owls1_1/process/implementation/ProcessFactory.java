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
package EDU.cmu.Atlas.owls1_1.process.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.process.Process;
import com.hp.hpl.jena.ontology.Individual;

/**
 * @author Naveen Srinivasan
 */
public class ProcessFactory {

    static Logger logger = Logger.getLogger(ProcessFactory.class);
    private static ProcessFactoryDynamic pf = new ProcessFactoryDynamic(); 

    /**
     * extract and parses a process from the Jena model
     * @param processInd the instance of the process in the a-box
     * @return an instance of Process
     * @throws NotInstanceOfProcessException
     * @throws SyntacticError when the instance is defined with the wrong type of process, or some
     *             parts for the process are ill defined
     */
    public static Process parseProcess(Individual processInd) throws NotInstanceOfProcessException {
    	return pf.parseProcess(processInd);
    }

    /**
     * check whether an individual is of type atomic process
     * @author Massimo
     * @param processIndividual
     * @return true if the individual is an atomic process false otherwise
     */
    public final static boolean isAtomicProcess(Individual processIndividual) {
        // check whether the type of the individual is "atomic process"
        return pf.isAtomicProcess(processIndividual);
    }

    /**
     * check whether an individual is of type composite process
     * @author Massimo
     * @param processIndividual
     * @return true if the individual is an composite process false otherwise
     */
    public final static boolean isCompositeProcess(Individual processIndividual) {
        // check whether the type of the individual is "composite process"
        return pf.isCompositeProcess(processIndividual);
    }

    /**
     * check whether an individual is of type composite process
     * @author Massimo
     * @param processIndividual
     * @return true if the individual is an composite process false otherwise
     */
    public final static boolean isSimpleProcess(Individual processIndividual) {
        // check whether the type of the individual is "atomic process"
        return pf.isSimpleProcess(processIndividual);
    }


    public static Process parseProcess(Individual processInd, OWLSErrorHandler handler) {
    	return pf.parseProcess(processInd, handler);
    }
}