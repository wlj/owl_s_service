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
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfActorException;
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProfileURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProfileProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 *  
 */
public class ActorImpl extends OWLS_ObjectImpl implements Actor {

    private String name;

    private String title;

    private String phone;

    private String fax;

    private String email;

    private String physicalAddress;

    private String webURL;

    static Logger logger = Logger.getLogger(ActorImpl.class);

    public ActorImpl(Individual instance) throws NotInstanceOfActorException {
        super(instance);

        // check whether the instance passed is the instance requested
        if (instance != null && !instance.hasRDFType(OWLSProfileURI.Actor)) {
            throw new NotInstanceOfActorException("Instance " + instance.getURI() + " not an instance of Actor");
        }

        Literal tempLiteral;

        //extract name
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(instance, OWLSProfileProperties.actor_name);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfActorException("Actor : " + instance.getURI() + "value of property 'name' is not a literal", e);
        }

        if (tempLiteral != null)
            setName(tempLiteral.getString().trim());
        else
            logger.warn("owl-s-api-warning : Property 'name' missing in Actor Instance " + instance.getURI());

        //extract title
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(instance, OWLSProfileProperties.actor_title);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfActorException("Actor : " + instance.getURI() + "value of property 'title' is not a literal", e);
        }
        if (tempLiteral != null)
            setTitle(tempLiteral.getString().trim());
        else
            logger.warn("owl-s-api-warning : Property 'title' missing in Actor Instance " + instance.getURI());

        //extract phone
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(instance, OWLSProfileProperties.actor_phone);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfActorException("Actor : " + instance.getURI() + "value of property 'phone' is not a literal", e);
        }
        if (tempLiteral != null)
            setPhone(tempLiteral.getString().trim());
        else
            logger.warn("owl-s-api-warning : Property 'phone' missing in Actor Instance " + instance.getURI());
        //extract fax
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(instance, OWLSProfileProperties.actor_fax);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfActorException("Actor : " + instance.getURI() + "value of property 'fax' is not a literal", e);
        }
        if (tempLiteral != null)
            setFax(tempLiteral.getString().trim());
        else
            logger.warn("owl-s-api-warning : Property 'fax' missing in Actor Instance " + instance.getURI());

        //extract email
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(instance, OWLSProfileProperties.actor_email);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfActorException("Actor : " + instance.getURI() + "value of property 'e-mail' is not a literal", e);
        }
        if (tempLiteral != null)
            setEmail(tempLiteral.getString().trim());
        else
            logger.warn("owl-s-api-warning : Property 'email' missing in Actor Instance " + instance.getURI());

        //extract address
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(instance, OWLSProfileProperties.actor_address);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfActorException("Actor : " + instance.getURI() + "value of property 'address' is not a literal", e);
        }
        if (tempLiteral != null) {
            setAddress(tempLiteral.getString().trim());
        } else
            logger.warn("owl-s-api-warning : Property 'address' missing in Actor Instance " + instance.getURI());

        //extract weburl
        try {
            tempLiteral = OWLUtil.getLiteralFromProperty(instance, OWLSProfileProperties.actor_weburl);
        } catch (NotAnLiteralException e) {
            throw new NotInstanceOfActorException("Actor : " + instance.getURI() + "value of property 'weburl' is not a literal", e);
        }

        if (tempLiteral != null)
            setWebURL(tempLiteral.getString().trim());
        else
            logger.warn("owl-s-api-warning : Property 'webURL' missing in Actor Instance " + instance.getURI());

    }

    public ActorImpl(String uri) {
        super(uri);
    }

    public ActorImpl() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return physicalAddress;
    }

    public void setAddress(String address) {
        this.physicalAddress = address;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("\nActor");
        // add name title phone fax email Address webURL
        sb.append("\n\t URI= " + getURI());
        sb.append("\n\t name= " + getName());
        sb.append("\n\t title= " + getTitle());
        sb.append("\n\t phone= " + getPhone());
        sb.append("\n\t fax= " + getFax());
        sb.append("\n\t email= " + getEmail());
        sb.append("\n\t Address= " + getAddress());
        sb.append("\n\t webURL= " + getWebURL());
        // return string constructed
        return sb.toString();
    }

    public String toString(String indent) {
        return toString();
    }

}