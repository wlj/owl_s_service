/*
 * Created on Dec 13, 2003
 */
package EDU.cmu.Atlas.owls1_1.expression;

import EDU.cmu.Atlas.owls1_1.core.OWLS_Store;
import EDU.cmu.Atlas.owls1_1.expression.Condition;

/**
 * @author nsrini1
 */
public interface ConditionList extends OWLS_Store {

    /** a vector that records the names of the parameterDescriptions recorded */
    public void addCondition(Condition condition);

    /**
     * add a ParameterDescription to the ParameterDescriptionsTable
     * @param parameterDescription the parameterDescription to add
     */
    public boolean removeCondition(Condition condition);

    /**
     * function that gives access to profiles sequentially
     * @param index the index of the profile to look at
     * @return the profile found
     * @throws IndexOutOfRangeException when index does not point to a Profile in the DB
     */
    public Condition getNthCondition(int index) throws IndexOutOfBoundsException;

    public boolean removeCondition(String uri);

    public Condition getCondition(String uri);

}