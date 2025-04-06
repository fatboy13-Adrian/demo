package com.demo.Interface;
import java.util.List;
import java.util.Optional;
import com.demo.DTO.DeletedIdDTO;

public interface DeletedIdService 
{
    DeletedIdDTO createDeletedId(DeletedIdDTO deletedIdDTO);            //Method to create a new Deleted ID entry.
    Optional<DeletedIdDTO> getDeletedId(Long id);                       //Method to retrieve a Deleted ID by its ID.
    List<DeletedIdDTO> getDeletedIds();                                 //Method to retrieve all Deleted IDs.
    DeletedIdDTO updateDeletedId(Long id, DeletedIdDTO deletedIdDTO);   //Method to update an existing Deleted ID.
    void deleteDeletedId(Long id);                                      //Method to delete a Deleted ID by its ID.
}