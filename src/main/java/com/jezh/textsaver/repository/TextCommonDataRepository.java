package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.AppUser;
import com.jezh.textsaver.entity.TextCommonData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TextCommonDataRepository extends JpaRepository<TextCommonData, Long>{

    @Query(value = "SELECT * FROM public.find_textcommondata_by_id(?1)", nativeQuery = true)
    Optional<TextCommonData> findTextCommonDataById(Long id);

    // todo: remake to 'findAllByUserOrderByNameCreatedDateAsc'
    List<TextCommonData> findAllByUserOrderByNameAsc(AppUser authenticated);

//    List<TextCommonData> findAllByOrderByNameAsc();

}
