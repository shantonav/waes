package com.waes.json.assignment.base64diff.util;

import com.waes.json.assignment.base64diff.domain.Difference;
import com.waes.json.assignment.base64diff.exception.IllegalStateOfModelException;
import com.waes.json.assignment.base64diff.exception.InConsistentDomainStateAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiffUtil {
    private static final Logger log = LoggerFactory.getLogger(DiffUtil.class);
    public static List<Difference> getListOfDifferencecIfAny(final String leftData, final String rightData) throws IllegalStateOfModelException {
        List<Difference> differences = new ArrayList<Difference>();
        BufferedInputStream buffISLeft = null;
        BufferedInputStream buffISRight = null;
        try {

            buffISLeft = new BufferedInputStream(
                    new ByteArrayInputStream(leftData.getBytes()));
            buffISRight = new BufferedInputStream(new
                    ByteArrayInputStream(rightData.getBytes()));

            int pos = 1;
            log.debug("Let's find out the differences");
            int b1 = 0, b2 = 0;
            while (b1 != -1 && b2 != -1) {
                if (b1 != b2) {
                    log.debug("Files differ at position " + pos);
                    differences.add(new Difference(pos));
                }
                pos++;
                b1 = buffISLeft.read();
                b2 = buffISRight.read();
            }


        } catch (IOException e) {
            throw new IllegalStateOfModelException("Not sure what did you send us, but pls bear in mind" +
                    "this is not a GIGO system");
        } finally {
            if (buffISLeft != null) {
                try {
                    buffISLeft.close();
                } catch (IOException e) {
                    throw new InConsistentDomainStateAPIException("ERROR!! System error (your need not know the cause)");
                }
            }
            if (buffISRight != null) {
                try {
                    buffISRight.close();
                } catch (IOException e) {
                    throw new InConsistentDomainStateAPIException("ERROR!! System error (your need not know the cause)");
                }
            }
        }
        return differences;
    }
}