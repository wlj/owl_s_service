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
public interface ServiceCategoriesList extends OWLS_Store {

    /**
     * add an actor to the list
     * 
     * @param category the ServiceCategory to add
     */
    public void addServiceCategory(ServiceCategory category);

    /**
     * remove a service category from the list
     * 
     * @param category the service category to remove
     */
    public boolean removeServiceCategory(ServiceCategory category);

    /**
     * get the service category at some position in the list
     * 
     * @param index the position of the service category in the list
     * @return the next service category or null if none
     */
    public ServiceCategory getServiceCategoryAt(int index);

    public boolean removeServiceCategory(String uri);

    public ServiceCategory getServiceCategory(String uri);

}

