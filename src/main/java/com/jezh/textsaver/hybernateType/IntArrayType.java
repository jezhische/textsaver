package com.jezh.textsaver.hybernateType;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

/**
 * https://vladmihalcea.com/how-to-map-java-and-sql-arrays-with-jpa-and-hibernate/
 * https://stackoverflow.com/questions/1647583/mapping-a-postgresql-array-with-hibernate
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
