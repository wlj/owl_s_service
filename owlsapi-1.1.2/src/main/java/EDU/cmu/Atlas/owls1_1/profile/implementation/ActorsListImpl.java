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
package EDU.cmu.Atlas.owls1_1.profile.implementation;

import org.apache.log4j.Logger;

import EDU.cmu.Atlas.owls1_1.core.implementation.OWLS_StoreImpl;
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;

/**
 * @author Naveen Srinivasan
 *  
 */
public class ActorsListImpl extends OWLS_StoreImpl implements ActorsList {

    static Logger logger = Logger.getLogger(ActorsListImpl.class);

    /**
     * add an actor to the list
     * @param actor the actor to add
     */
    public void addActor(Actor actor) {
        add(actor);
    }

    /**
     * remove an actor from the list
     * @param actor the actor to remove
     */
    public boolean removeActor(Actor actor) {
        return remove(actor);
    }

    /**
     * get the actor at some position in the list
     * @param index the position of the actor in the list
     * @return the next actor or null if none
     */
    public Actor getActorAt(int index) {
        return (Actor) getNth(index);
    }

    public boolean removeActor(String uri) {
        return remove(uri);
    }

    public Actor getActor(String uri) {
        return (Actor) get(uri);
    }

    public Actor getNthActor(int index) {
        return (Actor) getNth(index);
    }

}