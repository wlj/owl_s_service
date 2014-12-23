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
import EDU.cmu.Atlas.owls1_1.process.AtomicProcessMultiparty;
import EDU.cmu.Atlas.owls1_1.process.Process;
import com.hp.hpl.jena.ontology.Individual;

/**
 * @author Roman Vaculin
 */
public class ProcessFactoryDynamicMultiparty extends ProcessFactoryDynamic {

    private static Logger logger = Logger.getLogger(ProcessFactory.class);

    /**
     * extract and parses a process from the Jena model
     * @param processInd the instance of the process in the a-box
     * @return an instance of Process
     * @throws NotInstanceOfProcessException
     * @throws SyntacticError when the instance is defined with the wrong type of process, or some
     *             parts for the process are ill defined
     */
    public Process parseProcess(Individual processInd) throws NotInstanceOfProcessException {
    	
    	return super.parseProcess(processInd);
    	
    	// TODO: parse dependences + messagetypes
    }


    /**
     * @param hasProcess
     * @return
     */
    protected Process extractAtomicProcess(Individual process) {
        logger.debug("AtomicProcess " + process.getURI());
        AtomicProcessMultiparty atomicProcess = new AtomicProcessMultipartyImpl(process);
        return atomicProcess;
    }


    public Process parseProcess(Individual processInd, OWLSErrorHandler handler) {

    	return super.parseProcess(processInd, handler);
    	
    	// TODO: parse dependences + messagetypes

    }

}