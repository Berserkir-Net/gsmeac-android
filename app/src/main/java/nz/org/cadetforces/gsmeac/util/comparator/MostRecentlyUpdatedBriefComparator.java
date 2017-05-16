package nz.org.cadetforces.gsmeac.util.comparator;

import nz.org.cadetforces.gsmeac.database.model.Brief;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

public class MostRecentlyUpdatedBriefComparator implements Comparator<Brief> {

    @Override
    public int compare(Brief a, Brief b) {
        return new CompareToBuilder().append(b.getUpdatedAt(), a.getUpdatedAt()).toComparison();
    }

}