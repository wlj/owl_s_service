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
package EDU.cmu.Atlas.owls1_1.process.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfControlConstructException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfControlConstructListException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfPerformException;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

import com.hp.hpl.jena.ontology.Individual;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Naveen Srinivasan
 * 
 */
public class ControlConstructFactory {

    static Logger logger = Logger.getLogger(ControlConstructFactory.class);

    /**
     * select the type of control construct and call the appropriate parser
     * @param controlConstructInd an instance of the control construct to parse in the TBox
     * @return the instance of control construct
     * @throws NotInstanceOfControlConstructException
     */
    public static ControlConstruct extractControlConstruct(Individual controlConstructInd) throws NotInstanceOfControlConstructException {

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();

        // extract the components of the control construct
        ControlConstructList ctrlConstructList = extractComponents(controlConstructInd);

        // check whether the control construct is of type perform
        if (OWLUtil.typeOf(controlConstructInd, OWLSProcessURI.Perform)) {
            try {
                return builder.createPerform(controlConstructInd);
            } catch (NotInstanceOfPerformException e) {
                throw new NotInstanceOfControlConstructException(e);
            }
        }

        ControlConstruct ctrlConstruct;
        if (OWLUtil.typeOf(controlConstructInd, OWLSProcessURI.Sequence)) {
            ctrlConstruct = new SequenceImpl(controlConstructInd);
            // builder.createControlConstruct(controlConstructInd, OWLS_Builder_Util.SEQUENCE);
        } else if (OWLUtil.typeOf(controlConstructInd, OWLSProcessURI.Split)) {
            ctrlConstruct = new SplitImpl(controlConstructInd);
            // builder.createControlConstruct(controlConstructInd, OWLS_Builder_Util.SPLIT);
        } else if (OWLUtil.typeOf(controlConstructInd, OWLSProcessURI.Split_JOIN)) {
            ctrlConstruct = new SplitJoinImpl(controlConstructInd);
            // builder.createControlConstruct(controlConstructInd, OWLS_Builder_Util.SPLIT_JOIN);
        } else if (OWLUtil.typeOf(controlConstructInd, OWLSProcessURI.Choice)) {
            ctrlConstruct = new ChoiceImpl(controlConstructInd);
            // builder.createControlConstruct(controlConstructInd, OWLS_Builder_Util.CHOICE);
        } else if (OWLUtil.typeOf(controlConstructInd, OWLSProcessURI.AnyOrder)) {
            ctrlConstruct = new AnyOrderImpl(controlConstructInd);
            // builder.createControlConstruct(controlConstructInd, OWLS_Builder_Util.ANY_ORDER);
        } else if (OWLUtil.typeOf(controlConstructInd, OWLSProcessURI.If_Then_Else)) {
            ctrlConstruct = new IfThenElseImpl(controlConstructInd);
            // builder.createControlConstruct(controlConstructInd, OWLS_Builder_Util.IFTHENELSE);
        }else if (OWLUtil.typeOf(controlConstructInd, OWLSProcessURI.Repeat_While)) {
            ctrlConstruct = new RepeatWhileImpl(controlConstructInd);
            // builder.createControlConstruct(controlConstructInd, OWLS_Builder_Util.IFTHENELSE);
        }else {
            throw new NotInstanceOfControlConstructException("the process type of " + controlConstructInd.getURI() + " is invalid \n");
        }

        ctrlConstruct.setComponents(ctrlConstructList);
        return ctrlConstruct;
    }

    /**
     * extract the components of a control construct
     * @param controlConstructInd the Individual of the control construct
     * @return the list of process components
     * @throws NotInstanceOfControlConstructException
     */
    public static ControlConstructList extractComponents(Individual controlConstructInd) throws NotInstanceOfControlConstructException {
        Individual components;
        try {
            components = OWLUtil.getInstanceFromFunctionalProperty(controlConstructInd, OWLSProcessProperties.components);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfControlConstructException("the sequence " + controlConstructInd.getURI()
                    + " has more than one components construct \n");
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfControlConstructException(e);
        }
        if (components == null)
            return null;
        // parse the components list and record it in the sequence
        ControlConstructList processes;
        try {
            processes = extractControlConstructList(components);
        } catch (NotInstanceOfControlConstructListException e) {
            throw new NotInstanceOfControlConstructException(e);
        }
        // return the processes found
        return processes;
    }

    /**
     * extract a list of components
     * @param ccListInd the instance of the list
     * @return the list of components
     * @throws a SyntacticError when there is an error in the list, or an error with the components
     *             of the list
     */
    public static ControlConstructList extractControlConstructList(Individual ccListInd) throws NotInstanceOfControlConstructListException {

        ControlConstructList list = new ControlConstructListImpl(ccListInd);

        // Check if the list is instance of Nil list
        if (ccListInd.getURI() != null && ccListInd.getURI().equals(OWLSProcessURI.shadow_rdf_nil)) {
            logger.debug("Found a Nil List");
            return null;
        }

        // extract the first
        Individual firstComponent;
        try {
            logger.debug("Individual= " + ccListInd.getURI());
            firstComponent = OWLUtil.getInstanceFromFunctionalProperty(ccListInd, OWLSProcessProperties.shadow_rdf_first);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfControlConstructListException(exp);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfControlConstructListException(e);
        }

        // first component of list should not be null and should be control
        // construct
        if (firstComponent == null) {
            logger.debug("firstcomponent == null");
            throw new NotInstanceOfControlConstructListException("FirstComponent of List is null");
        }

        /*
         * if ((firstComponent.getURI() != null) &&
         * (firstComponent.getURI()).equals(OWLSProcessURI.shadow_rdf_nil)) { logger.debug("Found a
         * NIL"); // return the empty list return list; }
         */

        // make Process Component
        ControlConstruct processComponent;
        try {
            processComponent = extractControlConstruct(firstComponent);
        } catch (NotInstanceOfControlConstructException e) {
            throw new NotInstanceOfControlConstructListException("First element should be a ConstrolConstruct", e);
        }

        // record the component found
        list.setFirst(processComponent);

        // extract the rest of the list
        Individual restComponents;
        try {
            restComponents = OWLUtil.getInstanceFromFunctionalProperty(ccListInd, OWLSProcessProperties.shadow_rdf_rest);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfControlConstructListException(exp);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfControlConstructListException(e);
        }
        // extract the list from the resource found
        ControlConstructList processComponentList = extractControlConstructList(restComponents);
        // record the list of components
        list.setRest(processComponentList);
        // return the list generated
        return list;
    }

}