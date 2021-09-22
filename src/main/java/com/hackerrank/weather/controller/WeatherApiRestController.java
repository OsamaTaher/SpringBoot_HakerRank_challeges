package com.hackerrank.weather.controller;


import com.hackerrank.weather.model.weatherDto;

import com.hackerrank.weather.service.WeatherServiceImpl;
import com.hackerrank.weather.service.serviceEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;


@RestController
@RequestMapping(path = "/weather")
public class WeatherApiRestController {

    @Autowired
    WeatherServiceImpl weatherService;


    /**
     * - creates a new weather data record
     * <p>
        - expects a valid weather data object as its body payload, except that it does not have an id property; you can assume that the given object is always valid
        <p>
        - adds the given object to the collection and assigns a unique integer id to it
        <p>
        - the response code is 201 and the response body is the created record, including its unique id
     * @param weather
     * @return ResponseEntity with created weather body
     * @throws ParseException
     */
    @PostMapping
    public ResponseEntity<?> createWeather(@RequestBody weatherDto weather) throws ParseException {
        weatherDto w =  weatherService.createNewWeather(weather);
        return new ResponseEntity<>(w, HttpStatus.CREATED);
    }


    /**
     * - the response code is 200
     * <p>
    - the response body is an array of matching records, ordered by their ids in increasing order
        <p>
    - accepts an optional query string parameter, date, in the format YYYY-MM-DD, for example /weather/?date=2019-06-11. When this parameter is present, only the records with the matching date are returned.
        <p>
    - accepts an optional query string parameter, city, and when this parameter is present, only the records with the matching city are returned. The value of this parameter is case insensitive, so "London" and "london" are equivalent. Moreover, it might contain several values, separated by commas (e.g. city=london,Moscow), meaning that records with the city matching any of these values must be returned.
        <p>
    - accepts an optional query string parameter, sort, that can take one of two values: either "date" or "-date". If the value is "date", then the ordering is by date in ascending order. If it is "-date", then the ordering is by date in descending order. If there are two records with the same date, the one with the smaller id must come first.
     * @param date Optinal
     * @param city Optinal
     * @param sort Optinal
     * @return
     * @throws Exception
     */
    @GetMapping
    public ResponseEntity<List<?>> getWeathers(@RequestParam(required = false) String date, @RequestParam(required = false) String city, @RequestParam(required = false) String sort) throws ParseException {
        List<weatherDto> weathers = new ArrayList<>();
        if (date != null || city != null || sort != null) {
            if (date != null) {
            weathers = weatherService.getWeatherfilter(date,serviceEnum.ByDate);
            }
            if (city != null) {
                weathers = weatherService.getWeatherfilter(city,serviceEnum.ByCity);
            }
            if (sort != null) {
                weathers = weatherService.getWeatherfilter(sort, serviceEnum.Sort);
            }
            return ResponseEntity.ok().body(weathers);
        }

        weathers = weatherService.getAllOoderedById();
        return ResponseEntity.ok().body(weathers);

    }

    
    /**
     * - returns a record with the given id
     * <p>
     - if the matching record exists, the response code is 200 and the response body is the matching object
     * <p>
     - if there is no record in the collection with the given id, the response code is 404
     * @param id
     * @return 404 if not found or 200 with weather data if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getWeatherById(@PathVariable Integer id) {

        weatherDto ret_weather_dto = weatherService.getWeatherUsingId(id);
        if(ret_weather_dto == null){
            // ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
            // return responseEntity;
           return new ResponseEntity<Error>(HttpStatus.NOT_FOUND); 
        }else{
            return ResponseEntity.ok().body(ret_weather_dto);
        }
    }
}








