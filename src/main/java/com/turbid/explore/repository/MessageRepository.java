package com.turbid.explore.repository;


import com.turbid.explore.pojo.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Date;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,String> {

    @Query("SELECT count(m) from Message m where m.mebile=:mebile and m.authcode=:authcode and  m.create_time>= :now ")
    int findCountByMailAndAuthode(@Param("mebile") String mebile, @Param("authcode") String authcode, @Param("now") Date now);

    @Query("SELECT count(m) from Message m where m.email=:mail and m.authcode=:authcode and  m.create_time>= :now ")
    int findMessagesByMailAndAuthode(@Param("mail") String mail, @Param("authcode") String authcode, @Param("now") Date now);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT m.authcode from Message m where m.mebile=:mebile and m.create_time>= :now order by m.create_time desc ")
    Page<String> findCodeByPhone(Pageable pageable, @Param("mebile") String mebile, @Param("now") Date now);
}
