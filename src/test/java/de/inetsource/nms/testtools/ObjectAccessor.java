package de.inetsource.nms.testtools;

import java.lang.reflect.Field;

public class ObjectAccessor {

    /**
     * Set a property on the given object, even if the field is not public.
     *
     * @param classWithField
     * @param fieldName
     * @param newValue
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setNotPublicField(Object classWithField, String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        Class<?> c = classWithField.getClass();
        Field f = c.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(classWithField, newValue);
    }

}
