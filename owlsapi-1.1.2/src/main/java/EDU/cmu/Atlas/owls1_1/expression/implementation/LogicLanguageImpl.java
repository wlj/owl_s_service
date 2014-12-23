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
package EDU.cmu.Atlas.owls1_1.expression.implementation;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfLogicLanguage;
import EDU.cmu.Atlas.owls1_1.expression.LogicLanguage;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;
import EDU.cmu.Atlas.owls1_1.uri.SemanticWebURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 */
public class LogicLanguageImpl extends OWLS_ObjectImpl implements LogicLanguage {

    public static LogicLanguage SWRL;

    public static LogicLanguage DRS;

    public static LogicLanguage KIF;

    static {
        SWRL = new LogicLanguageImpl(OWLSProcessURI.SWRL);
        SWRL.setRefURI(SemanticWebURI.SWRL);

        DRS = new LogicLanguageImpl(OWLSProcessURI.DRS);
        DRS.setRefURI(SemanticWebURI.DRS);

        KIF = new LogicLanguageImpl(OWLSProcessURI.KIF);
        KIF.setRefURI(SemanticWebURI.KIF);
    }

    protected String refURI;

    public LogicLanguageImpl(Individual individual) throws NotInstanceOfLogicLanguage {
        super(individual);

        //extracting refURI
        Literal refURI = null;
        try {
            refURI = OWLUtil.getLiteralFromProperty(individual, OWLSProcessProperties.refURI);
        } catch (NotAnLiteralException e1) {
            throw new NotInstanceOfLogicLanguage(individual.getURI() + " : Property 'refURI' is not a literal", e1);
        }

        if (refURI != null)
            setRefURI(refURI.getString().trim());

    }

    public LogicLanguageImpl(String uri) {
        super(uri);
    }

    public LogicLanguageImpl() {
        super();
    }

    public String getRefURI() {
        return refURI;
    }

    public void setRefURI(String uri) {
        refURI = uri;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String indent) {

        StringBuffer sb = new StringBuffer();
        sb.append("\nLogicalLanguage :");
        sb.append(getURI());
        sb.append("\nrefURI :");
        sb.append(refURI);
        return sb.toString();
    }

}