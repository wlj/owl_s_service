package EDU.pku.ly.Process.Implementation;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import org.junit.Assert;

import edu.pku.ly.SqlOpe.SQLHelper;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.ConditionList;
import EDU.cmu.Atlas.owls1_1.expression.Expression;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionImpl;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.ParameterList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.process.implementation.AnyOrderImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ChoiceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ControlConstructImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ControlConstructListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.IfThenElseImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ParameterImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.PerformImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.RepeatUntilImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.RepeatWhileImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SequenceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitJoinImpl;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.pku.ly.Process.ProcessParser;
import EDU.pku.ly.Process.util.ProcessResource;
import EDU.pku.ly.Process.util.ProcessSql;

public class ProcessParserImpl implements ProcessParser {
	
	private static final long serialVersionUID = -1L;
	
	public ProcessParserImpl()
	{}
	
	public void ProcessParserEntry(Process process, int service_id)
	{
		if(process == null || service_id == 0)
		{
			return;
		}
		ProcessPersistence(process, service_id);
	}
	
	public void ProcessPersistence(Process process, int service_id) 
	{
		//get process list and find root process
		//ProcessList processlist = owlsProcessModel.getProcessList();
		
		/*Process rootProcess = null;
		for(int i = 0; i < processlist.size(); i++)
		{
			if(processlist.getNthProcess(i).getName() == processname)
			{
				rootProcess = processlist.getNthProcess(i);
			}
		}*/
		
		String name = process.getName();
		String uri = process.getURI();
		//String namespace = (url == null || url == "") ? "" : url.substring(0, url.lastIndexOf("#"));
		int type = process.isAtomic() ? 1 : (process.isComposite() ? 2 : 3);
		int cc_type = 0;
		
		String sql = ProcessSql.sql_process_insert;
		Object[] params = null;
		if(type == 1) //simple
		{
			params = new Object[]{0, name, uri, type, cc_type, service_id};
			ProcessInsertOpe(sql, params);
		}
		else if(type == 2) //composite
		{
			cc_type = ProcessResource.GetCCType(process);
			
			//process itself
			params = new Object[]{0, name, uri, type, cc_type, service_id};
			ProcessInsertOpe(sql, params);
			
			int newid = SQLHelper.GetLastInsertID();
			
			//sub-processes
			ProcessParseOpe(process, newid, service_id);	
		}
		else
		{
			//simple
		}
	}

	private void ProcessParseOpe(Process rootProcess, int parent_id, int service_id)
	{
		ControlConstructImpl cci = ProcessResource.GetCCIFromProcess(rootProcess);

		if(cci instanceof SequenceImpl)
		{
			ParseForSequenceImpl(rootProcess, cci, parent_id, service_id);
		}
		else if(cci instanceof IfThenElseImpl)
		{
			ParseForIfThenElseImpl(cci, parent_id, service_id);
		}
		else if(cci instanceof SplitImpl)
		{
			ParseForSplitImpl(cci, parent_id, service_id);
		}
		else if(cci instanceof SplitJoinImpl)
		{
			ParseForSplitJoinImpl(cci, parent_id, service_id);
		}
		else if(cci instanceof ChoiceImpl)
		{
			ParseForChoiceImpl(cci, parent_id, service_id);
		}
		else if(cci instanceof AnyOrderImpl)
		{
			ParseForAnyOrderImpl(cci, parent_id, service_id);
		}
		else if(cci instanceof RepeatUntilImpl)
		{
			ParseForRepeatUntilImpl(cci, parent_id, service_id);
		}
		else if(cci instanceof RepeatWhileImpl)
		{
			ParseForRepeatWhileImpl(cci, parent_id, service_id);
		}
	}
	
	private void ParseForRepeatWhileImpl(ControlConstructImpl cci, int parent_id, int service_id) {
		// TODO Auto-generated method stub
		if(!(cci instanceof RepeatWhileImpl))
		{
			return;
		}
		
		RepeatWhileImpl itei = (RepeatWhileImpl)cci;
		
		//condition
		Condition condition = itei.getWhileCondition();
		String uri = condition.getURI();
		String language = condition.getExpressionLanguage().getURI();
		String body = condition.getExpressionBody();
		
		String sql = ProcessSql.sql_condition_insert;
		Object[] params = new Object[]{0, uri == null ? "" : uri, 
				language == null ? "" : language, body == null ? "" : body, 1, parent_id, 0};
		
		ProcessInsertOpe(sql, params);
		
		//then
		PerformImpl pfi = null;
		if(itei.getWhileProcess() != null)
		{
			pfi = (PerformImpl)itei.getWhileProcess();
			//ThenAndElseCCInsertOpe(pfi, parent_id , service_id, 2);
		}
	}

