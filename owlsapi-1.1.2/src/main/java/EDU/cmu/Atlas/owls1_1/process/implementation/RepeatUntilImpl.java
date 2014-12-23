package EDU.cmu.Atlas.owls1_1.process.implementation;

import com.hp.hpl.jena.ontology.Individual;

import edu.cmu.atlas.owl.exceptions.NotAnIndividualException;
import edu.cmu.atlas.owl.exceptions.PropertyNotFunctional;
import edu.cmu.atlas.owl.utils.OWLUtil;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfControlConstructException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfExpressionException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.RepeatUntil;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

public class RepeatUntilImpl extends ControlConstructImpl implements RepeatUntil {

	private Condition untilCondition;
	
	private ControlConstruct untilProcess;
	
	public RepeatUntilImpl(Individual instance)
    	throws NotInstanceOfControlConstructException {

		super(instance);
		
		Individual untilcond;
		try {
		    untilcond = OWLUtil.getInstanceFromFunctionalProperty(instance, OWLSProcessProperties.untilCondition);
		} catch (PropertyNotFunctional e) {
		    throw new NotInstanceOfControlConstructException(e);
		} catch (NotAnIndividualException e) {
		    throw new NotInstanceOfControlConstructException(getURI() + " : If condition should of type condition", e);
		}
		
		OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        if (untilcond != null)
            try {
            	untilCondition = builder.createCondition(untilcond);
            } catch (NotInstanceOfExpressionException e1) {
                throw new NotInstanceOfControlConstructException(getURI(), e1);
            }
        else
            throw new NotInstanceOfControlConstructException(getURI() + " : condition part should not be null");
	
        //UntilProcess part
        Individual untilInd;
        try {
            untilInd = OWLUtil.getInstanceFromFunctionalProperty(instance, OWLSProcessProperties.untilProcess);
        } catch (PropertyNotFunctional e) {
            throw new NotInstanceOfControlConstructException(e);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfControlConstructException(
                    getURI() + " : Then part should of type control construct", e);
        }

        if (untilInd != null)
        	untilProcess = builder.createControlConstruct(untilInd);
        else
            throw new NotInstanceOfControlConstructException(getURI() + " : then part should not be null");
	}
	
	public RepeatUntilImpl() {
        super();
    }
    public RepeatUntilImpl(String uri) {
        super(uri);
    }
	
	public Condition getUntilCondition() {
		return untilCondition;
	}

	public ControlConstruct getUntilProcess() {
		return untilProcess;
	}

	public void setUntilCondition(Condition condition) {
		untilCondition = condition;
	}

	public void setUntilProcess(ControlConstruct controlConstruct) {
		untilProcess = controlConstruct;
	}
	
	public ControlConstructList getComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setComponents(ControlConstructList obj) {
		// TODO Auto-generated method stub

	}

	public Individual getIndividual() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString(String indent) {
		StringBuffer sb = new StringBuffer();
        sb.append("\nRepeat-Until Process : ");
        sb.append(getURI());
        sb.append("\nCondition : ");
        sb.append(untilCondition);
        sb.append("\nUntilProcess : ");
        sb.append(untilProcess);
        return sb.toString();
	}
}
