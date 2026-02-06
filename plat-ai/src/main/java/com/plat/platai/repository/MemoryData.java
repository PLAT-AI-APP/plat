package com.plat.platai.repository;

public interface MemoryData<T> {
    
    Long getId();
    void setId(Long id);
    T get();
}
