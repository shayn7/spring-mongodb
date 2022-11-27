package com.naamad.springmongodb.service;

import com.naamad.springmongodb.collections.Person;
import com.naamad.springmongodb.dto.PersonRequest;
import com.naamad.springmongodb.dto.PersonResponse;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {
    String save(PersonRequest personRequest);
    List<PersonResponse> getPersonByName(String name);

    void delete(String id);

    List<PersonResponse> getByPersonAge(Integer minAge, Integer maxAge);

    List<PersonResponse> getAllPersons();

    PersonResponse getPersonById(String id);

    Page<Person> search(String name, Integer minAge, Integer maxAge, String city, Pageable pageable);

    void saveListOfPersons(Iterable<Person> personList);

    List<Document> getOldestPerson();
}
