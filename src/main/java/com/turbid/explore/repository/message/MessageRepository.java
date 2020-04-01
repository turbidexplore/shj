package com.turbid.explore.repository.message;


import com.turbid.explore.pojo.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,String> {

    @Query("SELECT count(m) from Message m where m.mebile=:mebile and m.authcode=:authcode and  m.create_time>= :now ")
    int findCountByMailAndAuthode(@Param("mebile") String mebile, @Param("authcode") String authcode, @Param("now") Calendar now);

    @Query("SELECT count(m) from Message m where m.email=:mail and m.authcode=:authcode and  m.create_time>= :now ")
    int findMessagesByMailAndAuthode(@Param("mail") String mail, @Param("authcode") String authcode, @Param("now") Calendar now);

    @Query("SELECT m from Message m where m.mebile=:mebile and  m.create_time>= :now order by m.create_time desc ")
    List<Message> findCodeByPhone(@Param("mebile") String mebile, @Param("now") Calendar now);
}
