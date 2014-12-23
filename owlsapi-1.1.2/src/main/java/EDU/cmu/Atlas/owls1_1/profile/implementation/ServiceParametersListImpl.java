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
package EDU.cmu.Atlas.owls1_1.profile.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParameter;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParametersList;


/**
 * @author Naveen Srinivasan
 *
 */
public class ServiceParametersListImpl extends OWLS_StoreImpl implements ServiceParametersList {

    static Logger logger = Logger.getLogger(ServiceParametersListImpl.class);

    /**
     * add an serviceParameter to the list
     * @param serviceParameter the serviceParameter to add
     */
    public void addServiceParameter(ServiceParameter serviceParameter) {
        add(serviceParameter);
    }

    /**
     * remove an serviceParameter from the list
     * @param serviceParameter the serviceParameter to remove
     */
    public boolean removeServiceParameter(ServiceParameter serviceParameter) {
        return remove(serviceParameter);
    }

    /**
     * get the serviceParameter at some position in the list
     * @param index the position of the serviceParameter in the list
     * @return the next serviceParameter or null if none
     */
    public ServiceParameter getServiceParameterAt(int index) {
        return (ServiceParameter) getNth(index);
    }

    public boolean removeServiceParameter(String uri) {
        return remove(uri);
    }

    public ServiceParameter getServiceParameter(String uri) {
        return (ServiceParameter) get(uri);
    }

}