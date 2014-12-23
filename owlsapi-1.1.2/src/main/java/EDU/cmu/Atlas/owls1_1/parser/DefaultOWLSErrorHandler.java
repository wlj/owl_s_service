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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Default implementation of OWLSErrorHandler. It collects all the warnings, errors and fatal errors.
 * @author Naveen Srinivasan
 */
public class DefaultOWLSErrorHandler implements OWLSErrorHandler {

	static Logger logger = Logger.getLogger(DefaultOWLSErrorHandler.class);
	
    private ArrayList warnings;

    private ArrayList errors;

    private ArrayList fatalErrors;

    public DefaultOWLSErrorHandler() {
        warnings = new ArrayList();
        errors = new ArrayList();
        fatalErrors = new ArrayList();
    }

    public void error(Exception e) {
        errors.add(e);
        logger.error("Error :" + e.getMessage());
    }

    public void error(Exception[] es) {
        for (int i = 0; i < es.length; i++) {
            errors.add(es[i]);
            logger.error("Error :" + es[i].getMessage());
        }
    }

    public void fatalError(Exception e) {
        fatalErrors.add(e);
        logger.fatal("Fatal Error :" + e.getMessage());
    }

    public void fatalError(Exception[] es) {
        for (int i = 0; i < es.length; i++) {
            fatalErrors.add(es[i]);
            logger.fatal("Error :" + es[i].getMessage());
        }
    }

    public void warning(Exception e) {
        warnings.add(e);
        logger.warn("Warning :" + e.getMessage());
    }

    public void warning(Exception[] es) {
        for (int i = 0; i < es.length; i++) {
            warnings.add(es[i]);
            logger.warn("Error :" + es[i].getMessage());
        }
    }

    public Exception[] getWarnings() {
        Exception[] ex = new Exception[warnings.size()];
        for (int i = 0; i < warnings.size(); i++)
            ex[i] = (Exception) warnings.get(i);
        return ex;
    }

    public Exception[] getErrors() {
        Exception[] ex = new Exception[errors.size()];
        for (int i = 0; i < errors.size(); i++)
            ex[i] = (Exception) errors.get(i);
        return ex;
    }

    public Exception[] getFatalErrors() {
        Exception[] ex = new Exception[fatalErrors.size()];
        for (int i = 0; i < fatalErrors.size(); i++)
            ex[i] = (Exception) fatalErrors.get(i);
        return ex;
    }

    public String toString() {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.print("\n========WARNINGS=========");
        Exception[] warning = getWarnings();
        for (int i = 0; i < warning.length; i++) {
            pw.println();
            warning[i].printStackTrace(pw);
        }

        pw.print("\n========ERRORS=========");
        Exception[] errors = getErrors();
        for (int i = 0; i < errors.length; i++) {
            pw.println();
            errors[i].printStackTrace(pw);
        }

        pw.print("\n========FATAL ERRORS=========");
        Exception[] fatalError = getFatalErrors();
        for (int i = 0; i < fatalError.length; i++) {
            pw.println();
            fatalError[i].printStackTrace(pw);
        }
        pw.flush();
        pw.close();
        return sw.getBuffer().toString();
    }
}