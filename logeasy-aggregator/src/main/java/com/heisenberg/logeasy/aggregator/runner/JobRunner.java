package com.heisenberg.logeasy.aggregator.runner;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.heisenberg.logeasy.aggregator.dal.AggregatorDAL;
import com.heisenberg.logeasy.aggregator.model.LogDetails;
import com.heisenberg.logeasy.aggregator.service.FileService;
import com.heisenberg.logeasy.aggregator.utils.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JobRunner implements Runnable {
    private AggregatorDAL aggregatorDAL ;

    private String jobId ;
    private String aggregatorId ;
    private LogDetails jobDetails ;
    public JobRunner(String jobId, String aggregatorId, LogDetails details, AggregatorDAL aggregatorDAL) {
        super();
        this.aggregatorId = aggregatorId ;
        this.jobDetails = details ;
        this.jobId = jobId ;
        this.aggregatorDAL = aggregatorDAL ;
        aggregatorDAL.incrementThreads(this.aggregatorId) ;
    }

    @Override
    public void run() {
        try{
            aggregatorDAL.setJobStatus(this.jobId) ;
            //System.out.println("Job started");
            // Get the log path.
            String logPath = this.jobDetails.getLogPath() ;
            // Check for the last ingested date in the DB.
            long lastIngestedDate = aggregatorDAL.getLastIngestedTime(logPath) ;
            HashMap<String, Integer> directoryListing = aggregatorDAL.getDirectoryListing(logPath) ;

            // Read files in the path that have last modified date after the value stored in DB.
            List<File> filesUnderPath = FileService.getFilesList(logPath) ;
            if(directoryListing == null){
                //System.out.println("directoryListing not found in DB.");
                directoryListing = FileService.getDirectoryListing(filesUnderPath) ;
                //System.out.println("Extarcted the file service : " + directoryListing.toString());
            }
            Integer lineCounter ;
            String logItem ;
            for(File file : filesUnderPath){
                if(file.lastModified() > lastIngestedDate){
                    lineCounter = directoryListing.get(file.getName().replace('.', '_')) ;
                    if(lineCounter == null){
                        lineCounter = 0 ;
                    }
                    FileInputStream fis = new FileInputStream(file) ;
                    BufferedReader br = new BufferedReader( new InputStreamReader(fis)) ;

                    for(int i = 0 ; i < lineCounter ; i++){
                        br.readLine() ;
                    }
                    while((logItem=br.readLine()) != null){
                        // Process logItem.
                        List<String> logFields = Arrays.asList(logItem.split(" ", jobDetails.getLogSchema().size()));
                        // Select the fields based on the given set of fields.
                        Map<String, String> logItemJson = new HashMap<String, String>() ;
                        for (Integer fieldIndex : jobDetails.getLogFields()){
                            logItemJson.put(jobDetails.getLogSchema().get(fieldIndex), logFields.get(fieldIndex)) ;
                        }
                        // Store the records back to DB.
                        // Write the JSONObject to logs collection in the DB with logPath as key.
                        logItemJson.put("sourceLogFile", file.getName()) ;
                        aggregatorDAL.saveLogItem(logPath, logItemJson) ;
                        //System.out.println(logItemJson.toString());
                        lineCounter++ ;
                    }
                    directoryListing.put(file.getName().replace(".", "_"), lineCounter) ;
                    //System.out.println("Final directory Listing : " + directoryListing.toString());
                    aggregatorDAL.setDirectoryListing(logPath, directoryListing) ;
                }
            }
            // Update the lastIngestedTime for the logPath.
            aggregatorDAL.setLastIngestedTime(logPath, System.currentTimeMillis()) ;
            aggregatorDAL.setJobStatus(this.jobId, Constants.JOB_DONE) ;

        }
        catch (Exception e){
            e.printStackTrace();
            aggregatorDAL.setJobStatus(this.jobId, Constants.JOB_FAILED) ;
        }
        finally {
            aggregatorDAL.decrementThreads(this.aggregatorId) ;
            System.out.println("Job finished");
        }
    }
}
