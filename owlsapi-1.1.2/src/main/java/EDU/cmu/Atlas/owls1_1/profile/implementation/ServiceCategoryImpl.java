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

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceCategoryException;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProfileURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProfileProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;


/**
 * @author Naveen Srinivasan
 *
 */
public class ServiceCategoryImpl extends OWLS_ObjectImpl implements ServiceCategory {

    public String categoryName;

    public String taxonomy;

    public String value;

    public String code;

    static Logger logger = Logger.getLogger(ServiceCategoryImpl.class);

    public ServiceCategoryImpl(Individual srvCatInstance) throws NotInstanceOfServiceCategoryException {

        super(srvCatInstance);
        if (srvCatInstance != null && !srvCatInstance.hasRDFType(OWLSProfileURI.ServiceCategory)) {
            throw new NotInstanceOfServiceCategoryException("Instance " + srvCatInstance.getURI() + " not an instance of Service Category");
        }
        String instURI = srvCatInstance.getURI();
        //Checking if the Service Category is an instance of any OWL-S
        // Predefined Service Cateogories.
        //If an pre-defined is found then set the category name and taxonomy
        // appropriately.
        if (srvCatInstance.getRDFType().toString().equals(OWLSProfileURI.NAICS)) {

            setCategoryName("NAICS");
            setTaxonomy(OWLSProfileURI.NAICS);

        } else {

            if (srvCatInstance.getRDFType().toString().equals(OWLSProfileURI.UNSPSC)) {
                setCategoryName("UNSPSC");
                setTaxonomy(OWLSProfileURI.UNSPSC);
            } else {
                Literal categoryName;
                try {
                    categoryName = OWLUtil.getLiteralFromFunctionalProperty(srvCatInstance, OWLSProfileProperties.categoryName);
                } catch (PropertyNotFunctional e) {
                    throw new NotInstanceOfServiceCategoryException(e);
                } catch (NotAnLiteralException e) {
                    throw new NotInstanceOfServiceCategoryException("Property 'categoryName' is not a literal in ServiceCategory : "
                            + instURI, e);
                }
                if (categoryName != null)
                    setCategoryName(categoryName.toString());
                else
                    throw new NotInstanceOfServiceCategoryException("Property 'categoryName' missing in Service Category :" + instURI);

                Literal taxonomy;
                try {
                    taxonomy = OWLUtil.getLiteralFromFunctionalProperty(srvCatInstance, OWLSProfileProperties.taxonomy);
                } catch (PropertyNotFunctional e1) {
                    throw new NotInstanceOfServiceCategoryException(e1);
                } catch (NotAnLiteralException e1) {
                    throw new NotInstanceOfServiceCategoryException("Property 'taxonomy' is not a literal in ServiceCategory:" + instURI);
                }
                if (taxonomy != null)
                    setTaxonomy(taxonomy.toString());
                else
                    throw new NotInstanceOfServiceCategoryException("Property 'categoryName' missing in Service Category :" + instURI);

            }

        }

        Literal value;
        try {
            value = OWLUtil.getLiteralFromFunctionalProperty(srvCatInstance, OWLSProfileProperties.value);
        } catch (PropertyNotFunctional e) {
            throw new NotInstanceOfServiceCategoryException(e);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfServiceCategoryException("Property 'value' is not a literal in ServiceCategory:" + instURI);
        }
        if (value != null)
            setValue(value.toString().trim());
        else
            throw new NotInstanceOfServiceCategoryException("Property 'value' missing in Service Category :" + instURI);

        Literal code;
        try {
            code = OWLUtil.getLiteralFromFunctionalProperty(srvCatInstance, OWLSProfileProperties.code);
        } catch (PropertyNotFunctional e1) {
            throw new NotInstanceOfServiceCategoryException(e1);
        } catch (NotAnLiteralException e1) {
            throw new NotInstanceOfServiceCategoryException("Property 'code' is not a literal in ServiceCategory:" + instURI);
        }
        if (code != null)
            setCode(code.toString().trim());
        else
            throw new NotInstanceOfServiceCategoryException("Property 'code' missing in Service Category :" + instURI);

    }

    public ServiceCategoryImpl(String uri) {
        super(uri);
    }

    public ServiceCategoryImpl() {
    }

    /**
     * @return
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * @return
     */
    public String getTaxonomy() {
        return taxonomy;
    }

    /**
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * @param string
     */
    public void setCategoryName(String string) {
        categoryName = string;
    }

    /**
     * @param string
     */
    public void setCode(String string) {
        code = string;
    }

    /**
     * @param string
     */
    public void setTaxonomy(String string) {
        taxonomy = string;
    }

    /**
     * @param string
     */
    public void setValue(String string) {
        value = string;
    }

    public String toString() {
        StringBuffer st = new StringBuffer("\nServiceCategory");
        st.append("\n\t URI : ");
        st.append(getURI());
        st.append("\n\t CategoryName= ");
        st.append(categoryName);
        st.append("\n\t Taxonomy= ");
        st.append(taxonomy);
        st.append("\n\t Value= ");
        st.append(value);
        st.append(code);
        return st.toString();
    }

    public String toString(String indent) {
        return toString();
    }
}