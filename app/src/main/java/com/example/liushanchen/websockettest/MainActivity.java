package com.example.liushanchen.websockettest;

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

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends ActionBarActivity {
    private String tag = "MainActivity";
    private int count = 0;
    private int connCount = 0;
    private Button testBtn;

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

            try {
                ExampleClient c = new ExampleClient(new URI("ws://115.159.37.43:9000/con"), new Draft_17());
                c.setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(String message) {
                        Log.i(tag, message);

                        if (!message.equals("?")) {
                            count++;
                            try {
                                j2sMode1(message, count);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            connCount++;
                        }

//                        outMsg("", "", "", count);
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
        testBtn = (Button) findViewById(R.id.test_btn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testActivity = new Intent(MainActivity.this, TestActivity.class);
                startActivity(testActivity);
            }
        });
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

    private void j2sMode1(String message, int count) throws JSONException {
        JSONObject dataJson = new JSONObject(message);//"你的Json数据"
        String a = dataJson.getString("a");
        if (a.equals("prescription.arriver")) {
            JSONObject d = dataJson.getJSONObject("d");
            String id = d.getString("id");
            String name = d.getString("name");
            JSONObject doctor = d.getJSONObject("doctor");
            String doctorName = doctor.getString("name");
            String doctorScore = doctor.getString("score");
            outMsg(a, name + "|" + id, doctorName + "|" + doctorScore, count);
        } else if (a.equals("notice")) {
            JSONObject d = dataJson.getJSONObject("d");
            String title = d.getString("title");
            String version = d.getString("version");
            String url = d.getString("url");
            outMsg(a, title, version + "|" + url);
        }


    }

    private void j2s(String masage) throws JSONException {
        JSONObject dataJson = new JSONObject(masage);//"你的Json数据"
        JSONObject response = dataJson.getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        JSONObject info = data.getJSONObject(0);
        String province = info.getString("province");
        String city = info.getString("city");
        String district = info.getString("district");
        String address = info.getString("address");
        System.out.println(province + city + district + address);
    }

    private void wakeup() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);//获取电源管理器对象  
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");//获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag  
        wl.acquire();//点亮屏幕  
        wl.release();
        
        KeyguardManager km= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);//得到键盘锁管理器对象  
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");//参数是LogCat里用的Tag  
        kl.disableKeyguard();//解锁

    }

    protected void outMsg(String mabstract, String tittle, String context, int myCode) {
        wakeup();
        NotificationManager manage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);//定义系统消息管理类
        Notification myiding = new Notification(R.drawable.ic_launcher, mabstract + myCode + "", System.currentTimeMillis());//初始化消息
        Intent dongzuo = new Intent(MainActivity.this, MainActivity.class);//设置消息点击后动作
        String s = "abstract" +mabstract+"|tittle"+tittle+"|context"+context+"|myCode"+ myCode + "...";
        dongzuo.putExtra("t", s);
        PendingIntent zhixing = PendingIntent.getActivity(getApplicationContext(), myCode, dongzuo, myCode);//设置指定消息动作
        myiding.setLatestEventInfo(getApplicationContext(), tittle + myCode + "", context, zhixing);//设置消息内容
        myiding.flags = Notification.FLAG_AUTO_CANCEL;//设置消息点击后消失
        myiding.defaults = Notification.DEFAULT_ALL; //默认声音
        manage.notify(myCode, myiding);//发送消息
    }
    protected void outMsg(String mabstract, String tittle, String context) {
        wakeup();
        NotificationManager manage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);//定义系统消息管理类
        Notification myiding = new Notification(R.drawable.ic_launcher, mabstract + count + "", System.currentTimeMillis());//初始化消息
        Intent dongzuo = new Intent(MainActivity.this, TestActivity.class);//设置消息点击后动作
        PendingIntent zhixing = PendingIntent.getActivity(getApplicationContext(), count, dongzuo, count);//设置指定消息动作
        myiding.setLatestEventInfo(getApplicationContext(), tittle + count + "", context, zhixing);//设置消息内容
        myiding.flags = Notification.FLAG_AUTO_CANCEL;//设置消息点击后消失
        myiding.defaults = Notification.DEFAULT_ALL; //默认声音
        manage.notify(count, myiding);//发送消息
    }

}
