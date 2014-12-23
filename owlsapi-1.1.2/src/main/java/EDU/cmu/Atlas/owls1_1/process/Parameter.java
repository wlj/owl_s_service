/*
 * OWL-S API provides functionalities to create and to manipulate OWL-S files. Copyright (C) 2005
 * Katia Sycara, Softagents Lab, Carnegie Mellon University
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
package EDU.cmu.Atlas.owls1_1.process;

import EDU.cmu.Atlas.owls1_1.exception.OWLSParameterNotIndividual;


/**
 * @author Naveen Srinivasan
 * @author Roman Vaculin
 */
public interface Parameter extends Variable {

    public String getParameterType();

    public void setParameterType(String parameterType);

    
    
    /**
     * Returns the value of parameter as String. If the paramater value is an instance
     * of OWL Individual it returns it RDF XML representation. 
     * 
     * @return
     */
    public String getParameterValue();
    
    /**
     * Return the parameter value as OWL Individual.
     * 
     * @return 
     * @throws OWLSParameterNotIndividual
     */
//    public Individual getParameterValueAsIndividual() throws OWLSParameterNotIndividual;
//    
//    public String getParameterValueAsString();

    
    public void setParameterValue(String parameterValue);
    
//    public void setParameterValue(Individual parameterValue);

    
    
    public boolean isInput();

    public boolean isOutput();
    
    
    
    
//    public boolean isValueIndividual();
//    
//    public boolean isValueLiteral();

}