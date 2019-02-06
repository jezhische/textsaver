package com.jezh.textsaver.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jezh.textsaver.entity.TextPart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

// The @RepositoryRestResource annotation is optional and is used to customize the REST endpoint. If we decided
// to omit it, Spring would automatically create an endpoint at “/textParts” instead of “/text-parts“.
//@RepositoryRestResource(collectionResourceRel = "text-parts", path = "text-parts")
@Repository
public interface TextPartRepository extends JpaRepository<TextPart, Long> {

    /**
     * find textPart by the field 'username' of bounded textCommonData
     */
    List<TextPart> findByTextCommonDataName(String textCommonDataName);

    /**
     * find textPart by 'nextItem' field
     */
    Optional<TextPart> findByNextItem(Long nextItem);

    /**
     * find all the textPart with the given textCommonData id in an order,
     * where currentTextPart.nextItem = nextTextPart.id
     */
    @Query(value = "SELECT * FROM public.get_all_texparts_ordered_set(?1)", nativeQuery = true)
    List<TextPart> findSortedSetByTextCommonDataId(Long textCommonDataId);

    /**
     * find a list of textPart in an order, where currentTextPart.nextItem = nextTextPart.id, with the given size
     * and start from given textPart.id
     */
    @Query(value = "SELECT * FROM public.get_textparts_ordered_set(?1, ?2)", nativeQuery = true)
    List<TextPart> findSortedTextPartBunchByStartId(Long startId, int size);

    /**
     * find a list of textPart in an order, where currentTextPart.nextItem = nextTextPart.id, start from given textPart.id
     */
    @Query(value = "SELECT * FROM public.get_remaining_texparts_ordered_set(?1)", nativeQuery = true)
    List<TextPart> findRemainingSortedTextPartBunchByStartId(Long startId);

    /**
     * fint textPart by id
     */
    @Query(value = "SELECT * FROM public.find_textpart_by_id(?1)", nativeQuery = true)
    Optional<TextPart> findTextPartById(Long id);

    List<TextPart> findAllByTextCommonDataId(Long id);

// ====================================================================================
    /**
     * find previous textPart by current one
     */
    @Query("select previous from TextPart previous where previous.nextItem = (select current.id from TextPart current where current = ?1)")
    Optional<TextPart> findPreviousByCurrentInSequence(TextPart current);

    /**
     * find next textPart by current one
     */
    @Query ("select next from TextPart as next where next.id = (select current.nextItem from TextPart as current where current = ?1)")
    Optional<TextPart> findNextByCurrentInSequence(TextPart current);

    // https://docs.spring.io/spring-data/jpa/docs/current-SNAPSHOT/reference/html/#jpa.query-methods.sorting
    @Query(value = "SELECT * FROM public.get_all_texparts_ordered_set(?1)",
            countQuery = "SELECT count(*) FROM public.get_all_texparts_ordered_set(?1)",
            nativeQuery = true)
    Page<TextPart> findPageByDocDataId(Long textCommonDataId, Pageable pageable);

// NB: id BIGSERIAL PRIMARY KEY will be cast to BigInteger and cannot be cast to Long automatically
@Query(value = "SELECT * FROM public.get_all_texparts_id_ordered_set(?1)", nativeQuery = true)
List<BigInteger> findSortedTextPartIdByTextCommonDataId(Long textCommonDataId);


    @Query (value = "SELECT * FROM public.update_text_part_by_id(?1, ?2, ?3)", nativeQuery = true)
    Optional<Date> updateById(Long id, String body, Date updated);

    @Override
    void delete(TextPart textPart);

    //    @Query (value = "SELECT * FROM public.update_text_part_by_id(?1, ?2, ?3)", nativeQuery = true)
//    Optional<Date> updateByTextPartId(Long id, String body, Date updated);
}



