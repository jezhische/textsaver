package com.jezh.textsaver.hybernateType;

/**
 * https://vladmihalcea.com/how-to-map-java-and-sql-arrays-with-jpa-and-hibernate/
 * https://stackoverflow.com/questions/1647583/mapping-a-postgresql-array-with-hibernate
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
