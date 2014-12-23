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
	 //获取OWLModel对象
     OWLModel owlModel = ProtegeOWL.createJenaOWLModel();
     //创建Person类
     OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
     
//     OWLIndividual personClass1 = personClass.createOWLIndividual("Person");
    
     //创建一个name的数据属性
     OWLDatatypeProperty nameProperty = owlModel.createOWLDatatypeProperty("name");
     //name数据属性的值域是xsd:String
     nameProperty.setRange(owlModel.getXSDstring());
     //设置name数据属性的值域为Person类
     nameProperty.setDomain(personClass);
    
     //创建数据属性age
     OWLDatatypeProperty ageProperty = owlModel.createOWLDatatypeProperty("age");
     //设置属性的值域是xsd:int
     ageProperty.setRange(owlModel.getXSDint());
     //设置属性的定义域为person
     ageProperty.setDomain(personClass);
    
     //创建对象属性children
     OWLObjectProperty childrenProperty = owlModel.createOWLObjectProperty("children");
     //设置对象属性的值域为person
     childrenProperty.setRange(personClass);
     //设置对象属性的定义域为person
     childrenProperty.setDomain(personClass);
     //创建person类的个体Darwin
     RDFIndividual darwin = personClass.createRDFIndividual("Darwin");
     //设置Darwin对象的name数据属性
     darwin.setPropertyValue(nameProperty, new String("Darwin"));
     //设置该个体的数据属性age的值为0
     darwin.setPropertyValue(ageProperty, new Integer(0));
     
     personClass.createAnonymousInstance();
     
     Collection instances = null;
     instances.add(-1);
     instances.add(0);
     instances.add(1);
     OWLEnumeratedClass Bool = owlModel.createOWLEnumeratedClass(instances);
     
     //创建person类的个体Holger
     RDFIndividual holgi = personClass.createRDFIndividual("Holger");
     //设置holgi个和darwin个体之间的对象属性值为children
     holgi.setPropertyValue(childrenProperty, darwin);
     //设置holgi个体的数据属性值为33
     holgi.setPropertyValue(ageProperty, new Integer(33));
     //设置holgi对象的name数据属性
     holgi.setPropertyValue(nameProperty, new String("holgi"));
    
     //创建类Brother，该类的父类默认是owl:Thing类
     OWLNamedClass brotherClass = owlModel.createOWLNamedClass("Brother");
     //设置Brother类的父类为Person类
     brotherClass.addSuperclass(personClass);
     //移除Brother默认的父类owl:Thing
     brotherClass.removeSuperclass(owlModel.getOWLThingClass());
     //另外一种创建Person子类的方法
     OWLNamedClass sisterClass = owlModel.createOWLNamedSubclass("Sister", personClass);
    
     //创建Brother类的实例Hans
     OWLIndividual hans = brotherClass.createOWLIndividual("Hans");
     //获取Brother的实例对象集合,参数false说明只获取直接是类
     Collection brothers = brotherClass.getInstances(false);
     //判断Brother的实例集合中是否包括Hans这个实例
     System.out.println("判断Brother是否包含hans实例："+brothers.contains(hans));
     //计算Brother类的实例个数
     System.out.println("Brother类的直接对象数目："+brothers.size());
    
     //获取Person类的所有直接和间接对象
     Collection persons=personClass.getInstances(true);
     //打印Person类对象的个数
     System.out.println("Person类的间接和直接类的总数目："+persons.size());
     //判断Person类对象的实例是否包含holgi
     System.out.println("判断Person类是否包含holgi实例："+persons.contains(holgi));
     //获取hans对象的类，然后判断是否跟Brother类相等
     System.out.println("判断hans的类型是否为Brother类："+hans.getRDFType().equals(brotherClass));
     //判断hans是否为Brother类的对象
     System.out.println("判断hans是否为Brother类:"+hans.hasRDFType(brotherClass));
     //判断hans是否为Person类的直接实例
     System.out.println("判断hans是否为Person类的直接实例:"+hans.hasRDFType(personClass, false));
     //判断hans是否为Person类的间接实例
     System.out.println("判断hans是否为Person类的间接实例:"+hans.hasRDFType(personClass, true));
     //获取Drawin实例的name属性值
     String nameValue = (String)darwin.getPropertyValue(nameProperty);
     System.out.println("Drawin是实例的name属性值："+nameValue);
     System.out.println("Drawin是实例的age属性值："+(Integer)darwin.getPropertyValue(ageProperty));
    
     //其它数据类型被封装成一个RDFSLiteral
     //获取xsd:date的数据类型
     RDFSDatatype xsdDate = owlModel.getRDFSDatatypeByName("xsd:date");
     //以xsd:date数据类型作为值域创建日期的数据属性
     OWLDatatypeProperty dateProperty = owlModel.createOWLDatatypeProperty("dateProperty", xsdDate);
     //创建
     RDFSLiteral dateLiteral = owlModel.createRDFSLiteral("2010-09-19", xsdDate);
     //用RDFSLiteral对象设置hans实例的日期属性值
     hans.setPropertyValue(dateProperty, dateLiteral);
     //从hans实例中获取RDFSLiteral对象
     RDFSLiteral myDate = (RDFSLiteral) hans.getPropertyValue(dateProperty);
     System.out.println("Date: " + myDate);
    
     //删除hans实例
     hans.delete();
     System.out.println("\n\n本体的类结构层次：");
     //打印所有类的层次关系
     printClassTree(personClass, "");
 }
 
 //递归调用答应所有类的层次关系,indentation参数是使的打印的子类比其父类更缩进
 private static void printClassTree(RDFSClass cls, String indentation)
 {
        System.out.println(indentation + cls.getName());
        //遍历并打印子类
        for (Iterator it = cls.getSubclasses(false).iterator(); it.hasNext();)
        {
            RDFSClass subclass = (RDFSClass) it.next();
            printClassTree(subclass, indentation + "    ");
        }
    }
}