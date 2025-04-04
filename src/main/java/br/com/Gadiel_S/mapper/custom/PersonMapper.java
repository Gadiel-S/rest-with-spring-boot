package br.com.Gadiel_S.mapper.custom;

import br.com.Gadiel_S.data.dto.v2.PersonDTOV2;
import br.com.Gadiel_S.models.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDTO(Person person) {
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setBirthDay(new Date());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        return dto;
    }

    public Person convertDTOToEntity(PersonDTOV2 dto) {
        Person entity = new Person();
        dto.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        // entity.setBirthDay(new Date());
        entity.setAddress(dto.getAddress());
        entity.setGender(dto.getGender());
        return entity;
    }

}
