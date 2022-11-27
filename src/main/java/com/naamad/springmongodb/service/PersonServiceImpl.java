package com.naamad.springmongodb.service;

import com.naamad.springmongodb.collections.Person;
import com.naamad.springmongodb.dto.PersonRequest;
import com.naamad.springmongodb.dto.PersonResponse;
import com.naamad.springmongodb.exception.PersonRequestException;
import com.naamad.springmongodb.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public String save(PersonRequest personRequest) {
        Person person = Person
                .builder()
                .firstName(personRequest.getFirstName())
                .lastName(personRequest.getLastName())
                .age(personRequest.getAge())
                .hobbies(personRequest.getHobbies())
                .addresses(personRequest.getAddresses())
                .build();
        return personRepository.save(person).getPersonId();
    }

    @Override
    public List<PersonResponse> getPersonByName(String name) {
        List<Person> persons = personRepository.findByFirstName(name);
        return persons.stream().map(this::mapToPersonResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        Optional<Person> person = personRepository.findById(id);
        if(person.isEmpty()) throw new PersonRequestException("person not found");
        personRepository.deleteById(id);
    }

    @Override
    public List<PersonResponse> getByPersonAge(Integer minAge, Integer maxAge) {
        List<Person> persons = personRepository.findPersonByAgeBetween(minAge, maxAge);
        return persons.stream().map(this::mapToPersonResponse).collect(Collectors.toList());

    }

    @Override
    public List<PersonResponse> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return persons.stream().map(this::mapToPersonResponse).collect(Collectors.toList());
    }

    @Override
    public PersonResponse getPersonById(String id) {
        Optional<Person> person = personRepository.findById(id);
        if(person.isEmpty()) throw new PersonRequestException("person not found");
        return mapToPersonResponse(person.get());
    }

    @Override
    public Page<Person> search(String name, Integer minAge, Integer maxAge, String city, Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();

        if(name != null && !name.isEmpty())
            criteria.add((Criteria.where("firstName").regex(name, "i")));
        if(minAge != null && maxAge != null)
            criteria.add(Criteria.where("age").gte(minAge).lte(maxAge));
        if(city != null && !city.isEmpty())
            criteria.add(Criteria.where("addresses.city").is(city));
        if(!criteria.isEmpty())
            query.addCriteria(new Criteria().andOperator(criteria.toArray(criteria.toArray(new Criteria[0]))));

        return PageableExecutionUtils
                .getPage(mongoTemplate.find(
                          query,
                          Person.class),
                          pageable,
                          () -> mongoTemplate.count(query.skip(0).limit(0), Person.class));
    }

    @Override
    public void saveListOfPersons(Iterable<Person> list) {
        personRepository.saveAll(list);
    }

    @Override
    public List<Document> getOldestPerson() {
        
        UnwindOperation unwindOperation = Aggregation.unwind("addresses");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "age");
        GroupOperation groupOperation = Aggregation.group("addresses.city").first(Aggregation.ROOT).as("oldestPerson");

        Aggregation aggregation = Aggregation.newAggregation(unwindOperation, sortOperation, groupOperation);

        return mongoTemplate.aggregate(aggregation, Person.class, Document.class).getMappedResults();
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return PersonResponse
                .builder()
                .personId(person.getPersonId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .age(person.getAge())
                .hobbies(person.getHobbies())
                .addresses(person.getAddresses())
                .build();
    }

}
