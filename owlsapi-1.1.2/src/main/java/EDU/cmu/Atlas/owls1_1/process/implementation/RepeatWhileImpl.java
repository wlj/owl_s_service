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
import EDU.cmu.Atlas.owls1_1.process.RepeatWhile;
import EDU.cmu.Atlas.owls1_1.utils.OWLSProcessProperties;

public class RepeatWhileImpl extends ControlConstructImpl implements RepeatWhile {
	
	private Condition whileCondition;
	
	private ControlConstruct whileProcess;
	
	public RepeatWhileImpl(Individual instance)
    	throws NotInstanceOfControlConstructException {

		super(instance);
		
		Individual whileCond;
		try {
		    whileCond = OWLUtil.getInstanceFromFunctionalProperty(instance, OWLSProcessProperties.whileCondition);
		} catch (PropertyNotFunctional e) {
		    throw new NotInstanceOfControlConstructException(e);
		} catch (NotAnIndividualException e) {
		    throw new NotInstanceOfControlConstructException(getURI() + " : If condition should of type condition", e);
		}
		
		OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        if (whileCond != null)
            try {
            	whileCondition = builder.createCondition(whileCond);
            } catch (NotInstanceOfExpressionException e1) {
                throw new NotInstanceOfControlConstructException(getURI(), e1);
            }
        else
            throw new NotInstanceOfControlConstructException(getURI() + " : condition part should not be null");
	
        //UntilProcess part
        Individual whileInd;
        try {
            whileInd = OWLUtil.getInstanceFromFunctionalProperty(instance, OWLSProcessProperties.whileProcess);
        } catch (PropertyNotFunctional e) {
            throw new NotInstanceOfControlConstructException(e);
        } catch (NotAnIndividualException e) {
            throw new NotInstanceOfControlConstructException(
                    getURI() + " : Then part should of type control construct", e);
        }

        if (whileInd != null)
        	whileProcess = builder.createControlConstruct(whileInd);
        else
            throw new NotInstanceOfControlConstructException(getURI() + " : then part should not be null");
	}
	
	public RepeatWhileImpl() {
        super();
    }
    public RepeatWhileImpl(String uri) {
        super(uri);
    }
	
	public Condition getWhileCondition() {
		return whileCondition;
	}

	public ControlConstruct getWhileProcess() {
		return whileProcess;
	}

	public void setWhileCondition(Condition condition) {
		whileCondition = condition;
	}

	public void setWhileProcess(ControlConstruct controlConstruct) {
		whileProcess = controlConstruct;
	}
	
	public String toString(String indent) {
		StringBuffer sb = new StringBuffer();
        sb.append("\nRepeat-While Process : ");
        sb.append(getURI());
        sb.append("\nCondition : ");
        sb.append(whileCondition);
        sb.append("\nWhileProcess : ");
        sb.append(whileProcess);
        return sb.toString();
	}
}
