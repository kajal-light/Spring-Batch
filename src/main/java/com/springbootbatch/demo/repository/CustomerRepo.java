package com.springbootbatch.demo.repository;

import com.springbootbatch.demo.entity.CutomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<CutomerEntity,Integer> {
}
