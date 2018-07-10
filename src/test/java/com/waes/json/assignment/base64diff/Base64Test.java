package com.waes.json.assignment.base64diff;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.util.Objects;


@RunWith(JUnit4.class)
public class Base64Test {

    @Test
    public void testBase64EncodingAndDecoding() throws IOException, ClassNotFoundException, EncoderException, DecoderException {
        String originalTextLeft= "This is common \n\r" +
                "Original text left";
        String originalTextRight= "This is common \n\r" +
                "Original text right";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(originalTextRight);
        oos.flush();
        byte[] binaryData = bos.toByteArray();
        oos.close();
        bos.close();

        Base64 base64 = new Base64();
        // We are converting the object's binary form into base64 encoded equivalent.
        // This object can be anything an image, PDF  etc

        String encodedObject =  base64.encodeAsString(binaryData);
        Base64 base641 = new Base64();
        // The base64 encoded string representation of the object is now decoded
        byte[] decodedBinary = base641.decode("rO0ABXQAI1RoaXMgaXMgY29tbW9uIAoNT3JpZ2luYWwgdGV4dCBsZWZ0".getBytes());

        String finalText = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(decodedBinary);
            ois = new ObjectInputStream(bis);
            finalText = (String )ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        // Finally the objects are checked for equality
        Assert.assertTrue(Objects.equals(originalTextLeft,finalText));

        BufferedInputStream buffISLeft = null;
        BufferedInputStream buffISRight = null;
        try {

            buffISLeft = new BufferedInputStream(
                    new ByteArrayInputStream(originalTextLeft.getBytes()));
            buffISRight = new BufferedInputStream(new
                    ByteArrayInputStream(originalTextRight.getBytes()));

            int b1 = 0, b2 = 0, pos = 1;
            while (b1 != -1 && b2 != -1) {
                if (b1 != b2) {
                    System.out.println("Files differ at position " + pos);
                }
                pos++;
                b1 = buffISLeft.read();
                b2 = buffISRight.read();
            }
            if ( originalTextLeft.length() != originalTextRight.length()) {
                System.out.println("Files have different length");
            }else {
                System.out.println("Files are identical, you can delete one of them.");
            }

        } finally {
            if (buffISLeft != null) {
                buffISLeft.close();
            }
            if (buffISRight != null) {
                buffISRight.close();
            }
        }



    }

}
