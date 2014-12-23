package EDU.pku.ly.Process.Implementation;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import EDU.pku.ly.Process.ProcessDelete;
import EDU.pku.ly.Process.util.ProcessSql;

import edu.pku.ly.SqlOpe.SQLHelper;

public class ProcessDeleteImpl implements ProcessDelete {
	
	private static final long serialVersionUID = -1L;
	
	public int ProcessDeleteEntry(int service_id) {
		// TODO Auto-generated method stub
		
		String sql = ProcessSql.sql_process_inquiry_by_service_id;
		Object[] params = new Object[]{ service_id };
		ResultSet rs_process = SQLHelper.ExecuteQueryRtnSet(sql, params);
		
		List<Integer> process_id_tobedeleted = new ArrayList<Integer>();
		
		try {
			while(rs_process.next())
			{
				process_id_tobedeleted.add(Integer.parseInt(rs_process.getString("id")));
			}
			
			//delete process
			sql = ProcessSql.sql_process_delete;
			params = new Object[]{ service_id };
			SQLHelper.ExecuteNoneQuery(sql, params);
			
			for(int i = 0; i < process_id_tobedeleted.size(); i++)
			{
				int process_id = process_id_tobedeleted.get(i);
				
				//delete relation
				sql = ProcessSql.sql_relation_delete;
				params = new Object[]{ process_id, process_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
				
				//param
				sql = ProcessSql.sql_param_delete;
				params = new Object[]{ process_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
				
				//conditon
				sql = ProcessSql.sql_condition_delete_by_process_id;
				params = new Object[]{ process_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
				
				//result
				sql = ProcessSql.sql_result_inquiry;
				params = new Object[]{ process_id };
				ResultSet rs_result = SQLHelper.ExecuteQueryRtnSet(sql, params);
				
				List<Integer> result_id_tobedeleted = new ArrayList<Integer>();
				while(rs_result.next())
				{
					result_id_tobedeleted.add(Integer.parseInt(rs_result.getString("id")));
				}
				
				//delete result
				sql = ProcessSql.sql_result_delete;
				params = new Object[]{ process_id };
				SQLHelper.ExecuteNoneQuery(sql, params);
				
				for(int j = 0; j < result_id_tobedeleted.size(); j++)
				{
					int result_id = result_id_tobedeleted.get(j);
					
					//delete condition
					sql = ProcessSql.sql_condition_delete_by_result_id;
					params = new Object[]{ result_id };
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
		
		return 0;
	}

}
