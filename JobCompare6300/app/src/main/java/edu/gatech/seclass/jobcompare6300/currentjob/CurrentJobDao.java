package edu.gatech.seclass.jobcompare6300.currentjob;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CurrentJobDao {
    @Query("SELECT * FROM currentjob LIMIT 1")
    CurrentJob findOne();

    @Update
    public void updateCurrentJob(CurrentJob currentJob);

    @Insert
    public void insertCurrentJob(CurrentJob currentJob);
}
