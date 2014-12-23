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
package EDU.cmu.Atlas.owls1_1.builder;

/**
 * OWLS Builder Util used by OWL-S Builder to build OWL-S object
 * @author Naveen Srinivasan
 */
public interface OWLS_Builder_Util {

    public static int INPUT = 0;

    public static int OUTPUT = 1;

    public static int LOCAL = 2;

    public static int RESULT_VAR = 3;

    public static int ATOMIC = 4;

    public static int COMPOSITE = 5;

    public static int SIMPLE = 6;

    public static int SEQUENCE = 7;

    public static int CHOICE = 8;

    public static int SPLIT = 9;

    public static int IFTHENELSE = 10;

    public static int SPLIT_JOIN = 11;
    
    public static int ANY_ORDER = 12;
    
    public static int SERVICE = 13;
    
    public static int PROFILE = 14;

    public static int GROUNDING = 15;
    
    public static int PROVIDER = 16;
    
    public static int ATOMIC_MULTIPARTY = 17;
    
    public static int REMOTE = 18;
    
    public static int MESSAGE_TYPE = 19;
    
    public static int RESULT = 20;
    
    public static int BINDING = 21;
    
    public static int INPUT_BINDING = 22;
    
    public static int OUTPUT_BINDING = 23;
}