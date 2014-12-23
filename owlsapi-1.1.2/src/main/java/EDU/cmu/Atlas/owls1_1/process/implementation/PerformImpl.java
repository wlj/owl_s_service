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

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfPerformException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.process.BindingList;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.uri.OWLSProcessURI;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;
import EDU.cmu.Atlas.owls1_1.utils.OWLS_StoreUtil;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

/**
 * @author Massimo Paolucci
 */
public class PerformImpl extends ControlConstructImpl implements Perform {

    public static final Perform PARENT_PERFORM = new PerformImpl(OWLSProcessURI.TheParentPerform);

    public static final Perform THIS_PERFORM = new PerformImpl(OWLSProcessURI.ThisPerform);

    /** the process to perform */
    private Process process;

    /** the binding associated with this Perform */
    private BindingList binding;
    
    /** stores the reference to the global model of the execution environment */
    OntModel globalServiceExecModel;

    /** @return the current process */
    public Process getProcess() {
        return process;
    }

    /** @param process to record */
    public void setProcess(Process process) {
        this.process = process;
    }

    public PerformImpl(Individual instance) throws NotInstanceOfPerformException {
        super(instance);

        // extract the process
        Individual processRes;
        try {
            processRes = OWLUtil.getInstanceFromFunctionalProperty(instance,
                    OWLSProcessProperties.processPerformProperty);
        } catch (PropertyNotFunctional exp) {
            throw new NotInstanceOfPerformException("process property of Perform " + instance.getURI()
                    + " has more than one value \n");
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfPerformException(e);
        }

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        Process process;
        try {
            process = builder.createProcess(processRes);
        } catch (NotInstanceOfProcessException e) {
            throw new NotInstanceOfPerformException(instance.getURI() + " : Property 'process' not an process ", e);
        }

        setProcess(process);

        try {
            // extract & store each biding in the Perform object
            setBinding((BindingList) OWLS_StoreUtil.extractOWLS_Store_UsingBuilder(instance,
                    OWLSProcessProperties.hasDataFrom, "createBinding", BindingListImpl.class.getName()));
        } catch (OWLS_Store_Exception e) {
            throw new NotInstanceOfPerformException(instance.getURI(),e);
        }

    }

    public PerformImpl(String uri) {
        super(uri);
    }

    public PerformImpl() {
        super();
    }
    
    public OntModel getGlobalServiceModel() {
    	return globalServiceExecModel;
    }
    
    public void setGlobalServiceModel(OntModel globalServiceModel) {
    	this.globalServiceExecModel = globalServiceModel;
    }

    /**
     * @return the binding associated with the Perform
     */
    public BindingList getBinding() {
        return binding;
    }

    /** @param binding to record */
    public void setBinding(BindingList binding) {
        this.binding = binding;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        StringBuffer output = new StringBuffer(indent);
        output.append("\nPerform URI : " + getURI());

        if (getURI() != null
                && (getURI().equals(OWLSProcessURI.TheParentPerform) || getURI().equals(OWLSProcessURI.ThisPerform)))
            return output.toString();

        output.append(indent);
        output.append("\nProcess : ");
        output.append(process.getURI());
        output.append("\nBinding");
        if (binding != null)
            output.append(binding.toString(indent + "\t"));
        else
            output.append("No Binding");
        return output.toString();
    }

}