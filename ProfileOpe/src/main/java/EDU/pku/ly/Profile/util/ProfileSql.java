package EDU.pku.ly.Profile.util;

public class ProfileSql {

	//insert
	public final static String sql_profile_insert = "insert into profile_profile values(?, ?, ?, ?, ?, ?);";

	//contact
	public final static String sql_contact_insert = "insert into profile_contact values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	//service category
	public final static String sql_sc_insert = "insert into profile_service_category values(?, ?, ?, ?, ?, ?, ?)";
	
	//input
	public final static String sql_input_insert = "insert into profile_input values(?, ?, ?);";
	
	//output
	public final static String sql_output_insert = "insert into profile_output values(?, ?, ?);";
	
	//preconditon
	public final static String sql_precondition_insert = "insert into profile_precondition values(?, ?, ?);";
	
	//result
	public final static String sql_result_insert = "";

	//inquiry
	public final static String sql_profile_inquiry = "select * from profile_profile where service_id=?;";
	
	public final static String sql_process_inquiry = "select * from process_process where uri=? and service_id=?;";
	
	public final static String sql_contact_inquiry = "select * from profile_contact where profile_id=?;";
	
	public final static String sql_sc_inquiry = "select * from profile_service_category where profile_id=?;";
	
	//input
	public final static String sql_input_id_inquiry = "select * from profile_input where profile_id=?;";

	public final static String sql_input_inquiry_by_id = "select * from process_param where id=?;";
	
	//output
	public final static String sql_output_id_inquiry = "select * from profile_output where profile_id=?;";
	
	public final static String sql_output_inquiry_by_id = "select * from process_param where id=?;";
	
	//input
	public final static String sql_input_inquiry = "select * from process_param where uri=? and is_input=?;";
	
	//output
	public final static String sql_output_inquiry = "select * from process_param where uri=? and is_output=?;";

	//precondition
	public final static String sql_precondition_inquiry = "select * from process_condition where uri=? and type=?;";
	
	public final static String sql_precondition_id_inquiry = "select * from profile_precondition where profile_id=?;";
	
	public final static String sql_precondition_inquiry_by_id = "select * from process_condition where id=?;";
	
	//result
	public final static String sql_result_inquiry = "select * from process_result where process_id=?;";

	public final static String sql_result_inquiry_by_id = "select * from process_condition where result_id=?;";
	
	
	//delete
	public final static String sql_profile_delete = "delete from profile_profile where service_id=?;";
	
	//contact
	public final static String sql_contact_delete = "delete from profile_contact where profile_id=?;";
	
	//input
	public final static String sql_input_delete = "delete from profile_input where profile_id=?;";

	//output
	public final static String sql_output_delete = "delete from profile_output where profile_id=?;";
	
	//precondition
	public final static String sql_precondition_delete = "delete from profile_precondition where profile_id=?;";
	
	//service category
	public final static String sql_sc_delete = "delete from profile_service_category where profile_id=?;";
	
	//result
	public final static String sql_inquiry_result_id = "SELECT id FROM juddiv3.process_result where process_id in (select id from process_process where service_id=?);";
	
	//
	public final static String sql_inquiry_condition = "select * from process_condition where result_id=?;";
}
