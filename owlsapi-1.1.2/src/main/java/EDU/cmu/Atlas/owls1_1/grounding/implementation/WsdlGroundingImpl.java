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

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlGroundingException;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.parser.OWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceGroundingImpl;
import EDU.cmu.Atlas.owls1_1.utils.OWLSGroundingProperties;
import EDU.cmu.Atlas.owls1_1.utils.OWLS_StoreUtil;

import com.hp.hpl.jena.ontology.Individual;

/**
 * @author Naveen Srinivasan
 *  
 */
public class WsdlGroundingImpl extends ServiceGroundingImpl implements WsdlGrounding {

    WsdlAtomicProcessGroundingList wapgList;

    /**
     * @param individual
     * @throws NotInstanceOfWsdlGroundingException
     */
    public WsdlGroundingImpl(Individual individual) throws NotInstanceOfWsdlGroundingException {
        super(individual);

        try {
            setWsdlAtomicProcessGroundingList((WsdlAtomicProcessGroundingList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                    OWLSGroundingProperties.HasAtomicProcessGrounding, "createWsdlAtomicProcessGrounding",
                    WsdlAtomicProcessGroundingListImpl.class.getName()));
        } catch (OWLS_Store_Exception e) {
            throw new NotInstanceOfWsdlGroundingException(e);
        }

    }

    /**
     * @param individual
     * @throws NotInstanceOfWsdlGroundingException
     */
    public WsdlGroundingImpl(Individual individual, OWLSErrorHandler errHandler) {

        super(individual);
        setWsdlAtomicProcessGroundingList((WsdlAtomicProcessGroundingList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(individual,
                OWLSGroundingProperties.HasAtomicProcessGrounding, "createWsdlAtomicProcessGrounding",
                WsdlAtomicProcessGroundingListImpl.class.getName(), errHandler));

    }

    public WsdlGroundingImpl() {
        super();
    }

    public WsdlGroundingImpl(String uri) {
        super(uri);
    }

    public WsdlAtomicProcessGroundingList getWsdlAtomicProcessGroundingList() {
        return wapgList;
    }

    public void setWsdlAtomicProcessGroundingList(WsdlAtomicProcessGroundingList list) {
        wapgList = list;
    }

    public String toString() {
        return wapgList.toString();
    }

    public String toString(String indent) {
        return wapgList.toString();
    }
}