package com.ixigo.demmanager.services.interfaces;

import java.util.Map;
import java.util.Optional;

import reactor.core.publisher.Flux;

public interface ChartsData {
	public Flux<?> getDataForTable(String tableName, Optional<Map<String, String>> whereClause);
}
