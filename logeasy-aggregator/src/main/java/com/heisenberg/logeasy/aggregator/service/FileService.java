package com.heisenberg.logeasy.aggregator.service;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FileService {

    public static List<File> getFilesList(String logPath) {
        //System.out.println(logPath);
        List<File> filesList = new ArrayList<>() ;
        for(File file : (new File(logPath)).listFiles()){
            filesList.add(file) ;
        }
        return filesList ;
    }

    public static HashMap<String, Integer> getDirectoryListing(List<File> filesUnderPath) {
        HashMap<String, Integer> listing = new HashMap<>() ;

        for(File file : filesUnderPath){
            listing.put(file.getName().replace('.', '_'), 0) ;
        }
        return listing ;
    }
}
