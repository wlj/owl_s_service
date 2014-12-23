package EDU.pku.ly.Process.util;

public class ProcessSql {
	
	//process_process
	public final static String sql_process_insert = "insert into process_process values(?, ?, ?, ?, ?, ?);";
	
	//process_condition
	public final static String sql_condition_insert = "insert into process_condition values(?, ?, ?, ?, ?, ?, ?);";
	
	//process_relation
	public final static String sql_relation_insert = "insert into process_relation values(?, ?, ?, ?);";
	
	//process_result
	public final static String sql_result_insert = "insert into process_result values(?, ?);";
	
	//process_param
	public final static String sql_param_insert = "insert into process_param values(?, ?, ?, ?, ?, ?, ?, ?, ?);";

	//grl_element
	public final static String sql_grl_element = "insert into grl_element values(?, ?, ?, ?, ?);";

	//grl_link
	public final static String sql_grl_link = "insert into grl_link values(?, ?, ?, ?, ?, ?, ?, ?);";
	
	//inquiry
	public final static String sql_result_inquiry = "select * from process_result where process_id=?;";
	
	public final static String sql_process_inquiry_by_service_id = "select * from process_process where service_id=?;";
	
	public final static String sql_process_inquiry_by_process_id = "select * from process_process where id=?;";
	
	public final static String sql_query_parameter_by_id = "select * from process_param where id=?;";
	
	//delete
	public final static String sql_process_delete = "delete from process_process where service_id=?;";

	//relation
	public final static String sql_relation_delete = "delete from process_relation where process_id=? or parent_id=?;";
	
	//param
	public final static String sql_param_delete = "delete from process_param where process_id=?;";
	
	//condition
	public final static String sql_condition_delete_by_process_id = "delete from process_condition where process_id=?;";
	
	public final static String sql_condition_delete_by_result_id = "delete from process_condition where result_id=?;";
	
	//result
	public final static String sql_result_delete = "delete from process_result where process_id=?;";
}
