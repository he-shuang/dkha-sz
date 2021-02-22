package com.dkha.utils;
import com.dkha.model.aidoor.response.BaseResponse;
import com.dkha.aidoor.Constants;
public class ResponseFactory {

    public static BaseResponse createFailedResponse() {
        return createBaseResponse(Constants.CODE_RESPONSE_FAILED_BASE,
                Constants.MSG_RESPONSE_FAILED);
    }

    public static BaseResponse createSuccessResponse() {
        return createBaseResponse(Constants.CODE_RESPONSE_SUCCESS,
                Constants.MSG_RESPONSE_SUCCESS);

    }

    public static BaseResponse createFailedResponse(String msg) {
        return createBaseResponse(Constants.CODE_RESPONSE_FAILED_BASE, msg);
    }

    public static BaseResponse createBaseResponse(int code, String msg) {
        BaseResponse response = new BaseResponse();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
}
