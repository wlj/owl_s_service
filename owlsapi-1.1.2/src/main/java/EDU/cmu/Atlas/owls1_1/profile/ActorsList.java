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
package EDU.cmu.Atlas.owls1_1.profile;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;
/**
 * @author Naveen Srinivasan
 */
public interface ActorsList extends OWLS_Store {

    /**
     * This interface is implemented to store the list of actors referenced by a
     * profile This interface is based on the class Vector
     */

    /**
     * add an actor to the list
     * @param actor the actor to add
     */
    public void addActor(Actor actor);

    /**
     * remove an actor from the list
     * @param actor the actor to remove
     */
    public boolean removeActor(Actor actor);

    /**
     * remove an actor from the list
     * @param actor the actor to remove
     */
    public boolean removeActor(String uri);

    /**
     * get an actor with the give uri
     * @param actor the actor to remove
     */
    public Actor getActor(String uri);

    /**
     * get the actor at some position in the list
     * @param index the position of the actor in the list
     * @return the next actor or null if none
     */
    public Actor getNthActor(int index);

}

