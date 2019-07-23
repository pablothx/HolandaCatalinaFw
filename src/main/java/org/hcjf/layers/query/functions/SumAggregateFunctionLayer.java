package org.hcjf.layers.query.functions;

import org.hcjf.errors.HCJFRuntimeException;
import org.hcjf.layers.query.Query;
import org.hcjf.utils.Introspection;

import java.util.Collection;
import java.util.Map;

public class SumAggregateFunctionLayer extends BaseQueryAggregateFunctionLayer implements NumberSetFunction {

    public static final String NAME = "aggregateSum";

    public SumAggregateFunctionLayer() {
        super(NAME);
    }

    @Override
    public Collection evaluate(String alias, Collection resultSet, Object... parameters) {
        Collection result = resultSet;
        if(parameters.length >= 1) {
            try {
                Query.QueryReturnField queryReturnField = (Query.QueryReturnField) parameters[0];
                Number accumulatedValue;
                for(Object row : resultSet) {
                    accumulatedValue = 0;
                    accumulatedValue = accumulateFunction(accumulatedValue, new Object[]{queryReturnField.resolve(row)}, (A,V)->A.add(V))[1];
                    ((Map)row).put(alias, accumulatedValue);
                }
            } catch (Exception ex){
                throw new HCJFRuntimeException("Sum aggregate function fail", ex);
            }
        } else {
            throw new HCJFRuntimeException("Sum aggregate function need at leas one parameter");
        }
        return result;
    }
}
