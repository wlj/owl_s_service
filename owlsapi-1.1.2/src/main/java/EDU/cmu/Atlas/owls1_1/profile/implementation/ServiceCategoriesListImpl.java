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
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;


/**
 * @author Naveen Srinivasan
 *
 */
public class ServiceCategoriesListImpl extends OWLS_StoreImpl implements ServiceCategoriesList {

    static Logger logger = Logger.getLogger(ServiceCategoriesListImpl.class);

    /**
     * add an serviceCategory to the list
     * @param serviceCategory the serviceCategory to add
     */
    public void addServiceCategory(ServiceCategory serviceCategory) {
        add(serviceCategory);
    }

    /**
     * remove an serviceCategory from the list
     * @param serviceCategory the serviceCategory to remove
     */
    public boolean removeServiceCategory(ServiceCategory serviceCategory) {
        return remove(serviceCategory);
    }

    /**
     * get the serviceCategory at some position in the list
     * @param index the position of the serviceCategory in the list
     * @return the next serviceCategory or null if none
     */
    public ServiceCategory getServiceCategoryAt(int index) {
        return (ServiceCategory) getNth(index);
    }

    public boolean removeServiceCategory(String uri) {
        return remove(uri);
    }

    public ServiceCategory getServiceCategory(String uri) {
        return (ServiceCategory) get(uri);
    }


}