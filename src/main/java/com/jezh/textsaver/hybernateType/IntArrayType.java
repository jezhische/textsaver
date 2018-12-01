package com.jezh.textsaver.hybernateType;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

/**
 * https://stackoverflow.com/questions/48017960/save-large-objects-to-postgresql-db-using-hibernate-jpa
 * @author Vlad Mihalcea
 */
public class IntArrayType extends AbstractSingleColumnStandardBasicType<int[]> implements DynamicParameterizedType{

//    public IntArrayType(SqlTypeDescriptor sqlTypeDescriptor, JavaTypeDescriptor<int[]> javaTypeDescriptor) {
//        super(sqlTypeDescriptor, javaTypeDescriptor);
//    }

    public IntArrayType() {
        super(
                ArraySqlTypeDescriptor.INSTANCE,
                IntArrayTypeDescriptor.INSTANCE
        );
    }

    // AbstractSingleColumnStandardBasicType method
    @Override
    public String getName() {
        return "int-array";
    }

    // AbstractSingleColumnStandardBasicType method
    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    // DynamicParameterizedType method
    @Override
    public void setParameterValues(Properties parameters) {
        // AbstractSingleColumnStandardBasicType method
        ((IntArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}
