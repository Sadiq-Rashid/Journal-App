package com.example.gurleensethi.roomcontacts;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton mIstance;
    private RequestQueue requestQueue;
    private  static Context mCtx;

    private MySingleton (Context context)
    {
        mCtx= context;
        requestQueue= getRequestQueue();
    }

    private RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }
    public static synchronized MySingleton getmIstance(Context context)
    {
        if(mIstance==null)
        {
            mIstance = new MySingleton(context);
        }
        return mIstance;
    }

    public <T> void addToRequestQue (Request<T> request)
    {
        getRequestQueue().add(request);
    }
}
