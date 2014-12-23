/*
 * OWL-S API provides functionalities to create and to manipulate OWL-S files. Copyright
 * (C) 2005 Katia Sycara, Softagents Lab, Carnegie Mellon University
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the
 * 
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * The Intelligent Software Agents Lab The Robotics Institute Carnegie Mellon University 5000 Forbes
 * Avenue Pittsburgh PA 15213
 * 
 * More information available at http://www.cs.cmu.edu/~softagents/
 */
package samples.writers;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.exception.OWLSWriterException;
import EDU.cmu.Atlas.owls1_1.parser.OWLSServiceParser;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.writer.OWLSServiceWriter;

/**
 * @author Naveen Srinivasan
 *  
 */
public class TestServiceWriter {

	private  static Logger logger = Logger.getLogger(TestServiceWriter.class);
	
    public static void readURL() {
        //String url = "http://www.daml.ri.cmu.edu/owls/BravoAirService.owl";
        String url = "http://www.daml.org/services/owl-s/1.1/BravoAirService.owl";

        OWLSServiceParser parser = new OWLSServiceParser();

        OWLSServiceModel srvModel;
        try {
            srvModel = parser.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        } catch (NotInstanceOfServiceException e1) {
            e1.printStackTrace();
            return;
        }
        ServiceList srvList = srvModel.getServiceList();

        logger.debug("#################################");
        try {
            OWLSServiceWriter.write(srvList, url, System.out);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (OWLSWriterException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] str) throws IndexOutOfBoundsException, OWLSWriterException, IOException,
            NotInstanceOfServiceException {

        URL configURL = ClassLoader.getSystemResource("EDU/cmu/Atlas/owls1_1/conf/log4j.properties");
        PropertyConfigurator.configure(configURL);

        readURL();
    }

}