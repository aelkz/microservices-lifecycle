package com.microservices.lifecycle.bankstatement.controller;

import com.microservices.lifecycle.bankstatement.controller.validator.ExtractValidator;
import com.microservices.lifecycle.bankstatement.model.StatementRecord;
import com.microservices.lifecycle.bankstatement.repository.StatementCustomRespository;
import com.microservices.lifecycle.bankstatement.service.StatementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Validated //required for @Valid on method parameters such as @RequesParam, @PathVariable, @RequestHeader
public class StatementController extends BaseController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final transient Logger logger = LoggerFactory.getLogger(StatementController.class);

    @Autowired
    StatementService statementService;

    @RequestMapping(path = "/v1/statement/{id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all banking records",
            notes = "Returns first N banking records specified by the size parameter with page offset specified by page parameter.",
            response = Page.class)
    public Page<StatementRecord> getAll(
            @ApiParam("The size of the page to be returned") @RequestParam(required = false) Integer size,
            @ApiParam("Zero-based page index") @RequestParam(required = false) Integer page,
            @PathVariable("id") Long id) {

        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }

        if (page == null) {
            page = 0;
        }

        // This is only for demonstration. Will be improved in a later version.
        id = 1L;

        Pageable pageable = new PageRequest(page, size);
        Page<StatementRecord> products = statementService.findAllSorted(pageable);

        return products;
    }

    @RequestMapping(path = "/v1/report/analytic", method = RequestMethod.GET)
    @ApiOperation(
            value = "Analytic report of banking records",
            notes = "Returns first N banking records specified by the size parameter with page offset specified by page parameter.",
            response = Page.class)
    public Page<StatementRecord> getAnalytic(
            @ApiParam("The size of the page to be returned") @RequestParam(required = false) Integer size,
            @ApiParam("Zero-based page index") @RequestParam(required = false) Integer page) {

        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }

        if (page == null) {
            page = 0;
        }

        Pageable pageable = new PageRequest(page, size);
        Page<StatementRecord> products = statementService.findAllSorted(pageable);

        return products;
    }

    @RequestMapping(path = "/v1/report/custom", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(
            value = "Custom report of banking records",
            notes = "Returns first N banking records specified by the size parameter with page offset specified by page parameter.",
            response = Page.class)
    public Page<StatementRecord> getCustom(
            @ApiParam("The size of the page to be returned") @RequestParam(required = false) Integer size,
            @ApiParam("Zero-based page index") @RequestParam(required = false) Integer page,
            @Valid @RequestBody StatementRecord record) {

        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }

        if (page == null) {
            page = 0;
        }

        Pageable pageable = new PageRequest(page, size);
        // TODO - filter based on record bean
        Page<StatementRecord> products = statementService.findAllSorted(pageable);

        return products;
    }

    @RequestMapping(path = "/v1/statement", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(
            value = "Create new banking record",
            notes = "Creates new banking record of Withdrawal or Deposit type. Returns created record with id.",
            response = StatementCustomRespository.class)
    public ResponseEntity<StatementRecord> add(
            @Valid @RequestBody StatementRecord record) {

        record = statementService.save(record);
        return ResponseEntity.ok().body(record);
    }

    @InitBinder("event")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new ExtractValidator());
    }
}
