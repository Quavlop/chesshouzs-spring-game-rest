package com.chesshouzs.server.model;

public interface ModelInterface<E, Dto> {
    Dto convertToDto(E obj);
}