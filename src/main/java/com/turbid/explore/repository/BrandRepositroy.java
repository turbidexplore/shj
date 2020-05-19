package com.turbid.explore.repository;

import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface BrandRepositroy extends JpaRepository<Brand,String> {

    @Query("select b from Brand b where b.company.code=:code")
    List<Brand> getByShop(@Param("code") String code);

    @Query( "select new Brand(s.code,s.name,s.logo) from Brand s where s.label LIKE CONCAT(:label,'%') and s.brandgroup LIKE CONCAT(:brandgroup,'%') ")
    List<Brand> getByLabel(@Param("label")String label, @Param("brandgroup") String brandgroup);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select b.name from Brand b where b.company.code=:code ")
   Page<String> getOneByShop(Pageable pageable, @Param("code") String code);
}
