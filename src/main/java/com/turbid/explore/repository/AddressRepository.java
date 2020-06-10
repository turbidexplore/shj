package com.turbid.explore.repository;

import com.turbid.explore.pojo.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,String> {

    List<Address> findByUsercodeAndStatus(String usercode,Integer status);

    @Modifying
    @Transactional
    @Query("update Address a set a.isdef=false where a.usercode=:usercode and a.code not in (:code)")
    int updateDef(@Param("usercode") String usercode,@Param("code") String code);

    Address findByUsercodeAndIsdefAndStatus(String usercode,Boolean isdef,Integer status);

    @Modifying
    @Transactional
    @Query("update Address a set a.status=1 where a.code =:code")
    int updateStatus(@Param("code")String code);
}
