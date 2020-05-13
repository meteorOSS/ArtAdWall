package com.meteor.artadwall.tools;

import com.meteor.artadwall.data.AdData;

import java.util.ArrayList;
import java.util.List;

public class AdPage {
    public static List<AdData> getPageList(List<AdData> list,int page,int size){
        if(list==null||list.size()==0){
            return new ArrayList<>();
        }
        Integer c = list.size();
        Integer pageC = 0;
        if(c%size==0){
            pageC = c/size;
        }else {
            pageC = c/size+1;
        }
        int start,end = 0;
        if(page!=pageC){
            start = (page-1) * size;
            end = start+size;
        }else{
            start = (page-1)*size;
            end = c;
        }
        List<AdData> rnlist = list.subList(start,end);
        return rnlist;
    }
}
