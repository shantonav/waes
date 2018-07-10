package com.waes.json.assignment.base64diff.service;

import com.waes.json.assignment.base64diff.domain.Data;
import com.waes.json.assignment.base64diff.domain.DiffOutcome;
import com.waes.json.assignment.base64diff.exception.IllegalStateOfModelException;

public interface DataService {

    public void createLeftData(final Integer id, final Data data) ;

    public DiffOutcome getDiff(final Integer id) ;

    public void createRightData(Integer id, Data righttData) ;

}
