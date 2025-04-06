package com.demo.Controller;
import com.demo.DTO.DeletedIdDTO;
import com.demo.Exception.DeletedIdNotFoundException;
import com.demo.Interface.DeletedIdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController                                         //This annotation marks the class as a controller for RESTful web services
@RequestMapping("/deleted-ids")                         //Defines the base URI for all the API endpoints in this controller
public class DeletedIdController 
{
    private final DeletedIdService deletedIdService;    //This is a service dependency for handling the business logic

    //Constructor to inject the DeletedIdService dependency
    public DeletedIdController(DeletedIdService deletedIdService) 
    {
        this.deletedIdService = deletedIdService;
    }

    @PostMapping  //Maps POST requests to /api/deletedIds
    public ResponseEntity<DeletedIdDTO> createDeletedId(@RequestBody DeletedIdDTO deletedIdDTO) 
    {
        try 
        {
            DeletedIdDTO createdDTO = deletedIdService.createDeletedId(deletedIdDTO);   //Calls the service to create a new DeletedId           
            return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);                //Returns the created DTO with HTTP status 201 (Created)
        } 
        
        catch (IllegalArgumentException e) 
        {
            //Handles the case when the input is invalid, returns HTTP 400 (Bad Request)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}") //Maps GET requests to /api/deletedIds/{id}
    public ResponseEntity<DeletedIdDTO> getDeletedId(@PathVariable Long id) 
    {
        //Tries to fetch the DeletedIdDTO from the service using the provided ID
        Optional<DeletedIdDTO> deletedIdDTO = deletedIdService.getDeletedId(id);

        //If found, return the DTO with HTTP status 200 (OK), otherwise return HTTP 404 (Not Found)
        return deletedIdDTO.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping //Maps GET requests to /api/deletedIds
    public ResponseEntity<List<DeletedIdDTO>> getDeletedIds() 
    {
        List<DeletedIdDTO> deletedIdDTOs = deletedIdService.getDeletedIds();    //Fetches all DeletedIdDTOs from the service
        return new ResponseEntity<>(deletedIdDTOs, HttpStatus.OK);              //Returns the list with HTTP status 200 (OK)
    }

    @PutMapping("/{id}") //Maps PUT requests to /api/deletedIds/{id}
    public ResponseEntity<DeletedIdDTO> updateDeletedId(@PathVariable Long id, @RequestBody DeletedIdDTO updatedDTO) 
    {
        try 
        {
            DeletedIdDTO updated = deletedIdService.updateDeletedId(id, updatedDTO);    //Calls the service to update the existing DeletedId
            return new ResponseEntity<>(updated, HttpStatus.OK);                        //Returns the updated DTO with HTTP status 200 (OK)
        } 
        
        catch (DeletedIdNotFoundException e) 
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  //Handles the case when the ID does not exist, returns HTTP 404 (Not Found)
        }
    }

    @DeleteMapping("/{id}") //Maps DELETE requests to /api/deletedIds/{id}
    public ResponseEntity<Void> deleteDeletedId(@PathVariable Long id) 
    {
        try 
        {
            deletedIdService.deleteDeletedId(id);               //Calls the service to delete the DeletedId
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //Returns HTTP 204 (No Content) to indicate successful deletion
        } 
        
        catch (DeletedIdNotFoundException e) 
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  //Handles the case when the ID does not exist, returns HTTP 404 (Not Found)
        }
    }
}