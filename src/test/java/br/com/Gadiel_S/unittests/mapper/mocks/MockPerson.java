package br.com.Gadiel_S.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.Gadiel_S.data.dto.v1.PersonDTO;
import br.com.Gadiel_S.models.PersonDTO;

public class MockPerson {


    public PersonDTO mockEntity() {
        return mockEntity(0);
    }
    
    public PersonDTO mockDTO() {
        return mockDTO(0);
    }
    
    public List<PersonDTO> mockEntityList() {
        List<PersonDTO> persons = new ArrayList<PersonDTO>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonDTO> mockDTOList() {
        List<PersonDTO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockDTO(i));
        }
        return persons;
    }
    
    public PersonDTO mockEntity(Integer number) {
        PersonDTO person = new PersonDTO();
        person.setAddress("Address Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setLastName("Last Name Test" + number);
        return person;
    }

    public PersonDTO mockDTO(Integer number) {
        PersonDTO person = new PersonDTO();
        person.setAddress("Address Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setLastName("Last Name Test" + number);
        return person;
    }

}