package edu.gatech.seclass.jobcompare6300.job;

import androidx.room.TypeConverter;

import java.math.BigDecimal;

public class Converter {
    @TypeConverter
    public static String fromLocation(Location location) {
        return location.getCity() + "," + location.getState();
    }

    @TypeConverter
    public static Location toLocation(String location) {
        String[] splitLocation = location.split(",");
        return new Location(splitLocation[0], splitLocation[1]);
    }

    @TypeConverter
    public static String fromBigDecimal(BigDecimal x) {
        return x.toString();
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(String x) {
        return new BigDecimal(x);
    }
}
