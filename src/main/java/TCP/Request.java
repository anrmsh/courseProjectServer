package TCP;

import Enums.RequestType;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType requestType;
    private String requestMessage;
    private String userLogin;

    public Request(RequestType requestType, String requestMessage, int userId) {
        this.requestType = requestType;
        this.requestMessage = requestMessage;
        this.userLogin = userLogin;
    }

    public Request(RequestType requestType, String requestMessage) {
        this.requestType = requestType;
        this.requestMessage = requestMessage;
    }
    public Request(){

    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}
