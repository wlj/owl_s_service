namespace java cn.edu.pku.ss.matchmaker.thrift

// request info
struct RequestInfo {
	  // owls info
	  1: required string owlsURI;
	  2: optional string owlsContent;
	  
	  // grl info
	  3: optional string grlURI;
	  4: optional string grlContent;
}

// response info
struct ResponseInfo {
    //service key
    1: required string serviceKey;
    2: optional string wsdlURI; 
}

service ServiceDiscoverer {
	list<ResponseInfo> getServices(1: RequestInfo requestinfo);
}