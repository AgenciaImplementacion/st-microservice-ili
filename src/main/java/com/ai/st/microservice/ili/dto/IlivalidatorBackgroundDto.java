package com.ai.st.microservice.ili.dto;

import java.io.Serializable;

public class IlivalidatorBackgroundDto implements Serializable {

    private static final long serialVersionUID = -5774043946431854011L;

    private String pathFile;
    private Long requestId;
    private Long supplyRequestedId;
    private String filenameTemporal;
    private Long userCode;
    private String observations;
    private String versionModel;
    private Boolean hasGeometryValidation;

    public IlivalidatorBackgroundDto() {
        this.versionModel = "2.9.4";
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getSupplyRequestedId() {
        return supplyRequestedId;
    }

    public void setSupplyRequestedId(Long supplyRequestedId) {
        this.supplyRequestedId = supplyRequestedId;
    }

    public String getFilenameTemporal() {
        return filenameTemporal;
    }

    public void setFilenameTemporal(String filenameTemporal) {
        this.filenameTemporal = filenameTemporal;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getVersionModel() {
        return versionModel;
    }

    public void setVersionModel(String versionModel) {
        this.versionModel = versionModel;
    }

    public Boolean getHasGeometryValidation() {
        return hasGeometryValidation;
    }

    public void setHasGeometryValidation(Boolean hasGeometryValidation) {
        this.hasGeometryValidation = hasGeometryValidation;
    }
}
