package com.example.liushanchen.websockettest.websocket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by liushanchen on 15/6/1.
 */
public class ExampleClient extends WebSocketClient {
    String tag = "ExampleClient";
    private OnProgressListener onProgressListener;

    public ExampleClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public ExampleClient(URI serverURI) {
        super(serverURI);
    }

    /**
     * 注册回调接口的方法，供外部调用
     *
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i(tag, "opened connection");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient  
    }

    @Override
    public void onMessage(String message) {
        Log.i(tag, "received: " + message);
        if (onProgressListener != null) {
            onProgressListener.onProgress(message);
        }
    }

    @Override
    public void onFragment(Framedata fragment) {
        Log.i(tag, "received fragment: " + new String(fragment.getPayloadData().array()));

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        Log.i(tag, "Connection closed by " + (remote ? "remote peer" : "us"));
        Log.i(tag, "Connection closed code : " + code);
        Log.i(tag, "Connection closed reason : " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally  
    }


}
