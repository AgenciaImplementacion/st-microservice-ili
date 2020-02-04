package com.ai.st.microservice.ili.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.ili.dto.VersionDataDto;
import com.ai.st.microservice.ili.dto.VersionDto;
import com.ai.st.microservice.ili.entities.ConceptEntity;
import com.ai.st.microservice.ili.entities.ModelEntity;
import com.ai.st.microservice.ili.entities.VersionConceptEntity;
import com.ai.st.microservice.ili.entities.VersionEntity;
import com.ai.st.microservice.ili.exceptions.BusinessException;
import com.ai.st.microservice.ili.services.IConceptService;
import com.ai.st.microservice.ili.services.IVersionService;

@Component
public class VersionBusiness {

	@Autowired
	private IVersionService versionService;

	@Autowired
	private IConceptService conceptService;

	public VersionDataDto getDataVersion(String versionName, Long conceptId) throws BusinessException {

		VersionDataDto versionDataDto = null;

		VersionEntity versionEntity = versionService.getVersionByName(versionName);

		ConceptEntity conceptEntity = conceptService.getConceptById(conceptId);

		if (versionEntity instanceof VersionEntity && conceptEntity instanceof ConceptEntity) {

			versionDataDto = new VersionDataDto();
			versionDataDto.setVersion(versionName);

			VersionConceptEntity versionConcept = versionEntity.getVersionsConcepts().get(0);

			String models = "";
			for (ModelEntity modelEntity : versionConcept.getModels()) {
				models += modelEntity.getName() + ";";
			}

			versionDataDto.setUrl(versionConcept.getUrl());
			versionDataDto.setModels(models);

		} else {
			throw new BusinessException("No se ha encontrado la versión");
		}

		return versionDataDto;
	}

	public List<VersionDto> getAvailableVersions() throws BusinessException {

		List<VersionDto> versionsDto = new ArrayList<>();

		List<VersionEntity> versionsEntity = versionService.getVersions();

		for (VersionEntity versionEntity : versionsEntity) {
			VersionDto versionDataDto = new VersionDto();
			versionDataDto.setId(versionEntity.getId());
			versionDataDto.setName(versionEntity.getName());
			versionDataDto.setCreatedAt(versionEntity.getCreatedAt());

			versionsDto.add(versionDataDto);
		}

		return versionsDto;
	}

}