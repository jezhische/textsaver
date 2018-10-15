package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.TextPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("select tp from TextPart tp where tp.previousItem = (select p.nextItem from TextPart p where p = ?1)")
    Optional<TextPart> findNextByCurrentInSequence(TextPart current);

    @Query("select tp from TextPart tp where tp.nextItem = (select p.previousItem from TextPart p where p = ?1)")
    Optional<TextPart> findPreviousByCurrentInSequence(TextPart current);

    /** find all the textPart with the given textCommonData id in an order, where textPart.nextItem = nextTextPart.id */
    @Query(value = "SELECT * FROM public.get_all_texparts_ordered_set(?1)", nativeQuery = true)
    List<TextPart> findSortedSetByTextCommonDataId(Long textCommonDataId);


    /** find a list of textPart in an order, where textPart.nextItem = nextTextPart.id, with the given size and start
     * from given textPart.id */
    @Query(value = "SELECT * FROM public.get_textparts_ordered_set(?1, ?2)", nativeQuery = true)
    List<TextPart> findSortedTextPartBunchByStartId(Long startId, int size);


    @Query(value = "SELECT * FROM public.get_textpart_by_id(?1)", nativeQuery = true)
    Optional<TextPart> findTextPartById(Long id);



}
