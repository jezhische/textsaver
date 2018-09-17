package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.TextPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// The @RepositoryRestResource annotation is optional and is used to customize the REST endpoint. If we decided
// to omit it, Spring would automatically create an endpoint at “/textParts” instead of “/text-parts“.
//@RepositoryRestResource(collectionResourceRel = "text-parts", path = "text-parts")
@Repository
public interface TextPartRepository extends JpaRepository<TextPart, Long> {
    Optional<TextPart> findByPreviousItem(Long previousItem);

    List<TextPart> findByTextCommonDataName(String textCommonDataName);

//    @Query("select tp from TextPart tp where tp.previousItem = ?1")
//    Optional<TextPart> findNextByCurrentInSequence(Long nextItem);

//    @Query("select tp from TextPart tp where tp.previousItem = (select tp.nextItem from tp where tp.nextItem = ?1)")
//    Optional<TextPart> findNextByCurrentInSequence(Long nextItem);

    @Query("select tp from TextPart tp where tp.previousItem = (select p.nextItem from TextPart p where p = ?1)")
    Optional<TextPart> findNextByCurrentInSequence(TextPart current);

    @Query("select tp from TextPart tp where tp.nextItem = (select p.previousItem from TextPart p where p = ?1)")
    Optional<TextPart> findPreviousByCurrentInSequence(TextPart current);

////    "CREATE UNIQUE INDEX IF NOT EXISTS idx_prev_it ON public.text_parts USING btree(previous_item)"
//    @Query(value = "CREATE INDEX IF NOT EXISTS idx_prev_it ON public.text_parts(previous_item)", nativeQuery = true)
//    void indexPreviousItems();
////
//    @Query(value = "DROP INDEX IF EXISTS idx_prev_it", nativeQuery = true)
//    void dropIndexPreviousItems();

    List<TextPart> findByTextCommonDataId(Long textCommonDataId);
}
