package com.batch.service;

import com.batch.entities.Person;
import com.batch.persistence.IPersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonServiceImpl implements IPersonService{

    @Autowired
    private IPersonDAO personDAO;

    @Override
    @Transactional
    public void saveAll(List<Person> personList) {
        personDAO.saveAll(personList);

    }
}
