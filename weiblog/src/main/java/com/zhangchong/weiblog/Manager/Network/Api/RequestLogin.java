package com.zhangchong.weiblog.Manager.Network.Api;

import com.alibaba.fastjson.JSON;
import com.zhangchong.libnetwork.Core.Exception.AuthFailureException;
import com.zhangchong.libnetwork.Core.Exception.ParseException;
import com.zhangchong.libnetwork.Core.NetworkResponse;
import com.zhangchong.libnetwork.Core.Response;
import com.zhangchong.libnetwork.Tools.HttpHeaderParser;
import com.zhangchong.libnetwork.Tools.Request.JsonRequest;
import com.zhangchong.weiblog.Manager.Network.Bean.LoginBean;
import com.zhangchong.weiblog.Manager.Network.NetworkConfig;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhangchong on 2015/4/27.
 */
public class RequestLogin extends JsonRequest<LoginBean>{
    private String mName;
    private String mPwd;
    public RequestLogin(String name, String pwd, Response.Listener<LoginBean> listener, Response.ErrorListener errorListener) {
        super(Method.POST, NetworkConfig.URL_LOGIN, null, listener, errorListener);
        mName = name;
        mPwd = pwd;
    }

    @Override
    protected Map<String, String> getPostParams() throws AuthFailureException {
        HashMap<String, String> params = new HashMap<>();
        params.put(NetworkConfig.PARAMS_NAME, mName);
        params.put(NetworkConfig.PARAMS_PWD, mPwd);
        return params;
    }

    @Override
    public Response<LoginBean> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(JSON.parseObject(jsonString, LoginBean.class),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseException(e));
        } catch (Exception e){

        }
        return null;
    }
}
