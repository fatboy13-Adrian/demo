package com.demo.Service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;
import com.demo.DTO.DeletedIdDTO;
import com.demo.Entity.DeletedID;
import com.demo.Exception.DeletedIdNotFoundException;
import com.demo.Repository.DeletedIdRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)                     //Using Mockito extension for mocking
class DeletedIdServiceImplTest 
{
    @Mock
    private DeletedIdRepository deletedIdRepository;    //Mocking the repository

    @InjectMocks
    private DeletedIdServiceImpl deletedIdService;      //Injecting mocks into the service

    @Test
    void testCreateDeletedId_Positive() 
    {
        //Test the creation of a valid DeletedIdDTO
        DeletedIdDTO dto = DeletedIdDTO.builder().entityType("TestEntity").build();
        DeletedID saved = DeletedID.builder().id(1L).entityType("TestEntity").build();

        //Mock repository save operation to return the saved entity
        when(deletedIdRepository.save(any(DeletedID.class))).thenReturn(saved);

        //Call service method and check if the result matches the expected output
        DeletedIdDTO result = deletedIdService.createDeletedId(dto);

        assertEquals(1L, result.getDeletedId());
        assertEquals("TestEntity", result.getEntityType());
    }

    @Test
    void testCreateDeletedId_EmptyEntityType_Negative() 
    {
        //Test case when entity type is empty
        DeletedIdDTO dto = DeletedIdDTO.builder().entityType("").build();

        //Expecting IllegalArgumentException because the entity type cannot be empty
        Exception ex = assertThrows(IllegalArgumentException.class, () -> deletedIdService.createDeletedId(dto));

        assertEquals("EntityType must not be null or empty.", ex.getMessage());
    }

    @Test
    void testCreateDeletedId_ExistingId_Negative() 
    {
        //Test case when trying to create a Deleted ID that already exists
        DeletedIdDTO dto = DeletedIdDTO.builder().deletedId(1L).entityType("Dup").build();

        //Mock repository to return true for existence check
        when(deletedIdRepository.existsById(1L)).thenReturn(true);

        //Expecting IllegalArgumentException because the ID already exists
        Exception ex = assertThrows(IllegalArgumentException.class, () -> deletedIdService.createDeletedId(dto));

        assertEquals("Deleted ID with ID 1 already exists.", ex.getMessage());
    }

    @Test
    void testCreateDeletedId_NullDTO_Negative() 
    {
        //Test case when input DTO is null
        assertThrows(IllegalArgumentException.class, () -> deletedIdService.createDeletedId(null));
    }

    @Test
    void testGetDeletedId_Positive() 
    {
        //Test case to get a Deleted ID by its ID
        DeletedID entity = DeletedID.builder().id(1L).entityType("Entity").build();

        //Mock repository to return a valid entity for the given ID
        when(deletedIdRepository.findById(1L)).thenReturn(Optional.of(entity));

        //Call service method and check the result
        Optional<DeletedIdDTO> result = deletedIdService.getDeletedId(1L);

        assertTrue(result.isPresent());
        assertEquals("Entity", result.get().getEntityType());
    }

    @Test
    void testGetDeletedId_InvalidId_Negative() 
    {
        //Test case when invalid ID is provided (negative value)
        assertThrows(IllegalArgumentException.class, () -> deletedIdService.getDeletedId(-1L));
    }

    @Test
    void testGetDeletedIds_Positive() 
    {
        //Test case to get all Deleted IDs
        List<DeletedID> mockList = List.of(DeletedID.builder().id(1L).entityType("One").build(),
        DeletedID.builder().id(2L).entityType("Two").build());

        //Mock repository to return the mock list
        when(deletedIdRepository.findAll()).thenReturn(mockList);

        //Call service method and check if the result size matches the mock list size
        List<DeletedIdDTO> result = deletedIdService.getDeletedIds();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdateDeletedId_Positive() 
    {
        //Test case to update an existing Deleted ID
        DeletedID existing = DeletedID.builder().id(1L).entityType("Old").build();
        DeletedID updated = DeletedID.builder().id(1L).entityType("New").build();
        DeletedIdDTO updateDTO = DeletedIdDTO.builder().entityType("New").build();

        //Mock repository to return the existing entity and then return updated entity on save
        when(deletedIdRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(deletedIdRepository.save(existing)).thenReturn(updated);

        //Call service method and verify the result after update
        DeletedIdDTO result = deletedIdService.updateDeletedId(1L, updateDTO);

        assertEquals("New", result.getEntityType());
    }

    @Test
    void testUpdateDeletedId_InvalidId_Negative() 
    {
        //Test case when updating a Deleted ID with invalid ID (0L)
        DeletedIdDTO dto = DeletedIdDTO.builder().entityType("Update").build();

        //Expecting IllegalArgumentException because the ID is invalid
        assertThrows(IllegalArgumentException.class, () -> deletedIdService.updateDeletedId(0L, dto));
    }

    @Test
    void testUpdateDeletedId_NotFound_Negative() 
    {
        //Test case when trying to update a Deleted ID that does not exist
        DeletedIdDTO dto = DeletedIdDTO.builder().entityType("Update").build();

        //Mock repository to return Optional.empty() when searching for the ID
        when(deletedIdRepository.findById(100L)).thenReturn(Optional.empty());

        //Expecting DeletedIdNotFoundException because the ID is not found
        assertThrows(DeletedIdNotFoundException.class, () -> deletedIdService.updateDeletedId(100L, dto));
    }

    @Test
    void testDeleteDeletedId_Positive() 
    {
        //Test case to delete an existing Deleted ID
        when(deletedIdRepository.existsById(1L)).thenReturn(true);
        doNothing().when(deletedIdRepository).deleteById(1L);

        //Assert that no exception is thrown when deleting
        assertDoesNotThrow(() -> deletedIdService.deleteDeletedId(1L));

        //Verify that deleteById was called once
        verify(deletedIdRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteDeletedId_NotFound_Negative() 
    {
        //Test case when trying to delete a Deleted ID that does not exist
        when(deletedIdRepository.existsById(99L)).thenReturn(false);

        //Expecting DeletedIdNotFoundException because the ID is not found
        assertThrows(DeletedIdNotFoundException.class, () -> deletedIdService.deleteDeletedId(99L));
    }

    @Test
    void testDeleteDeletedId_InvalidId_Negative() 
    {
        //Test case when deleting a Deleted ID with invalid ID (negative value)
        assertThrows(IllegalArgumentException.class, () -> deletedIdService.deleteDeletedId(-1L));
    }
}