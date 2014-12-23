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
package EDU.cmu.Atlas.owls1_1.core.implementation;

import java.io.Serializable;
import java.util.Vector;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;
import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;

/**
 * @author Massimo Paolucci
 * @author Naveen Srinivasan
 */
public class OWLS_StoreImpl implements OWLS_Store {

	private Vector objectList;

    static Logger logger = Logger.getLogger(OWLS_StoreImpl.class);

    public OWLS_StoreImpl() {
        objectList = new Vector();
    }

    public void add(OWLS_Object object) {
        objectList.addElement(object);
    }

    public boolean remove(OWLS_Object object) {
        return objectList.removeElement(object);
    }

    public boolean remove(String URI) {

        for (int i = 0; i < size(); i++) {
            String objURI = getNth(i).getURI();
            if (objURI != null && objURI.equals(URI)) {
                objectList.removeElement(getNth(i));
                return true;
            }
        }
        return false;
    }
    
    public OWLS_Object get(String URI) {

        for (int i = 0; i < size(); i++) {
            String objURI = getNth(i).getURI();
            if (objURI != null && objURI.equals(URI)) {
                return getNth(i);
            }
        }
        return null;
    }
    
    public int size() {
        return objectList.size();
    }

    public OWLS_Object getNth(int index) throws IndexOutOfBoundsException {

        if (index < 0 || index >= objectList.size()) {
            throw new IndexOutOfBoundsException();
        }
        return (OWLS_Object) objectList.elementAt(index);
    }

    public String toString() {
        return toString("");
    }

    public String toString(String indent) {

        StringBuffer sb = new StringBuffer(indent);
        String className = getClass().getName();
        if (className.indexOf('.') != -1) {
            className = className.substring(className.lastIndexOf('.') + 1, className.length());
        }

        if (size() <= 0) {
            sb.append("\nList is empty");
            return sb.toString();
        }

        try {

            for (int i = 0; i < size(); i++) {
                sb.append(indent);
                sb.append("\n");
                sb.append("[");
                sb.append(i + 1);
                sb.append("]");
                sb.append(getNth(i).toString());
            }
            return sb.toString();
        } catch (IndexOutOfBoundsException e) {
            return sb.toString();
        }
    }

}