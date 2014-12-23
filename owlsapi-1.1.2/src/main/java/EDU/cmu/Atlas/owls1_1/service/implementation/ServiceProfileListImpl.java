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
package EDU.cmu.Atlas.owls1_1.service.implementation;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfile;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;


/**
 * @author Naveen Srinivasan
 *
 */
public class ServiceProfileListImpl extends OWLS_StoreImpl implements ServiceProfileList {

    /**
     * add a profile to the profiles table
     * @param profile the profile to add
     * @return true profile has been recorded, false otherwise
     */
    public void addServiceProfile(ServiceProfile profile) {
        add(profile);
    }

    /**
     * remove profile corresponding to the service uri provided
     * @param uri the uri of the service whose profile is to be removed
     */
    public boolean removeServiceProfile(String uri) {
        return remove(uri);
    }

    /**
     * remove a profile corresponding from the table
     * @param profile the profile to be removed
     */
    public boolean removeServiceProfile(ServiceProfile profile) {
        return remove(profile);
    }

    /**
     * extract the profile of a specified service
     * @param uri the uri of the service
     * @return the profile of the service, or null when no profile is found
     */
    public ServiceProfile getServiceProfile(String uri) {        
        return (ServiceProfile) get(uri);
    }

    /**
     * function that gives access to profiles sequentially
     * @param index the index of the profile to look at
     * @return the profile found
     * @throws IndexOutOfRangeException when index does not point to a Profile
     *             in the DB
     */
    public ServiceProfile getNthServiceProfile(int index) throws IndexOutOfBoundsException {
        return (ServiceProfile) getNth(index);
    }

}