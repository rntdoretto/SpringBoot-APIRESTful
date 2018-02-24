package com.pontointeligente.api.audit.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pontointeligente.api.audit.documents.Audit;

public interface AuditRepository extends MongoRepository<Audit, String> {

	Audit findByNomeSistema(String nomeSistema);
	
	@Query("{ 'ip' : { &eq : ?0} }")
	List<Audit> findByIp(String ip);
}
