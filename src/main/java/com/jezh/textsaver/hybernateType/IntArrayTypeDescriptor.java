package com.jezh.textsaver.hybernateType;

/**
 * https://stackoverflow.com/questions/48017960/save-large-objects-to-postgresql-db-using-hibernate-jpa
 * @author Vlad Mihalcea
 */
public class IntArrayTypeDescriptor extends AbstractArrayTypeDescriptor<int[]> {

    public static final IntArrayTypeDescriptor INSTANCE =
            new IntArrayTypeDescriptor();

    public IntArrayTypeDescriptor() {
        super( int[].class );
    }

    @Override
    protected String getSqlArrayType() {
        return "integer";
    }
}
