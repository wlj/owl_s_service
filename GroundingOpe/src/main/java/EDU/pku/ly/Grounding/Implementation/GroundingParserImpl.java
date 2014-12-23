package EDU.pku.ly.Grounding.Implementation;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;

import edu.pku.ly.SqlOpe.SQLHelper;

import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlInputMessageMapImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlInputMessageMapListImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlOperationRefImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlOutputMessageMapImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlOutputMessageMapListImpl;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.process.implementation.ParameterImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ProcessImpl;
import EDU.pku.ly.Grounding.GroundingParser;
import EDU.pku.ly.Grounding.util.GroundingSql;

public class GroundingParserImpl implements GroundingParser{

	private static final long serialVersionUID = -1L;
	
	public GroundingParserImpl()
	{}
	
	public void GroundingParserEntry(WsdlGrounding grounding, int service_id) {
		// TODO Auto-generated method stub
		
		//grounding itself
		String uri = grounding.getURI();
		
		String sql = GroundingSql.sql_wsdlgrounding;
		Object[] params = new Object[]{ 0, uri, service_id };
		
		GroundingInsertOpe(sql, params);
		
		int new_grounding_id = SQLHelper.GetLastInsertID();
		
		//wapg list
		String wsdl_input_message = "";
		String wsdl_output_message = "";
		String wsdl_version = "";
		WsdlAtomicProcessGroundingList wapg_list = grounding.getWsdlAtomicProcessGroundingList(); 
		for(int i = 0; i < wapg_list.size(); i++)
		{
			WsdlAtomicProcessGrounding wapg = wapg_list.getNthWsdlAtomicProcessGrounding(i);
			uri = wapg.getURI();
			
			Process process = (ProcessImpl)wapg.getOwlsProcess();
			String process_uri = process.getURI();
			int process_id = GetAtomicProcessIdByUri(process_uri, service_id);
			
			wsdl_input_message = wapg.getWsdlInputMessage() == null ? "" : wapg.getWsdlInputMessage();
			wsdl_output_message = wapg.getWsdlOutputMessage() == null ? "" : wapg.getWsdlOutputMessage();
			wsdl_version = wapg.getWsdlVersion() == null ? "" : wapg.getWsdlVersion();
			
			sql = GroundingSql.sql_wapg;
			params = new Object[]{ 0, uri, process_id, wsdl_input_message, 
					wsdl_output_message, wsdl_version, new_grounding_id};
			
			//wapg itself
			GroundingInsertOpe(sql, params);
			
			int new_wapg_id = SQLHelper.GetLastInsertID();
			
			//input message map
			String xslt_transformation_string = "";
			String xslt_transformation_uri = "";
			String wsdl_message_part = "";
			String param_uri = "";
			WsdlInputMessageMapList wimm_list = (WsdlInputMessageMapListImpl)wapg.getWsdlInputs();
			for(int j = 0; j < wimm_list.size(); j++)
			{
				WsdlInputMessageMap wimm = (WsdlInputMessageMapImpl)wimm_list.getNthWsdlInputMessageMap(j);
				uri = wimm.getURI() == null ? "" : wimm.getURI();
				
				xslt_transformation_string = wimm.getXSLTTransformationString() == null ? "" : wimm.getXSLTTransformationString();
				xslt_transformation_uri = wimm.getXSLTTransformationURI() == null ? "" : wimm.getXSLTTransformationURI();
				wsdl_message_part = wimm.getWSDLMessagePart() == null ? "" : wimm.getWSDLMessagePart();
				
				Parameter param = (ParameterImpl)wimm.getOWLSParameter();
				param_uri = param.getURI();
				
				sql = GroundingSql.sql_query_input_id;
				int param_id = GetParamterByUri(sql, param_uri, process_id);
				
				sql = GroundingSql.sql_wsdl_param_message_map;
				params = new Object[]{ 0, uri, wsdl_message_part, xslt_transformation_string, 
						xslt_transformation_uri, 1, 0, param_id, new_wapg_id };
				
				GroundingInsertOpe(sql, params);
			}
			
			//output message map
			WsdlOutputMessageMapList womm_list = (WsdlOutputMessageMapListImpl)wapg.getWsdlOutputs();
			for(int j = 0; j < womm_list.size(); j++)
			{
				WsdlOutputMessageMap womm = (WsdlOutputMessageMapImpl)womm_list.getNthWsdlOutputMessageMap(j);
				uri = womm.getURI() == null ? "" : womm.getURI();
				
				xslt_transformation_string = womm.getXSLTTransformationString() == null ? "" : womm.getXSLTTransformationString();
				xslt_transformation_uri = womm.getXSLTTransformationURI() == null ? "" : womm.getXSLTTransformationURI();
				wsdl_message_part = womm.getWSDLMessagePart() == null ? "" : womm.getWSDLMessagePart();
				
				Parameter param = (ParameterImpl)womm.getOWLSParameter();
				param_uri = param.getURI();
				
				sql = GroundingSql.sql_query_output_id;
				int param_id = GetParamterByUri(sql, param_uri, process_id);
				
				sql = GroundingSql.sql_wsdl_param_message_map;
				params = new Object[]{ 0, uri, wsdl_message_part, xslt_transformation_string, 
						xslt_transformation_uri, 0, 1, param_id, new_wapg_id };
				
				GroundingInsertOpe(sql, params);
			}
			
			//operation
			WsdlOperationRef wor = (WsdlOperationRefImpl)wapg.getWsdlOperation();
			uri = wor.getURI();
			String operation = wor.getOperation();
			String porttype = wor.getPortType();
			
			sql = GroundingSql.sql_operation;
			params = new Object[]{ 0, uri, operation, porttype, new_wapg_id};
			
			GroundingInsertOpe(sql, params);
		}
	}
	
	private int GetParamterByUri(String sql, String param_uri, int process_id) {
		// TODO Auto-generated method stub
		
		int param_id = 0;
		
		Object[] params = new Object[]{ param_uri, process_id };
		
		ResultSet rs = SQLHelper.ExecuteQueryRtnSet(sql, params);
		try {
			if(rs.next())
			{
				param_id = Integer.parseInt(rs.getString("id"));
				
				return param_id;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return param_id;
	}

	private int GetAtomicProcessIdByUri(String process_uri, int service_id) {
		// TODO Auto-generated method stub
		
		int process_id = 0;
		
		String sql = GroundingSql.sql_query_process_id;
		Object[] params = new Object[]{ process_uri, service_id };
		
		ResultSet rs = SQLHelper.ExecuteQueryRtnSet(sql, params);
		try {
			if(rs.next())
			{
				process_id = Integer.parseInt(rs.getString("id"));
				
				return process_id;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return process_id;
	}

	private void GroundingInsertOpe(String sql, Object[] params)
	{
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
	

	public void GroundingPersistence(OWLSGroundingModel owlsProcessModel, String groundingname) {
		// TODO Auto-generated method stub
		
		String grounding_version = "";
		String url = "";
		
		
	}

}
