package com.zhangchong.libnetwork;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.zhangchong.libnetwork.Core.Cache;
import com.zhangchong.libnetwork.Core.Dispatch.HttpExcutor;
import com.zhangchong.libnetwork.Core.Dispatch.NetworkExcutor;
import com.zhangchong.libnetwork.Core.Exception.NetException;
import com.zhangchong.libnetwork.Core.NetworkResponse;
import com.zhangchong.libnetwork.Core.Request;
import com.zhangchong.libnetwork.Core.RequestQueue;
import com.zhangchong.libnetwork.Core.Response;
import com.zhangchong.libnetwork.Tools.Cache.DataBaseCache;
import com.zhangchong.libnetwork.Tools.Excutor.BasicNetworkExcutor;
import com.zhangchong.libnetwork.Tools.Excutor.HttpClientExcutor;
import com.zhangchong.libnetwork.Tools.Excutor.HurlExcutor;
import com.zhangchong.libnetwork.Tools.NetworkSSLManager;

import java.io.File;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Zhangchong on 2015/4/16.
 */
public class NetworkManager {
    private static final String DEFAULT_CACHE_DIR = "network";
    public static NetworkManager networkManager;
    private final RequestQueue mRequestQueue;
    private NetworkExcutor mNetworkExcutor;
    private Cache mCache;
    public static NetworkManager getInstance() {
        return networkManager;
    }
    private Context mContext;

    public static NetworkManager createNetworkManager(Context context) {
        if(networkManager == null){
            networkManager = new NetworkManager(context);
        }
        return networkManager;
    }

    private NetworkManager(Context context){
        mContext = context;
        mRequestQueue = initRequestQueue(context, null);
    }

    public Response<?> startRquestSync(Request<?> request) throws NetException{
        if(mNetworkExcutor != null){
            NetworkResponse networkResponse = mNetworkExcutor.performRequest(request);
            if(networkResponse == null)
                return null;
            else{
                return parseNetworkResponse(request, networkResponse);
            }
        }
        return null;
    }

    private Response<?> parseNetworkResponse(Request<?> request, NetworkResponse networkResponse){
        request.addMarker("network-http-complete");

        Response<?> response = null;
        if (networkResponse.notModified && request.hasHadResponseDelivered()) {
            request.finish("not-modified");
            response = request.parseNetworkResponse(networkResponse);
            return response;
        }

        response = request.parseNetworkResponse(networkResponse);
        request.addMarker("network-parse-complete");

        if (request.shouldCache() && response.cacheEntry != null) {
            mCache.put(request.getCacheKey(), response.cacheEntry);
            request.addMarker("network-cache-written");
        }

        return response;
    }

    public void startRquestAsync(Request<?> request){
        this.addRequestToQuene(request);
    }

    private void addRequestToQuene(Request<?> request) {
        mRequestQueue.add(request);
    }

    private RequestQueue initRequestQueue(Context context, HttpExcutor stack) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

        String userAgent = null;
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                SSLSocketFactory factory = null;
                try {
                    factory = new NetworkSSLManager(context).getContext().getSocketFactory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stack = new HurlExcutor(null, factory);
            } else {
                stack = new HttpClientExcutor(AndroidHttpClient.newInstance(userAgent));
            }
        }

        mNetworkExcutor = new BasicNetworkExcutor(stack);
        mCache = new DataBaseCache(mContext);
        RequestQueue queue = new RequestQueue(mCache, mNetworkExcutor);
//        RequestQueue queue = new RequestQueue(new NetworkCache(context), network, 2, new ExecutorDelivery(
//                DatabaseThread.getInstance().getDataBaseThreadHandler()));
        queue.start();

        return queue;
    }
}
