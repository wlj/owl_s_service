package EDU.pku.ly.Grounding.Implementation;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import EDU.pku.ly.Grounding.GroundingDelete;
import EDU.pku.ly.Grounding.util.GroundingSql;

import edu.pku.ly.SqlOpe.SQLHelper;

public class GroundingDeleteImpl implements GroundingDelete {
	
	private static final long serialVersionUID = -1L;
	
	public void GroundingDeleteEntry(int service_id) {
		// TODO Auto-generated method stub
		
		List<Integer> grounding_id_tobedeleted = new ArrayList<Integer>();
		
		String sql = GroundingSql.sql_query_grounding;
		Object[] params = new Object[]{ service_id };
		ResultSet rs_grounding = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		try {
			while(rs_grounding.next())
			{
				grounding_id_tobedeleted.add(Integer.parseInt(rs_grounding.getString("id")));
			}
			
			//delete profile info
			sql = GroundingSql.sql_grounding_delete;
			params = new Object[]{ service_id };
			SQLHelper.ExecuteNoneQuery(sql, params);
			
			List<Integer> wapg_id_tobedeleted = new ArrayList<Integer>();
			
			for(int i = 0; i < grounding_id_tobedeleted.size(); i++)
			{
				int grounding_id = grounding_id_tobedeleted.get(i);
				
				sql = GroundingSql.sql_query_wapg;
				params = new Object[]{ grounding_id };
				ResultSet rs_wapg = SQLHelper.ExecuteQueryRtnSet(sql, params);
				
				while(rs_wapg.next())
				{
					wapg_id_tobedeleted.add(Integer.parseInt(rs_wapg.getString("id")));
				}
				
				//delete wapg
				sql = GroundingSql.sql_wapg_delete;
				params = new Object[]{ grounding_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
				
				for(int j = 0; j < wapg_id_tobedeleted.size(); j++)
				{
					int wapg_id = wapg_id_tobedeleted.get(j);
					
					//delete wpmm
					sql = GroundingSql.sql_wpmm_delete;
					params = new Object[]{ wapg_id };
					SQLHelper.ExecuteNoneQuery(sql, params);
					
					//delete operation
					sql = GroundingSql.sql_operation_delete;
					params = new Object[]{ wapg_id };
					SQLHelper.ExecuteNoneQuery(sql, params);
				}
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
