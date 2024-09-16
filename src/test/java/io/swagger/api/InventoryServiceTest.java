package io.swagger.api;

import io.swagger.entity.InventoryEntity;
import io.swagger.model.InventoryItem;
import io.swagger.model.SellItempayload;
import io.swagger.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import io.swagger.repository.inventoryRepo;
import io.swagger.repository.userRepo;



import java.util.Optional;

class InventoryServiceTest {

    @Mock
    private inventoryRepo inventoryRepo;

    @Mock
    private userRepo userRepo;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateInventory() {
        InventoryItem item = new InventoryItem(1L,"ItemName", 10, 5.0f);
        inventoryService.createInventory(item);
        verify(inventoryRepo, times(1)).save(any(InventoryEntity.class));
    }

    @Test
    void testUpdateInventory() {
        InventoryItem item = new InventoryItem(1L, "UpdatedItemName", 20, 10.0f);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(new InventoryEntity("ItemName", 10, 5.0f)));
        inventoryService.updateInventory(item);
        verify(inventoryRepo, times(1)).save(any(InventoryEntity.class));
    }

    @Test
    void testCheckInventoryById() {
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(new InventoryEntity("ItemName", 10, 5.0f)));
        assertFalse(inventoryService.checkInventoryById(1L));
    }

    @Test
    void testDeleteInventory() {
        InventoryEntity inventoryEntity = new InventoryEntity("ItemName", 10, 5.0f);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(inventoryEntity));
        inventoryService.deleteInventory(1L);
        verify(inventoryRepo, times(1)).delete(any(InventoryEntity.class));
    }

    @Test
    void testFetchInventory() {
        inventoryService.fetchInventory(null);
        verify(inventoryRepo, times(1)).findAll();
        inventoryService.fetchInventory("ItemName");
        verify(inventoryRepo, times(1)).findByName("ItemName");
    }

    @Test
    void testSellInventory() {
        SellItempayload sellItemPayload = new SellItempayload(1L, 5);
        InventoryEntity inventoryEntity = new InventoryEntity("ItemName", 10, 5.0f);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(inventoryEntity));


        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getWalBalnce()).thenReturn(20.0f);
        when(userRepo.findByUserId(anyString())).thenReturn(userEntity);

        assertEquals("ItemName is sold at 5.0 each,which remains 5", inventoryService.sellInventory(sellItemPayload, "STAFF", "userId"));
    }

    @Test
    void testCheckWalletbalance() {
        SellItempayload sellItemPayload = new SellItempayload(1L, 5);
        InventoryEntity inventoryEntity = new InventoryEntity("ItemName", 10, 5.0f);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(inventoryEntity));

        // Mock UserEntity
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getWalBalnce()).thenReturn(20.0f);
        when(userRepo.findByUserId(anyString())).thenReturn(userEntity);

        assertTrue(inventoryService.checkWalletbalance(sellItemPayload, "STAFF", "userId"));
    }

    @Test
    void testCheckInventoryQuantity() {
        SellItempayload sellItemPayload = new SellItempayload(1L, 15);
        InventoryEntity inventoryEntity = new InventoryEntity("ItemName", 10, 5.0f);
        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(inventoryEntity));
        assertTrue(inventoryService.checkInventoryQuantity(sellItemPayload));
    }
}
