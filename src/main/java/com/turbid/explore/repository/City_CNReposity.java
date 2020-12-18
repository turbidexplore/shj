package com.turbid.explore.repository;

import com.turbid.explore.pojo.City_CN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface City_CNReposity extends JpaRepository<City_CN,String> {

    @Query("select c.country from City_CN c group by c.country")
    List<String> countrys();

    @Query("select c.state from City_CN c where c.country=:country and c.state is not null group by c.state")
    List<String> states(@Param("country")String country);

    @Query("select c.city from City_CN c where c.state=:state")
    List<String> citys(@Param("state")String state);

    @Query("select c.city from City_CN c where c.country=:country")
    List<String> citys1(@Param("country")String country);
}
