package com.ivanantsiferov.yamblzweather.room_database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.ringov.yamblzweather.data.database.AppDatabase;
import com.ringov.yamblzweather.data.database.dao.WeatherDAO;
import com.ringov.yamblzweather.data.database.entity.DBWeather;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ForecastTableTest {

    private WeatherDAO weatherDAO;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();

        weatherDAO = db.weatherDAO();
        // Fill DB with data
        List<DBWeather> forecastMock = ForecastTableTestUtil.getAll();
        weatherDAO.insertAll(forecastMock);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void checkIfMockDataWrittenSuccessfully() {
        List<DBWeather> weatherDb = weatherDAO.getAll();
        assertEquals(ForecastTableTestUtil.getAll().size(), weatherDb.size());
    }

    @Test
    public void checkEntities() {
        DBWeather moscowCurrentMock = ForecastTableTestUtil.MoscowCurrent();
        DBWeather moscowCurrentDb = weatherDAO.getCurrentWeather(524901);

        assertEquals(moscowCurrentMock.getTemperature(), moscowCurrentDb.getTemperature());
        assertEquals(moscowCurrentMock.getCondition(), moscowCurrentDb.getCondition());

        DBWeather kievCurrentMock = ForecastTableTestUtil.KievCurrent();
        DBWeather kievCurrentDb = weatherDAO.getCurrentWeather(703448);

        assertEquals(kievCurrentMock.getTemperature(), kievCurrentDb.getTemperature());
        assertEquals(kievCurrentMock.getHumidity(), kievCurrentDb.getHumidity());

        long time = kievCurrentDb.getTime();
        int cityId = kievCurrentDb.getCityId();
        DBWeather kievCurrentDbByCityIdAndTime = weatherDAO.getWeather(cityId, time);
        assertEquals(kievCurrentDb.getCondition(), kievCurrentDbByCityIdAndTime.getCondition());
        assertEquals(kievCurrentDb.getWindDegree(), kievCurrentDbByCityIdAndTime.getWindDegree());
    }

    @Test
    public void forecastQuery() {
        List<DBWeather> forecast = weatherDAO.getForecast(524901, System.currentTimeMillis());

        DBWeather moscowTomorrowMock = ForecastTableTestUtil.MoscowTomorrow();
        DBWeather moscowTomorrowDb = forecast.get(0);

        assertEquals(moscowTomorrowMock.getPressure(), moscowTomorrowDb.getPressure());
        assertEquals(moscowTomorrowMock.getWindSpeed(), moscowTomorrowDb.getWindSpeed());
    }

    @Test
    public void invalidateOldCache() {
        long currentTime = System.currentTimeMillis();
        List<DBWeather> oldWeather = weatherDAO.getByTime(currentTime);
        assertTrue(oldWeather.size() > 0);

        weatherDAO.deleteAll(oldWeather);
        List<DBWeather> allFromDb = weatherDAO.getAll();
        assertEquals(1, allFromDb.size());
    }

    @Test
    public void removeAllFromDb() {
        List<DBWeather> all = weatherDAO.getAll();
        assertTrue(!all.isEmpty());
        weatherDAO.deleteAll(all);
        assertEquals(0, weatherDAO.getAll().size());
    }
}
