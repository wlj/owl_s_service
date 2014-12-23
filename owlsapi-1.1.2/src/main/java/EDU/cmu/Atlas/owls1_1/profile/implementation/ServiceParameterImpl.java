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
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceParameterException;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParameter;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProfileURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProfileProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;


/**
 * @author Naveen Srinivasan
 *
 */
public class ServiceParameterImpl extends OWLS_ObjectImpl implements ServiceParameter {

    public String serviceParameterName;

    public Individual sParameter;

    static Logger logger = Logger.getLogger(ServiceParameterImpl.class);

    public ServiceParameterImpl(Individual srvParamInstance) throws NotInstanceOfServiceParameterException {
        super(srvParamInstance);
        if (srvParamInstance != null && !srvParamInstance.hasRDFType(OWLSProfileURI.ServiceParameter)) {
            throw new NotInstanceOfServiceParameterException("Instance " + srvParamInstance.getURI()
                    + " not an instance of Service Parameter");
        }

        String instURI = srvParamInstance.getURI();
        //extracting Service parameter name
        Literal paramName;
        try {
            paramName = OWLUtil.getLiteralFromFunctionalProperty(srvParamInstance, OWLSProfileProperties.serviceParameterName);
        } catch (PropertyNotFunctional e) {
            throw new NotInstanceOfServiceParameterException("serviceParameterName", e);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfServiceParameterException("serviceParameterName", e);
        }

        if (paramName != null)
            setServiceParameterName(paramName.getString().trim());
        else
            throw new NotInstanceOfServiceParameterException("Property 'serviceParameterName' missing in Service Category :" + instURI);

        //extracting sParameter
        Individual sParameter;
        try {
            sParameter = OWLUtil.getInstanceFromFunctionalProperty(srvParamInstance, OWLSProfileProperties.sParameter);
        } catch (NotAnIndividualException e1) {
            throw new NotInstanceOfServiceParameterException("sParameter", e1);
        } catch (PropertyNotFunctional e1) {
            throw new NotInstanceOfServiceParameterException("sParameter", e1);
        }
        if (sParameter != null)
            setsParameter(sParameter);
        else
            throw new NotInstanceOfServiceParameterException("Property 'sParameter' missing in Service Category :" + instURI);

    }

    public ServiceParameterImpl(String uri) {
        super(uri);
    }

    public ServiceParameterImpl() {
    }

    /**
     * @return
     */
    public String getServiceParameterName() {
        return serviceParameterName;
    }

    /**
     * @return
     */
    public Individual getsParameter() {
        return sParameter;
    }

    /**
     * @param string
     */
    public void setServiceParameterName(String string) {
        serviceParameterName = string;
    }

    /**
     * @param string
     */
    public void setsParameter(Individual ind) {
        sParameter = ind;
    }

    public String toString() {
        StringBuffer st = new StringBuffer("\nServiceParameter");
        st.append("\n\t URI= ");
        st.append(getURI());
        st.append("\n\t ParameterName= ");
        st.append(getServiceParameterName());

        if (getsParameter() != null) {
            st.append("\n\t Parameter= ");
            st.append(getsParameter().toString());
        } else {
            st.append("\n\t Parameter= null");
        }
        return st.toString();
    }

    public String toString(String indent) {
        return toString();
    }

}