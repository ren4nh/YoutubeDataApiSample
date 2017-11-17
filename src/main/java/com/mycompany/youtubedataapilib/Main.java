package com.mycompany.youtubedataapilib;

import java.util.List;


/**
 *
 * @author renan
 */
public class Main {
    
    public static void main(String[] args) {
        Search search = new Search();
        List<VideoInfo> lista = search.execute();
        
        for (VideoInfo videoInfo : lista) {
            System.out.println("Id video " + videoInfo.getVideoId());
            System.out.println("Thumb url " + videoInfo.getThumbnailUrl());
            System.out.println("Title " + videoInfo.getTitle());
        }
    }
    
}
