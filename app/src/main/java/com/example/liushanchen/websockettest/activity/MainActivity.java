package com.example.liushanchen.websockettest.activity;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.liushanchen.websockettest.R;
import com.example.liushanchen.websockettest.Utils.JsonUtil;
import com.example.liushanchen.websockettest.domain.NotificationModel;
import com.example.liushanchen.websockettest.websocket.ExampleClient;
import com.example.liushanchen.websockettest.websocket.OnProgressListener;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {
    private String tag = "MainActivity";
    private int count = 0;
    private int connCount = 0;
    private Button testBtn;
    private ExampleClient c;
    private boolean websNotConn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.getString("t") != null) {
            String s = bundle.getString("t");
            Log.i(tag, s);
            TextView textView = (TextView) findViewById(R.id.tv);
            textView.setText(s);
        } else {
            connWebSocket();
        }
        testBtn = (Button) findViewById(R.id.test_btn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testActivity = new Intent(MainActivity.this, TestActivity.class);
                startActivity(testActivity);
            }
        });
        checkWebSocket();
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

private void checkWebSocket(){
    Timer timer = new Timer(true);
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
            Log.i(tag, "websNotConn:" + websNotConn + "");
            if (websNotConn) {
                reConnWebSocket();
                websNotConn = false;
            }
            websNotConn = true;
        }
    }, 3000, 10000);
}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnWebSocket(0, "finish activity");
    }
    //    public String s2j() throws JSONException {
//
//        String json = "{\"p0\":\"p1\"}";
//        JSONObject jsonObj = JSONObject.formObject(json);
//        String name = jsonObj.getString("name");
//        jsonObj.put("initial", name.substring(0, 1).toUpperCase());
//        String[] likes = new String[]{"JavaScript", "Skiing", "Apple Pie"};
//        jsonObj.put("likes", likes);
//        Map<String, String> ingredients = new HashMap<String, String>();
//        ingredients.put("apples", "3kg");
//        ingredients.put("sugar", "1kg");
//        ingredients.put("pastry", "2.4kg");
//        ingredients.put("bestEaten", "outdoors");
//        jsonObj.put("ingredients", ingredients);
//
//        System.out.println(jsonObj);
//        return jsonObj.toString();
//    }

    private void connWebSocket() {

        try {
            c = new ExampleClient(new URI("ws://115.159.37.43:9000/con"), new Draft_17());
            c.setOnProgressListener(new OnProgressListener() {
                @Override
                public void onProgress(String message) {
                    websNotConn = false;
                    Log.i(tag, message);
                    if (!message.equals("?")) {
                        count++;
                        try {
                            outMsg(JsonUtil.j2sMode1(message), count);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        connCount++;
                        Log.i(tag, "connCount:" + connCount);
                    }
                }
            });
            c.connectBlocking();
            c.send("{p0:\"guest.login\",p1:{\"mobile\":\"13716379973\",\"pwd\":\"123456}}");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (WebsocketNotConnectedException e) {
            e.printStackTrace();
        }
    }

    private void reConnWebSocket() {
        disConnWebSocket(1, "reconnect");
        connWebSocket();
    }

    private void disConnWebSocket(int code, String reason) {
        if (c != null) {
            c.close();
        }
    }


    private void wakeup() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);//获取电源管理器对象  
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");//获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag  
        wl.acquire();//点亮屏幕  
        wl.release();

        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);//得到键盘锁管理器对象  
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");//参数是LogCat里用的Tag  
        kl.disableKeyguard();//解锁

    }

    protected void outMsg(NotificationModel nm, int count) {

        wakeup();
        NotificationManager manage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);//定义系统消息管理类
        Notification myiding = new Notification(R.drawable.ic_launcher, nm.getMabstract() + count + "", System.currentTimeMillis());//初始化消息
        Intent dongzuo = new Intent(MainActivity.this, MainActivity.class);//设置消息点击后动作
        String s = "abstract" + nm.getMabstract() + "|tittle" + nm.getTittle() + "|context" + nm.getContext() + "|myCode" + count + "...";
        dongzuo.putExtra("t", s);
        PendingIntent zhixing = PendingIntent.getActivity(getApplicationContext(), count, dongzuo, count);//设置指定消息动作
        myiding.setLatestEventInfo(getApplicationContext(), nm.getTittle() + count + "", nm.getContext(), zhixing);//设置消息内容
        myiding.flags = Notification.FLAG_AUTO_CANCEL;//设置消息点击后消失
        myiding.defaults = Notification.DEFAULT_ALL; //默认声音+震动
        manage.notify(count, myiding);//发送消息
    }

    protected void outMsg1(String mabstract, String tittle, String context, int count) {
        wakeup();
        NotificationManager manage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);//定义系统消息管理类
        Notification myiding = new Notification(R.drawable.ic_launcher, mabstract + count + "", System.currentTimeMillis());//初始化消息
        Intent dongzuo = new Intent(MainActivity.this, TestActivity.class);//设置消息点击后动作
        PendingIntent zhixing = PendingIntent.getActivity(getApplicationContext(), count, dongzuo, count);//设置指定消息动作
        myiding.setLatestEventInfo(getApplicationContext(), tittle + count + "", context, zhixing);//设置消息内容
        myiding.flags = Notification.FLAG_AUTO_CANCEL;//设置消息点击后消失
        myiding.defaults = Notification.DEFAULT_ALL; //默认声音＋震动
        manage.notify(count, myiding);//发送消息
    }

}
