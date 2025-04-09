package com.demo.Repository.Item;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.Item.Item;

public interface ItemRepository extends JpaRepository<Item, Long> 
{
    //Custom method to find an item by its ID
    Optional<Item> findById(Long iid);  //Returns an Optional of Item to handle the case where the item might not be found
}