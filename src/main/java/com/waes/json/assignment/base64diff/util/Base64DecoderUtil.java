package com.waes.json.assignment.base64diff.util;

import com.waes.json.assignment.base64diff.exception.IllegalStateOfModelException;
import com.waes.json.assignment.base64diff.exception.InConsistentDomainStateAPIException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class Base64DecoderUtil {
    private static final Logger log = LoggerFactory.getLogger(Base64DecoderUtil.class);
    public static String getDataDecodedFromBase64Representation(String base64EncodedObject) throws IllegalStateOfModelException {
        Base64 base64 = new Base64();

        log.debug("base64 encoded OBJECT (String in our case) as string  is : "+base64EncodedObject);
        byte[] decodedBinary = base64.decode(base64EncodedObject.getBytes());

        String finalText = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(decodedBinary);
            ois = new ObjectInputStream(bis);
            Object obj  = ois.readObject();

            if (! (obj instanceof String) ){
                throw new IllegalStateOfModelException("Did I forget to tell you that you cannot throw" +
                        " any tom, dick harry object to the APIs. ? :( " +
                        "System expects ONLY and strictly String objects, so please get you act together and try again");
            }
            finalText = (String) obj;
        } catch (ClassNotFoundException e) {
            throw new InConsistentDomainStateAPIException("ERROR!! Check the type of the pbject you sent");
        } catch (IOException e) {
            throw new InConsistentDomainStateAPIException("ERROR!! Something went horribly wrong with IO");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    throw new InConsistentDomainStateAPIException("ERROR!! System error (your need not know the cause)");
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    throw new InConsistentDomainStateAPIException("ERROR!! System error (your need not know the cause)");
                }
            }
        }

        log.debug("Base 64 decoding done on the Object and the original text (String) returned" +
                "Sorry cannot show you the original text, it is against my professional ethics :P");
        return finalText;
    }



}
