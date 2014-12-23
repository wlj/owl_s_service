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
package EDU.cmu.Atlas.owls1_1.builder.implementation;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Builder_Util;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderMultiparty;
import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfMessageTypeException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfResultException;
import EDU.cmu.Atlas.owls1_1.process.MessageType;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultMultiparty;
import EDU.cmu.Atlas.owls1_1.process.implementation.AtomicProcessMultipartyImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.MessageTypeImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ProcessFactoryDynamicMultiparty;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultMultipartyImp;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

/**
 * @author Roman Vaculin
 *
 */
public class OWLS_Object_BuilderMultipartyImpl extends OWLS_Object_BuilderImpl implements OWLS_Object_BuilderMultiparty{

	public OWLS_Object_BuilderMultipartyImpl() {
		super(new ProcessFactoryDynamicMultiparty());
	}

	
	// Process
    public Process createProcess(String uri, int type) throws NotInstanceOfProcessException {
        
    	Process process = super.createProcess(uri, type);
    	
    	if (type == OWLS_Builder_Util.ATOMIC_MULTIPARTY) {
    		process = new AtomicProcessMultipartyImpl(uri);
    	}
    	
        return process;
    }

    public Process createProcess(int type) {

    	Process process = super.createProcess(type);
    	
    	if (type == OWLS_Builder_Util.ATOMIC_MULTIPARTY) {
    		process = new AtomicProcessMultipartyImpl();
    	}
    	
        return process;
    }
	
	
	
	//Result
    public Result createResult(Individual instance) throws NotInstanceOfResultException {
        OWLS_Object obj;
        if ((obj = getOWLS_Object(instance)) != null) {
            if (obj instanceof ResultMultiparty)
                return (ResultMultiparty) obj;
            else
                throw new NotInstanceOfResultException("Instance " + obj.getURI() + " not of type Result");
        }
        ResultMultiparty result = new ResultMultipartyImp(instance);
        storeOWLS_Object(result);
        return result;

    }

    public Result createResult(String uri) throws NotInstanceOfResultException {
        OWLS_Object obj;
        if ((obj = getOWLS_Object(uri)) != null) {
            if (obj instanceof ResultMultiparty)
                return (ResultMultiparty) obj;
            else
                throw new NotInstanceOfResultException("Instance " + obj.getURI() + " not of type Result");
        }
        Result result = new ResultMultipartyImp(uri);
        storeOWLS_Object(result);
        return result;

    }

    public Result createResult() {
        return new ResultMultipartyImp();

    }


    public MessageType createMessageType(String URI, OntClass messageTypeClass) throws NotInstanceOfMessageTypeException {
        OWLS_Object obj;
        
        if ((obj = getOWLS_Object(URI)) != null) {
            if (obj instanceof MessageType)
                return (MessageType) obj;
            else
                throw new NotInstanceOfMessageTypeException("Instance " + obj.getURI() + " not of type MessageType");
        }

        MessageType messageType = new MessageTypeImpl(URI, messageTypeClass);
        storeOWLS_Object(messageType);
        return messageType;    	
    }


}
