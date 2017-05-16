package nz.org.cadetforces.gsmeac.database.model;

import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.PrimaryKey;
import com.yahoo.squidb.annotations.TableModelSpec;

@TableModelSpec(className = "Brief", tableName = "briefs")
public class BriefSpec {

    @PrimaryKey
    @ColumnSpec(name="_id")
    long _id;

    @ColumnSpec(name="created_at")
    long createdAt;

    @ColumnSpec(name="updated_at")
    long updatedAt;

    @ColumnSpec(name="name", defaultValue="")
    String name;

    @ColumnSpec(name="ground", defaultValue="")
    String ground;

    @ColumnSpec(name="situation", defaultValue="")
    String situation;

    @ColumnSpec(name="mission", defaultValue="")
    String mission;

    @ColumnSpec(name="execution", defaultValue="")
    String execution;

    @ColumnSpec(name="administrationAndLogistics", defaultValue="")
    String administrationAndLogistics;

    @ColumnSpec(name="commandAndSignals", defaultValue="")
    String commandAndSignals;

}