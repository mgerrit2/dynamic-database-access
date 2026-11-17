package com.example.dynamicAccess.DynamicDatabaseAccess.repository;



import com.example.dynamicAccess.DynamicDatabaseAccess.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> { }
