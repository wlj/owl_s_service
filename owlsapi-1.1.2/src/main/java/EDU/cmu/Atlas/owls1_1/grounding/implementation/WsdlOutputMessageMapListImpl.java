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
package EDU.cmu.Atlas.owls1_1.grounding.implementation;

import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMapList;

/**
 * @author Naveen Srinivasan
 *  
 */
public class WsdlOutputMessageMapListImpl extends WsdlMessageMapListImpl implements WsdlOutputMessageMapList {

    public boolean removeWsdlOutputMessageMap(String uri) {
        return remove(uri);
    }

    public boolean removeWsdlOutputMessageMap(WsdlOutputMessageMap wsdlOutputMessageMap) {
        return remove(wsdlOutputMessageMap);
    }

    public WsdlOutputMessageMap getWsdlOutputMessageMap(String uri) {
        return (WsdlOutputMessageMap) get(uri);
    }

    public void addWsdlOutputMessageMap(WsdlOutputMessageMap wsdlOutputMessageMap) {
        add(wsdlOutputMessageMap);
    }

    public WsdlOutputMessageMap getNthWsdlOutputMessageMap(int index) throws IndexOutOfBoundsException {
        return (WsdlOutputMessageMap) getNth(index);
    }

}