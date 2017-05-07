package com.liamcottle.gsmeac.util.comparator;

import com.liamcottle.gsmeac.database.model.Brief;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

public class MostRecentlyUpdatedBriefComparator implements Comparator<Brief> {

    @Override
    public int compare(Brief a, Brief b) {
        return new CompareToBuilder().append(b.getUpdatedAt(), a.getUpdatedAt()).toComparison();
    }

}