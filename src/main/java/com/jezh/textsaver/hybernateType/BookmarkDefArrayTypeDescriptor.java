package com.jezh.textsaver.hybernateType;

import com.jezh.textsaver.entity.BookmarkDef;

/**
 * https://vladmihalcea.com/how-to-map-java-and-sql-arrays-with-jpa-and-hibernate/
 * https://stackoverflow.com/questions/1647583/mapping-a-postgresql-array-with-hibernate
 * @author Vlad Mihalcea
 * @author Ivan
 */
public class BookmarkDefArrayTypeDescriptor extends AbstractArrayTypeDescriptor<BookmarkDef[]> {

    public static final BookmarkDefArrayTypeDescriptor INSTANCE =
            new BookmarkDefArrayTypeDescriptor();

    public BookmarkDefArrayTypeDescriptor() {
        super( BookmarkDef[].class );
    }

    @Override
    protected String getSqlArrayType() {
        return "bookmark_def";
    }
}
