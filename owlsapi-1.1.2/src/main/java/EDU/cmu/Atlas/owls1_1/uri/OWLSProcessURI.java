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
package EDU.cmu.Atlas.owls1_1.uri;

/**
 * @author Naveen Srinivasan
 */
public class OWLSProcessURI {

    public static final String base = "http://localhost:8080/juddiv3/owl-s/1.1/";

    // Process.owl
    public static final String process = base + "Process.owl";

    //Process

    public static final String Process = process + "#Process";

    public static final String hasParameter = process + "#hasParameter";

    public static final String hasInput = process + "#hasInput";

    public static final String hasOutput = process + "#hasOutput";

    public static final String hasLocal = process + "#hasLocal";

    public static final String hasPrecondition = process + "#hasPrecondition";

    public static final String hasResult = process + "#hasResult";

    public static final String Result = process + "#Result";

    public static final String hasResultVar = process + "#hasResultVar";

    public static final String inCondition = process + "#inCondition";

    public static final String processHasEffect = process + "#hasEffect";

    public static final String withOutput = process + "#withOutput";

    public static final String Input = process + "#Input";

    public static final String Local = process + "#Local";

    public static final String ResultVar = process + "#ResultVar";

    public static final String Output = process + "#Output";

    public static final String Parameter = process + "#Parameter";

    public static final String parameterType = process + "#parameterType";

    public static final String parameterValue = process + "#parameterValue";

    public static final String processName = process + "#name";

    public static final String invocable = process + "#invocable";

    //Atomic Process
    public static final String AtomicProcess = process + "#AtomicProcess";

    //Composite Process
    public static final String CompositeProcess = process + "#CompositeProcess";

    public static final String composedOf = process + "#composedOf";

    //ControlConstruct
    public static final String ControlConstruct = process + "#ControlConstruct";

    public static final String ControlConstructList = process + "#ControlConstructList";

    public static final String components = process + "#components";

    public static final String Sequence = process + "#Sequence";

    public static final String Split = process + "#Split";

    public static final String Split_JOIN = process + "#Split-Join";

    public static final String Unordered = process + "#Unordered";

    public static final String AnyOrder = process + "#Any-Order";

    public static final String Choice = process + "#Choice";

    public static final String Iterate = process + "#Iterate";

    public static final String If_Then_Else = process + "#If-Then-Else";

    public static final String ifCondition = process + "#ifCondition";

    public static final String then = process + "#then";

    public static final String process_else = process + "#else";

    //Simple Process
    public static final String SimpleProcess = process + "#SimpleProcess";

    public static final String collapsesTo = process + "#collapsesTo";

    public static final String expandsTo = process + "#expandsTo";

    public static final String realizedBy = process + "#realizedBy";

    public static final String realizes = process + "#realizes";

    //RDF List
    public static final String first = "http://www.w3.org/1999/02/22-rdf-syntax-ns#first";

    public static final String rest = "http://www.w3.org/1999/02/22-rdf-syntax-ns#rest";

    public static final String rdfType = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

    // shadow RDF list
    public static final String shadow_rdf = "http://localhost:8080/juddiv3/owl-s/1.1/ObjectList.owl";//http://www.daml.org/services/owl-s/1.1/generic/

    public static final String shadow_rdf_first = shadow_rdf + "#first";

    public static final String shadow_rdf_rest = shadow_rdf + "#rest";

    public static final String shadow_rdf_nil = shadow_rdf + "#nil";

    //Perform
    public static final String Perform = process + "#Perform";

    public static final String hasDataFrom = process + "#hasDataFrom";

    public static final String processPerformProperty = process + "#process";

    //Binding
    public static final String Binding = process + "#Binding";
    
    public static final String InputBinding = process + "#InputBinding";
    
    public static final String OutputBinding = process + "#OutputBinding";

    public static final String toParam = process + "#toParam";

    public static final String valueSource = process + "#valueSource";

    public static final String valueData = process + "#valueData";

    public static final String valueSpecifier = process + "#valueSpecifier";

    public static final String valueFunction = process + "#valueFunction";

    public static final String valueForm = process + "#valueForm";
    
    public static final String valueType = process + "#valueType";

    //ValueOf
    public static final String ValueOf = process + "#ValueOf";

    public static final String theVar = process + "#theVar";

    public static final String fromProcess = process + "#fromProcess";

    public static final String TheParentPerform = process + "#TheParentPerform";

    public static final String ThisPerform = process + "#ThisPerform";

    //	Condition and Expression
    public static final String expression = "http://localhost:8080/juddiv3/owl-s/1.1/Expression.owl";//http://www.daml.org/services/owl-s/1.1/generic/

    public static final String Expression = expression + "#Expression";

    public static final String Condition = expression + "#Condition";

    public static final String expressionBody = expression + "#expressionBody";

    public static final String expressionLanguage = expression + "#expressionLanguage";

    public static final String SWRL = expression + "#SWRL";

    public static final String KIF = expression + "#KIF";

    public static final String DRS = expression + "#DRS";

    public static final String LogicLanguage = expression + "#LogicLanguage";

    public static final String refURI = expression + "#refURI";
    
    // Exps
    public static final String Repeat_While = process + "#Repeat-While";
    
    public static final String untilcondition = process + "#untilCondition";
    
    public static final String untilprocess = process + "#untilProcess";
    
    public static final String whilecondition = process + "#whileCondition";
    
    public static final String whileprocess = process + "#whileProcess";
}