package EDU.pku.ly.Grounding.util;

public class GroundingSql {
	
	//grounding_wsdlgrounding
	public final static String sql_wsdlgrounding = "insert into grounding_wsdlgrounding values(?, ?, ?);";
	
	//grounding_wsdl_param_message_map
	public final static String sql_wsdl_param_message_map = "insert into grounding_wsdl_param_message_map values(?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	//grounding_wapg
	public final static String sql_wapg = "insert into grounding_wapg values(?, ?, ?, ?, ?, ?, ?);";
	
	//grounding_operation
	public final static String sql_operation = "insert into grounding_operation values(?, ?, ?, ?, ?);";
	
	//query process
	public final static String sql_query_process_id = "select * from process_process where uri=? and service_id=? and type=1;";
	
	//query param input
	public final static String sql_query_input_id = "select * from process_param where uri=? and process_id=? and is_input=1;";

	//query param input
	public final static String sql_query_output_id = "select * from process_param where uri=? and process_id=? and is_output=1;";

	//query grounding
	public final static String sql_query_grounding = "select * from grounding_wsdlgrounding where service_id=?;";

	//query wapg
	public final static String sql_query_wapg = "select * from grounding_wapg where wsdlgrounding_id=?;";
	
	//query wpmm
	public final static String sql_query_wpmm = "select * from grounding_wsdl_param_message_map where wapg_id=? and is_input=? and is_output=?;";
	
	//query operation
	public final static String sql_query_operation = "select * from grounding_operation where wapg_id=?;";

	//delete
	public final static String sql_grounding_delete = "delete from grounding_wsdlgrounding where service_id=?;";
	
	//wapg
	public final static String sql_wapg_delete = "delete from grounding_wapg where wsdlgrounding_id=?;";
	
	//wpmm
	public final static String sql_wpmm_delete = "delete from grounding_wsdl_param_message_map where wapg_id=?;";
	
	//operation
	public final static String sql_operation_delete = "delete from grounding_operation where wapg_id=?;";
	
}


