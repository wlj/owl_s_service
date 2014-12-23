import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSProcessParser;
import EDU.cmu.Atlas.owls1_1.process.OWLSProcessModel;
import EDU.cmu.Atlas.owls1_1.process.ProcessList;

public class Test {
	public static void main(String args[])
			throws NotInstanceOfServiceException, MalformedURLException,
			NotInstanceOfProcessException, IOException {

		OWLSProcessParser processParser = new OWLSProcessParser();
		OWLSProcessModel processModel = processParser.read(new FileReader(
				"G:/BravoAirProcess.xml"));
		ProcessList processList = processModel.getProcessList();
		if (processList == null) {
			System.err.println("error");
			return;
		}

		if (processList.size() == 0) {
			System.err.println("size = 0");
			return;
		}

		for (int i = 0; i < processList.size(); ++i) {
			EDU.cmu.Atlas.owls1_1.process.Process process = processList
					.getNthProcess(i);
			System.out.println(process);
		}
	}
}
