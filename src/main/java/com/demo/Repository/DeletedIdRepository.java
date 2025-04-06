package com.demo.Repository;
import com.demo.Entity.DeletedID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository // Marks this interface as a Spring Data repository
public interface DeletedIdRepository extends JpaRepository<DeletedID, Long> 
{
    Optional<DeletedID> findByEntityType(String entityType);    //Custom query method to find a DeletedID by its entity type
}