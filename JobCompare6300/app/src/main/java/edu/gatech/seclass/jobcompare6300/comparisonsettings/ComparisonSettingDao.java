package edu.gatech.seclass.jobcompare6300.comparisonsettings;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Insert;

@Dao
public interface ComparisonSettingDao {
    @Query("SELECT * FROM currentComparisonSetting LIMIT 1")
    CurrentComparisonSetting findOne();

    @Update
    public void updateComparisonSetting(CurrentComparisonSetting currentComparisonSetting);

    @Insert
    public void insertComparisonSetting(CurrentComparisonSetting currentComparisonSetting);
}
