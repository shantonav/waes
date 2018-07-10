package com.waes.json.assignment.base64diff;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.json.assignment.base64diff.domain.ApiError;
import com.waes.json.assignment.base64diff.domain.DiffOutcome;
import com.waes.json.assignment.base64diff.domain.StatusCode;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;


@RunWith(TestOrderRunner.class)
@SpringBootTest(classes = {Base64DiffApplication.class})
public class RestDocsTest {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation))
                .alwaysDo(MockMvcRestDocumentation.document("{method-name}",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .build();
    }
    @Test
    @Order(order = 4)
    public void testDiffExample() throws Exception {
        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/diff/{id}",123).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("nodiff-for-123-HTTP-repsonse-200",
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")))
                ).andReturn();
        String jsonResponseString = result.getResponse().getContentAsString();
        DiffOutcome diffOutcome = this.objectMapper.readValue(jsonResponseString, DiffOutcome.class);
        Assert.assertNotNull(diffOutcome);
        Assert.assertEquals(StatusCode.OBJECTS_SAME,diffOutcome.getStatusCode());
        Assert.assertNull(diffOutcome.getDifferences());
    }
    @Test
    @Order(order = 2)
    public void testDiffExampleWithOnlyLeftDataAdded() throws Exception {
        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/diff/{id}",123).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("diff-for-123-with-only-left-HTTP-400",
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")))
                ).andReturn();
        String responseErrorMesage = result.getResolvedException().getMessage();
        Assert.assertEquals("Can you drive a bike if the rear wheel is not there ? " +
                "System cannot diff if right data is not present",responseErrorMesage);
    }


    @Test
    @Order(order = 1)
    public void testAddLeftData() throws Exception {
        Map<String,String> jsonData = new HashMap<>();
        jsonData.put("base64EncodedString",getBase64EncodedString("This is common \n\r" +
                "Original text left"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/diff/{id}/left",123)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(jsonData)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("addLeft-for-123-HTTP-201",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("base64EncodedString")
                                        .description("The base64 encoded String object of left data, in String format")
                        )));


    }

    @Test
    @Order(order = 3)
    public void testAddRightData() throws Exception {
        Map<String,String> jsonData = new HashMap<>();
        jsonData.put("base64EncodedString",getBase64EncodedString("This is common \n\r" +
                "Original text left"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/diff/{id}/right",123)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(jsonData)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("addRight-for-123-HTTP-201",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("base64EncodedString")
                                        .description("The base64 encoded String object of right data, in String format")
                        )));


    }

    @Test
    @Order(order = 5)
    public void testDiffExampleWithNonExistentID() throws Exception {
        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/diff/{id}",124).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("diff-for-unknown-ID-124-HTTP-400",
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")))
                ).andReturn();
        String responseErrorMesage = result.getResolvedException().getMessage();
        Assert.assertEquals("Trying to be too ambitous ? First add the left and right data",responseErrorMesage);

    }

    @Test
    @Order(order = 6)
    public void testDiffExampleWithPOSTWhichIsNotAllowed() throws Exception {
        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/diff/{id}",124).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
                .andDo(MockMvcRestDocumentation.document("diff-with-POST-HTTP-405",
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")))
                ).andReturn();

        String jsonResponseString = result.getResponse().getContentAsString();
        ApiError apiError = this.objectMapper.readValue(jsonResponseString, ApiError.class);
        Assert.assertNotNull(apiError);
        Assert.assertEquals(Integer.valueOf(333333),apiError.getErrorCode());
        Assert.assertEquals("HTTP_ERROR: read https://spring.io/understanding/REST",apiError.getSystemErrorMessage());
        Assert.assertEquals("Request method 'POST' not supported",apiError.getRootCause());


    }

    @Test
    @Order(order = 7)
    public void testLeftDataWithNonBase64EncodedStringText() throws Exception {
        Map<String,String> jsonData = new HashMap<>();
        jsonData.put("base64EncodedString","shit");

        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/diff/{id}/right",123)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(jsonData)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("addRight-for-123-with-non-base64-data-HTTP-repsonse-400",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("base64EncodedString")
                                        .description("The base64 encoded String object of right data, in String format")
                        ))).andReturn();
        String jsonResponseString = result.getResponse().getContentAsString();
        ApiError apiError = this.objectMapper.readValue(jsonResponseString, ApiError.class);
        Assert.assertNotNull(apiError);
        Assert.assertEquals(Integer.valueOf(223000),apiError.getErrorCode());
        Assert.assertEquals("Input is not correct",apiError.getSystemErrorMessage());
        Assert.assertEquals("Constraint validation exception",apiError.getRootCause());
        Assert.assertNotNull(apiError.getErrors());
        Assert.assertEquals("base64EncodedString: Input string must be a valid Base64 encoded" +
                " String (wish I could underline this) Object and of course not null",apiError.getErrors().get(0));


    }

    @Test
    @Order(order = 8)
    public void testLeftDataWithNonBase64EncodedNull() throws Exception {
        Map<String,String> jsonData = new HashMap<>();
        jsonData.put("base64EncodedString",null);

        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/diff/{id}/right",123)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(jsonData)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("addRight-for-123-with-null-data-HTTP-repsonse-400",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("base64EncodedString")
                                        .description("The base64 encoded String object of right data, in String format")
                        ))).andReturn();
        String jsonResponseString = result.getResponse().getContentAsString();
        ApiError apiError = this.objectMapper.readValue(jsonResponseString, ApiError.class);
        Assert.assertNotNull(apiError);
        Assert.assertEquals(Integer.valueOf(223000),apiError.getErrorCode());
        Assert.assertEquals("Input is not correct",apiError.getSystemErrorMessage());
        Assert.assertEquals("Constraint validation exception",apiError.getRootCause());
        Assert.assertNotNull(apiError.getErrors());
        Assert.assertEquals("base64EncodedString: Input string must be a valid Base64 encoded" +
                " String (wish I could underline this) Object and of course not null",apiError.getErrors().get(0));


    }

    @Test
    @Order(order = 9)
    public void testAddLeftDataForID222() throws Exception {
        Map<String,String> jsonData = new HashMap<>();
        jsonData.put("base64EncodedString",getBase64EncodedString("This is common \n\r" +
                "Original text left"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/diff/{id}/left",222)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(jsonData)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("addLeft-for-222-with-base64-encoded-data-HTTP-repsonse-201",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("base64EncodedString")
                                        .description("The base64 encoded String object of left data, in String format")
                        )));


    }

    @Test
    @Order(order = 10)
    public void testAddRightDataForID222() throws Exception {
        Map<String,String> jsonData = new HashMap<>();
        jsonData.put("base64EncodedString",getBase64EncodedString("This is common \n\r" +
                "Original test left"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/diff/{id}/right",222)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(jsonData)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("addright-for-222-with-base64-encoded-data-HTTP-repsonse-201",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("base64EncodedString")
                                        .description("The base64 encoded String object of right data, in String format")
                        )));


    }
    @Test
    @Order(order = 11)
    public void testDiffForID222() throws Exception {
        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/diff/{id}",222).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("diff-for-222-with-1-diff-HTTP-repsonse-200",
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")))
                ).andReturn();
        String jsonResponseString = result.getResponse().getContentAsString();
        Assert.assertEquals("{\"statusCode\":\"OBJECT_HAS_DIFFERENCES\"," +
                "\"differences\":[{\"offset\":30,\"textsAreOfDiffLength\":false}]}",jsonResponseString);
    }


    @Test
    @Order(order = 12)
    public void testAddLeftDataForID223() throws Exception {
        Map<String,String> jsonData = new HashMap<>();
        jsonData.put("base64EncodedString",getBase64EncodedString("This is common \n\r" +
                "Original text left"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/diff/{id}/left",223)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(jsonData)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("addLeft-for-223-with-base64-encoded-data-HTTP-repsonse-201",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("base64EncodedString")
                                        .description("The base64 encoded String object of left data, in String format")
                        )));


    }

    @Test
    @Order(order = 13)
    public void testAddRightDataForID223() throws Exception {
        Map<String,String> jsonData = new HashMap<>();
        jsonData.put("base64EncodedString",getBase64EncodedString("This is common \n\r" +
                "Original test right"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/diff/{id}/right",223)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(jsonData)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcRestDocumentation.document("addright-for-223-with-base64-encoded-data-HTTP-repsonse-201",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("base64EncodedString")
                                        .description("The base64 encoded String object of right data, in String format")
                        )));


    }
    @Test
    @Order(order = 14)
    public void testDiffForID223() throws Exception {
        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/diff/{id}",223).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document("not-same-length-diff-for-223-HTTP-repsonse-200",
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("id").
                                        description("The id of the binary to diff")))
                ).andReturn();
        String jsonResponseString = result.getResponse().getContentAsString();
        Assert.assertEquals("{\"statusCode\":\"OBJECT_NOT_SAME_LENGTH\"," +
                "\"differences\":null}",jsonResponseString);
    }

    private String getBase64EncodedString(final String originalText) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(originalText);
        oos.flush();
        byte[] binaryData = bos.toByteArray();
        oos.close();
        bos.close();

        Base64 base64 = new Base64();
        // We are converting the object's binary form into base64 encoded equivalent.
        // This object can be anything an image, PDF  etc

        return  base64.encodeAsString(binaryData);
    }
}
