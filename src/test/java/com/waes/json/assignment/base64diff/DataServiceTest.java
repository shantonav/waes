package com.waes.json.assignment.base64diff;

import com.waes.json.assignment.base64diff.domain.Data;
import com.waes.json.assignment.base64diff.exception.IllegalStateOfModelException;
import com.waes.json.assignment.base64diff.model.Binary;
import com.waes.json.assignment.base64diff.repository.BinaryDataStore;
import com.waes.json.assignment.base64diff.service.DataServiceImpl;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * Created by shantonav on 29/07/2018.
 */
@RunWith(SpringRunner.class)
public class DataServiceTest {
    @MockBean
    private BinaryDataStore dataStore;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testDiffWithNonExistentId(){
        Mockito.when(dataStore.findById(Mockito.any(Integer.class))).thenReturn(Optional.empty());
        expectedException.expect(IllegalStateOfModelException.class);
        expectedException.expectMessage("Trying to be too ambitous ? First add the left and right data");
        DataServiceImpl dataService = new DataServiceImpl(dataStore);
        dataService.getDiff(Integer.valueOf(123));

    }

    @Test
    public void testCreationOfLeftDataWhenItAlreadyExists(){
        Binary binaryData = new Binary();
        binaryData.setId(123);
        binaryData.setBase64DecodedStringLeftData("Some garbage");
        Mockito.when(dataStore.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(binaryData));
        Mockito.when(dataStore.save(Mockito.any(Binary.class))).thenReturn(binaryData);
        expectedException.expect(IllegalStateOfModelException.class);
        expectedException.expectMessage("Please don't try to fool the system when you " +
                "have already added a left data for ID:123");
        DataServiceImpl dataService = new DataServiceImpl(dataStore);
        Data leftData = new Data();
        leftData.setBase64EncodedString("Some garbage");
        dataService.createLeftData(binaryData.getId(),leftData);

        //Assert.assertEquals(leftData.getBase64EncodedString(),binaryData.getBase64DecodedStringLeftData());

    }

    @Test
    public void testCreationOfLeftDataWhenItDoesNotExist(){
        Binary binaryData = new Binary();
        binaryData.setId(123);

        Binary savedBinaryData = new Binary();
        savedBinaryData.setId(123);
        savedBinaryData.setBase64DecodedStringLeftData("Some garbage");

        Mockito.when(dataStore.findById(Mockito.any(Integer.class))).thenReturn(Optional.of(binaryData));
        Mockito.when(dataStore.save(Mockito.any(Binary.class))).thenReturn(savedBinaryData);

        DataServiceImpl dataService = new DataServiceImpl(dataStore);
        Data leftData = new Data();
        leftData.setBase64EncodedString("Some garbage");
        dataService.createLeftData(binaryData.getId(),leftData);

        Assert.assertEquals(leftData.getBase64EncodedString(),savedBinaryData.getBase64DecodedStringLeftData());

    }




}
