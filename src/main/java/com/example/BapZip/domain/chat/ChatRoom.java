package com.example.BapZip.domain.chat;

import com.example.BapZip.domain.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "storeId")
    private Store store;

    @Column(length = 50)
    private String name;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessagesList=new ArrayList<>();


}