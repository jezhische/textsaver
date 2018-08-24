package com.jezh.textsaver.repository;

import com.jezh.textsaver.entity.AssemblyData;
import com.jezh.textsaver.entity.TextPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

// The @RepositoryRestResource annotation is optional and is used to customize the REST endpoint. If we decided
// to omit it, Spring would automatically create an endpoint at “/textParts” instead of “/text-parts“.
@RepositoryRestResource(collectionResourceRel = "text-parts", path = "text-parts")
public interface TextPartRepository extends PagingAndSortingRepository<TextPart, Long> {
    List<TextPart> findByAssemblyData(@Param("assemblyData") AssemblyData assemblyData);
}
