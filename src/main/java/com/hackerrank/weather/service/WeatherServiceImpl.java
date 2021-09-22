package com.hackerrank.weather.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.model.weatherDto;
import com.hackerrank.weather.repository.WeatherRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 
 * @author https://github.com/OsamaTaher/Spring_boot_tests
 * 
 */
@Service
public class WeatherServiceImpl {
    @Autowired
    WeatherRepository weatherRepository;
    
    private static ModelMapper modelMapper = new ModelMapper();
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    

    /**
     * create new weather data and store in database with uniqe id
     * @param weather_dto
     * @return the created object reformatted to match date format
     * @throws ParseException
     */
    public weatherDto createNewWeather(weatherDto weather_dto) throws ParseException{
        weather_dto.setId(null);
        formatter.setLenient(false);
        Weather weather_entity = modelMapper.map(weather_dto, Weather.class);
        weather_entity.setDate(formatter.parse(weather_dto.getDate()));
        weather_entity = weatherRepository.save(weather_entity);
        return getWeatherDto(weather_entity);
    }


    /**
     * get weather data from database based on specific parameters and type of parameter
     * @param param Can be filters like Date,City or sorting
     * @param type  the type of parameter filter or sort
     * @see serviceEnum
     * @return      List of weatherDto ready to be returend to user
     * @throws ParseException
     */
    public List<weatherDto> getWeatherfilter(String param,serviceEnum type) throws ParseException{
        List<Weather> weathers_list = new ArrayList<Weather>();
        switch(type){
            case ByDate:
               Date formated_date =  formatter.parse(param);
               weathers_list =  weatherRepository.findAllByDate(formated_date);
               break;
            case ByCity:
            Comparator<Weather> ascendingOrder = (w1, w2) -> {
                return Integer.valueOf(w1.getId()).compareTo(w2.getId());
            };
            List<String> mycity = new ArrayList<String>(Arrays.asList(param.split(",")));
            for (String c : mycity) {
                weathers_list.addAll(weatherRepository.findAllByCityIgnoreCase(c));
            }
            Collections.sort(weathers_list, ascendingOrder);
            break;
            case Sort:
            if(param.equalsIgnoreCase("date"))
            weathers_list = weatherRepository.findAllByOrderByDateAsc();
            if(param.equalsIgnoreCase("-date"))
            weathers_list = weatherRepository.findAllByOrderByDateDesc();
            break;
            default:
            break;
        }
        return getListWeatherDto(weathers_list);
    }


    /**
     * get all Weathers data stored in H2 database ordered by Id in ascending order
     * @param void
     * @return formatted List Of weatherDto ready to be returned
     */
    public List<weatherDto> getAllOoderedById(){
        return getListWeatherDto(
            weatherRepository.findAllByOrderByIdAsc()
            );
    }


    /**
     * get Weather data stored in database using Id
     * @param id
     * @return formatted weatherDto ready to be returned
     */
    public weatherDto getWeatherUsingId(Integer id) {

        if (weatherRepository.existsById(id)) {
            Weather weatherEntity = weatherRepository.findById(id).get();
            return getWeatherDto(weatherEntity);
        }else{
            return null;
        }
    }


    /**
     * 
     * @param weathers_entity_list
     * @return Formmated lis of weatherDto
     */
    private List<weatherDto> getListWeatherDto(List<Weather> weathers_entity_list){
        formatter.setLenient(false);
        List<weatherDto> weathersDto_list = new ArrayList<weatherDto>();
        for (Weather weather_entity : weathers_entity_list) {
            weathersDto_list.add(getWeatherDto(weather_entity));
        }
        return weathersDto_list;
    }

    /**
     * 
     * @param weatherEntity
     * @return formmated weatherDto
     */
    private weatherDto getWeatherDto(Weather weatherEntity){
        String formated_date = formatter.format(weatherEntity.getDate());
        weatherDto w = modelMapper.map(weatherEntity, weatherDto.class);
        w.setDate(formated_date);
        return w;    
    }
}
