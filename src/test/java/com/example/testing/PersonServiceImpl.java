package com.example.testing;

import com.example.infrastructure.validation.Validatable;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PersonServiceImpl implements PersonService, Validatable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Override
    public Person create(@Valid Person person, long timestamp) {
        LOGGER.info("Creating person {} at {}", person, timestamp);
        return new Person(UUID.randomUUID(), person.name(), person.age());
    }
}
