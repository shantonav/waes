package com.waes.json.assignment.base64diff.controller;

import com.waes.json.assignment.base64diff.domain.Data;
import com.waes.json.assignment.base64diff.domain.DiffOutcome;
import com.waes.json.assignment.base64diff.exception.IllegalStateOfModelException;
import com.waes.json.assignment.base64diff.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/v1/diff/{id}", produces = "application/json", consumes = "application/json")
public class DataHandler {
    private static final Logger log = LoggerFactory.getLogger(DataHandler.class);

    @Autowired
    DataService dataService;

    @PostMapping("left")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLeft(@PathVariable("id") Integer id,
                        @Valid
                        @RequestBody  Data leftData)  {
        dataService.createLeftData(id,leftData);
        log.info("Left data received:"+leftData+ "for ID:"+id);
    }

    @PostMapping("right")
    @ResponseStatus(HttpStatus.CREATED)
    public void addRight(@PathVariable("id") Integer id,
                         @Valid
                         @RequestBody  Data righttData)  {
        dataService.createRightData(id,righttData);
        log.info("Right data received:"+righttData+ "for ID:"+id);
    }

    @GetMapping
    public @ResponseBody ResponseEntity<DiffOutcome> getDiff(@PathVariable("id") Integer id) {
        log.debug("Inside DataHandler.getDiff");
        return new ResponseEntity<DiffOutcome>(dataService.getDiff(id),HttpStatus.OK);
    }

}
