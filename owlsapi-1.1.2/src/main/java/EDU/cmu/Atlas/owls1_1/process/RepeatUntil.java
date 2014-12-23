package EDU.cmu.Atlas.owls1_1.process;

import EDU.cmu.Atlas.owls1_1.expression.Condition;

public interface RepeatUntil extends ControlConstruct {

	public Condition getUntilCondition();
	
	public void setUntilCondition(Condition condition);
	
	public ControlConstruct getUntilProcess();
	
	public void setUntilProcess(ControlConstruct controlConstruct);
}
