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

import com.hp.hpl.jena.ontology.Individual;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;

/**
 * @author Massimo Paolucci
 * @author Naveen Srinivasan
 */
public abstract class OWLS_ObjectImpl implements OWLS_Object {

	/** the store for an individual */
    private Individual individual;

    /** 
     * store for the URI 
     */
    private String uri;

    /**
     * default constructor
     */
    public OWLS_ObjectImpl() {
    }

    /**
     * constructor
     * @param resource the resource from which this object is constructed
     */
    public OWLS_ObjectImpl(Individual individual) {
        // set the individual, the resource and the URI of this object
        if (individual != null) {
            setIndividual(individual);
            setURI(individual.getURI());
        }
    }

    public OWLS_ObjectImpl(String uri) {
        setURI(uri);
    }

    /**
     * extract the individual of this object
     * @return the individual
     */
    public Individual getIndividual() {
        return individual;
    }

    /**
     * set the individual of this object
     * @param individual the individual of this object
     */
    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    /**
     * set the URI of any object
     * @param uri it is the uri of this object
     * @see EDU.cmu.Atlas.owls1_1.core.OWLS_Object#setURI(java.lang.String)
     */
    protected void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * get the uri of the resource corresponding to the object
     * @return the URI of the object
     * @see EDU.cmu.Atlas.owls1_1.core.OWLS_Object#getURI()
     */
    public String getURI() {
        return uri;
    }
    
    public String toString() {
        return toString("");
    }

}