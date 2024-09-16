package io.swagger.api;

import io.swagger.entity.InventoryEntity;
import io.swagger.entity.UserEntity;
import io.swagger.model.InventoryItem;
import io.swagger.model.SellItempayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.swagger.repository.inventoryRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class InventoryService {

    @Autowired
    private final inventoryRepo inventoryRepo;

    @Autowired
    private final io.swagger.repository.userRepo userRepo;

    public InventoryService(io.swagger.repository.inventoryRepo inventoryRepo, io.swagger.repository.userRepo userRepo) {
        this.inventoryRepo = inventoryRepo;
        this.userRepo = userRepo;
    }


    public void createInventory(InventoryItem body){
       InventoryEntity  i = new InventoryEntity(body.getName() , body.getQuantity(), body.getPrice());

        inventoryRepo.save(i);

    }

    public void updateInventory(InventoryItem body) {
        Optional<InventoryEntity> optionalInventory = inventoryRepo.findById(body.getId());


        InventoryEntity existingInventory = optionalInventory.get();

        // Update the inventory fields
        existingInventory.setName(body.getName());
        existingInventory.setQuantity(body.getQuantity());
        existingInventory.setPrice(body.getPrice());
        // Update other fields as needed

        // Save the updated inventory entity
        inventoryRepo.save(existingInventory);



    }



    public boolean checkInventoryById(Long id) {
        Optional<InventoryEntity> optionalInventory = inventoryRepo.findById(id);

        if (optionalInventory.isPresent()) {

            return false;
        }
        return true;
    }

    public void deleteInventory(Long itemId) {

        Optional<InventoryEntity> optionalInventory = inventoryRepo.findById(itemId);
        InventoryEntity existingInventory = optionalInventory.get();

        inventoryRepo.delete(existingInventory);
    }

    public List<InventoryEntity> fetchInventory(String name) {
        if(name != null){

            List<InventoryEntity> i = new ArrayList<>();
            i.add(inventoryRepo.findByName(name));

            return i;
        }

        return inventoryRepo.findAll();
    }

    public String sellInventory(SellItempayload body , String role , String userId) {
        Optional<InventoryEntity> inventory = inventoryRepo.findById(body.getId());
        InventoryEntity sellingInventory = inventory.get();

        Integer quantity =  sellingInventory.getQuantity() - body.getQuantity();
        sellingInventory.setQuantity(quantity);

        Float prize = sellingInventory.getPrice();
        Float totalPrize = body.getQuantity() * prize;

        if (role.equals("STAFF")){
            UserEntity u = userRepo.findByUserId(userId);

            Float oldwalletbalance = u.getWalBalnce();
            Float newWalletBalance = oldwalletbalance - totalPrize;

            u.setWalBalnce(newWalletBalance);

            userRepo.save(u);
        }

        inventoryRepo.save(sellingInventory);

        return  sellingInventory.getName() + " is sold at " + sellingInventory.getPrice() + " each,which remains "  +  quantity;

    }

    public boolean checkWalletbalance(SellItempayload body ,String role ,String userId) {
        if (role == "STAFF") {
            UserEntity u = userRepo.findByUserId(userId);

            Optional<InventoryEntity> inventory = inventoryRepo.findById(body.getId());
            InventoryEntity sellingInventory = inventory.get();

            Integer quantity =  sellingInventory.getQuantity() - body.getQuantity();
            sellingInventory.setQuantity(quantity);

            Float prize = sellingInventory.getPrice();
            Float totalPrize = quantity * prize;

            if (u.getWalBalnce() <= totalPrize){
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean checkInventoryQuantity(SellItempayload body) {
        Optional<InventoryEntity> inventory = inventoryRepo.findById(body.getId());
        InventoryEntity sellingInventory = inventory.get();
        if(body.getQuantity() > sellingInventory.getQuantity()){
            return true;
        }
        return false;
    }
}
