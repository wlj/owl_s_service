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
package EDU.cmu.Atlas.owls1_1.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfWsdlGroundingException;
import EDU.cmu.Atlas.owls1_1.grounding.OWLSGroundingModel;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGrounding;
import EDU.cmu.Atlas.owls1_1.grounding.WsdlGroundingList;
import EDU.cmu.Atlas.owls1_1.uri.OWLSGroundingURI;
import EDU.cmu.Atlas.owls1_1.writer.OWLSWriterUtils;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * OWL-S Grounding Parser
 * @author Naveen Srinivasan
 */
public class OWLSGroundingParser {

    private OntModel ontModel;

    static Logger logger = Logger.getLogger(OWLSGroundingParser.class);

    private OWLS_Object_Builder objectBuilder;

    private OWLS_Store_Builder storeBuilder;

    public OWLSGroundingParser() {
        this(true);
    }

    public OWLSGroundingParser(boolean imports) {
        this(OWLSWriterUtils.getNewOntModel());
        ontModel.setDynamicImports(imports);
    }

    public OWLSGroundingParser(OntModel ontModel) {
        this.ontModel = OWLSWriterUtils.getNewOntModel(ontModel);
        //removing objects from the symbol table
        objectBuilder = OWLS_Object_BuilderFactory.instance();
        objectBuilder.reset();
        storeBuilder = OWLS_Store_BuilderFactory.instance();

    }

    public OWLSGroundingModel read(String URI) throws IOException, NotInstanceOfWsdlGroundingException {
        return read(URI, "");
    }

    public OWLSGroundingModel read(Reader reader) throws NotInstanceOfWsdlGroundingException {
        return read(reader, "");
    }

    public OWLSGroundingModel read(String URI, String base) throws IOException, NotInstanceOfWsdlGroundingException {

        URL url = new URL(URI);
        InputStream is = url.openStream();
        ontModel.addLoadedImport(URI);
        return read(new InputStreamReader(is), base);
    }

    public OWLSGroundingModel read(Reader reader, String base) throws NotInstanceOfWsdlGroundingException {
        OWLSGroundingModel gndModel = objectBuilder.createOWLSGroundingModel();
        gndModel.setOntModel(ontModel);
        ontModel.read(reader, base);
        WsdlGroundingList list = extractWsdlGrounding(ontModel);
        gndModel.setWsdlGroundingList(list);
        return gndModel;
    }

    public OWLSGroundingModel read(OntModel model) throws NotInstanceOfWsdlGroundingException {

        OWLSGroundingModel owlsModel = objectBuilder.createOWLSGroundingModel(model);
        ontModel = OWLSWriterUtils.getNewOntModel(model);
        WsdlGroundingList list = extractWsdlGrounding(ontModel);
        owlsModel.setWsdlGroundingList(list);
        return owlsModel;

    }

    public OWLSGroundingModel read(OntModel model, OWLSErrorHandler errHandler) {

        OWLSGroundingModel owlsModel = objectBuilder.createOWLSGroundingModel(model);
        ontModel = OWLSWriterUtils.getNewOntModel(model);
        WsdlGroundingList list = extractWsdlGrounding(ontModel, errHandler);
        owlsModel.setWsdlGroundingList(list);
        return owlsModel;

    }

    private WsdlGroundingList extractWsdlGrounding(OntModel ontModel) throws NotInstanceOfWsdlGroundingException {

        WsdlGroundingList wsdlGroundingList = storeBuilder.createWsdlGroundingList();

        OntClass wapgClass = ontModel.getOntClass(OWLSGroundingURI.WsdlGrounding);
        ExtendedIterator iter = wapgClass.listInstances();
        if (iter.hasNext() == false) {
            logger.debug("No WsdlGrounding found");
            return null;
        }
        WsdlGrounding wsdlGrounding = null;
        while (iter.hasNext()) {
            wsdlGrounding = objectBuilder.createWSDLGrounding((Individual) iter.next());
            wsdlGroundingList.add(wsdlGrounding);
        }
        return wsdlGroundingList;
    }

    public OWLSGroundingModel read(String URI, OWLSErrorHandler errHandler) {
        return read(URI, "", errHandler);
    }

    public OWLSGroundingModel read(Reader reader, OWLSErrorHandler errHandler) {
        return read(reader, "", errHandler);
    }

    public OWLSGroundingModel read(String URI, String base, OWLSErrorHandler errHandler) {

        URL url;
        try {
            url = new URL(URI);
        } catch (MalformedURLException e) {
            errHandler.fatalError(e);
            return null;
        }
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e1) {
            errHandler.fatalError(e1);
            return null;
        }
        ontModel.addLoadedImport(URI);
        return read(new InputStreamReader(is), base, errHandler);
    }

    public OWLSGroundingModel read(Reader reader, String base, OWLSErrorHandler errHandler) {
        OWLSGroundingModel gndModel = objectBuilder.createOWLSGroundingModel();
        gndModel.setOntModel(ontModel);
        ontModel.read(reader, base);
        WsdlGroundingList list = extractWsdlGrounding(ontModel, errHandler);
        gndModel.setWsdlGroundingList(list);
        return gndModel;
    }

    private WsdlGroundingList extractWsdlGrounding(OntModel ontModel, OWLSErrorHandler errHandler) {

        WsdlGroundingList wsdlGroundingList = storeBuilder.createWsdlGroundingList();

        OntClass wapgClass = ontModel.getOntClass(OWLSGroundingURI.WsdlGrounding);
        ExtendedIterator iter = wapgClass.listInstances();
        if (iter.hasNext() == false) {
            logger.debug("No WsdlGrounding found");
            return null;
        }
        WsdlGrounding wsdlGrounding = null;
        while (iter.hasNext()) {
            wsdlGrounding = objectBuilder.createWSDLGrounding((Individual) iter.next(), errHandler);
            wsdlGroundingList.add(wsdlGrounding);
        }
        return wsdlGroundingList;
    }

}