	private void ParseForRepeatUntilImpl(ControlConstructImpl cci, int parent_id, int service_id) {
		// TODO Auto-generated method stub
		if(!(cci instanceof RepeatUntilImpl))
		{
			return;
		}
		
		RepeatUntilImpl itei = (RepeatUntilImpl)cci;
		
		//condition
		Condition condition = itei.getUntilCondition();
		String uri = condition.getURI();
		String language = condition.getExpressionLanguage().getURI();
		String body = condition.getExpressionBody();
		
		String sql = ProcessSql.sql_condition_insert;
		Object[] params = new Object[]{0, uri == null ? "" : uri, 
				language == null ? "" : language, body == null ? "" : body, 1, parent_id, 0};
		
		ProcessInsertOpe(sql, params);
		
		//then
		PerformImpl pfi = null;
		if(itei.getUntilProcess() != null)
		{
			pfi = (PerformImpl)itei.getUntilProcess();
			//ThenAndElseCCInsertOpe(pfi, parent_id , service_id, 2);
		}
	}

	private void ParseForAnyOrderImpl(ControlConstructImpl cci, int parent_id, int service_id) {
		// TODO Auto-generated method stub
		AnyOrderImpl itei = (AnyOrderImpl)cci;
		PerformImpl pfi = (PerformImpl)itei.getComponents();
		
		Process first_process = pfi.getProcess();
		
		String name = first_process.getName();
		String uri = first_process.getURI();
		
		int type = first_process.isAtomic() ? 1 : 2;
		
		int cc_type = 0;
		if(type == 2)
		{
			cc_type = ProcessResource.GetCCIFromProcess(first_process).isSequence() ? 1 : 0;
		}
		
		//process table
		String sql = ProcessSql.sql_process_insert;
		Object[] params = new Object[]{ 0, name, uri, type, cc_type, service_id };
		
		try {
			SQLHelper.ExecuteNoneQuery(sql, params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void ParseForChoiceImpl(ControlConstructImpl cci, int parent_id, int service_id) {
		// TODO Auto-generated method stub
		ChoiceImpl itei = (ChoiceImpl)cci;
		PerformImpl pfi = (PerformImpl)itei.getComponents();
		
		Process first_process = pfi.getProcess();
		
		String name = first_process.getName();
		String uri = first_process.getURI();
		
		int type = first_process.isAtomic() ? 1 : 2;
		
		int cc_type = 0;
		if(type == 2)
		{
			cc_type = ProcessResource.GetCCIFromProcess(first_process).isSequence() ? 1 : 0;
		}
		
		//process table
		String sql = "insert into process_process values(?, ?, ?, ?, ?);";
		Object[] params = new Object[]{0, name, uri, type, cc_type};
		
		try {
			SQLHelper.ExecuteNoneQuery(sql, params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void ParseForSplitJoinImpl(ControlConstructImpl cci, int parent_id, int service_id) {
		// TODO Auto-generated method stub
		SplitJoinImpl itei = (SplitJoinImpl)cci;
		PerformImpl pfi = (PerformImpl)itei.getComponents();
		
		Process first_process = pfi.getProcess();
		
		String name = first_process.getName();
		String uri = first_process.getURI();
		
		int type = first_process.isAtomic() ? 1 : 2;
		
		int cc_type = 0;
		if(type == 2)
		{
			cc_type = ProcessResource.GetCCIFromProcess(first_process).isSequence() ? 1 : 0;
		}
		
		//process table
		String sql = ProcessSql.sql_process_insert;
		Object[] params = new Object[]{ 0, name, uri, type, cc_type, service_id };
		
		try {
			SQLHelper.ExecuteNoneQuery(sql, params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void ParseForSplitImpl(ControlConstructImpl cci, int parent_id, int service_id) 
	{
		// TODO Auto-generated method stub
		SplitImpl itei = (SplitImpl)cci;
		PerformImpl pfi = (PerformImpl)itei.getComponents();
		
		Process first_process = pfi.getProcess();
		
		String name = first_process.getName();
		String uri = first_process.getURI();
		
		int type = first_process.isAtomic() ? 1 : 2;
		
		int cc_type = 0;
		if(type == 2)
		{
			cc_type = ProcessResource.GetCCIFromProcess(first_process).isSequence() ? 1 : 0;
		}
		
		//process table
		String sql = ProcessSql.sql_process_insert;
		Object[] params = new Object[]{ 0, name, uri, type, cc_type, service_id };
		
		try {
			SQLHelper.ExecuteNoneQuery(sql, params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void ParseForSequenceImpl(Process rootProcess, ControlConstructImpl cci, int parent_id, int service_id) 
	{
		// TODO Auto-generated method stub
		ControlConstructListImpl ccli = ProcessResource.GetCCLIFromProcess(rootProcess);
		
		while(ccli.getRest() != null)
		{
			///first
			PerformImpl pfi = (PerformImpl)ccli.getFirst();
			Process first_process = pfi.getProcess();
			
			String name = first_process.getName();
			String uri = first_process.getURI();
			int type = first_process.isAtomic() ? 1 : (first_process.isComposite() ? 2 : 3);
			int cc_type = 0;
			if(type == 2)
			{
				cc_type = ProcessResource.GetCCType(first_process);
			}

			//process itself
			String sql = ProcessSql.sql_process_insert;
			Object[] params = new Object[]{0, name, uri, type, cc_type, service_id};
			
			ProcessInsertOpe(sql, params);
			
			int newid = SQLHelper.GetLastInsertID();
			//to = newid;

			//input
			InputList inputs = first_process.getInputList();
			ParameterInsertOpe(inputs, newid);
			
			//output
			OutputList outputs = first_process.getOutputList();
			ParameterInsertOpe(outputs, newid);
			
			//preconditions
			PreConditionInsertOpe(first_process, newid);
			
			//results
			ResultInsertOpe(first_process, newid);
		
			//relation
			sql = ProcessSql.sql_relation_insert;
			params = new Object[]{ 0, newid, parent_id, 1 };
			
			ProcessInsertOpe(sql, params);

			if(first_process.isComposite())
			{
				ProcessParseOpe(first_process, newid, service_id); //µü´ú
			}
			
			//deal with rest cc
			ccli = (ControlConstructListImpl) ccli.getRest();
			if(ccli.getRest() == null)
			{
				pfi = (PerformImpl)ccli.getFirst();
				first_process = pfi.getProcess();
				
				name = first_process.getName();
				uri = first_process.getURI();
				type = first_process.isAtomic() ? 1 : (first_process.isComposite() ? 2 : 3);
				cc_type = 0;
				if(type == 2)
				{
					cc_type = ProcessResource.GetCCType(first_process);
				}
				
				//process itself
				sql = ProcessSql.sql_process_insert;
				params = new Object[]{0, name, uri, type, cc_type, service_id};
				
				ProcessInsertOpe(sql, params);
				
				int next_newid = SQLHelper.GetLastInsertID();
				
				//input
				inputs = first_process.getInputList();
				ParameterInsertOpe(inputs, next_newid);
				
				//output
				outputs = first_process.getOutputList();
				ParameterInsertOpe(outputs, next_newid);
				
				//preconditions
				PreConditionInsertOpe(first_process, next_newid);
				
				//results
				ResultInsertOpe(first_process, next_newid);

				//relation
				sql = ProcessSql.sql_relation_insert;
				params = new Object[]{ 0, next_newid, parent_id, 1 };
				
				ProcessInsertOpe(sql, params);

				if(first_process.isComposite())
				{
					ProcessParseOpe(first_process, next_newid, service_id); //µü´ú
				}
			}
		}
	}
	
	private void ResultInsertOpe(Process first_process, int process_id) {
		// TODO Auto-generated method stub
		
		ResultList results = first_process.getResultList();
		if(results == null || results.size() == 0)
			return;
		
		String uri = "";
		String language = "";
		String body = "";
		String sql = "";
		Object[] params = null;

		for(int i = 0; i < results.size(); i++)
		{
			Result result = (ResultImpl)results.getNthResult(i);
			
			//result itself
			sql = ProcessSql.sql_result_insert;
			params = new Object[]{0, process_id};
			
			ProcessInsertOpe(sql, params);
			
			int result_id = SQLHelper.GetLastInsertID();
			
			//effect
			EffectList effects = results.getNthResult(i).getHasEffects();
			int j = 0;
			for(j = 0; j < effects.size(); j++)
			{
				Expression expression = effects.getNthEffect(j);
				uri = expression.getURI();
				language = expression.getExpressionLanguage().getURI();
				body = expression.getExpressionBody();
				sql = ProcessSql.sql_condition_insert;
				params = new Object[]{0, uri == null ? "" : uri, 
						language == null ? "" : language, body == null ? "" : body, 3, 0, result_id};
				
				ProcessInsertOpe(sql, params);
			}
			
			//incondition
			ConditionList inconditions = result.getInCondition();
			for(j = 0; j < inconditions.size(); j++)
			{
				Condition condition = (ConditionImpl)inconditions.getNthCondition(j);
				uri = condition.getURI();
				language = condition.getExpressionLanguage().getURI();
				body = condition.getExpressionBody();
				sql = ProcessSql.sql_condition_insert;
				params = new Object[]{0, uri == null ? "" : uri, 
						language == null ? "" : language, body == null ? "" : body, 4, 0, result_id};
				
				ProcessInsertOpe(sql, params);
			}
		}
	}

	private void EffectInsertOpe(Process first_process, int parent_id) {
		// TODO Auto-generated method stub
		
		ResultList results = first_process.getResultList();
		
		if(results == null || results.size() == 0)
			return;
		
		String uri = "";
		String language = "";
		String body = "";
		String sql = ProcessSql.sql_condition_insert;
		Object[] params = null;

		for(int i = 0; i < results.size(); i++)
		{
			EffectList effects = results.getNthResult(i).getHasEffects();
			for(int j = 0; j < effects.size(); j++)
			{
				Expression expression = effects.getNthEffect(j);
				uri = expression.getURI();
				language = expression.getExpressionLanguage().getURI();
				body = expression.getExpressionBody();

				params = new Object[]{0, uri == null ? "" : uri, 
						language == null ? "" : language, body == null ? "" : body, 3, parent_id};
				
				ProcessInsertOpe(sql, params);
			}
		}
	}

	private void PreConditionInsertOpe(Process first_process, int parent_id) {
		// TODO Auto-generated method stub
		
		PreConditionList preconditions = first_process.getPreConditionList();
		
		if(preconditions == null || preconditions.size() == 0)
			return;
		
		String uri = "";
		String language = "";
		String body = "";
		String sql = ProcessSql.sql_condition_insert;
		Object[] params = null;
		
		Condition condition = null;
		for(int i = 0; i < preconditions.size(); i++)
		{
			condition = preconditions.getNthPreCondition(i);
			
			uri = condition.getURI();
			language = condition.getExpressionLanguage().getURI();
			body = condition.getExpressionBody();
			
			params = new Object[]{0, uri == null ? "" : uri, 
					language == null ? "" : language, body == null ? "" : body, 2, parent_id, 0};

			ProcessInsertOpe(sql, params);
		}
	}
	
	private void ParameterInsertOpe(OutputList list, int parent_id) {
		// TODO Auto-generated method stub
		if(list == null || list.size() == 0)
			return;
		
		String sql = ProcessSql.sql_param_insert;
		Object[] params = null;
		
		String uri = "";
		String param_type = "";
		String param_value = "";
		
		for(int i = 0; i < list.size(); i++)
		{
			OutputImpl output = (OutputImpl)list.getNthOutput(i);
			uri = output.getURI();
			param_type = output.getParameterType();
			param_value = output.getParameterValue();
			
			params = new Object[]{0, uri == null ? "" : uri, 
					param_type == null ? "" : param_type, 
							param_value == null ? "" : param_value, 
									0, 1, 0, 0, parent_id};

			ProcessInsertOpe(sql, params);
		}
	}
	
	private void ParameterInsertOpe(InputList list, int parent_id)
	{
		if(list == null || list.size() == 0)
			return;
		
		String sql = ProcessSql.sql_param_insert;
		Object[] params = null;
		
		String uri = "";
		String param_type = "";
		String param_value = "";
		
		for(int i = 0; i < list.size(); i++)
		{
			InputImpl input = (InputImpl)list.getNthInput(i);
			uri = input.getURI();
			param_type = input.getParameterType();
			param_value = input.getParameterValue();
			
			params = new Object[]{0, uri == null ? "" : uri, 
					param_type == null ? "" : param_type, 
							param_value == null ? "" : param_value, 
									1, 0, 0, 0, parent_id};

			ProcessInsertOpe(sql, params);
		}
	}

	private <T> void ParameterInsertOpe(T param_list, String type, int parent_id)
	{
		ParameterList list = (ParameterList)param_list;
		
		if(list == null || list.size() == 0)
			return;
		
		String sql = ProcessSql.sql_param_insert;
		Object[] params = null;
		
		String uri = "";
		String param_type = "";
		String param_value = "";

		if(type == "input")
		{
			params = new Object[]{0, uri, param_type, param_value, 1, 0, 0, 0, parent_id};
		}
		else if(type == "output")
		{
			params = new Object[]{0, uri, param_type, param_value, 0, 1, 0, 0, parent_id};
		}
		
		for(int i = 0; i < list.size(); i++)
		{
			Parameter param = (ParameterImpl)list.getNth(i);
			uri = param.getURI();
			param_type = param.getParameterType();
			param_value = param.getParameterValue();

			ProcessInsertOpe(sql, params);
		}
	}

	private void ParseForIfThenElseImpl(ControlConstructImpl cci, int parent_id, int service_id) 
	{
		if(!(cci instanceof IfThenElseImpl))
		{
			return;
		}
		
		IfThenElseImpl itei = (IfThenElseImpl)cci;
		
		//condition
		Condition condition = itei.getIfCondition();
		String uri = condition.getURI();
		String language = condition.getExpressionLanguage().getURI();
		String body = condition.getExpressionBody();
		
		String sql = ProcessSql.sql_condition_insert;
		Object[] params = new Object[]{0, uri == null ? "" : uri, 
				language == null ? "" : language, body == null ? "" : body, 1, parent_id, 0};
		
		ProcessInsertOpe(sql, params);
		
		//then
		PerformImpl pfi = null;
		if(itei.getThen() != null)
		{
			pfi = (PerformImpl)itei.getThen();
			ThenAndElseCCInsertOpe(pfi, parent_id , service_id, 2);
		}

		//else
		if(itei.getElse() != null)
		{
			pfi = (PerformImpl)itei.getElse();
			ThenAndElseCCInsertOpe(pfi, parent_id , service_id, 3);
		}
	}

	private void ThenAndElseCCInsertOpe(PerformImpl pfi, int parent_id, int service_id, int relation) 
	{
		Process process = pfi.getProcess();
		
		String name = process.getName();
		String uri = process.getURI();
		int type = process.isAtomic() ? 1 : (process.isComposite() ? 2 : 3);
		int cc_type = 0;
		if(type == 2)
		{
			cc_type = ProcessResource.GetCCType(process);
		}
		
		//process itself
		String sql = ProcessSql.sql_process_insert;
		Object[] params = new Object[]{0, name, uri, type, cc_type, service_id};
		
		ProcessInsertOpe(sql, params);

		int newid = SQLHelper.GetLastInsertID();
		
		//input
		InputList inputs = process.getInputList();
		ParameterInsertOpe(inputs, newid);
		
		//output
		OutputList outputs = process.getOutputList();
		ParameterInsertOpe(outputs, newid);
		
		//preconditions
		PreConditionInsertOpe(process, newid);
		
		//results
		ResultInsertOpe(process, newid);
		
		//relation
		sql = ProcessSql.sql_relation_insert;
		params = new Object[]{0, newid, parent_id, relation};
		
		ProcessInsertOpe(sql, params);
		
		if(process.isComposite())
		{
			ProcessParseOpe(process, newid, service_id);
		}
	}

	private void ProcessInsertOpe(String sql, Object[] params) {
		// TODO Auto-generated method stub
		try 
		{
			SQLHelper.ExecuteNoneQuery(sql, params);
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}
