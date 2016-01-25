package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.constants.Constants;
import com.emmaprojects.creativehub.domain.Category;
import com.emmaprojects.creativehub.repository.CategoryRepository;
import com.emmaprojects.creativehub.web.rest.util.HeaderUtil;
import com.emmaprojects.creativehub.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Category.
 */
@RestController
@RequestMapping("/api")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    @Inject
    private CategoryRepository categoryRepository;

    /**
     * POST  /categorys -> Create a new category.
     */
    @RequestMapping(value = "/categorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> createCategory(@RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to save Category : {}", category);
        if (category.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new category cannot already have an ID").body(null);
        }
        Category result = categoryRepository.save(category);
        return ResponseEntity.created(new URI("/api/categorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("category", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categorys -> Updates an existing category.
     */
    @RequestMapping(value = "/categorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to update Category : {}", category);
        if (category.getId() == null) {
            return createCategory(category);
        }
        Category result = categoryRepository.save(category);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("category", category.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categorys -> get all the categorys.
     */
    @RequestMapping(value = "/categorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Category>> getAllCategorys(Pageable pageable)
        throws URISyntaxException {
        Page<Category> page = categoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categorys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categorys/:id -> get the "id" category.
     */
    @RequestMapping(value = "/categorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        return Optional.ofNullable(categoryRepository.findOneWithEagerRelationships(id))
            .map(category -> new ResponseEntity<>(
                category,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categorys/:id -> delete the "id" category.
     */
    @RequestMapping(value = "/categorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.debug("REST request to delete Category : {}", id);
        categoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("category", id.toString())).build();
    }

    /**
     * GET  /categories/artwork -> obtener categorias hijas de Artwork.
     */
    @RequestMapping(value = "/categories/artwork",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Category>> getArtworkCategories() {
        log.debug("REST request to get Artwork Categories");
        Optional<Category> cat = Optional.ofNullable(categoryRepository.findOneWithEagerRelationships(Constants.ARTWORK_CATEGORY_ID));
            /*.map(category -> new ResponseEntity<>(
                category,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));*/
        if(cat.isPresent() && cat.get().getSubCategories().size() > 0) {
            Set<Category> categories = cat.map(c -> c.getSubCategories()).get();
            return new ResponseEntity<Set<Category>>(categories,HttpStatus.OK);
        }else{
            return new ResponseEntity<Set<Category>>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET  /categories/artwork -> obtener categorias hijas de Professional.
     */
    @RequestMapping(value = "/categories/professional",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Category>> getProfessionalCategories() {
        log.debug("REST request to get Professional Categories");
        Optional<Category> cat = Optional.ofNullable(categoryRepository.findOneWithEagerRelationships(Constants.PROFESSIONAL_CATEGORY_ID));
        if(cat.isPresent() && cat.get().getSubCategories().size() > 0) {
            Set<Category> categories = cat.map(c -> c.getSubCategories()).get();
            return new ResponseEntity<Set<Category>>(categories,HttpStatus.OK);
        }else{
            return new ResponseEntity<Set<Category>>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET  /categories/artwork -> obtener categorias hijas de Professional.
     */
    @RequestMapping(value = "/categories/events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Category>> getEventCategories() {
        log.debug("REST request to get Professional Categories");
        Optional<Category> cat = Optional.ofNullable(categoryRepository.findOneWithEagerRelationships(Constants.EVENTS_CATEGORY_ID));
        if(cat.isPresent() && cat.get().getSubCategories().size() > 0) {
            Set<Category> categories = cat.map(c -> c.getSubCategories()).get();
            return new ResponseEntity<Set<Category>>(categories,HttpStatus.OK);
        }else{
            return new ResponseEntity<Set<Category>>(HttpStatus.NOT_FOUND);
        }
    }
}
