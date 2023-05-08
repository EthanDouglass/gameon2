package com.example.frontendfinal;

import android.content.Context;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This class is for the volley requestQueue! This is called a "singleton" because only 1 can be made.
 * This is basically an overarching requestQueue we can access at all times from everywhere (Highly important)
 *
 * PARTS
 * //Get a requestQueue
 * RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
 *     getRequestQueue();
 *
 * //Add a request (in this example, called stringRequest) to your requestQueue
 * MySingleton.getInstance(this).addToRequestQueue(stringRequest);
 */

public class MySingleton {
    private static MySingleton instance;
    private RequestQueue requestQueue;

    private MySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
