package EDU.pku.ly.Grounding.Implementation;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.pku.ly.SqlOpe.SQLHelper;
import EDU.cmu.Atlas.owls1_1.process.Parameter;
import EDU.cmu.Atlas.owls1_1.process.Process;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlAtomicProcessGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlInputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOperationRef;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMap;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlOutputMessageMapList;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlAtomicProcessGroundingImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlAtomicProcessGroundingListImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlGroundingImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlGroundingListImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlInputMessageMapImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlInputMessageMapListImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlOperationRefImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlOutputMessageMapImpl;
import EDU.cmu.Atlas.owls1_1.grounding.implementation.WsdlOutputMessageMapListImpl;
import EDU.pku.ly.Grounding.GroundingInquiry;
import EDU.pku.ly.Grounding.util.GroundingSql;
import EDU.pku.ly.Process.ProcessInquiry;
import EDU.pku.ly.Process.Implementation.ProcessInquiryImpl;

public class GroundingInquiryImpl implements GroundingInquiry {
	
	private static final long serialVersionUID = -1L;
	
	public GroundingInquiryImpl(){
		
	}
	
	public WsdlGroundingList GroundingInquiryEntry(int service_id) {
		// TODO Auto-generated method stub

		ProcessInquiry inquiry = new ProcessInquiryImpl();
		
		String sql = GroundingSql.sql_query_grounding;
		Object[] params = new Object[]{ service_id };
		ResultSet rs_grounding = SQLHelper.ExecuteQueryRtnSet(sql, params);

		WsdlGroundingList wsdl_grounding_lst = new WsdlGroundingListImpl();
		WsdlGrounding wsdl_grounding = null;
		try {
			while(rs_grounding.next())
			{
				wsdl_grounding = new WsdlGroundingImpl(rs_grounding.getString("uri"));
				
				int wsdlgrounding_id = Integer.parseInt(rs_grounding.getString("id"));

				//wsdlgrounding itself

				sql = GroundingSql.sql_query_wapg;
				params = new Object[]{ wsdlgrounding_id };
				ResultSet rs_wapg = SQLHelper.ExecuteQueryRtnSet(sql, params);
				
				WsdlAtomicProcessGroundingList wapg_list = new WsdlAtomicProcessGroundingListImpl();
				WsdlAtomicProcessGrounding wapg = null;
				Process process = null;
				while(rs_wapg.next())
				{
					int wapg_id = Integer.parseInt(rs_wapg.getString("id"));
					wapg = new WsdlAtomicProcessGroundingImpl(rs_wapg.getString("uri"));

					int process_id = Integer.parseInt(rs_wapg.getString("process_id"));
					process = inquiry.ProcessInquiryEntry(process_id, "");
					
					wapg.setOwlsProcess(process);
					wapg.setWsdlInputMessage(rs_wapg.getString("wsdl_input_message"));
					wapg.setWsdlOutputMessage(rs_wapg.getString("wsdl_output_message"));
					wapg.setWsdlVersion(rs_wapg.getString("wsdl_version"));

					//wsdl input message map
					sql = GroundingSql.sql_query_wpmm;
					params = new Object[]{ wapg_id, 1, 0 };
					ResultSet rs_wimm = SQLHelper.ExecuteQueryRtnSet(sql, params);

					wapg.setWsdlInputs(GetWimmList(rs_wimm, inquiry));
					
					//wsdl output message map
					sql = GroundingSql.sql_query_wpmm;
					params = new Object[]{ wapg_id, 0, 1 };
					ResultSet rs_womm = SQLHelper.ExecuteQueryRtnSet(sql, params);

					wapg.setWsdlOutputs(GetWommList(rs_womm, inquiry));
					
					//operation
					sql = GroundingSql.sql_query_operation;
					params = new Object[]{ wapg_id };
					ResultSet rs_operation = SQLHelper.ExecuteQueryRtnSet(sql, params);

					wapg.setWsdlOperation(GetWsdlOperationRef(rs_operation));
					
					wapg_list.addWsdlAtomicProcessGrounding(wapg);
				}
				wsdl_grounding.setWsdlAtomicProcessGroundingList(wapg_list);
			}
			wsdl_grounding_lst.addWsdlGrounding(wsdl_grounding);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(wsdl_grounding_lst);
		return wsdl_grounding_lst;
	}

	private WsdlOperationRef GetWsdlOperationRef(ResultSet rs_operation) {
		// TODO Auto-generated method stub
		
		WsdlOperationRef wor = null;
		try {
			if(rs_operation.next())
			{
				String uri = rs_operation.getString("uri");
				String operation = rs_operation.getString("operation");
				String porttype = rs_operation.getString("porttype");
				
				wor = new WsdlOperationRefImpl(uri);
				
				wor.setOperation(operation);
				wor.setPortType(porttype);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return wor;
	}

	private WsdlOutputMessageMapList GetWommList(ResultSet rs_womm, ProcessInquiry inquiry) {
		// TODO Auto-generated method stub
		
		WsdlOutputMessageMapList womm_lst = new WsdlOutputMessageMapListImpl();
		WsdlOutputMessageMap womm = null;
		Parameter param = null;
		try {
			while(rs_womm.next())
			{
				womm = new WsdlOutputMessageMapImpl(rs_womm.getString("uri"));
				womm.setWSDLMessagePart(rs_womm.getString("wsdl_message_part"));
				womm.setXSLTTransformationString(rs_womm.getString("xslt_transformation_string"));
				womm.setXSLTTransformationURI(rs_womm.getString("xslt_transformation_uri"));
				
				int param_id = Integer.parseInt(rs_womm.getString("owls_param_id"));
				param = inquiry.ParameterInquiry(param_id);
				
				womm.setOWLSParameter(param);
				
				womm_lst.addWsdlOutputMessageMap(womm);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return womm_lst;
	}

	private WsdlInputMessageMapList GetWimmList(ResultSet rs_wimm, ProcessInquiry inquiry) {
		// TODO Auto-generated method stub
		
		WsdlInputMessageMapList wimm_lst = new WsdlInputMessageMapListImpl();
		WsdlInputMessageMap wimm = null;
		Parameter param = null;
		try {
			while(rs_wimm.next())
			{
				wimm = new WsdlInputMessageMapImpl(rs_wimm.getString("uri"));
				wimm.setWSDLMessagePart(rs_wimm.getString("wsdl_message_part"));
				wimm.setXSLTTransformationString(rs_wimm.getString("xslt_transformation_string"));
				wimm.setXSLTTransformationURI(rs_wimm.getString("xslt_transformation_uri"));
				
				int param_id = Integer.parseInt(rs_wimm.getString("owls_param_id"));
				param = inquiry.ParameterInquiry(param_id);
				
				wimm.setOWLSParameter(param);
				
				wimm_lst.addWsdlInputMessageMap(wimm);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return wimm_lst;
	}

}
