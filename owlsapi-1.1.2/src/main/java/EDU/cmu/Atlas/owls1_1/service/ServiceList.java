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
package EDU.cmu.Atlas.owls1_1.service;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

/**
 * @author Naveen Srinivasan
 *  
 */
public interface ServiceList extends OWLS_Store {

    /**
     * add a service to the services table
     * @param service the service to add
     * @return true service has been recorded, false otherwise
     */
    public void addService(Service service);

    /**
     * remove service corresponding to the service uri provided
     * @param uri the uri of the service whose service is to be removed
     */
    public boolean removeService(String uri);

    /**
     * remove a service corresponding from the table
     * @param service the service to be removed
     */
    public boolean removeService(Service service);

    /**
     * extract the service of a specified service
     * @param uri the uri of the service
     * @return the service of the service, or null when no service is found
     */
    public Service getService(String uri);

    /**
     * function that gives access to services sequentially
     * @param index the index of the service to look at
     * @return the service found or null index is out of range
     */
    public Service getNthService(int index) throws IndexOutOfBoundsException;

}