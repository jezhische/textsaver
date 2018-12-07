package com.jezh.textsaver.hybernateType;

import com.jezh.textsaver.dto.BookmarkDef;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

/**
 * https://vladmihalcea.com/how-to-map-java-and-sql-arrays-with-jpa-and-hibernate/
 * https://stackoverflow.com/questions/1647583/mapping-a-postgresql-array-with-hibernate
 * @author Vlad Mihalcea
 * @author Ivan
 */
public class BookmarkDefArrayType extends AbstractSingleColumnStandardBasicType<BookmarkDef[]> implements DynamicParameterizedType{

    public BookmarkDefArrayType() {
        super(
                ArraySqlTypeDescriptor.INSTANCE,
                BookmarkDefArrayTypeDescriptor.INSTANCE
        );
    }

    // AbstractSingleColumnStandardBasicType method
    @Override
    public String getName() {
        return "bookmark-def-array";
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
        ((BookmarkDefArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}
