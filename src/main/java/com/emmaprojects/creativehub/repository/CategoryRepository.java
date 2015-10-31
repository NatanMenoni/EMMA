package com.emmaprojects.creativehub.repository;

import com.emmaprojects.creativehub.domain.Category;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("select distinct category from Category category left join fetch category.subCategories")
    List<Category> findAllWithEagerRelationships();

    @Query("select category from Category category left join fetch category.subCategories where category.id =:id")
    Category findOneWithEagerRelationships(@Param("id") Long id);

}
