package com.turbid.explore.repository;

import com.turbid.explore.pojo.Comment;
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
public interface CommentRepositroy extends JpaRepository<Comment,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Comment c where c.relation= :relation ")
    Page<Comment> listByPage(Pageable pageable,@Param("relation") String relation);

    @Query("SELECT count(c) from Comment c where c.relation= :relation ")
    int listByCount(@Param("relation") String relation);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Comment c where c.relation in (select b.code from Brand b where b.company.code=:relation)  ")
    Page<Comment> listByShopPage(Pageable pageable,@Param("relation") String relation);

    @Query("SELECT count(c) from Comment c where c.relation in (select b.code from Brand b where b.company.code=:relation) ")
    int listByShopCount(@Param("relation") String relation);

    @Query("SELECT c from Comment c where c.relation =:relation and c.userSecurity.phonenumber=:name  ")
    List<Comment> mycomments(@Param("name") String name, @Param("relation") String relation);
}
