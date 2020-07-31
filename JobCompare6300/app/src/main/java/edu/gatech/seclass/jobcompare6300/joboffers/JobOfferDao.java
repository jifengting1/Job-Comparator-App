package edu.gatech.seclass.jobcompare6300.joboffers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.gatech.seclass.jobcompare6300.currentjob.CurrentJob;

@Dao
public interface JobOfferDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertJobOffer(JobOffer jobOffer);

    @Query("SELECT * FROM joboffer")
    List<JobOffer> getAll();
}
