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
package EDU.cmu.Atlas.owls1_1.process;

/**
 * @author Massimo Paolucci
 * @author Roman Vaculin
 *
 * Class for parsing Perform statements.
 */
public interface Perform extends ControlConstruct {
	/**
	 * @return the current process
	 */
	public Process getProcess();

	/**
	 * @param process to record
	 */
	public void setProcess(Process process);
	

	/**
	 * @return the binding associated with the Perform
	 */
	public BindingList getBinding();

	/** @param binding to record */
	public void setBinding(BindingList binding);
	
	/**
	 * Returns the global ontology model that stores all OWL instances created
	 * during the execution. Model should be set by the execution environment
	 * appropriately. 
	 * This is important, e.g., for creating inputs values that are complex types.
	 * 
	 * @return the current process
	 */
//	public OntModel getGlobalServiceModel();

	
	/**
	 * Sets the reference to the global ontology model of the OWL-S exection 
	 * environment. Is usefull only during execution. 
	 * The execution environment is responsible for setting this reference 
	 * correctly.
	 */
//	public void setGlobalServiceModel(OntModel globalServiceModel);
	
}
