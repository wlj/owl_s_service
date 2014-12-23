package edu.pku.util;

import java.util.List;
import java.util.Map;

import cn.edu.pku.ss.matchmaker.thrift.Category;
import cn.edu.pku.ss.matchmaker.thrift.IOModel;
import cn.edu.pku.ss.matchmaker.thrift.PECRModel;
import cn.edu.pku.ss.matchmaker.thrift.ProfileInfo;
import edu.pku.context.model.OWL_SE;

public class ProfileInfoToOWL_SE {

	private ProfileInfo profileInfo;
	private OWL_SE owl_se;
	private boolean taskOrService;//task:false  service:true
	
	public ProfileInfoToOWL_SE(ProfileInfo profileInfo, OWL_SE owl_se, boolean taskOrService) {
		
		this.profileInfo = profileInfo;
		this.owl_se = owl_se;
		this.taskOrService = taskOrService;
	}
	
	public void transfer() {
		
		//key
		owl_se.setKey(profileInfo.getServiceKey());
		//name
		owl_se.setName(profileInfo.getServiceName());
		//C
		String Category_ = "";
		List<Category> categories = profileInfo.getCategoryList();
		if (categories.size() != 0) {
			Category_ = categories.get(0).getValue();
			for (int i = 1; i < categories.size(); i++) {
				Category_ = Category_ + ", " + categories.get(i).getValue();
			}
		}
		owl_se.setCategory(Category_);
		//I
		String Input_ = "";
		List<IOModel> inputs = profileInfo.getInputList();
		if (inputs.size() != 0) {
			Input_ = inputs.get(0).getParameterType();
			for (int i = 1; i < inputs.size(); i++) {
				Input_ = Input_ + ", " + inputs.get(i).getParameterType();
			}
		}
		owl_se.setInput(Input_);
		//O
		String Output_ = "";
		List<IOModel> outputs = profileInfo.getOutputList();
		if (outputs.size() != 0) {
			Output_ = outputs.get(0).getParameterType();
			for (int i = 1; i < outputs.size(); i++) {
				Output_ = Output_ + ", " + outputs.get(i).getParameterType();
			}
		}
		owl_se.setOutput(Output_);
		//P
		String Precondition_ = "";
		List<PECRModel> preconditions = profileInfo.getPreconditionList();
		if (preconditions.size() != 0) {
			Precondition_ = preconditions.get(0).getExpressionBody();
			for (int i = 1; i < preconditions.size(); i++) {
				Precondition_ = Precondition_ + " ^ " + preconditions.get(i).getExpressionBody();
			}
		}
		owl_se.setPrecondition(Precondition_);
		//E
		String Effect_ = "";
		List<PECRModel> effects = profileInfo.getEffectList();
		if (effects.size() != 0) {
			Effect_ = effects.get(0).getExpressionBody();
			for (int i = 1; i < effects.size(); i++) {
				Effect_ = Effect_ + " ^ " + effects.get(i).getExpressionBody();
			}
		}
		owl_se.setEffect(Effect_);
		
		//Context(task) & ContextRule(service)
		String ContextOrContextRule_ = "";
		List<PECRModel> contexts = profileInfo.getContext();
		if (contexts.size() != 0) {
			ContextOrContextRule_ = contexts.get(0).getExpressionBody();
			for (int i = 1; i < contexts.size(); i++) {
				ContextOrContextRule_ = ContextOrContextRule_ + " ^ " + contexts.get(i).getExpressionBody();
			}
		}
		owl_se.setContext(ContextOrContextRule_);
		
		//task-QoS
		if (taskOrService == false) {
			
			//QoS
			Map map = null;
			List<cn.edu.pku.ss.matchmaker.thrift.QoS> QoSs = profileInfo.getQosList();
			for (int i = 0; i < QoSs.size(); i++) {
				String name = QoSs.get(i).getName();
				edu.pku.context.model.QoS qos = new edu.pku.context.model.QoS();
				qos.setName(name);
				qos.setWeight(QoSs.get(i).getWeight());
				map.put(name, qos);
			}
			owl_se.setMap(map);
		}
		else {//service-QoS
			
			//QoS
			Map map = null;
			List<cn.edu.pku.ss.matchmaker.thrift.QoS> QoSs = profileInfo.getQosList();
			for (int i = 0; i < QoSs.size(); i++) {
				String name = QoSs.get(i).getName();
				edu.pku.context.model.QoS qos = new edu.pku.context.model.QoS();
				qos.setName(name);
				qos.setType(QoSs.get(i).type);
				qos.setPointOfSupply(QoSs.get(i).getPointOfSupply());
				qos.setPointOfValuation(QoSs.get(i).getPointOfValuation());
				qos.setPointAll(0.0);
				
				//ReturnPoint
				edu.pku.context.model.ReturnPoint returnPoint = new edu.pku.context.model.ReturnPoint();
				returnPoint.setCurrentPoint(QoSs.get(i).getReturnPoint().getCurrentPoint());
				returnPoint.setTimes(QoSs.get(i).getReturnPoint().getTimes());
				returnPoint.setTotalPoint(QoSs.get(i).getReturnPoint().getTotalPoint());
				
				qos.setReturnPoint(returnPoint);
				
				map.put(name, qos);
			}
			owl_se.setMap(map);
		}
	}
	
	public static void main(String[] args) {
		
		
	}
}
