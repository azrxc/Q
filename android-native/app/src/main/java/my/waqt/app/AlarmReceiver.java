package my.waqt.app;
import android.app.*;import android.content.*;
public class AlarmReceiver extends BroadcastReceiver {
 public void onReceive(Context c,Intent i){
 if("my.waqt.TEST".equals(i.getAction())){long delay=System.currentTimeMillis()-i.getLongExtra("at",0);if(delay>=-1000&&delay<120000)try{c.startForegroundService(new Intent(c,AzanService.class).putExtra("prayer","Ujian Azan"));}catch(Exception ignored){}return;}
 boolean valid=Alarms.valid(c,i);Alarms.arm(c);if(!valid)return;
 String prayer=i.getStringExtra("prayer"),kind=i.getStringExtra("kind");
 if("full".equals(kind)){try{c.startForegroundService(new Intent(c,AzanService.class).putExtra("prayer",prayer));}catch(Exception ignored){}return;}
 NotificationManager nm=c.getSystemService(NotificationManager.class);if(!nm.areNotificationsEnabled())return;
 NotificationChannel channel=new NotificationChannel("waqt_silent","Peringatan solat senyap",NotificationManager.IMPORTANCE_LOW);channel.setSound(null,null);channel.enableVibration(false);nm.createNotificationChannel(channel);
 PendingIntent open=PendingIntent.getActivity(c,94,new Intent(c,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
 boolean early="early".equals(kind);String title=early?prayer+" dalam "+i.getIntExtra("lead",0)+" minit":"Telah masuk waktu "+prayer;
 nm.notify((prayer+kind).hashCode(),new Notification.Builder(c,"waqt_silent").setSmallIcon(R.drawable.waqt).setContentTitle(title).setContentText(early?"Bersedia untuk solat":"Waqt · Waktu solat Malaysia").setContentIntent(open).setAutoCancel(true).build());
 }
}