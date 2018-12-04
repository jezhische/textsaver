package com.jezh.textsaver.hybernateType;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.MutabilityPlan;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.hibernate.usertype.DynamicParameterizedType;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

/**
 * https://vladmihalcea.com/how-to-map-java-and-sql-arrays-with-jpa-and-hibernate/
 * https://stackoverflow.com/questions/1647583/mapping-a-postgresql-array-with-hibernate
 *
 * @author Vlad Mihalcea
 */
public abstract class AbstractArrayTypeDescriptor<T> extends AbstractTypeDescriptor<T> implements DynamicParameterizedType {

    private Class<T> arrayObjectClass;

    @SuppressWarnings("unchecked")
    public AbstractArrayTypeDescriptor(Class<T> arrayObjectClass) {
        super(arrayObjectClass,
                (MutabilityPlan<T>) new MutableMutabilityPlan<Object>() {
                    @Override
                    protected Object deepCopyNotNull(Object value) {
                        return ArrayUtil.deepCopy(value);
                    }
                });
    }


    @SuppressWarnings("unchecked")
    @Override
    public void setParameterValues(Properties parameters) {
        arrayObjectClass = ((ParameterType) parameters.get(PARAMETER_TYPE)).getReturnedClass();
    }

    @Override
    public boolean areEqual(T one, T another) {
        if (one == another) return true;
        if (one == null || another == null) return false;
        return ArrayUtil.isEquals(one, another);
    }

    @Override
    public String toString(Object value) {
        return Arrays.deepToString(((Object[]) value));
    }

    @Override
    public T fromString(String string) {
        return ArrayUtil.fromString(string, arrayObjectClass);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X unwrap(T value, Class<X> type, WrapperOptions options) {
        return (X) ArrayUtil.wrapArray(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> T wrap(X value, WrapperOptions options) {
        if( value instanceof Array) {
            Array array = (Array) value;
            try {
                return ArrayUtil.unwrapArray(
                        (Object[]) array.getArray(),
                        arrayObjectClass
                );
            }
            catch (SQLException e) {
                throw new IllegalArgumentException( e );
            }
        }
        return (T) value;
    }

    protected abstract String getSqlArrayType();
}
