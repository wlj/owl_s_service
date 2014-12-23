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
package EDU.cmu.Atlas.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

/**
 * @author Naveen Srinivasan
 */
public interface ServiceParametersList extends OWLS_Store {

    /**
     * This interface is implemented to store the list of service categories referenced by a profile This interface is
     * loosely based on the class Vector
     */

    /**
     * add an actor to the list
     * @param parameter the ServiceParameter to add
     */
    public void addServiceParameter(ServiceParameter parameter);

    /**
     * remove a service parameter from the list
     * 
     * @param parameter the service parameter to remove
     */
    public boolean removeServiceParameter(ServiceParameter parameter);

    /**
     * get the service parameter at some position in the list
     * 
     * @param index the position of the service parameter in the list
     * @return the next service parameter or null if none
     */
    public ServiceParameter getServiceParameterAt(int index);

    public boolean removeServiceParameter(String uri);

    public ServiceParameter getServiceParameter(String uri);

}

