package EDU.pku.ly.Profile.Implementation;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.pku.ly.SqlOpe.SQLHelper;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionImpl;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.PreConditionListImpl;
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.OWLSProfileModel;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.profile.ServiceParametersList;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ActorImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoriesListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoryImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceParametersListImpl;
import EDU.pku.ly.Profile.ProfileParser;
import EDU.pku.ly.Profile.util.ProfileResource;
import EDU.pku.ly.Profile.util.ProfileSql;

public class ProfileParserImpl implements ProfileParser {
	
	private static final long serialVersionUID = -1L;
	
	public void ProfileParserEntry(String service_url)
	{
		OWLSProfileModel model = ProfileResource.GetOWLSProfileModel(service_url);
		
		Profile profile = model.getProfileList().getNthProfile(0);
		
		for(int i = 0; i < 100; i++)
		{
			ProfileParserEntry(profile, i);
		}
	}
	
	public void ProfileParserEntry(Profile profile, int service_id) {
		// TODO Auto-generated method stub

		String uri = profile.getURI();
		String service_name = profile.getServiceName();
		String description = profile.getTextDescription();
		
		//process
		Process process = profile.getHasProcess();
		int process_id = GetProcessID(process, service_id);
		
		//profile itself
		String sql = ProfileSql.sql_profile_insert;
		Object[] params = new Object[]{ 0, uri, service_name, description, process_id, service_id };
		ProfileInsertOpe(sql, params);
		
		int new_profile_id = SQLHelper.GetLastInsertID();
		
		//actor
		ActorsList actor_lst = profile.getContactInformation();
		ActorInsertOpe(actor_lst, new_profile_id);
		
		//servicecategory
		ServiceCategoriesListImpl sc_lst = (ServiceCategoriesListImpl)profile.getServiceCategory();
		ServiceCategoryInsertOpe(sc_lst, new_profile_id);
		
		//serviceparameter
		ServiceParametersList sp_lst = (ServiceParametersListImpl)profile.getServiceParameter();
		
		//input
		InputList input_lst = (InputListImpl)profile.getInputList();
		InputInsertOpe(input_lst, new_profile_id);
		
		//output
		OutputList output_lst = (OutputListImpl)profile.getOutputList();
		OutputInsertOpe(output_lst, new_profile_id);
		
		//preconditions
		PreConditionList precondition_lst = (PreConditionListImpl)profile.getPreconditionList();
		PreConditionInsertOpe(precondition_lst, new_profile_id);
		
		//results
		profile.getResultList();
		
	}

