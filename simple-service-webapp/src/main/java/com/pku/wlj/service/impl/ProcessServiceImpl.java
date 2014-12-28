package com.pku.wlj.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.ConditionList;
import EDU.cmu.Atlas.owls1_1.expression.Expression;
import EDU.cmu.Atlas.owls1_1.expression.LogicLanguage;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionListImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ExpressionImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.LogicLanguageImpl;
import EDU.cmu.Atlas.owls1_1.process.ControlConstruct;
import EDU.cmu.Atlas.owls1_1.process.ControlConstructList;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.Perform;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.process.implementation.AnyOrderImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.AtomicProcessImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ChoiceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.CompositeProcessImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ControlConstructImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ControlConstructListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.EffectListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.IfThenElseImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.PerformImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.PreConditionListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.RepeatUntilImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.RepeatWhileImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SequenceImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.SplitJoinImpl;
import EDU.pku.ly.Process.util.ProcessResource;
import EDU.pku.ly.Process.util.ProcessSql;

import com.pku.wlj.bean.process.ProcessProcessBean;
import com.pku.wlj.dao.ProcessDao;
import com.pku.wlj.service.ProcessService;

import edu.pku.ly.SqlOpe.SQLHelper;

//@Service("ProcessService")
public class ProcessServiceImpl implements ProcessService {
	
	@Autowired
	private ProcessDao processDao;

	public void setProcessDao(ProcessDao processDao) {
		this.processDao = processDao;
	}

	@Override
	public Process ProcessInquiryEntry(int process_id, String flag) {
		ProcessProcessBean bean = processDao.getProcessBean(process_id);

		return ProcessInquiryEntry(bean);
	}

	@Override
	public Process ProcessInquiryEntry(int service_id) {
		// TODO Auto-generated method stub
		return null;
	}

	private Process ProcessInquiryEntry(ProcessProcessBean bean) {
		Process process = null;
		switch (bean.getType()) {
		case 1:
			process = new AtomicProcessImpl(bean.getUri());

			process.setIsAtomic(true);

			// iope
			process.setName(bean.getName());
			process.setInputList(GetInputListByProcessID(bean.getId()));
			process.setOutputList(GetOutputListByprocessID(bean.getId()));
			process.setPreConditionList(GetPreConditionListByProcessID(bean
					.getId()));
			process.setResultList(GetResultListByProcessID(bean.getId()));
			break;
		case 2:
			process = new CompositeProcessImpl(bean.getUri());

			process.setIsComposite(true);

			// iope
			process.setName(bean.getName());
			process.setInputList(GetInputListByProcessID(bean.getId()));
			process.setOutputList(GetOutputListByprocessID(bean.getId()));
			process.setPreConditionList(GetPreConditionListByProcessID(bean
					.getId()));
			process.setResultList(GetResultListByProcessID(bean.getId()));

			ControlConstruct cci = null;
			switch (bean.getCcType()) {
			case 1:
				cci = new SequenceImpl();
				break;
			case 2:
				cci = new IfThenElseImpl();
				break;
			case 3:
				cci = new SplitImpl();
				break;
			case 4:
				cci = new SplitJoinImpl();
				break;
			case 5:
				cci = new ChoiceImpl();
				break;
			case 6:
				cci = new AnyOrderImpl();
				break;
			case 7:
				cci = new RepeatUntilImpl();
				break;
			case 8:
				cci = new RepeatWhileImpl();
				break;
			}

			((CompositeProcessImpl) process).setComposedOf(cci);

			ProcessRestoreOpe(process, bean.getId()); // iterate
			break;
		}
		;
		return process;
	}

