package com.nir.utils;

import com.google.common.collect.Lists;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static com.nir.utils.ExpressionUtils.vals;
import static com.nir.utils.ExpressionUtils.vars;
import static com.nir.utils.ExpressionUtils.varsAndVals;

public class Exp4JTest {

    @Test
    public void testForExp4J() {
        dataProvider().forEach((triple) -> {
            final String stringExpression = triple.getLeft();
            final ExpressionBuilder expressionBuilder = new ExpressionBuilder(stringExpression);

            final Map<String, Double> variablesAndValues = triple.getMiddle();
            final Set<String> variables = variablesAndValues.keySet();
            final Expression expression = expressionBuilder.variables(variables).build();
            expression.setVariables(variablesAndValues);
            final double actual = expression.evaluate();
            final double expected = triple.getRight();

            try {
                Assertions.assertEquals(expected, actual);
            } catch (Exception ignored) {
                throw ignored;
            }
        });
    }

    public ArrayList<Triple<String, Map<String, Double>, Double>> dataProvider() {
        final String expression = "1+(1-a)/(a*(3*a-2))";
        return Lists.newArrayList(
            Triple.of(expression, varsAndVals(vars("a"), vals(1.0)), 1.0),
            Triple.of(expression, varsAndVals(vars("a"), vals(2.0)), 0.875),
            Triple.of(expression, varsAndVals(vars("a"), vals(5.0)), 0.9384615384615385)
        );
    }
}
