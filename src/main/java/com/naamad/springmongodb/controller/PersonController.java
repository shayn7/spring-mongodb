package com.naamad.springmongodb.controller;

import com.naamad.springmongodb.collections.Person;
import com.naamad.springmongodb.dto.PersonRequest;
import com.naamad.springmongodb.dto.PersonResponse;
import com.naamad.springmongodb.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String save(@RequestBody PersonRequest personRequest){
        return personService.save(personRequest);
    }

    @PostMapping("/persons_list")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveListOfPersons(@RequestBody Iterable<Person> personList){
         personService.saveListOfPersons(personList);
    }

    @GetMapping("/all_persons")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getAllPersons(){
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse getPersonById(@PathVariable String id){
        return personService.getPersonById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getPersonByName(@RequestParam("name") String name){
        return personService.getPersonByName(name);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id){
        personService.delete(id);
    }

    @GetMapping("/age")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getByPersonAge(@RequestParam Integer minAge,
                                       @RequestParam Integer maxAge){
        return personService.getByPersonAge(minAge, maxAge);
    }

    @GetMapping("/search")
    public Page<Person> searchPerson(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return personService.search(name, minAge, maxAge, city, pageable);
    }

    @GetMapping("/oldestPerson")
    public List<Document> getOldestPerson(){
        return personService.getOldestPerson();
    }
}
