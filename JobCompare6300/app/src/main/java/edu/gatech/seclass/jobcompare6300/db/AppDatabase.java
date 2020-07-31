package edu.gatech.seclass.jobcompare6300.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;




import edu.gatech.seclass.jobcompare6300.comparisonsettings.ComparisonSettingDao;
import edu.gatech.seclass.jobcompare6300.comparisonsettings.CurrentComparisonSetting;

import edu.gatech.seclass.jobcompare6300.currentjob.CurrentJob;
import edu.gatech.seclass.jobcompare6300.currentjob.CurrentJobDao;
import edu.gatech.seclass.jobcompare6300.job.Converter;
import edu.gatech.seclass.jobcompare6300.joboffers.JobOffer;
import edu.gatech.seclass.jobcompare6300.joboffers.JobOfferDao;

@Database(entities = {CurrentJob.class, JobOffer.class, CurrentComparisonSetting.class}, version = 1, exportSchema = false)

@TypeConverters(Converter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase dbInstance;

    public abstract CurrentJobDao currentJobDao();
    public abstract ComparisonSettingDao comparisonSettingDao();

    public abstract JobOfferDao JobOfferDao();


    public static AppDatabase getInstance(final Context context) {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context, AppDatabase.class, "db").build();
        }
        return dbInstance;
    }
}

