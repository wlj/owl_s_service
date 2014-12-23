package EDU.pku.ly.Profile.Implementation;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import EDU.pku.ly.Profile.ProfileDelete;
import EDU.pku.ly.Profile.util.ProfileSql;

import edu.pku.ly.SqlOpe.SQLHelper;

public class ProfileDeleteImpl implements ProfileDelete {

	private static final long serialVersionUID = -1L;
	
	public void ProfileDeleteEntry(int service_id) {
		// TODO Auto-generated method stub
		
		List<Integer> profile_id_tobedeleted = new ArrayList<Integer>();
		
		String sql = ProfileSql.sql_profile_inquiry;
		Object[] params = new Object[]{ service_id };
		
		ResultSet rs_profile = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		try {
			while(rs_profile.next())
			{
				profile_id_tobedeleted.add(Integer.parseInt(rs_profile.getString("id")));
			}
			
			//delete profile info
			sql = ProfileSql.sql_profile_delete;
			params = new Object[]{ service_id };
			SQLHelper.ExecuteNoneQuery(sql, params);
			
			for(int i = 0; i < profile_id_tobedeleted.size(); i++)
			{
				int profile_id = profile_id_tobedeleted.get(i);
				
				//contact
				sql = ProfileSql.sql_contact_delete;
				params = new Object[]{ profile_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
				
				//input
				sql = ProfileSql.sql_contact_delete;
				params = new Object[]{ profile_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
				
				//output
				sql = ProfileSql.sql_output_delete;
				params = new Object[]{ profile_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
				
				//precondition
				sql = ProfileSql.sql_precondition_delete;
				params = new Object[]{ profile_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
				
				//service category
				sql = ProfileSql.sql_sc_delete;
				params = new Object[]{ profile_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
