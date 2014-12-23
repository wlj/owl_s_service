package edu.pku.ss.oes.evolution;

import javax.jws.WebService;

@WebService(targetNamespace = "http://evolution.oes.ss.pku.edu/")
public interface IBaseAction {
	public boolean addSuberClass(String name, String superClassName);

	public boolean removeSuberClass(String name, String superClassName);

	public boolean addObjectProperty(String name);

	public boolean removeObjectProperty(String name);

	public boolean addDataProperty(String name);

	public boolean removeDataProperty(String name);

	public boolean addOWLNamedIndividual(String owlClassName,
			String owlIndividualName);

	public boolean removeOWLNamedIndividual(String owlClassName,
			String owlIndividualName);

	public boolean addOwlClass(String name);

	public boolean removeOwlClass(String name);

	public boolean addSuberObjectProperty(String name, String superName);

	public boolean addSuberDataProperty(String name, String superName);

	public boolean removeSuberObjectProperty(String name, String superName);

	public boolean removeSuberDataProperty(String name, String superName);

	public void convertOwl() throws Exception;

	public void addObjectPropetyRelation(String individualA,
			String individualB, String objectProperty, int expireSeconds);

	public void setGloableParameterValue(int value);
	
	public void conflictTrace(String swrl);
}
