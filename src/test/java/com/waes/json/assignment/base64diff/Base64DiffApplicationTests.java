package com.waes.json.assignment.base64diff;

import com.waes.json.assignment.base64diff.exception.IllegalStateOfModelException;
import com.waes.json.assignment.base64diff.util.Base64DecoderUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Base64DiffApplicationTests {

	@Test
	public void testBase64DecodingToGetOriginalStringObject() throws IllegalStateOfModelException {
		final String originalTextLeft= "This is common \n\r" +
				"Original text left";
		final String decodedStringObject =
				Base64DecoderUtil.getDataDecodedFromBase64Representation("rO0ABXQAI1RoaXMgaXMgY29tbW9uIAoNT3JpZ2luYWwgdGV4dCBsZWZ0");

		Assert.assertEquals(originalTextLeft,decodedStringObject);

	}

}
