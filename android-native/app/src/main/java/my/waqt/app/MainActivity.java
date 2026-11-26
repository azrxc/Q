package my.waqt.app;
import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import android.webkit.*;
import androidx.webkit.*;
import java.util.Collections;
import org.json.*;

public class MainActivity extends Activity {
 private WebView web; private JavaScriptReplyProxy reply;
 private static final String ORIGIN="https://appassets.androidplatform.net";
 @Override public void onCreate(Bundle state){super.onCreate(state);
  web=new WebView(this);setContentView(web);web.setBackgroundColor(0xff171717);
  WebSettings settings=web.getSettings();settings.setJavaScriptEnabled(true);settings.setDomStorageEnabled(true);settings.setGeolocationEnabled(true);settings.setAllowFileAccess(false);settings.setAllowContentAccess(false);settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
  WebViewAssetLoader loader=new WebViewAssetLoader.Builder().addPathHandler("/assets/",new WebViewAssetLoader.AssetsPathHandler(this)).build();
  web.setWebViewClient(new WebViewClient(){public WebResourceResponse shouldInterceptRequest(WebView v,WebResourceRequest r){return loader.shouldInterceptRequest(r.getUrl());}
   public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest r){Uri u=r.getUrl();if(ORIGIN.equals(u.getScheme()+"://"+u.getHost())&&u.getPath().startsWith("/assets/"))return false;if(r.isForMainFrame()&&"https".equals(u.getScheme()))try{startActivity(new Intent(Intent.ACTION_VIEW,u));}catch(Exception ignored){}return true;}});
  web.setWebChromeClient(new WebChromeClient(){public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissions.Callback cb){if(!ORIGIN.equals(origin)){cb.invoke(origin,false,false);return;}if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){cb.invoke(origin,true,false);}else{pendingGeo=cb;requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},77);}}});
  if(WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)){WebViewCompat.addWebMessageListener(web,"WaqtNative",Collections.singleton(ORIGIN),(view,message,source,isMainFrame,proxy)->{if(!isMainFrame||!ORIGIN.equals(source.toString()))return;reply=proxy;try{handle(new JSONObject(message.getData()));}catch(Exception e){status("Tetapan tidak dapat disimpan. Cuba lagi.");}});}
  web.loadUrl(ORIGIN+"/assets/index.html");
 }
 private GeolocationPermissions.Callback pendingGeo;
 @Override public void onRequestPermissionsResult(int req,String[] p,int[] result){super.onRequestPermissionsResult(req,p,result);if(req==77&&pendingGeo!=null){pendingGeo.invoke(ORIGIN,checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED,false);pendingGeo=null;}if(req==78)requestExact();}
 private void requestExact(){if(!Alarms.allowed(this)&&Build.VERSION.SDK_INT>=31)try{startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,Uri.parse("package:"+getPackageName())));}catch(Exception ignored){}}
 private void handle(JSONObject m)throws Exception{switch(m.optString("action")){
 case "enable":Alarms.enabled(this,true);if(Build.VERSION.SDK_INT>=33&&checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},78);else requestExact();Alarms.arm(this);break;
 case "disable":Alarms.enabled(this,false);Alarms.cancel(this);Alarms.cancelTest(this);stopService(new Intent(this,AzanService.class));break;
 case "clear":Alarms.clear(this);Alarms.cancelTest(this);stopService(new Intent(this,AzanService.class));break;
 case "schedule":Alarms.save(this,m.getString("zone"),m.getJSONArray("slots"),m.optBoolean("enabled"));Alarms.arm(this);break;
 case "pinWidget":android.appwidget.AppWidgetManager wm=android.appwidget.AppWidgetManager.getInstance(this);if(wm.isRequestPinAppWidgetSupported())wm.requestPinAppWidget(new ComponentName(this,PrayerWidget.class),null,null);else status("Tambah widget melalui skrin utama telefon.");return;
 case "permissions":if(Build.VERSION.SDK_INT>=33&&checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},78);else requestExact();break;
 case "alarmTest":if(!Alarms.allowed(this)){requestExact();status("Benarkan penggera tepat, kemudian cuba ujian semula.");return;}Alarms.test(this);status("Ujian azan dijadualkan dalam 20 saat. Kunci skrin sekarang.");return;
 case "alertPrefs":Alarms.saveAlertPrefs(this,m.getJSONObject("prefs"));Alarms.arm(this);break;
 case "voices":Alarms.saveVoices(this,m.getJSONObject("voices"));return;
 case "test":startForegroundService(new Intent(this,AzanService.class).putExtra("prayer",m.optString("prayer","Zohor")).putExtra("voice",m.optString("voice","")));break;
 case "stop":Alarms.cancelTest(this);stopService(new Intent(this,AzanService.class));break;
 }status(null);}
 private void status(String override){if(reply==null)return;try{boolean ready=Alarms.allowed(this)&&Alarms.isEnabled(this)&&Alarms.next(this)!=null;JSONObject j=new JSONObject();j.put("ready",ready);JSONObject next=ready?Alarms.next(this):null;if(next!=null){j.put("nextAt",next.optLong("at"));j.put("nextPrayer",next.optString("prayer"));j.put("nextKind",next.optString("kind"));}android.media.AudioManager am=getSystemService(android.media.AudioManager.class);int max=am.getStreamMaxVolume(android.media.AudioManager.STREAM_ALARM),vol=am.getStreamVolume(android.media.AudioManager.STREAM_ALARM);JSONObject diag=new JSONObject();diag.put("exact",Alarms.allowed(this));diag.put("notifications",getSystemService(NotificationManager.class).areNotificationsEnabled());diag.put("alarmVolume",max>0?vol*100/max:0);diag.put("powerSave",getSystemService(PowerManager.class).isPowerSaveMode());diag.put("coverage",Alarms.coverage(this));j.put("diagnostics",diag);j.put("status",override!=null?override:!Alarms.isEnabled(this)?"Azan dimatikan.":!Alarms.allowed(this)?"Benarkan Penggera & peringatan dalam tetapan Android.":ready?"Penggera seterusnya telah disediakan.": "Pilih zon dan muat turun jadual dahulu.");reply.postMessage(j.toString());}catch(Exception ignored){}}
 @Override public void onResume(){super.onResume();Alarms.arm(this);status(null);}
 @Override public void onBackPressed(){if(web.canGoBack())web.goBack();else super.onBackPressed();}
}