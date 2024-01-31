package com.example.BapZip.web.controller;

import com.example.BapZip.domain.Store;
import com.example.BapZip.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreRepository storeRepository;

    @GetMapping
    public List<Store> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        stores.forEach(System.out::println);
        return stores;
    }
}
