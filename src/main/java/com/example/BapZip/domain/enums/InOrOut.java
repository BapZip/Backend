package com.example.BapZip.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InOrOut {
    IN("교내") , OUT("교외");
    private String name;
    }
