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
package EDU.cmu.Atlas.owls1_1.grounding;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;
import EDU.cmu.Atlas.owls1_1.process.Process;

/**
 * @author Naveen Srinivasan
 */
public interface WsdlAtomicProcessGrounding extends OWLS_Object {

    public Process getOwlsProcess();

    public void setOwlsProcess(Process process);

    public WsdlOperationRef getWsdlOperation();

    public void setWsdlOperation(WsdlOperationRef opRef);

    public String getWsdlInputMessage();

    public void setWsdlInputMessage(String ipMsg);

    public WsdlInputMessageMapList getWsdlInputs();

    public void setWsdlInputs(WsdlInputMessageMapList msgMapList);

    public String getWsdlOutputMessage();

    public void setWsdlOutputMessage(String opMsg);

    public WsdlOutputMessageMapList getWsdlOutputs();

    public void setWsdlOutputs(WsdlOutputMessageMapList msgMapList);

    public String getWsdlDocument();

    public void setWsdlDocument(String wsdlDoc);

    public String getWsdlVersion();

    public void setWsdlVersion(String version);

}