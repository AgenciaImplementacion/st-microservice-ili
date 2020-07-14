package com.ai.st.microservice.ili.business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.ili.drivers.PostgresDriver;
import com.ai.st.microservice.ili.dto.ItemRegistralRevisionDto;
import com.ai.st.microservice.ili.dto.QueryResultRegistralRevisionDto;
import com.ai.st.microservice.ili.entities.QueryEntity;
import com.ai.st.microservice.ili.entities.VersionConceptEntity;
import com.ai.st.microservice.ili.entities.VersionEntity;
import com.ai.st.microservice.ili.services.IVersionService;

@Component
public class QueryBusiness {

	@Autowired
	private IVersionService versionService;

	public QueryResultRegistralRevisionDto executeQueryRegistralToRevision(String modelVersion, Long conceptId,
			String host, String database, String port, String schema, String username, String password, int page,
			int limit) {

		QueryResultRegistralRevisionDto queryResultDto = new QueryResultRegistralRevisionDto();
		queryResultDto.setCurrentPage(page);

		List<ItemRegistralRevisionDto> records = new ArrayList<>();

		VersionEntity versionEntity = versionService.getVersionByName(modelVersion);
		if (versionEntity instanceof VersionEntity) {

			VersionConceptEntity versionConcept = versionEntity.getVersionsConcepts().stream()
					.filter(vC -> vC.getConcept().getId().equals(conceptId)).findAny().orElse(null);

			if (versionConcept != null) {

				PostgresDriver connection = new PostgresDriver();

				String urlConnection = "jdbc:postgresql://" + host + ":" + port + "/" + database;
				connection.connect(urlConnection, username, password, "org.postgresql.Driver");

				QueryEntity queryEntity = versionConcept.getQuerys().stream()
						.filter(q -> q.getQueryType().getId()
								.equals(QueryTypeBusiness.QUERY_TYPE_REGISTRAL_GET_RECORDS_TO_REVISION))
						.findAny().orElse(null);

				if (queryEntity != null) {

					String pageString = String.valueOf((page - 1));
					String limitString = String.valueOf(limit);

					String sqlObjects = queryEntity.getQuery().replace("{dbschema}", schema)
							.replace("{page}", pageString).replace("{limit}", limitString);

					ResultSet resultsetObjects = connection.getResultSetFromSql(sqlObjects);

					try {
						while (resultsetObjects.next()) {

							ItemRegistralRevisionDto item = new ItemRegistralRevisionDto();

							String file = resultsetObjects.getString("file");
							if (file != null) {
								item.setFileId(Long.parseLong(file));
							}

							String boundarySpace = resultsetObjects.getString("cabida_linderos");
							if (boundarySpace != null) {
								item.setBoundarySpace(boundarySpace);
							}

							String id = resultsetObjects.getString("t_id");
							if (id != null) {
								item.setId(Long.parseLong(id));
							}

							String newFmi = resultsetObjects.getString("numero_predial_nuevo_en_fmi");
							if (newFmi != null) {
								item.setNewFmi(newFmi);
							}

							String nomenclature = resultsetObjects.getString("nomenclatura_registro");
							if (nomenclature != null) {
								item.setNomenclature(nomenclature);
							}

							String oldFmi = resultsetObjects.getString("numero_predial_anterior_en_fmi");
							if (oldFmi != null) {
								item.setOldFmi(oldFmi);
							}

							String orip = resultsetObjects.getString("codigo_orip");
							if (orip != null) {
								item.setOrip(orip);
							}

							String realEstateRegistration = resultsetObjects.getString("matricula_inmobiliaria");
							if (realEstateRegistration != null) {
								item.setRealEstateRegistration(realEstateRegistration);
							}

							records.add(item);

						}
					} catch (SQLException e) {
						System.out.println("Error with query: " + e.getMessage());
					}

					QueryEntity queryCountEntity = versionConcept.getQuerys().stream()
							.filter(q -> q.getQueryType().getId()
									.equals(QueryTypeBusiness.QUERY_TYPE_COUNT_REGISTRAL_GET_RECORDS_TO_REVISION))
							.findAny().orElse(null);
					String sqlCount = queryCountEntity.getQuery().replace("{dbschema}", schema);
					long countRecords = connection.count(sqlCount);
					queryResultDto.setTotalPages((int) countRecords / limit);

					connection.disconnect();

				}

			}

		}

		queryResultDto.setRecords(records);

		return queryResultDto;

	}

}
