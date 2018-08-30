package paymentcloud.creaj.sv.com.comercio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;

public class Lottie extends AppCompatActivity {
    LottieAnimationView mLottieAnimationView;
    String[] mAnimFiles = new String[]{"empty_status.json", "letter_b_monster.json", "permission.json", "ice_cream_animation.json"};
    int mCurrentAnim = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentlottie);
        mLottieAnimationView = findViewById(R.id.lottie_animation_view);

        LottieComposition.Factory.fromAssetFileName(this, "Charging.json", new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                mLottieAnimationView.setComposition(composition);
                mLottieAnimationView.playAnimation();
            }
        });


        new Handler().postDelayed(new running(), 2000);
    }

    class running implements Runnable {
        running() {
        }

        public void run() {
            Intent i =  new Intent(getApplicationContext(), Summary.class);
            startActivity(i);
        }

    }
    @Override
    protected void onDestroy() {
        mLottieAnimationView.cancelAnimation();
        mLottieAnimationView = null;
        super.onDestroy();
    }
    public void onBackPressed() {
        if (Integer.valueOf(Integer.valueOf(0).intValue() + 1).intValue() != 1) {
            finish();
        }
    }

}

