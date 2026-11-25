package my.waqt.app;
import android.app.*;import android.content.*;import android.os.*;import org.json.*;import java.util.*;
final class Alarms {
 static android.content.SharedPreferences prefs(Context c){return c.getSharedPreferences("waqt_alarms",0);}
 static boolean allowed(Context c){return Build.VERSION.SDK_INT<31||c.getSystemService(AlarmManager.class).canScheduleExactAlarms();}
 static boolean isEnabled(Context c){return prefs(c).getBoolean("enabled",false);}
 static void enabled(Context c,boolean v){prefs(c).edit().putBoolean("enabled",v).commit();}
 static void save(Context c,String zone,JSONArray slots,boolean en)throws Exception{
 if(!zone.matches("[A-Z]{3}[0-9]{2}")||slots.length()>1850)throw new IllegalArgumentException();JSONArray valid=new JSONArray();long now=System.currentTimeMillis(),limit=now+370L*86400000;
 for(int i=0;i<slots.length();i++){JSONObject a=slots.getJSONObject(i);long at=a.getLong("at");String p=a.getString("prayer");if(at>now&&at<limit&&Arrays.asList("Subuh","Zohor","Asar","Maghrib","Isyak").contains(p))valid.put(new JSONObject().put("at",at).put("prayer",p));}
 if(valid.length()==0)throw new IllegalArgumentException();prefs(c).edit().putString("zone",zone).putString("slots",valid.toString()).putBoolean("enabled",en).commit();}
 static JSONObject next(Context c){try{JSONArray a=new JSONArray(prefs(c).getString("slots","[]"));JSONObject best=null;long now=System.currentTimeMillis();for(int i=0;i<a.length();i++){JSONObject v=a.getJSONObject(i);long at=v.getLong("at");if(at>now&&(best==null||at<best.getLong("at")))best=v;}return best;}catch(Exception e){return null;}}
 static PendingIntent pending(Context c,JSONObject n){Intent i=new Intent(c,AlarmReceiver.class);if(n!=null){i.putExtra("at",n.optLong("at"));i.putExtra("prayer",n.optString("prayer"));i.putExtra("zone",prefs(c).getString("zone",""));}return PendingIntent.getBroadcast(c,91,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);}
 static void cancel(Context c){c.getSystemService(AlarmManager.class).cancel(pending(c,null));}
 static void clear(Context c){cancel(c);prefs(c).edit().remove("zone").remove("slots").commit();}
 static void arm(Context c){cancel(c);if(!isEnabled(c)||!allowed(c))return;JSONObject n=next(c);if(n==null)return;PendingIntent show=PendingIntent.getActivity(c,92,new Intent(c,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);try{c.getSystemService(AlarmManager.class).setAlarmClock(new AlarmManager.AlarmClockInfo(n.optLong("at"),show),pending(c,n));}catch(SecurityException ignored){}}
 static PendingIntent testIntent(Context c,long at){return PendingIntent.getBroadcast(c,93,new Intent(c,AlarmReceiver.class).setAction("my.waqt.TEST").putExtra("at",at),PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);}
 static void cancelTest(Context c){c.getSystemService(AlarmManager.class).cancel(testIntent(c,0));}
 static void test(Context c){long at=System.currentTimeMillis()+20000;PendingIntent show=PendingIntent.getActivity(c,92,new Intent(c,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);c.getSystemService(AlarmManager.class).setAlarmClock(new AlarmManager.AlarmClockInfo(at,show),testIntent(c,at));}
 static boolean valid(Context c,Intent i){long at=i.getLongExtra("at",0),now=System.currentTimeMillis();if(!isEnabled(c)||at>now+1000||now-at>120000||!prefs(c).getString("zone","").equals(i.getStringExtra("zone")))return false;try{JSONArray a=new JSONArray(prefs(c).getString("slots","[]"));for(int k=0;k<a.length();k++){JSONObject r=a.getJSONObject(k);if(r.getLong("at")==at&&r.getString("prayer").equals(i.getStringExtra("prayer")))return true;}}catch(Exception ignored){}return false;}
}