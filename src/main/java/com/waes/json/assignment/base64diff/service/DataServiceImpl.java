package com.waes.json.assignment.base64diff.service;

import com.waes.json.assignment.base64diff.domain.Data;
import com.waes.json.assignment.base64diff.domain.DiffOutcome;
import com.waes.json.assignment.base64diff.domain.Difference;
import com.waes.json.assignment.base64diff.domain.StatusCode;
import com.waes.json.assignment.base64diff.exception.IllegalStateOfModelException;
import com.waes.json.assignment.base64diff.model.Binary;
import com.waes.json.assignment.base64diff.repository.BinaryDataStore;
import com.waes.json.assignment.base64diff.util.Base64DecoderUtil;
import com.waes.json.assignment.base64diff.util.DiffUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class DataServiceImpl implements DataService{
    private static final Logger log = LoggerFactory.getLogger(DataServiceImpl.class);

    private BinaryDataStore dataStore;
    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock writeLock = lock.writeLock();
    Lock readLock = lock.readLock();

    @Autowired
    public DataServiceImpl(BinaryDataStore dataStore){
        this.dataStore = dataStore;
    }

    public void createLeftData(final Integer id, final Data leftData)  {

        Binary binaryDataFromDB = null;
        binaryDataFromDB = getBinary(id);

        if (binaryDataFromDB != null ){
            log.debug("Binary left data found for ID:"+ id);
            if ( binaryDataFromDB.getBase64DecodedStringLeftData() == null ){
                log.debug("Good thing is left data does not exist yet for ID:"+ id);
                try{
                    log.debug("Waiting for write lock on ID:"+ id);
                    writeLock.lock();
                    log.debug("Write lock acquired to right left data for ID:"+ id);
                    binaryDataFromDB.setBase64DecodedStringLeftData(leftData.getBase64EncodedString());
                    dataStore.save(binaryDataFromDB);
                    log.debug("Left data written in data store for ID:"+ id);
                }finally {
                    writeLock.unlock();
                    log.debug("Write lock released for ID:"+ id);
                }
            }else{
                throw new IllegalStateOfModelException("Please don't try to fool the system when you " +
                        "have already added a left data for ID:"+id);
            }

        }else{
            log.debug("No binary data available for ID: "+id+
                    ", one must be created in data store and left data written:"+ id);
            try{
                log.debug("Waiting for write lock on ID:"+ id);
                writeLock.lock();
                log.debug("Write lock acquired for ID:"+ id);
                Binary aNewBinary = new Binary();
                aNewBinary.setId(id);
                aNewBinary.setBase64DecodedStringLeftData(leftData.getBase64EncodedString());
                dataStore.save(aNewBinary);
                log.debug("Left data written in data store for new ID object:"+ id);
            }finally {
                writeLock.unlock();
                log.debug("Write lock released for ID:"+ id);
            }

        }

    }

    @Override
    public DiffOutcome getDiff(Integer id)  {
        Binary binaryFromDB = getBinary(id);


        if(binaryFromDB == null){
            throw new IllegalStateOfModelException("Trying to be too ambitous ? First add the left and right data");
        }else if (StringUtils.isEmpty(binaryFromDB.getBase64DecodedStringLeftData())
                && StringUtils.isEmpty(binaryFromDB.getBase64DecodedStringRightData())){
            throw new IllegalStateOfModelException("You got to be kidding me !! " +
                    "System cannot diff if both left and right are not present");
        }else if (StringUtils.isEmpty(binaryFromDB.getBase64DecodedStringLeftData())){
            throw new IllegalStateOfModelException("Can you drive a bike if the front wheel is not there ? " +
                    "System cannot diff if left data is not present");
        }else if (StringUtils.isEmpty(binaryFromDB.getBase64DecodedStringRightData())){
            throw new IllegalStateOfModelException("Can you drive a bike if the rear wheel is not there ? " +
                    "System cannot diff if right data is not present");
        }
        log.debug("Going to decode base64 encoded object for ID:"+ id);
        String originalLeftData =
                Base64DecoderUtil.getDataDecodedFromBase64Representation(binaryFromDB.getBase64DecodedStringLeftData());
        String originalRightData =
                Base64DecoderUtil.getDataDecodedFromBase64Representation(binaryFromDB.getBase64DecodedStringRightData());
        log.debug("Decoding base64 encoded object for ID:"+ id+ " is DONE");
        log.debug("Going to get the diff (or not diff) for ID:"+ id);

        return getDiffOutcome(originalLeftData,originalRightData);
    }

    private DiffOutcome getDiffOutcome(final String originalLeftData,final String originalRightData) {

        DiffOutcome diffOutcome = null;

        if (originalLeftData.length() != originalRightData.length()) {
            log.debug("Files have different length");
            diffOutcome = new DiffOutcome(StatusCode.OBJECT_NOT_SAME_LENGTH,null);
        } else if (Objects.equals(originalLeftData, originalRightData)) {
             log.debug("Files are same");
            diffOutcome = new DiffOutcome(StatusCode.OBJECTS_SAME,null);
        }else{
            List<Difference> differences = DiffUtil.getListOfDifferencecIfAny(originalLeftData,originalRightData);
            log.debug(StatusCode.OBJECT_HAS_DIFFERENCES.toString());
            diffOutcome = new DiffOutcome(StatusCode.OBJECT_HAS_DIFFERENCES,differences);
            log.debug("Differences are ?"+diffOutcome);
        }



        return diffOutcome;
    }

    @Override
    public void createRightData(Integer id, Data righttData)  {
        Binary binaryDataFromDB = null;
        binaryDataFromDB = getBinary(id);

        if (binaryDataFromDB != null ){
            log.debug("Binary right data found for ID:"+ id);
            if ( binaryDataFromDB.getBase64DecodedStringRightData() == null ){
                log.debug("Good thing is right data does not exist yet for ID:"+ id);
                try{
                    log.debug("Waiting for write lock on ID:"+ id);
                    writeLock.lock();
                    log.debug("Write lock acquired for ID:"+ id);
                    binaryDataFromDB.setBase64DecodedStringRightData(righttData.getBase64EncodedString());
                    dataStore.save(binaryDataFromDB);
                    log.debug("Right data written in data store for ID:"+ id);
                }finally {
                    writeLock.unlock();
                }
            }else{
                throw new IllegalStateOfModelException("Please don't try to fool the system when you " +
                        "have already added a right data for ID:"+id);
            }
        }else{
            log.debug("No binary data available for ID: "+id+
                    ", one must be created in data store and right data written:"+ id);
            try{
                log.debug("Waiting for write lock on ID:"+ id);
                writeLock.lock();
                log.debug("Write lock acquired for ID:"+ id);
                Binary aNewBinary = new Binary();
                aNewBinary.setId(id);
                aNewBinary.setBase64DecodedStringRightData(righttData.getBase64EncodedString());
                dataStore.save(aNewBinary);
                log.debug("Right data written in data store for new ID object:"+ id);
            }finally {
                writeLock.unlock();
            }

        }
    }

    private Binary getBinary(Integer id)  {
        Binary binaryDataFromDB = null;
        try{
            log.debug("Waiting for readlock on ID:"+id);
            readLock.lock();
            log.debug("Readlock acquired for readlock on ID:"+id);
            Optional<Binary> binaryOptional = dataStore.findById(id);
            if ( binaryOptional.isPresent()){
                binaryDataFromDB = binaryOptional.get();
            }
            log.debug("Binary data reteieved  for ID:"+id+" "+binaryDataFromDB);
        }finally {
            readLock.unlock();
            log.debug("Readlock for ID:"+id+" released" );
        }
        return binaryDataFromDB;
    }

}