	private void ProcessRestoreOpe(Process root_process, int process_id) {
		ControlConstructImpl cci = ProcessResource
				.GetCCIFromProcess(root_process);

		if (cci instanceof SequenceImpl) {
			try {
				RestoreForSequenceImpl(cci, process_id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (cci instanceof IfThenElseImpl) {
			try {
				RestoreForIfThenElseImpl(cci, process_id);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (cci instanceof SplitImpl) {
			RestoreForSplitImpl(cci, process_id);
		} else if (cci instanceof SplitJoinImpl) {
			RestoreForSplitJoinImpl(cci, process_id);
		} else if (cci instanceof ChoiceImpl) {
			RestoreForChoiceImpl(cci, process_id);
		} else if (cci instanceof AnyOrderImpl) {
			RestoreForAnyOrderImpl(cci, process_id);
		} else if (cci instanceof RepeatUntilImpl) {
			RestoreForRepeatUntilImpl(cci, process_id);
		} else if (cci instanceof RepeatWhileImpl) {
			try {
				RestoreForRepeatWhileImpl(cci, process_id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void RestoreForSequenceImpl(ControlConstructImpl cci, int parent_id)
			throws SQLException {
		// TODO Auto-generated method stub

		String sql = "select * from process_relation where parent_id=? order by id;";
		Object[] params = new Object[] { parent_id };

		ResultSet relation_set = SQLHelper.ExecuteQueryRtnSet(sql, params);

		ControlConstructList ccli = new ControlConstructListImpl();
		cci.setComponents(ccli);
		while (relation_set.next()) {
			// ccli = new ControlConstructListImpl();

			int process_id = Integer.parseInt(relation_set
					.getString("process_id"));
			sql = "select * from process_process where id=?;";
			params = new Object[] { process_id };

			ResultSet process_set = SQLHelper.ExecuteQueryRtnSet(sql, params);

			if (process_set.getRow() > 1) {
				System.out
						.println("error: there are more than one line of process item");
				return;
			} else if (process_set.wasNull())
				continue;

			if (process_set.next()) {
				int type = Integer.parseInt(process_set.getString("type"));
				int cc_type = Integer
						.parseInt(process_set.getString("cc_type"));
				String name = process_set.getString("name");
				String uri = process_set.getString("uri");
				// int process_id =
				// Integer.parseInt(process_set.getString("id"));

				Perform perform = new PerformImpl();

				Process process = null;
				switch (type) {
				case 1:
					process = new AtomicProcessImpl(uri);

					process.setIsAtomic(true);

					// iope
					process.setName(name);
					process.setInputList(GetInputListByProcessID(process_id));
					process.setOutputList(GetOutputListByprocessID(process_id));
					process.setPreConditionList(GetPreConditionListByProcessID(process_id));
					process.setResultList(GetResultListByProcessID(process_id));

					//
					((AtomicProcessImpl) process).isAtomic();

					// perform
					perform.setProcess(process);

					ccli.setFirst(perform);
					if (!relation_set.isLast())
						ccli.setRest(new ControlConstructListImpl());

					ccli = (ControlConstructListImpl) ccli.getRest();
					break;
				case 2:
					process = new CompositeProcessImpl(uri);

					process.setIsComposite(true);

					// iope
					process.setName(name);
					process.setInputList(GetInputListByProcessID(process_id));
					process.setOutputList(GetOutputListByprocessID(process_id));
					process.setPreConditionList(GetPreConditionListByProcessID(process_id));
					process.setResultList(GetResultListByProcessID(process_id));

					// cc
					ControlConstruct cc = null;
					switch (cc_type) {
					case 1:
						cc = new SequenceImpl();
						break;
					case 2:
						cc = new IfThenElseImpl();
						break;
					case 3:
						cc = new SplitImpl();
						break;
					case 4:
						cc = new SplitJoinImpl();
						break;
					case 5:
						cc = new ChoiceImpl();
						break;
					case 6:
						cc = new AnyOrderImpl();
						break;
					case 7:
						cc = new RepeatUntilImpl();
						break;
					case 8:
						cc = new RepeatWhileImpl();
						break;
					}
					((CompositeProcessImpl) process).setComposedOf(cc);

					// perform
					perform.setProcess(process);

					ccli.setFirst(perform);
					if (!relation_set.isLast())
						ccli.setRest(new ControlConstructListImpl());

					ccli = (ControlConstructListImpl) ccli.getRest();

					ProcessRestoreOpe(process, process_id);
					break;
				}
			}
		}
	}

	private void RestoreForIfThenElseImpl(ControlConstructImpl cci,
			int parent_id) throws NumberFormatException, SQLException {
		// TODO Auto-generated method stub

		// condition
		RestroForIfCondition(cci, parent_id);

		// then and else
		String sql = "select * from process_relation where parent_id=? order by id;";
		Object[] params = new Object[] { parent_id };
		ResultSet relation_set = SQLHelper.ExecuteQueryRtnSet(sql, params);
		ControlConstruct thenControlConstruct = null;
		ControlConstruct elseControlConstruct = null;
		while (relation_set.next()) {
			int process_id = Integer.parseInt(relation_set
					.getString("process_id"));
			int relation_type = Integer
					.parseInt(relation_set.getString("type"));

			sql = "select * from process_process where id=?;";
			params = new Object[] { process_id };

			ResultSet process_set = SQLHelper.ExecuteQueryRtnSet(sql, params);

			if (process_set.getRow() > 1) {
				System.out
						.println("error: there are more than one line of process item");
				return;
			} else if (process_set.wasNull())
				continue;

			if (process_set.next()) {
				int type = Integer.parseInt(process_set.getString("type"));
				int cc_type = Integer
						.parseInt(process_set.getString("cc_type"));
				String name = process_set.getString("name");
				String uri = process_set.getString("uri");

				// then and else
				Process process = null;
				switch (type) {
				case 1:
					process = new AtomicProcessImpl(uri);

					process.setIsAtomic(true);

					// iope
					process.setName(name);
					process.setInputList(GetInputListByProcessID(process_id));
					process.setOutputList(GetOutputListByprocessID(process_id));
					process.setPreConditionList(GetPreConditionListByProcessID(process_id));
					process.setResultList(GetResultListByProcessID(process_id));

					// then and else
					if (relation_type == 1)
						continue;
					else if (relation_type == 2) {
						thenControlConstruct = new PerformImpl();
						((PerformImpl) thenControlConstruct)
								.setProcess(process);
						((IfThenElseImpl) cci).setThen(thenControlConstruct);
					} else {
						elseControlConstruct = new PerformImpl();
						((PerformImpl) elseControlConstruct)
								.setProcess(process);
						((IfThenElseImpl) cci).setElse(thenControlConstruct);
					}
					break;
				case 2:
					process = new CompositeProcessImpl(uri);

					process.setIsComposite(true);

					// iope
					process.setName(name);
					process.setInputList(GetInputListByProcessID(process_id));
					process.setOutputList(GetOutputListByprocessID(process_id));
					process.setPreConditionList(GetPreConditionListByProcessID(process_id));
					process.setResultList(GetResultListByProcessID(process_id));

					ControlConstruct cc = null;
					switch (cc_type) {
					case 1:
						cc = new SequenceImpl();
						break;
					case 2:
						cc = new IfThenElseImpl();
						break;
					case 3:
						cc = new SplitImpl();
						break;
					case 4:
						cc = new SplitJoinImpl();
						break;
					case 5:
						cc = new ChoiceImpl();
						break;
					case 6:
						cc = new AnyOrderImpl();
						break;
					case 7:
						cc = new RepeatUntilImpl();
						break;
					case 8:
						cc = new RepeatWhileImpl();
						break;
					}

					((CompositeProcessImpl) process).setComposedOf(cc);

					// then and else
					if (relation_type == 1)
						continue;
					else if (relation_type == 2) {
						thenControlConstruct = new PerformImpl();
						((PerformImpl) thenControlConstruct)
								.setProcess(process);
						((IfThenElseImpl) cci).setThen(thenControlConstruct);
					} else {
						elseControlConstruct = new PerformImpl();
						((PerformImpl) elseControlConstruct)
								.setProcess(process);
						((IfThenElseImpl) cci).setElse(thenControlConstruct);
					}

					ProcessRestoreOpe(process, process_id);
					break;
				}
			}
		}
	}

	private void RestroForIfCondition(ControlConstructImpl cci, int parent_id) {
		// TODO Auto-generated method stub

		String sql = "select * from process_condition where type=? and process_id=?;";
		Object[] params = new Object[] { 1, parent_id };
		ResultSet condition_set = SQLHelper.ExecuteQueryRtnSet(sql, params);
		try {
			if (condition_set.getRow() > 1)
				return;
			else if (condition_set.wasNull())
				return;

			Condition ifcondition = null;
			if (condition_set.next()) {
				ifcondition = new ConditionImpl(condition_set.getString("uri"));
				LogicLanguage lang = new LogicLanguageImpl(
						condition_set.getString("expression_language"));
				ifcondition.setExpressionLanguage(lang);
				ifcondition.setExpressionBody(condition_set
						.getString("expression_body"));
			}
			((IfThenElseImpl) cci).setIfCondition(ifcondition);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ResultList GetResultListByProcessID(int process_id) {
		// TODO Auto-generated method stub

		ResultList resultList = new ResultListImpl();

		String sql = "select * from process_result where process_id=?;";
		Object[] params = new Object[] { process_id };
		ResultSet result_set = SQLHelper.ExecuteQueryRtnSet(sql, params);

		try {
			Result result = null;
			while (result_set.next()) {
				result = new ResultImpl();
				int result_id = Integer.parseInt(result_set.getString("id"));

				sql = "select * from process_condition where result_id=?";
				params = new Object[] { result_id };

				ResultSet condition_set = SQLHelper.ExecuteQueryRtnSet(sql,
						params);

				EffectList effectList = new EffectListImpl();
				ConditionList conditionList = new ConditionListImpl();
				Condition incondition = null;
				Expression effect = null;
				while (condition_set.next()) {
					String url = condition_set.getString("uri");
					int type = Integer
							.parseInt(condition_set.getString("type"));
					if (type == 4) {
						incondition = new ConditionImpl(url);
						incondition.setExpressionBody(condition_set
								.getString("expression_body"));

						LogicLanguage lang = new LogicLanguageImpl(
								condition_set.getString("expression_language"));
						incondition.setExpressionLanguage(lang);

						conditionList.addCondition(incondition);
					} else if (type == 3) {
						effect = new ExpressionImpl(url);
						effect.setExpressionBody(condition_set
								.getString("expression_body"));

						LogicLanguage lang = new LogicLanguageImpl(
								condition_set.getString("expression_language"));
						effect.setExpressionLanguage(lang);

						effectList.addExpression(effect);
					} else {
					}
				}
				result.setInCondition(conditionList);
				result.setHasEffects(effectList);
				resultList.addResult(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	private PreConditionList GetPreConditionListByProcessID(int process_id) {

		PreConditionList preConditionList = new PreConditionListImpl();

		String sql = "select * from process_condition where process_id=? and type=2;";
		Object[] params = new Object[] { process_id };
		ResultSet precondition_set = SQLHelper.ExecuteQueryRtnSet(sql, params);

		Condition precondition = null;
		try {
			while (precondition_set.next()) {
				precondition = new ConditionImpl(
						precondition_set.getString("uri"));

				LogicLanguage lang = new LogicLanguageImpl(
						precondition_set.getString("expression_language"));
				precondition.setExpressionLanguage(lang);
				precondition.setExpressionBody(precondition_set
						.getString("expression_body"));

				preConditionList.addPreCondition(precondition);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return preConditionList;
	}

	private OutputList GetOutputListByprocessID(int process_id) {

		OutputList outputList = new OutputListImpl();

		String sql = "select * from process_param where process_id=? and is_output=1;";
		Object[] params = new Object[] { process_id };
		ResultSet output_set = SQLHelper.ExecuteQueryRtnSet(sql, params);

		Output output = null;
		try {
			while (output_set.next()) {
				output = new OutputImpl(output_set.getString("uri"));
				output.setParameterType(output_set.getString("param_type"));
				output.setParameterValue(output_set.getString("param_value"));

				outputList.addOutput(output);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return outputList;
	}

	private InputList GetInputListByProcessID(int process_id) {

		InputList inputList = new InputListImpl();

		String sql = "select * from process_param where process_id=? and is_input=1;";
		Object[] params = new Object[] { process_id };
		ResultSet input_set = SQLHelper.ExecuteQueryRtnSet(sql, params);

		Input input = null;
		try {
			while (input_set.next()) {
				input = new InputImpl(input_set.getString("uri"));
				input.setParameterType(input_set.getString("param_type"));
				input.setParameterValue(input_set.getString("param_value"));

				inputList.addInput(input);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return inputList;
	}

	private void RestoreForSplitImpl(ControlConstructImpl cci, int process_id) {
		// TODO Auto-generated method stub

	}

	private void RestoreForSplitJoinImpl(ControlConstructImpl cci,
			int process_id) {
		// TODO Auto-generated method stub

	}

	private void RestoreForChoiceImpl(ControlConstructImpl cci, int process_id) {
		// TODO Auto-generated method stub

	}

	private void RestoreForAnyOrderImpl(ControlConstructImpl cci, int process_id) {
		// TODO Auto-generated method stub

	}

	private void RestoreForRepeatUntilImpl(ControlConstructImpl cci,
			int process_id) {
		// TODO Auto-generated method stub

	}

	private void RestoreForRepeatWhileImpl(ControlConstructImpl cci,
			int parent_id) throws SQLException {

		RestoreForWhileCondition(cci, parent_id);

		// then and else
		String sql = "select * from process_relation where parent_id=? order by id;";
		Object[] params = new Object[] { parent_id };
		ResultSet relation_set = SQLHelper.ExecuteQueryRtnSet(sql, params);
		ControlConstruct whileControlConstruct = null;
		while (relation_set.next()) {
			int process_id = Integer.parseInt(relation_set
					.getString("process_id"));
			int relation_type = Integer
					.parseInt(relation_set.getString("type"));

			sql = "select * from process_process where id=?;";
			params = new Object[] { process_id };

			ResultSet process_set = SQLHelper.ExecuteQueryRtnSet(sql, params);

			if (process_set.next()) {
				int type = Integer.parseInt(process_set.getString("type"));
				int cc_type = Integer
						.parseInt(process_set.getString("cc_type"));
				String name = process_set.getString("name");
				String uri = process_set.getString("uri");

				// then and else
				Process process = null;
				switch (type) {
				case 1:
					process = new AtomicProcessImpl(uri);

					process.setIsAtomic(true);

					// iope
					process.setName(name);
					process.setInputList(GetInputListByProcessID(process_id));
					process.setOutputList(GetOutputListByprocessID(process_id));
					process.setPreConditionList(GetPreConditionListByProcessID(process_id));
					process.setResultList(GetResultListByProcessID(process_id));

					// while controlconstruct
					whileControlConstruct = new PerformImpl();
					((PerformImpl) whileControlConstruct).setProcess(process);
					((RepeatWhileImpl) cci)
							.setWhileProcess(whileControlConstruct);
					break;
				case 2:
					process = new CompositeProcessImpl(uri);

					process.setIsComposite(true);

					// iope
					process.setName(name);
					process.setInputList(GetInputListByProcessID(process_id));
					process.setOutputList(GetOutputListByprocessID(process_id));
					process.setPreConditionList(GetPreConditionListByProcessID(process_id));
					process.setResultList(GetResultListByProcessID(process_id));

					ControlConstruct cc = null;
					switch (cc_type) {
					case 1:
						cc = new SequenceImpl();
						break;
					case 2:
						cc = new IfThenElseImpl();
						break;
					case 3:
						cc = new SplitImpl();
						break;
					case 4:
						cc = new SplitJoinImpl();
						break;
					case 5:
						cc = new ChoiceImpl();
						break;
					case 6:
						cc = new AnyOrderImpl();
						break;
					case 7:
						cc = new RepeatUntilImpl();
						break;
					case 8:
						cc = new RepeatWhileImpl();
						break;
					}

					((CompositeProcessImpl) process).setComposedOf(cc);

					// while controlconstruct
					whileControlConstruct = new PerformImpl();
					((PerformImpl) whileControlConstruct).setProcess(process);
					((RepeatWhileImpl) cci)
							.setWhileProcess(whileControlConstruct);

					ProcessRestoreOpe(process, process_id);
					break;
				}
			}
		}
	}

	private void RestoreForWhileCondition(ControlConstructImpl cci,
			int process_id) {

		String sql = "select * from process_condition where type=? and process_id=?;";
		Object[] params = new Object[] { 5, process_id };
		ResultSet condition_set = SQLHelper.ExecuteQueryRtnSet(sql, params);
		try {
			if (condition_set.getRow() > 1)
				return;
			else if (condition_set.wasNull())
				return;

			Condition whileCondition = null;
			if (condition_set.next()) {
				whileCondition = new ConditionImpl(
						condition_set.getString("uri"));
				LogicLanguage lang = new LogicLanguageImpl(
						condition_set.getString("expression_language"));
				whileCondition.setExpressionLanguage(lang);
				whileCondition.setExpressionBody(condition_set
						.getString("expression_body"));
			}
			((RepeatWhileImpl) cci).setWhileCondition(whileCondition);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Parameter ParameterInquiry(int param_id) {
		// TODO Auto-generated method stub

		String sql = ProcessSql.sql_query_parameter_by_id;
		Object[] params = new Object[] { param_id };

		ResultSet rs_param = SQLHelper.ExecuteQueryRtnSet(sql, params);

		Parameter param = null;
		try {
			if (rs_param.next()) {
				String uri = rs_param.getString("uri");
				int is_input = Integer.parseInt(rs_param.getString("is_input"));
				int is_output = Integer.parseInt(rs_param
						.getString("is_output"));

				String param_type = rs_param.getString("param_type");
				String param_value = rs_param.getString("param_value");

				if (is_input == 1) {
					param = new InputImpl(uri);
				} else if (is_output == 1) {
					param = new OutputImpl(uri);
				}

				param.setParameterType(param_type);
				param.setParameterValue(param_value);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return param;
	}
}