	private void PreConditionInsertOpe(PreConditionList precondition_lst, int profile_id) {
		// TODO Auto-generated method stub
		
		if(precondition_lst == null || precondition_lst.size() == 0)
		{
			return;
		}
		
		String sql = "";
		Object[] params = null;
		
		String uri = "";
		for(int i = 0; i < precondition_lst.size(); i++)
		{
			Condition condition = (ConditionImpl)precondition_lst.getNthPreCondition(i);
			uri = condition.getURI();
			
			sql = ProfileSql.sql_precondition_inquiry;
			params = new Object[]{ uri, 2 };
			ResultSet rs_precondition = SQLHelper.ExecuteQueryRtnSet(sql, params);
			
			try {
				if(rs_precondition.next())
				{
					int precondition_id = Integer.parseInt(rs_precondition.getString("id"));
					
					sql = ProfileSql.sql_precondition_insert;
					params = new Object[]{ 0, precondition_id, profile_id};
					ProfileInsertOpe(sql, params);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void OutputInsertOpe(OutputList output_lst, int profile_id) {
		// TODO Auto-generated method stub
		
		if(output_lst == null || output_lst.size() == 0)
		{
			return;
		}
		
		String sql = "";
		Object[] params = null;
		
		String uri = "";
		for(int i = 0; i < output_lst.size(); i++)
		{
			Output input = (OutputImpl)output_lst.getNthOutput(i);
			uri = input.getURI();
			
			sql = ProfileSql.sql_output_inquiry;
			params = new Object[]{ uri, 1 };
			ResultSet rs_output = SQLHelper.ExecuteQueryRtnSet(sql, params);
			
			try {
				if(rs_output.next())
				{
					int output_id = Integer.parseInt(rs_output.getString("id"));
					
					sql = ProfileSql.sql_output_insert;
					params = new Object[]{ 0, output_id, profile_id};
					ProfileInsertOpe(sql, params);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void InputInsertOpe(InputList input_lst, int profile_id) {
		// TODO Auto-generated method stub
		
		if(input_lst == null || input_lst.size() == 0)
		{
			return;
		}
		
		String sql = "";
		Object[] params = null;
		
		String uri = "";
		for(int i = 0; i < input_lst.size(); i++)
		{
			Input input = (InputImpl)input_lst.getNthInput(i);
			uri = input.getURI();
			
			sql = ProfileSql.sql_input_inquiry;
			params = new Object[]{ uri, 1 };
			ResultSet rs_input = SQLHelper.ExecuteQueryRtnSet(sql, params);
			
			try {
				if(rs_input.next())
				{
					int input_id = Integer.parseInt(rs_input.getString("id"));
					
					sql = ProfileSql.sql_input_insert;
					params = new Object[]{ 0, input_id, profile_id};
					ProfileInsertOpe(sql, params);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void ServiceCategoryInsertOpe(ServiceCategoriesListImpl sc_lst, int profile_id) {
		// TODO Auto-generated method stub
		
		if(sc_lst == null || sc_lst.size() == 0)
		{
			return;
		}
		
		String sql = ProfileSql.sql_sc_insert;
		Object[] params = null;
		
		String uri = "";
		String category_name = "";
	    String taxonomy = "";
	    String value = "";
	    String code = "";
		
	    for(int i = 0; i < sc_lst.size(); i++)
	    {
	    	ServiceCategory sc = (ServiceCategoryImpl)sc_lst.getServiceCategoryAt(i);
	    	uri = sc.getURI();
	    	category_name = sc.getCategoryName();
	    	taxonomy = sc.getTaxonomy();
	    	value = sc.getValue();
	    	code = sc.getCode();
	    	
	    	params = new Object[]{ 0, uri, category_name, taxonomy, value, code, profile_id };
			
			ProfileInsertOpe(sql, params);
	    }
	}

	private int GetProcessID(Process process, int service_id) {
		// TODO Auto-generated method stub
		
		int process_id = 0;
		
		String uri = process.getURI();
		String sql = ProfileSql.sql_process_inquiry;
		Object[] params = new Object[]{ uri, service_id };
		
		ResultSet rs_process = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		try {
			if(rs_process.next())
			{
				process_id = Integer.parseInt(rs_process.getString("id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return process_id;
	}

	private void ProfileInsertOpe(String sql, Object[] params) {
		// TODO Auto-generated method stub
		
		try {
			SQLHelper.ExecuteNoneQuery(sql, params);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void ActorInsertOpe(ActorsList actor_lst, int profile_id) 
	{
		// TODO Auto-generated method stub
		
		if(actor_lst == null || actor_lst.size() == 0)
		{
			return;
		}
		
		String sql = ProfileSql.sql_contact_insert;
		Object[] params = null;
		
		String uri = "";
		String name = "";
		String title = "";
		String phone = "";
		String fax = "";
		String email = "";
		String address = "";
		String weburl = "";
		for(int i = 0; i < actor_lst.size(); i++)
		{
			Actor actor = (ActorImpl)actor_lst.getNthActor(i);
			
			uri = actor.getURI();
			name = actor.getName();
			title = actor.getTitle();
			phone = actor.getPhone();
			fax = actor.getFax();
			email = actor.getEmail();
			address = actor.getAddress();
			weburl = actor.getWebURL();
			
			params = new Object[]{ 0, uri, name, title, phone, fax, email, address, weburl, profile_id };
		
			ProfileInsertOpe(sql, params);
		}
	}

}
