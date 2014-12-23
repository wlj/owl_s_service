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

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_ObjectImpl;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfExpressionException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfLogicLanguage;
import EDU.cmu.Atlas.owls1_1.expression.LogicLanguage;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.NotAnLiteralException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 */
public class ExpressionImpl extends OWLS_ObjectImpl implements EDU.cmu.Atlas.owls1_1.expression.Expression {

    LogicLanguage expressionLanguage;

    String expressionBody;

    static String SWRL_EXP = "http://www.daml.org/services/owl-s/1.1/generic/Expression.owl#SWRL-Expression";

    static String DRS_EXP = "http://www.daml.org/services/owl-s/1.1/generic/Expression.owl#DRS-Expression";

    static final String KIF_EXP = "http://www.daml.org/services/owl-s/1.1/generic/Expression.owl#KIF-Expression";

    public ExpressionImpl(Individual individual) throws NotInstanceOfExpressionException {
        super(individual);

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();

        //extracting expressionLanguage
        Individual expLanguageInst = null;
        try {
            expLanguageInst = OWLUtil.getInstanceFromFunctionalProperty(individual, OWLSProcessProperties.expressionLanguage);
        } catch (PropertyNotFunctional e) {
            throw new NotInstanceOfExpressionException(individual.getURI() + " : Property 'expressionLanguage' has more than one value", e);
        } catch (NotAnIndividualException e1) {
            throw new NotInstanceOfExpressionException(individual.getURI() + " : Property 'expressionLanguage' is not a individual", e1);
        }

        /*
         * hack. Ideally, instances of SWRL-Expression and DRS-Expression should automatically
         * contain the property correct expressionLanguage jena is not automatically do it, so the
         * parse does it.
         */
        if (expLanguageInst != null) {
            try {
                setExpressionLanguage(builder.createLogicLanguage(expLanguageInst));
            } catch (NotInstanceOfLogicLanguage e2) {
                throw new NotInstanceOfExpressionException(individual.getURI(), e2);
            }
        } else if (individual.hasRDFType(SWRL_EXP)) {
            try {
                setExpressionLanguage(builder.createLogicLanguage(OWLSProcessURI.SWRL));
            } catch (NotInstanceOfLogicLanguage e2) {
                throw new NotInstanceOfExpressionException(individual.getURI(), e2);
            }
        } else if (individual.hasRDFType(DRS_EXP)) {
            try {
                setExpressionLanguage(builder.createLogicLanguage(OWLSProcessURI.DRS));
            } catch (NotInstanceOfLogicLanguage e2) {
                throw new NotInstanceOfExpressionException(individual.getURI(), e2);
            }
        } else if (individual.hasRDFType(KIF_EXP)) {
            try {
                setExpressionLanguage(builder.createLogicLanguage(OWLSProcessURI.KIF));
            } catch (NotInstanceOfLogicLanguage e2) {
                throw new NotInstanceOfExpressionException(individual.getURI(), e2);
            }
        } else
            throw new NotInstanceOfExpressionException(individual.getURI() + " : Property 'expressionLanguage' is missing");

        //extracting expressionBody
        Literal expBodyLit = null;
        try {
            expBodyLit = OWLUtil.getLiteralFromFunctionalProperty(individual, OWLSProcessProperties.expressionBody);
        } catch (PropertyNotFunctional e) {
            throw new NotInstanceOfExpressionException(individual.getURI() + " : Property 'expressionBody' has more than one value", e);
        } catch (NotAnLiteralException e1) {
            throw new NotInstanceOfExpressionException(individual.getURI() + " : Property 'expressionBody' is not a literal", e1);
        }

        if (expBodyLit != null)
            setExpressionBody(expBodyLit.getString().trim());
        else
            throw new NotInstanceOfExpressionException(individual.getURI() + " : Property 'expressionBody' is missing");

    }

    public ExpressionImpl(String uri) {
        super(uri);
    }

    public ExpressionImpl() {
    }

    public LogicLanguage getExpressionLanguage() {
        return expressionLanguage;
    }

    public void setExpressionLanguage(LogicLanguage ll) {
        expressionLanguage = ll;
    }

    public String getExpressionBody() {
        return expressionBody;
    }

    public void setExpressionBody(String body) {
        expressionBody = body;
    }

    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("\nExpression :");
        sb.append(getURI());
        sb.append("\nexpressionLanguage :");
        sb.append(expressionLanguage);
        sb.append("\nexpressionBody : \n");
        sb.append(expressionBody);
        return sb.toString();
    }

    public String toString(String indent) {
        return toString();
    }

}