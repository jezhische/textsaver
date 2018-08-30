package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.TextPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

// The @RepositoryRestResource annotation is optional and is used to customize the REST endpoint. If we decided
// to omit it, Spring would automatically create an endpoint at “/textParts” instead of “/text-parts“.
//@RepositoryRestResource(collectionResourceRel = "text-parts", path = "text-parts")
@Repository
public interface TextPartRepository extends JpaRepository<TextPart, Long> {
}
