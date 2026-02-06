package com.plat.platai.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MemoryRepository<T> {

    private Long ids = 0L;
    private final Map<Long, T> db = new ConcurrentHashMap<>();


    public T save(MemoryData<T> t) {
        t.setId(ids++);
        db.put(t.getId(), t.get());
        return t.get();
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    public void removeById(Long id) {
        db.remove(id);
    }

}

