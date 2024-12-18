package com.clientes.crud.service;

import com.clientes.crud.dto.ClientDTO;
import com.clientes.crud.entities.Client;
import com.clientes.crud.repositories.ClientRepository;
import com.clientes.crud.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Optional<Client> result = repository.findById(id);
        Client client = result.orElseThrow(
                () -> new ResourceNotFoundException("Cliente não encontrado")
        );
        ClientDTO dto = new ClientDTO(client);
        return dto;
    }


    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable){
        Page<Client> result = repository.findAll(pageable);
        return result.map(x-> new ClientDTO(x));
    }


    @Transactional
    public ClientDTO insert (ClientDTO dto){
        Client entity = new Client();
        copyDtoToEntity(dto,entity);
        entity = repository.save(entity);

        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update (Long id, ClientDTO dto){

        try {
            Client entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ClientDTO(entity);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id não existente");
        }

    }

    @Transactional
    public void delete(Long id){
       repository.deleteById(id);
    }




    private void copyDtoToEntity(ClientDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }


}
