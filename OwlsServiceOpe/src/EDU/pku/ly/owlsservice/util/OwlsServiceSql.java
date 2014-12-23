package EDU.pku.ly.owlsservice.util;

public class OwlsServiceSql {
	
	//insert
	public final static String sql_insert_service = "insert into service_service values(?, ?, ?, ?);";
	
	public final static String sql_insert_owlsservice = "insert into service_owlsservice values(?, ?, ?);";
	
	//update
	public final static String sql_update_owlsservice = "update service_owlsservice set version=? where id=?;";
	
	//query
	public final static String sql_query_service = "select * from service_service where owlsservice_id=?;";
	
	public final static String sql_query_owlsservice = "select * from service_owlsservice where uri=?;";
	
	public final static String sql_query_all_owlsservice = "select * from service_owlsservice;";
	
	//delete
	public final static String sql_delete_owlsservice = "delete from service_owlsservice where id=?;";
	
	public final static String sql_delete_service_by_id = "delete from service_service where id=?;";
}
