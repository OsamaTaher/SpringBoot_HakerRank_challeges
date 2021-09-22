package com.hackerrank.weather.repository;

import java.util.Date;
import java.util.List;

import com.hackerrank.weather.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Integer> {
    public List<Weather> findAllByOrderByIdAsc();
    public List<Weather> findAllByOrderByDateAsc();
    public List<Weather> findAllByOrderByDateDesc();
    public List<Weather> findAllByDate(Date date);
    public List<Weather> findAllByCityIgnoreCase(String city);
}
