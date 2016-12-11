package wsd.com.wsd.comunication.utils;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class UserPropertiesTest {

    private UserProperties userProperties;
    String propKey = "key";
    String propVal = "val";


    @Before
    public void setUp() throws Exception {
        userProperties = new UserProperties();
    }


    @Test
    public void getProperty() throws Exception {
        userProperties.addProperty(propKey, propVal);
        String res = userProperties.getProperty(propKey);
        assertEquals(propVal, res);
    }

}