package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.Document;
import com.emmaprojects.creativehub.repository.DocumentRepository;
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

/**
 * REST controller for managing Document.
 */
@RestController
@RequestMapping("/api")
public class DocumentResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    @Inject
    private DocumentRepository documentRepository;

    /**
     * POST  /documents -> Create a new document.
     */
    @RequestMapping(value = "/documents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Document> createDocument(@RequestBody Document document) throws URISyntaxException {
        log.debug("REST request to save Document : {}", document);
        if (document.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new document cannot already have an ID").body(null);
        }
        Document result = documentRepository.save(document);
        return ResponseEntity.created(new URI("/api/documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("document", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /documents -> Updates an existing document.
     */
    @RequestMapping(value = "/documents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Document> updateDocument(@RequestBody Document document) throws URISyntaxException {
        log.debug("REST request to update Document : {}", document);
        if (document.getId() == null) {
            return createDocument(document);
        }
        Document result = documentRepository.save(document);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("document", document.getId().toString()))
            .body(result);
    }

    /**
     * GET  /documents -> get all the documents.
     */
    @RequestMapping(value = "/documents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Document>> getAllDocuments(Pageable pageable)
        throws URISyntaxException {
        Page<Document> page = documentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/documents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /documents/:id -> get the "id" document.
     */
    @RequestMapping(value = "/documents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        log.debug("REST request to get Document : {}", id);
        return Optional.ofNullable(documentRepository.findOne(id))
            .map(document -> new ResponseEntity<>(
                document,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /documents/:id -> delete the "id" document.
     */
    @RequestMapping(value = "/documents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        log.debug("REST request to delete Document : {}", id);
        documentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("document", id.toString())).build();
    }
}
