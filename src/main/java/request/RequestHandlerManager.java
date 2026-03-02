package request;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.RequestModel;
import request.handler.ApiVersionsRequestHandler;

public class RequestHandlerManager {
  private Map<Integer, RequestHandler> apiKeyToHandlerMapping;

  public RequestHandlerManager() {
    apiKeyToHandlerMapping = new HashMap<>();

    apiKeyToHandlerMapping.put(18, new ApiVersionsRequestHandler());
  }

  public void manage(RequestModel request, DataOutputStream dataOutputStream)
      throws IllegalArgumentException, IOException {
    if (!apiKeyToHandlerMapping.containsKey(request.getApiKey())) {
      throw new IllegalArgumentException("API key " + request.getApiKey() + " is not supported.");
    }

    RequestHandler requestHandler = apiKeyToHandlerMapping.get(request.getApiKey());
    requestHandler.handle(request, dataOutputStream);
  }
}
