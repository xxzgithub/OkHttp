package bwie.com.shopcardetails;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文 件 名: MyApplication
 * 创 建 人: 谢兴张
 * 创建日期: 2017/10/20
 * 邮   箱:
 * 博   客:
 * 修改时间：
 * 修改备注：OKHTTP的应用拦截器
 */

public class MyOKApplicationInterceptor implements Interceptor {
    //拦截器
    //打印Log
    //替换header
    @Override
    public Response intercept(Chain chain) throws IOException {
        //替换header
        Request request = chain.request().newBuilder()
                .header("shenfen", "chinesse")
                .build();
        //打印Log
        HttpUrl url = request.url();
        String httpUrl = url.url().toString();
        Log.e("TAG", "============" + httpUrl);
        Response response = chain.proceed(request);
        int code = response.code();
        Log.e("TAG", "============response.code() == " + code);
        return response;
    }
}