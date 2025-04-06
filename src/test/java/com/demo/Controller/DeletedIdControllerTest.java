package com.demo.Controller;
import com.demo.DTO.DeletedIdDTO;
import com.demo.Exception.DeletedIdNotFoundException;
import com.demo.Interface.DeletedIdService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)                     //Enable Mockito support for unit tests
class DeletedIdControllerTest 
{
    @Mock
    private DeletedIdService deletedIdService;          //Mock service dependency

    @InjectMocks
    private DeletedIdController deletedIdController;    //Inject the mocks into the controller being tested

    @Test   //Test for creating a Deleted ID - positive case
    void testCreateDeletedIdSuccess() 
    {
        DeletedIdDTO newDeletedIdDTO = new DeletedIdDTO();      //DTO to create
        newDeletedIdDTO.setDeletedId(1L);
        newDeletedIdDTO.setEntityType("User");

        //Mock the service method to return the created DTO
        when(deletedIdService.createDeletedId(any(DeletedIdDTO.class))).thenReturn(newDeletedIdDTO);

        ResponseEntity<DeletedIdDTO> response = deletedIdController.createDeletedId(newDeletedIdDTO);

        //Assert that the HTTP status is CREATED and the response body matches the DTO
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newDeletedIdDTO, response.getBody());
    }

    @Test   //Test for creating a Deleted ID - negative case (already exists)
    void testCreateDeletedIdAlreadyExists() 
    {
        DeletedIdDTO newDeletedIdDTO = new DeletedIdDTO();
        newDeletedIdDTO.setDeletedId(1L);
        newDeletedIdDTO.setEntityType("User");

        //Simulate the exception thrown when the ID already exists
        when(deletedIdService.createDeletedId(any(DeletedIdDTO.class))).thenThrow(new IllegalArgumentException("Deleted ID already exists"));

        ResponseEntity<DeletedIdDTO> response = deletedIdController.createDeletedId(newDeletedIdDTO);

        //Assert that the status is BAD_REQUEST (400) due to the exception
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test   //Test for getting a specific Deleted ID - positive case
    void testGetDeletedIdSuccess() 
    {
        DeletedIdDTO deletedIdDTO = new DeletedIdDTO();     //DTO for existing deleted ID
        deletedIdDTO.setDeletedId(1L);
        deletedIdDTO.setEntityType("User");

        //Mock the service to return the DTO when searching by ID
        when(deletedIdService.getDeletedId(1L)).thenReturn(Optional.of(deletedIdDTO));

        ResponseEntity<DeletedIdDTO> response = deletedIdController.getDeletedId(1L);

        //Assert that the HTTP status is OK and the body matches the DTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deletedIdDTO, response.getBody());
    }

    @Test   //Test for getting a specific Deleted ID - negative case (not found)
    void testGetDeletedIdNotFound() 
    {
        //Mock the service to return an empty Optional (ID not found)
        when(deletedIdService.getDeletedId(1L)).thenReturn(Optional.empty());

        ResponseEntity<DeletedIdDTO> response = deletedIdController.getDeletedId(1L);

        //Assert that the status is NOT_FOUND (404) since the ID doesn't exist
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   //Test for getting all Deleted IDs - positive case
    void testGetAllDeletedIdsSuccess() 
    {
        //Mock the list of deleted IDs
        List<DeletedIdDTO> deletedIdDTOList = Arrays.asList(new DeletedIdDTO(1L, "User"), 
        new DeletedIdDTO(2L, "Order"));

        //Mock the service method to return the list of IDs
        when(deletedIdService.getDeletedIds()).thenReturn(deletedIdDTOList);

        ResponseEntity<List<DeletedIdDTO>> response = deletedIdController.getDeletedIds();

        //Assert that the status is OK and the response body matches the list
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deletedIdDTOList, response.getBody());
    }

    @Test   //Test for updating a Deleted ID - positive case
    void testUpdateDeletedIdSuccess() 
    {
        DeletedIdDTO updatedDeletedIdDTO = new DeletedIdDTO();          //DTO for updated entity
        updatedDeletedIdDTO.setDeletedId(1L);
        updatedDeletedIdDTO.setEntityType("UserUpdated");

        //Mock the service to return the updated DTO
        when(deletedIdService.updateDeletedId(1L, updatedDeletedIdDTO)).thenReturn(updatedDeletedIdDTO);

        ResponseEntity<DeletedIdDTO> response = deletedIdController.updateDeletedId(1L, updatedDeletedIdDTO);

        //Assert that the status is OK and the body matches the updated DTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDeletedIdDTO, response.getBody());
    }

    @Test   //Test for updating a Deleted ID - negative case (not found)
    void testUpdateDeletedIdNotFound() 
    {
        DeletedIdDTO updatedDeletedIdDTO = new DeletedIdDTO();          //DTO for updated entity
        updatedDeletedIdDTO.setDeletedId(1L);
        updatedDeletedIdDTO.setEntityType("UserUpdated");

        //Simulate exception when the ID is not found for update
        when(deletedIdService.updateDeletedId(1L, updatedDeletedIdDTO)).thenThrow(new DeletedIdNotFoundException(1L));

        ResponseEntity<DeletedIdDTO> response = deletedIdController.updateDeletedId(1L, updatedDeletedIdDTO);

        //Assert that the status is NOT_FOUND (404) due to the exception
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test   //Test for deleting a Deleted ID - positive case
    void testDeleteDeletedIdSuccess() 
    {
        //Mock the service to do nothing on deletion
        doNothing().when(deletedIdService).deleteDeletedId(1L);

        ResponseEntity<Void> response = deletedIdController.deleteDeletedId(1L);

        //Assert that the status is NO_CONTENT (204) indicating successful deletion
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test   //Test for deleting a Deleted ID - negative case (not found)
    void testDeleteDeletedIdNotFound() 
    {
        //Simulate exception when the ID is not found for deletion
        doThrow(new DeletedIdNotFoundException(1L)).when(deletedIdService).deleteDeletedId(1L);

        ResponseEntity<Void> response = deletedIdController.deleteDeletedId(1L);

        //Assert that the status is NOT_FOUND (404) due to the exception
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}