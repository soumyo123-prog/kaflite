package model;

public class RequestModel {
  private Integer messageSize;
  private Integer apiKey;
  private Integer apiVersion;
  private Integer correlationId;

  public Integer getMessageSize() {
    return messageSize;
  }

  public Integer getApiKey() {
    return apiKey;
  }

  public Integer getApiVersion() {
    return apiVersion;
  }

  public Integer getCorrelationId() {
    return correlationId;
  }

  public void setMessageSize(Integer messageSize) {
    this.messageSize = messageSize;
  }

  public void setApiKey(Integer apiKey) {
    this.apiKey = apiKey;
  }

  public void setApiVersion(Integer apiVersion) {
    this.apiVersion = apiVersion;
  }

  public void setCorrelationId(Integer correlationId) {
    this.correlationId = correlationId;
  }

}
