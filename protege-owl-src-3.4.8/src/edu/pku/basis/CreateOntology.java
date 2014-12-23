package edu.pku.basis;

import java.util.Collection;
import java.util.Iterator;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;

public class CreateOntology
{
 public static void main(String []args) throws OntologyLoadException
 {
	 //��ȡOWLModel����
     OWLModel owlModel = ProtegeOWL.createJenaOWLModel();
     //����Person��
     OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
     
//     OWLIndividual personClass1 = personClass.createOWLIndividual("Person");
    
     //����һ��name����������
     OWLDatatypeProperty nameProperty = owlModel.createOWLDatatypeProperty("name");
     //name�������Ե�ֵ����xsd:String
     nameProperty.setRange(owlModel.getXSDstring());
     //����name�������Ե�ֵ��ΪPerson��
     nameProperty.setDomain(personClass);
    
     //������������age
     OWLDatatypeProperty ageProperty = owlModel.createOWLDatatypeProperty("age");
     //�������Ե�ֵ����xsd:int
     ageProperty.setRange(owlModel.getXSDint());
     //�������ԵĶ�����Ϊperson
     ageProperty.setDomain(personClass);
    
     //������������children
     OWLObjectProperty childrenProperty = owlModel.createOWLObjectProperty("children");
     //���ö������Ե�ֵ��Ϊperson
     childrenProperty.setRange(personClass);
     //���ö������ԵĶ�����Ϊperson
     childrenProperty.setDomain(personClass);
     //����person��ĸ���Darwin
     RDFIndividual darwin = personClass.createRDFIndividual("Darwin");
     //����Darwin�����name��������
     darwin.setPropertyValue(nameProperty, new String("Darwin"));
     //���øø������������age��ֵΪ0
     darwin.setPropertyValue(ageProperty, new Integer(0));
     
     personClass.createAnonymousInstance();
     
     Collection instances = null;
     instances.add(-1);
     instances.add(0);
     instances.add(1);
     OWLEnumeratedClass Bool = owlModel.createOWLEnumeratedClass(instances);
     
     //����person��ĸ���Holger
     RDFIndividual holgi = personClass.createRDFIndividual("Holger");
     //����holgi����darwin����֮��Ķ�������ֵΪchildren
     holgi.setPropertyValue(childrenProperty, darwin);
     //����holgi�������������ֵΪ33
     holgi.setPropertyValue(ageProperty, new Integer(33));
     //����holgi�����name��������
     holgi.setPropertyValue(nameProperty, new String("holgi"));
    
     //������Brother������ĸ���Ĭ����owl:Thing��
     OWLNamedClass brotherClass = owlModel.createOWLNamedClass("Brother");
     //����Brother��ĸ���ΪPerson��
     brotherClass.addSuperclass(personClass);
     //�Ƴ�BrotherĬ�ϵĸ���owl:Thing
     brotherClass.removeSuperclass(owlModel.getOWLThingClass());
     //����һ�ִ���Person����ķ���
     OWLNamedClass sisterClass = owlModel.createOWLNamedSubclass("Sister", personClass);
    
     //����Brother���ʵ��Hans
     OWLIndividual hans = brotherClass.createOWLIndividual("Hans");
     //��ȡBrother��ʵ�����󼯺�,����false˵��ֻ��ȡֱ������
     Collection brothers = brotherClass.getInstances(false);
     //�ж�Brother��ʵ���������Ƿ����Hans���ʵ��
     System.out.println("�ж�Brother�Ƿ����hansʵ����"+brothers.contains(hans));
     //����Brother���ʵ������
     System.out.println("Brother���ֱ�Ӷ�����Ŀ��"+brothers.size());
    
     //��ȡPerson�������ֱ�Ӻͼ�Ӷ���
     Collection persons=personClass.getInstances(true);
     //��ӡPerson�����ĸ���
     System.out.println("Person��ļ�Ӻ�ֱ���������Ŀ��"+persons.size());
     //�ж�Person������ʵ���Ƿ����holgi
     System.out.println("�ж�Person���Ƿ����holgiʵ����"+persons.contains(holgi));
     //��ȡhans������࣬Ȼ���ж��Ƿ��Brother�����
     System.out.println("�ж�hans�������Ƿ�ΪBrother�ࣺ"+hans.getRDFType().equals(brotherClass));
     //�ж�hans�Ƿ�ΪBrother��Ķ���
     System.out.println("�ж�hans�Ƿ�ΪBrother��:"+hans.hasRDFType(brotherClass));
     //�ж�hans�Ƿ�ΪPerson���ֱ��ʵ��
     System.out.println("�ж�hans�Ƿ�ΪPerson���ֱ��ʵ��:"+hans.hasRDFType(personClass, false));
     //�ж�hans�Ƿ�ΪPerson��ļ��ʵ��
     System.out.println("�ж�hans�Ƿ�ΪPerson��ļ��ʵ��:"+hans.hasRDFType(personClass, true));
     //��ȡDrawinʵ����name����ֵ
     String nameValue = (String)darwin.getPropertyValue(nameProperty);
     System.out.println("Drawin��ʵ����name����ֵ��"+nameValue);
     System.out.println("Drawin��ʵ����age����ֵ��"+(Integer)darwin.getPropertyValue(ageProperty));
    
     //�����������ͱ���װ��һ��RDFSLiteral
     //��ȡxsd:date����������
     RDFSDatatype xsdDate = owlModel.getRDFSDatatypeByName("xsd:date");
     //��xsd:date����������Ϊֵ�򴴽����ڵ���������
     OWLDatatypeProperty dateProperty = owlModel.createOWLDatatypeProperty("dateProperty", xsdDate);
     //����
     RDFSLiteral dateLiteral = owlModel.createRDFSLiteral("2010-09-19", xsdDate);
     //��RDFSLiteral��������hansʵ������������ֵ
     hans.setPropertyValue(dateProperty, dateLiteral);
     //��hansʵ���л�ȡRDFSLiteral����
     RDFSLiteral myDate = (RDFSLiteral) hans.getPropertyValue(dateProperty);
     System.out.println("Date: " + myDate);
    
     //ɾ��hansʵ��
     hans.delete();
     System.out.println("\n\n�������ṹ��Σ�");
     //��ӡ������Ĳ�ι�ϵ
     printClassTree(personClass, "");
 }
 
 //�ݹ���ô�Ӧ������Ĳ�ι�ϵ,indentation������ʹ�Ĵ�ӡ��������丸�������
 private static void printClassTree(RDFSClass cls, String indentation)
 {
        System.out.println(indentation + cls.getName());
        //��������ӡ����
        for (Iterator it = cls.getSubclasses(false).iterator(); it.hasNext();)
        {
            RDFSClass subclass = (RDFSClass) it.next();
            printClassTree(subclass, indentation + "    ");
        }
    }
}