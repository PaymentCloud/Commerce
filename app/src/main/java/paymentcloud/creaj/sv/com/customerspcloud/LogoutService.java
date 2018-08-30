package paymentcloud.creaj.sv.com.customerspcloud;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

public class LogoutService extends Service {
    public static final int notify = 1000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    class TimeDisplay extends TimerTask {

        class C04331 implements Runnable {
            C04331() {
            }

            public void run() {
                Log.d("Servicio", "Running");
            }
        }

        TimeDisplay() {
        }

        public void run() {
            LogoutService.this.mHandler.post(new C04331());
        }
    }

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onCreate() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        } else {
            this.mTimer = new Timer();
        }
        this.mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, 1000);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mTimer.cancel();
        Log.d("Servicio", "Service is Destroyed");
    }
}
