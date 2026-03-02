package request;

import java.io.DataOutputStream;
import java.io.IOException;

import model.RequestModel;

public interface RequestHandler {
  public void handle(RequestModel request, DataOutputStream dataOutputStream) throws IOException;

  public Integer getApiKey();
}
