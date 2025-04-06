package com.demo.Service;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.demo.DTO.DeletedIdDTO;
import com.demo.Entity.DeletedID;
import com.demo.Exception.DeletedIdNotFoundException;
import com.demo.Interface.DeletedIdService;
import com.demo.Repository.DeletedIdRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service    //Marks this class as a Spring Service component
public class DeletedIdServiceImpl implements DeletedIdService 
{
    //Injecting the DeletedIdRepository via constructor
    private final DeletedIdRepository deletedIdRepository;

    //Constructor to initialize the service with the repository
    public DeletedIdServiceImpl(DeletedIdRepository deletedIdRepository) 
    {
        this.deletedIdRepository = deletedIdRepository;
    }

    @Override   //Method to create a new Deleted ID entry
    public DeletedIdDTO createDeletedId(DeletedIdDTO deletedIdDTO) 
    {
        //Validating the DTO before creating
        validateDeletedIdDTO(deletedIdDTO, false);

        //Check if a Deleted ID with the same ID already exists
        if (deletedIdDTO.getDeletedId() != null && deletedIdRepository.existsById(deletedIdDTO.getDeletedId())) 
            throw new IllegalArgumentException("Deleted ID with ID " + deletedIdDTO.getDeletedId() + " already exists.");

        //Save the entity and map it to DTO before returning
        DeletedID saved = deletedIdRepository.save(mapToEntity(deletedIdDTO));
        
        return mapToDTO(saved);
    }

    @Override   //Method to retrieve a specific Deleted ID by its ID
    public Optional<DeletedIdDTO> getDeletedId(Long id) 
    {
        validateId(id); //Validate the ID

        //Fetch the entity from the repository and map it to DTO
        return deletedIdRepository.findById(id).map(this::mapToDTO);
    }

    
    @Override   //Method to retrieve all Deleted IDs
    public List<DeletedIdDTO> getDeletedIds() 
    {
        //Fetch all entities from the repository, map to DTOs and return as a list
        return deletedIdRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override   //Method to update an existing Deleted ID
    public DeletedIdDTO updateDeletedId(Long id, DeletedIdDTO updatedDTO) 
    {
        //Validate the ID and DTO before updating
        validateId(id);
        validateDeletedIdDTO(updatedDTO, true);

        //Fetch the existing entity, or throw an exception if not found
        DeletedID existing = deletedIdRepository.findById(id).orElseThrow(() -> new DeletedIdNotFoundException(id));

        //Update the entity with new data from the DTO
        existing.setEntityType(updatedDTO.getEntityType());
        DeletedID updated = deletedIdRepository.save(existing);

        return mapToDTO(updated);   //Return the updated entity as a DTO
    }

    @Override   //Method to delete a Deleted ID by its ID
    public void deleteDeletedId(Long id) 
    {
        validateId(id);                     //Validate the ID before deleting

        //Check if the Deleted ID exists, if not, throw an exception
        if (!deletedIdRepository.existsById(id))
            throw new DeletedIdNotFoundException(id);

        deletedIdRepository.deleteById(id); //Perform the deletion
    }

    //Method to validate the DeletedIdDTO before creation or update
    private void validateDeletedIdDTO(DeletedIdDTO dto, boolean isUpdate) 
    {
        //Ensure the DTO is not null
        if (dto == null) 
            throw new IllegalArgumentException("DeletedIdDTO cannot be null.");

        //Ensure the entity type is not null or empty
        if (!StringUtils.hasText(dto.getEntityType())) 
            throw new IllegalArgumentException("EntityType must not be null or empty.");

        //If updating, ensure the deleted ID is valid
        if (isUpdate && dto.getDeletedId() != null && dto.getDeletedId() <= 0) 
            throw new IllegalArgumentException("Invalid deleted ID.");
    }

    //Method to validate that the ID is a positive number
    private void validateId(Long id) 
    {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("ID must be a positive number.");
    }

    //Method to map the DeletedID entity to a DeletedIdDTO
    private DeletedIdDTO mapToDTO(DeletedID entity) 
    {
        return DeletedIdDTO.builder().deletedId(entity.getId()).entityType(entity.getEntityType()).build();
    }

    //Method to map the DeletedIdDTO to a DeletedID entity
    private DeletedID mapToEntity(DeletedIdDTO dto) 
    {
        return DeletedID.builder().id(dto.getDeletedId()).entityType(dto.getEntityType()).build();
    }
}