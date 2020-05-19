package com.meteor.artadwall.guiholder;

import com.meteor.artadwall.data.AdData;

import java.util.Comparator;
import java.util.List;
public enum Sort {
    DATE,LIKE;
    public List<AdData> sort(List<AdData> ads)
    {
        Sort sort = this;
        switch (sort){
            case DATE:
                ads.sort(Comparator.comparingLong(AdData::getDate));
                break;
            case LIKE:
                ads.sort((a,b)->Integer.compare(b.getLike(),a.getLike()));
                break;
        }
        return ads;
    }
}
