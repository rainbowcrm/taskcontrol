package com.primus.metadata.dao;

import com.primus.metadata.model.MetadataEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class MetadataDAO {

    @PersistenceContext
    protected EntityManager em;

    public MetadataEntity  getEntityDetails( String entityName)
    {
        return em.find(MetadataEntity.class,entityName);
    }
}
