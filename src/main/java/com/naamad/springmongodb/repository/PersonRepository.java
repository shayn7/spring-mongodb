package com.naamad.springmongodb.repository;

import com.naamad.springmongodb.collections.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {

    List<Person> findByFirstName(String name);

    @Query(value = "{ 'age': { $gt: ?0, $lt: ?1 } }", fields = "{addresses: 0}")
    List<Person> findPersonByAgeBetween(Integer min, Integer max);
}
