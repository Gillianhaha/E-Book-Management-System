package com.group.eBookManagementSystem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlreadyExistsException extends RuntimeException {

    private String message;

}
