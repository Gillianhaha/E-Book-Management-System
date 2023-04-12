package com.group.eBookManagementSystem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EBookManagementServiceException extends RuntimeException {

    private String message;

}
