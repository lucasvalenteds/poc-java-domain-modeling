package com.example.testing;

import com.example.infrastructure.validation.Validatable;
import jakarta.validation.Valid;

import java.util.UUID;

public class PersonServiceImpl implements PersonService, Validatable {

    @Override
    public Person create(@Valid Person person) {
        return new Person(UUID.randomUUID(), person.name(), person.age());
    }
}
