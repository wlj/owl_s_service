include "ServiceDiscovery.thrift"

namespace java cn.edu.pku.ss.serviceorch.thrift


struct TaskServiceInfo {
	1: optional string taskIOPE;
	2: optional list<ServiceDiscovery.ProfileInfo> candidateServices;
}


struct ServiceOrchRequest{
  1: optional string bpelContent;
  2: optional map<string, TaskServiceInfo> tasksInfo;
}


service ServiceOrch {
	void sendServiceOrch(1: ServiceOrchRequest serviceOrchRequest);
}