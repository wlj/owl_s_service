package EDU.pku.ly.Profile.Implementation;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.interceptors.ResultSetScannerInterceptor;

import edu.pku.ly.SqlOpe.SQLHelper;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.ConditionList;
import EDU.cmu.Atlas.owls1_1.expression.Expression;
import EDU.cmu.Atlas.owls1_1.expression.LogicLanguage;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionListImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ExpressionImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.LogicLanguageImpl;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.process.implementation.EffectListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.PreConditionListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultListImpl;
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorsListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoriesListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoryImpl;
import EDU.pku.ly.Process.ProcessInquiry;
import EDU.pku.ly.Process.Implementation.ProcessInquiryImpl;
import EDU.pku.ly.Profile.ProfileInquiry;
import EDU.pku.ly.Profile.util.ProfileSql;

public class ProfileInquiryImpl implements ProfileInquiry {

	private static final long serialVersionUID = -1L;
	
	public ProfileList ProfileInquiryEntry(int service_id) {
		// TODO Auto-generated method stub
		
		ProfileList profile_lst = new ProfileListImpl();
		Profile profile = new ProfileImpl();
		
		ProcessInquiry process_inquiry = new ProcessInquiryImpl();
		
		String sql = ProfileSql.sql_profile_inquiry;
		Object[] params = new Object[]{ service_id };
		
		ResultSet rs_profile = SQLHelper.ExecuteQueryRtnSet(sql, params);
		try {
			String uri = "";
			int process_id = 0;
			int profile_id = 0;
			while(rs_profile.next())
			{
				profile_id = Integer.parseInt(rs_profile.getString("id")); 
				uri = rs_profile.getString("uri");
				profile = new ProfileImpl(uri);
				
				//process
				process_id = Integer.parseInt(rs_profile.getString("process_id")); 
				profile.setHasProcess(process_inquiry.ProcessInquiryEntry(process_id, ""));
				
				//service_name
				profile.setServiceName(rs_profile.getString("service_name"));
				
				//test_description
				profile.setTextDescription(rs_profile.getString("description"));
				
				//result
				//profile.setResultList(new ResultListImpl());
				profile.setResultList(GetResultList(service_id));
				
				//contact
				profile.setContactInformation(GetActorsList(profile_id));
				
				//service category
				profile.setServiceCategory(GetServiceCategory(profile_id));
				
				//input
				profile.setInputList(GetInputList(profile_id));
				
				//output
				profile.setOutputList(GetOutputList(profile_id));
				
				//precondition
				profile.setPreconditionList(GetPreConditions(profile_id));
				
				//result
				profile.setResultList(GetResults(profile_id, process_id));	
				
				profile_lst.add(profile);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return profile_lst;
	}

	private ResultList GetResultList(int service_id) {
		// TODO Auto-generated method stub
		
		String sql = ProfileSql.sql_inquiry_result_id;
		Object[] params = new Object[]{ service_id };
		ResultSet rs_result = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		ResultList result_list = new ResultListImpl();
		try {
			Result result = null;
			Condition incondition = null;
			Expression effect = null;
			ConditionList condition_list = null;
			EffectList effect_list = null;
			while(rs_result.next())
			{
				result = new ResultImpl();
				
				int result_id = Integer.parseInt(rs_result.getString("id"));
				
				sql = ProfileSql.sql_inquiry_condition;
				params = new Object[]{ result_id };
				ResultSet rs_condition = SQLHelper.ExecuteQueryRtnSet(sql, params);
				
				condition_list = new ConditionListImpl();
				effect_list = new EffectListImpl();
				while(rs_condition.next())
				{
					int type = Integer.parseInt(rs_condition.getString("type"));
					String expression_language = rs_condition.getString("expression_language");
					String expression_body = rs_condition.getString("expression_language");
					if(type == 3)
					{
						incondition = new ConditionImpl(rs_condition.getString("uri"));
						incondition.setExpressionLanguage(new LogicLanguageImpl(expression_language));
						incondition.setExpressionBody(expression_body);
						
						condition_list.addCondition(incondition);
					}
					if(type == 4)
					{
						effect = new ExpressionImpl(rs_condition.getString("uri"));
						effect.setExpressionLanguage(new LogicLanguageImpl(expression_language));
						effect.setExpressionBody(expression_body);
						
						effect_list.addExpression(effect);
					}
				}
				
				result.setInCondition(condition_list);
				result.setHasEffects(effect_list);
				
				result_list.addResult(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result_list;
	}

	private ResultList GetResults(int profile_id, int process_id) {
		// TODO Auto-generated method stub
		
		ResultList result_lst = new ResultListImpl();
		Result result = null;
		
		String sql = ProfileSql.sql_result_inquiry;
		Object[] params = new Object[]{ process_id };
		ResultSet rs_result_ids = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		try {
			ConditionList condition_lst = null;
			EffectList effect_lst = null;
			int result_id = 0;
			String uri = "";
			while(rs_result_ids.next())
			{
				result = new ResultImpl();
				condition_lst = new ConditionListImpl();
				effect_lst = new EffectListImpl();
				Condition condition = null;
				Expression effect = null;
				
				result_id = Integer.parseInt(rs_result_ids.getString("id"));
				
				sql = ProfileSql.sql_result_inquiry_by_id;
				params = new Object[]{ result_id };
				ResultSet rs_result = SQLHelper.ExecuteQueryRtnSet(sql, params);
				
				LogicLanguage ll = null;
				while(rs_result.next())
				{
					uri = rs_result.getString("uri");
					int type = Integer.parseInt(rs_result.getString("type"));
					
					ll = new LogicLanguageImpl(rs_result.getString("expression_language"));
					
					if(type == 3)
					{
						effect = new ExpressionImpl(uri);
						
						effect.setExpressionLanguage(ll);
						effect.setExpressionBody(rs_result.getString("expression_body"));
						
						effect_lst.addExpression(effect);
					}
					else
					{
						condition = new ConditionImpl(uri);
						
						condition.setExpressionLanguage(ll);
						condition.setExpressionBody(rs_result.getString("expression_body"));
						
						condition_lst.addCondition(condition);
					}
				}
				result.setInCondition(condition_lst);
				result.setHasEffects(effect_lst);
			}
			result_lst.addResult(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result_lst;
	}

	private PreConditionList GetPreConditions(int profile_id) {
		// TODO Auto-generated method stub
		
		PreConditionList precondition_lst = new PreConditionListImpl();
		Condition precondition = null;
		
		String sql = ProfileSql.sql_precondition_id_inquiry;
		Object[] params = new Object[]{ profile_id };
		ResultSet rs_pc_ids = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		try {
			int pc_id = 0;
			while(rs_pc_ids.next())
			{
				pc_id = Integer.parseInt(rs_pc_ids.getString("condition_id"));
				
				sql = ProfileSql.sql_precondition_inquiry_by_id;
				params = new Object[]{ pc_id };
				ResultSet rs_pc = SQLHelper.ExecuteQueryRtnSet(sql, params);
				if(rs_pc.next())
				{
					precondition = new ConditionImpl(rs_pc.getString("uri"));
					
					LogicLanguage ll = new LogicLanguageImpl(rs_pc.getString("expression_language"));
					precondition.setExpressionLanguage(ll);
					precondition.setExpressionBody(rs_pc.getString("expression_body"));
					
					precondition_lst.addPreCondition(precondition);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return precondition_lst;
	}

	private OutputList GetOutputList(int profile_id) {
		// TODO Auto-generated method stub
		
		OutputList output_lst = new OutputListImpl();
		Output output = null;
		
		String sql = ProfileSql.sql_output_id_inquiry;
		Object[] params = new Object[]{ profile_id };
		ResultSet rs_output_ids = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		try {
			int output_id = 0;
			while(rs_output_ids.next())
			{
				output_id = Integer.parseInt(rs_output_ids.getString("output_id"));
				
				sql = ProfileSql.sql_output_inquiry_by_id;
				params = new Object[]{ output_id };
				ResultSet rs_output = SQLHelper.ExecuteQueryRtnSet(sql, params);
				if(rs_output.next())
				{
					output = new OutputImpl(rs_output.getString("uri"));
					
					output.setParameterType(rs_output.getString("param_type"));
					output.setParameterValue(rs_output.getString("param_value"));
					
					output_lst.addOutput(output);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output_lst;
	}

	private InputList GetInputList(int profile_id) {
		// TODO Auto-generated method stub
		
		InputList input_lst = new InputListImpl();
		Input input = null;
		
		String sql = ProfileSql.sql_input_id_inquiry;
		Object[] params = new Object[]{ profile_id };
		ResultSet rs_input_ids = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		try {
			int input_id = 0;
			while(rs_input_ids.next())
			{
				input_id = Integer.parseInt(rs_input_ids.getString("input_id"));
				
				sql = ProfileSql.sql_input_inquiry_by_id;
				params = new Object[]{ input_id };
				ResultSet rs_input = SQLHelper.ExecuteQueryRtnSet(sql, params);
				if(rs_input.next())
				{
					input = new InputImpl(rs_input.getString("uri"));
					
					input.setParameterType(rs_input.getString("param_type"));
					input.setParameterValue(rs_input.getString("param_value"));
					
					input_lst.addInput(input);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return input_lst;
	}

	private ServiceCategoriesList GetServiceCategory(int profile_id) {
		// TODO Auto-generated method stub
		
		ServiceCategoriesList sc_lst = new ServiceCategoriesListImpl();
		ServiceCategory sc = null;
		
		String sql = ProfileSql.sql_sc_inquiry;
		Object[] params = new Object[]{ profile_id };
		ResultSet rs_sc = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		try {
			while(rs_sc.next())
			{
				sc = new ServiceCategoryImpl(rs_sc.getString("uri"));
				
				sc.setCategoryName(rs_sc.getString("category_name"));
				sc.setTaxonomy(rs_sc.getString("taxonomy"));
				sc.setValue(rs_sc.getString("value"));
				sc.setCode(rs_sc.getString("code"));
				
				sc_lst.addServiceCategory(sc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sc_lst;
	}

	private ActorsList GetActorsList(int profile_id) {
		// TODO Auto-generated method stub
		
		ActorsList actor_lst = new ActorsListImpl();
		Actor actor = null;
		
		String sql = ProfileSql.sql_contact_inquiry;
		Object[] params = new Object[]{ profile_id };
		ResultSet rs_contact = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		try {
			while(rs_contact.next())
			{
				actor = new ActorImpl(rs_contact.getString("uri"));
				
				actor.setName(rs_contact.getString("name"));
				actor.setTitle(rs_contact.getString("title"));
				actor.setPhone(rs_contact.getString("phone"));
				actor.setFax(rs_contact.getString("fax"));
				actor.setEmail(rs_contact.getString("email"));
				actor.setAddress(rs_contact.getString("address"));
				actor.setWebURL(rs_contact.getString("weburl"));
				
				actor_lst.addActor(actor);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return actor_lst;
	}

}
