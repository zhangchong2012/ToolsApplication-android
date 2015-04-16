package com.zhangchong.libnetwork.Tools;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
* @author 张充 E-mail:zchong@meizu.com
* @version 创建时间：2014-11-4 下午3:18:54
* 类说明
*/
public class NetworkSSLManager {
    private SSLContext mContext;

    public NetworkSSLManager(Context context) throws KeyManagementException, NoSuchAlgorithmException, UnrecoverableKeyException,
            KeyStoreException, IOException {
        mContext = SSLContext.getInstance("TLS");
//        mContext.init(initKeyStore(context).getKeyManagers(), new X509TrustManager[] { new CustomTrustManager() },
//                new java.security.SecureRandom());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        mContext.init(null, tmf.getTrustManagers(), null);
    }

    public SSLContext getContext() {
        return mContext;
    }

    //信任证书
    private KeyManagerFactory initKeyStore(Context context) throws NoSuchAlgorithmException, CertificateException, IOException,
            UnrecoverableKeyException, KeyStoreException {
        // key store相关信息  
        String keyName = "meizu.com.key";
        char[] keyPwd = "KObyoLUedFSAwegNQO2PbQ6sWVchETTY".toCharArray();
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        // 装载当前目录下的key store. 可用jdk中的keytool工具生成keystore  
        InputStream in = context.getAssets().open(keyName);
        keyStore.load(in, keyPwd);
        in.close();

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyPwd);
        return kmf;
    }

    //信任机制的判断
    private static class CustomTrustManager implements X509TrustManager {

        //该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，因此我们只需要执行默认的信任管理器的这个方法。JSSE中，默认的信任管理器类为TrustManager。
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        //该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，因此我们只需要执行默认的信任管理器的这个方法。JSSE中，默认的信任管理器类为TrustManager。
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        //　返回受信任的X509证书数组。
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
