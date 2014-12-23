package EDU.pku.ly.owlservice.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.expression.Expression;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ConditionImpl;
import EDU.cmu.Atlas.owls1_1.expression.implementation.ExpressionImpl;
import EDU.cmu.Atlas.owls1_1.process.EffectList;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.process.implementation.EffectListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.InputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.OutputListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.PreConditionListImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultImpl;
import EDU.cmu.Atlas.owls1_1.process.implementation.ResultListImpl;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ProfileImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoriesListImpl;
import EDU.cmu.Atlas.owls1_1.profile.implementation.ServiceCategoryImpl;
import EDU.cmu.Atlas.owls1_1.service.ServiceProfileList;
import EDU.cmu.Atlas.owls1_1.service.implementation.ServiceProfileListImpl;
import EDU.pku.edu.ly.temporaryowls.PointOfReturn;
import EDU.pku.edu.ly.temporaryowls.QoS;
import EDU.pku.ly.owlsservice.ExtendedService;
import EDU.pku.ly.owlsservice.implementation.ExtendedServiceImpl;

public class TemporaryOwls {
	
	public static void main(String[] args)
	{
		List<ExtendedService> results = GetTemporaryServices();
		
		System.out.println("over...");
	}
	
	public static List<ExtendedService> GetTemporaryServices()
	{
		List<ExtendedService> results = new ArrayList<ExtendedService>();
		
		ExtendedService service = new ExtendedServiceImpl();

		String basedir = "C:\\luoshan\\SourceCode\\juddi\\running\\juddi-portal-bundle-3.0.2\\webapps\\juddiv3\\tempowls";
		
		File file = new File(basedir);
		File[] files = file.listFiles();
		File tmpFile;
		for (int i = 0; i < files.length; i++) 
		{
			service = new ExtendedServiceImpl();
			
			if(!files[i].isDirectory())
			{
				tmpFile = files[i];
				
				String filename = tmpFile.getName();
				service.setServiceKey(filename.split(".").length > 1 ? filename.split(".")[0] : filename);
				
				service.setServiceProfileList(ParseProfile(tmpFile.getAbsolutePath(), service));
			
				results.add(service);
			}
		}
		
		return results;
	}

	private static ServiceProfileList ParseProfile(String absolutePath, ExtendedService service) {
		// TODO Auto-generated method stub
		ServiceProfileList proflielist = new ServiceProfileListImpl();
		Profile profile = new ProfileImpl();
		
		File file = new File(absolutePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String str = "";
            while ((str = reader.readLine()) != null) {
                if(str.startsWith("C:"))
                {
                	String res = str.trim().split(":")[1].trim();
                	
                	ServiceCategoriesList clist= new ServiceCategoriesListImpl();
                	ServiceCategory category = new ServiceCategoryImpl();
                	category.setValue(res);
                	clist.addServiceCategory(category);
                	profile.setServiceCategory(clist);
                }
                else if(str.startsWith("I:"))
                {
                	String res = str.trim().split(":")[1].trim();
                	
                	InputList ilist = new InputListImpl();
                	Input input = new InputImpl();
                	input.setParameterValue(res);
                	ilist.addInput(input);
                	profile.setInputList(ilist);
                }
                else if(str.startsWith("O:"))
                {
                	String res = str.trim().split(":")[1].trim();
                	
                	OutputList olist = new OutputListImpl();
                	Output output = new OutputImpl();
                	output.setParameterValue(res);
                	olist.addOutput(output);
                	profile.setOutputList(olist);
                	
                }
                else if(str.startsWith("P:"))
                {
                	String res = str.trim().split(":")[1].trim();
                	
                	PreConditionList preConditionList = new PreConditionListImpl();
                	Condition condition = new ConditionImpl();
                	condition.setExpressionBody(res);
                	preConditionList.addPreCondition(condition);
                	profile.setPreconditionList(preConditionList);
                }
                else if(str.startsWith("E:"))
                {
                	String res = str.trim().split(":")[1].trim();
                	
                	ResultList resultList = new ResultListImpl();
                	Result result = new ResultImpl();
                	
                	EffectList effectList = new EffectListImpl();
                	Expression expression = new ExpressionImpl();
                	expression.setExpressionBody(res);
                	effectList.addExpression(expression);
                	
                	result.setHasEffects(effectList);
                	resultList.addResult(result);
                	
                	profile.setResultList(resultList);
                }
                else if(str.startsWith("ContextRule"))
                {
                	String res = str.trim().split(":")[1].trim();
                	service.setContextRule(res);
                }
                else {
                	String res = str.substring(4).trim();
                	
                	String[] res_arr = res.split("\\^");
                	List<QoS> qos_lst = new ArrayList<QoS>();
                	QoS qos = null;
                	
                	PointOfReturn pointOfReturn = new PointOfReturn();
                	for(int i = 0; i < res_arr.length; i++)
                	{
                		qos = new QoS();
                		
                		String refine = res_arr[i];
                		if(refine.startsWith("("))
                			refine = refine.substring(1);
                		if(refine.endsWith(")"))
                			refine = refine.substring(0, refine.length() - 1);
                		
                		String[] refine_arr = refine.split(",");
                		
                		for(int j = 0; j < refine_arr.length; j++)
                		{
                			String tmp = refine_arr[j].trim();
                			if(tmp.startsWith("Name"))
                			{
                				String name = tmp.split(":")[1].trim();
                				qos.Name = name;
                			}
                			else if(tmp.startsWith("Type"))
                			{
                				String type = tmp.split(":")[1].trim();
                				qos.Type = type;
                			}
                			else if(tmp.startsWith("PointOfSupply"))
                			{
                				String pointOfSupply = tmp.split(":")[1].trim();
                				qos.PointOfSupply = pointOfSupply;
                			}
                			else if(tmp.startsWith("PointOfValuation"))
                			{
                				String pointOfValuation = tmp.split(":")[1].trim();
                				qos.PointOfValuation = pointOfValuation;
                			}
                			else if(tmp.startsWith("PointOfReturn"))
                			{
                				pointOfReturn = new PointOfReturn();
                				
                				int startidx = tmp.indexOf("<");
                				int endidx = tmp.indexOf(">");
                				String pointOfReturnStr = tmp.substring(startidx + 1, endidx);
                				
                				String[] pointOfReturn_Arr = pointOfReturnStr.split(";");

                				for(int k = 0; k < pointOfReturn_Arr.length; k++)
                				{
                					String tmp_pointOfReturn = pointOfReturn_Arr[k].trim();
                					if(tmp_pointOfReturn.startsWith("hasTotalPoint"))
                					{
                						String hasTotalPoint = tmp_pointOfReturn.split(":")[1].trim();
                						pointOfReturn.hasTotalPoint = hasTotalPoint;
                					}
                					else if(tmp_pointOfReturn.startsWith("hasTimes"))
                					{
                						String hasTimes = tmp_pointOfReturn.split(":")[1].trim();
                						pointOfReturn.hasTimes = hasTimes;
                					}
                					else {
                						String hasCurrentPoint = tmp_pointOfReturn.split(":")[1].trim();
                						pointOfReturn.hasCurrentPoint = hasCurrentPoint;
									}
                				}
                				qos.pointOfReturn = pointOfReturn;
							}
                		}
                		qos_lst.add(qos);
                	}
                	service.setQoS(qos_lst);
				}
            }
            
            proflielist.addServiceProfile(profile);
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return proflielist;
	}
}
