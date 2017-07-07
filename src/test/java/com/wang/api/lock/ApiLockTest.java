package com.wang.api.lock;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by paopao on 2017/7/8.
 */
public class ApiLockTest {

    @Test
    public void testSpel() {
        String session = "get('id')";
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(session);

        Map<String, Object> user = new HashMap<String, Object>() {{
            put("id", "123");
        }};
        assertEquals("123", expression.getValue(user));
    }
}
