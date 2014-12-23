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
package EDU.cmu.Atlas.owls1_1.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.core.OWLS_Object;
import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;
import EDU.cmu.Atlas.owls1_1.exception.OWLS_Store_Exception;
import EDU.cmu.Atlas.owls1_1.parser.OWLSErrorHandler;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * OWLS_StoreUtil is used to generate a list of OWLS_Object from properties of a given individual.
 * @author Naveen Srinivasan
 */
public class OWLS_StoreUtil {

    static Logger logger = Logger.getLogger(OWLS_StoreUtil.class);

    public static Object extractOWLS_Store(Individual individual, Property property, String className, String listClassName)
            throws OWLS_Store_Exception {

        try {
            logger.debug("Looking for property " + property + " in instance " + individual.getURI());
            Class listClassN = OWLS_StoreUtil.class.getClassLoader().loadClass(listClassName);

            Class classN = OWLS_StoreUtil.class.getClassLoader().loadClass(className);
            Constructor constructor = classN.getConstructor(new Class[] { Individual.class });

            OWLS_Store listObject = (OWLS_Store) listClassN.newInstance();

            StmtIterator propStatementIterator = individual.listProperties(property);

            if (propStatementIterator.hasNext())
                logger.debug("Found property of type " + property);
            else
                logger.debug("Didnot Find property of type " + property);

            while (propStatementIterator.hasNext()) {

                Statement outputStatement = propStatementIterator.nextStatement();
                RDFNode tmpRDFNode = outputStatement.getObject();
                if (tmpRDFNode.canAs(Individual.class)) {
                    Individual paramInst = (Individual) tmpRDFNode.as(Individual.class);
                    //logger.debug("Found instance " + paramInst.getURI());
                    Object obj = constructor.newInstance(new Object[] { paramInst });

                    //logger.debug(listObject.getClass().getName());
                    listObject.add((OWLS_Object) obj);

                } else
                	logger.debug("Node " + tmpRDFNode.toString() + " cannot be casted as individual");
            }
            return listObject;
        } catch (Exception e) {
            throw new OWLS_Store_Exception(e);
        }

    }

    public static Object extractOWLS_Store_UsingBuilder(Individual individual, Property property, String methodName, String listClassName)
            throws OWLS_Store_Exception {

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();
        //looking for method in builder with methodName
        Method method;
        try {
            method = builder.getClass().getMethod(methodName, new Class[] { Individual.class });
        } catch (SecurityException e1) {
            throw new OWLS_Store_Exception(e1);
        } catch (NoSuchMethodException e1) {
            throw new OWLS_Store_Exception(e1);
        }

        //Looking for list class with name className
        Class listClass;
        try {
            listClass = OWLS_StoreUtil.class.getClassLoader().loadClass(listClassName);
        } catch (ClassNotFoundException e) {
            throw new OWLS_Store_Exception(listClassName, e);
        }

        //Creating an instance of list class
        OWLS_Store listObject;
        try {
            listObject = (OWLS_Store) listClass.newInstance();
        } catch (InstantiationException e2) {
            throw new OWLS_Store_Exception(e2);
        } catch (IllegalAccessException e2) {
            throw new OWLS_Store_Exception(e2);
        }

        logger.debug("Looking for property " + property + " in instance " + individual.getURI());
        StmtIterator propStatementIterator = individual.listProperties(property);
        if (propStatementIterator.hasNext())
            logger.debug("Found property of type " + property);
        else
            logger.debug("Didnot Find property of type " + property);

        //Creating OWL-S objects for each property using OWLS_Object_Builder
        //and adding it to the OWLS_Store object
        while (propStatementIterator.hasNext()) {

            Statement outputStatement = propStatementIterator.nextStatement();
            RDFNode tmpRDFNode = outputStatement.getObject();
            if (tmpRDFNode.canAs(Individual.class)) {
                Individual paramInst = (Individual) tmpRDFNode.as(Individual.class);
                Object obj;
                try {
                	//TODO:在此处报异常
                    obj = method.invoke(builder, new Object[] { paramInst });
                } catch (Exception e3) {
                    throw new OWLS_Store_Exception(e3);
                }

                listObject.add((OWLS_Object) obj);

            } else
            	logger.debug("Node " + tmpRDFNode.toString() + " cannot be casted as individual");
        }
        return listObject;

    }

    public static Object extractOWLS_Store_UsingBuilder(Individual individual, Property property, String methodName, String listClassName,
            OWLSErrorHandler errorHandler) {

        OWLS_Object_Builder builder = OWLS_Object_BuilderFactory.instance();

        //Looking for a method in OWLS_Object_Builder with the given
        // methodName.
        //First check if a method with OWLSErrorHandler is present if not look
        // for
        //method without OWLSErrorHandler.
        Method method = null;
        boolean builtInErrHandler = false;
        try {
            method = builder.getClass().getMethod(methodName, new Class[] { Individual.class, OWLSErrorHandler.class });
            builtInErrHandler = true;
            logger.debug("Found a method :" + methodName + " with built in OWLSErrorHandler");
        } catch (SecurityException e4) {
            errorHandler.error(e4);
            return null;
        } catch (NoSuchMethodException e4) {
            logger.debug("Didnot find a method :" + methodName + " with built in OWLSErrorHandler");
        }
        //looking for method without errorhandler
        if (builtInErrHandler == false) {
            try {
                method = builder.getClass().getMethod(methodName, new Class[] { Individual.class });
            } catch (SecurityException e1) {
                errorHandler.error(e1);
                return null;
            } catch (NoSuchMethodException e1) {
                errorHandler.error(e1);
                return null;
            }
        }

        //looking for list with the given listClassName
        logger.debug("Looking for property " + property + " in instance " + individual.getURI());
        Class listClass;
        try {
            listClass = OWLS_StoreUtil.class.getClassLoader().loadClass(listClassName);
        } catch (ClassNotFoundException e) {
            errorHandler.error(e);
            return null;
        }
        //Creating an instance of the listClassName
        OWLS_Store listObject;
        try {
            listObject = (OWLS_Store) listClass.newInstance();
        } catch (InstantiationException e2) {
            errorHandler.error(e2);
            return null;
        } catch (IllegalAccessException e2) {
            errorHandler.error(e2);
            return null;
        }

        //List the properties of the individual that has to converted into a
        // OWLS_Store
        StmtIterator propStatementIterator = individual.listProperties(property);

        if (propStatementIterator.hasNext())
            logger.debug("Found property of type " + property);
        else
            logger.debug("Didnot Find property of type " + property);

        //For each property found we convert it into corresponding OWLS Object
        // using the
        //OWLS_Object_Builder and add it to the OWLS_Store object
        while (propStatementIterator.hasNext()) {

            Statement outputStatement = propStatementIterator.nextStatement();
            RDFNode tmpRDFNode = outputStatement.getObject();
            if (tmpRDFNode.canAs(Individual.class)) {
                Individual paramInst = (Individual) tmpRDFNode.as(Individual.class);
                Object obj;
                try {
                    if (builtInErrHandler == true)
                        obj = method.invoke(builder, new Object[] { paramInst, errorHandler });
                    else
                        obj = method.invoke(builder, new Object[] { paramInst });
                } catch (Exception e3) {
                    errorHandler.error(e3);
                    continue;
                }
                listObject.add((OWLS_Object) obj);

            } else
            	logger.warn("Node " + tmpRDFNode.toString() + " cannot be casted as individual");
        }
        return listObject;

    }




}