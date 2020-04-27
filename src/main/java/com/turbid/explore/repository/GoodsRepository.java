package com.turbid.explore.repository;

import com.turbid.explore.pojo.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<Goods,String> {
}
