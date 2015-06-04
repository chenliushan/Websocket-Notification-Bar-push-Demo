package com.example.liushanchen.websockettest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends ActionBarActivity {
    String tag = "MainActivity";
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.getString("t") != null) {
            String s = bundle.getString("t");
            Log.i(tag,s);
            TextView textView = (TextView) findViewById(R.id.tv);
            textView.setText(s);
        }else {

            try {
                ExampleClient c = new ExampleClient(new URI("ws://192.168.1.10:9000/con"), new Draft_17());
                c.setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(String masage) {
                        Log.i(tag, masage);

                        count++;
                        outMsg("", "", "", count);
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

    private void j2s(String masage) throws JSONException {
        JSONObject dataJson = new JSONObject("你的Json数据");
        JSONObject response = dataJson.getJSONObject("response");
        JSONArray data = response.getJSONArray("data");
        JSONObject info = data.getJSONObject(0);
        String province = info.getString("province");
        String city = info.getString("city");
        String district = info.getString("district");
        String address = info.getString("address");
        System.out.println(province + city + district + address);
    }

    protected void outMsg(String mabstract, String tittle, String context,int count) {
        NotificationManager manage = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);//定义系统消息管理类
        Notification myiding = new Notification(R.drawable.ic_launcher, mabstract+count+"", System.currentTimeMillis());//初始化消息
        Intent dongzuo = new Intent(MainActivity.this, MainActivity.class);//设置消息点击后动作
        String s="dfadjflakt"+count+"...";
        dongzuo.putExtra("t", s);
        PendingIntent zhixing = PendingIntent.getActivity(getApplicationContext(), count, dongzuo, 0);//设置指定消息动作
        myiding.setLatestEventInfo(getApplicationContext(), tittle+count+"", context, zhixing);//设置消息内容
        myiding.flags = Notification.FLAG_AUTO_CANCEL;//设置消息点击后消失
//        notification.defaults |= Notification.DEFAULT_SOUND; //默认声音
        manage.notify(count, myiding);//发送消息
    }

//    private NotificationManager manage;
//    private Notification myiding;
//    //开始行动
//    protected void Outmsg1() {
//        // TODO Auto-generated method stub
//        manage = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);//定义系统消息管理类
//        myiding = new Notification(R.drawable.ic_launcher, "消息简介", System.currentTimeMillis());//初始化消息
//        Intent dongzuo = new Intent(MainActivity.this,Mymain.class);//设置消息点击后动作
//        PendingIntent zhixing = PendingIntent.getActivity(getApplicationContext(), 0, dongzuo, 0);//设置指定消息动作
//        //myiding.setLatestEventInfo(getApplicationContext(), "标题", "内容", zhixing);//设置消息内容
//        myiding.contentView=new RemoteViews(getPackageName(), R.layout.mymain);
//        myiding.contentView.setProgressBar(R.id.progressBar1, 100, 0, false);
//        myiding.flags=Notification.FLAG_AUTO_CANCEL;//设置消息点击后消失
//        handler.postDelayed(run, 100);
//    }
}
