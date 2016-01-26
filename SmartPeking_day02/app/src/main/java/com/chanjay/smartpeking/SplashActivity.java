package com.chanjay.smartpeking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.chanjay.smartpeking.utils.PrefUtil;

/**
 * 闪屏页
 */
public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rl_root = (RelativeLayout) findViewById(R.id.rl_root);

        startAnim();
    }

    private void startAnim() {

        //动画集合
        AnimationSet set = new AnimationSet(false) ;

        //旋转动画
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000); //动画时间
        rotate.setFillAfter(true); //保持动画状态

        //缩放动画
        ScaleAnimation scale = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(1000); //动画时间
        scale.setFillAfter(true); //保持动画状态

        //渐变动画
        AlphaAnimation alpha = new AlphaAnimation(0,1) ;
        alpha.setDuration(1000); //动画时间
        alpha.setFillAfter(true); //保持动画状态

        set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(alpha);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            //动画执行结束
            @Override
            public void onAnimationEnd(Animation animation) {
                 toNextActivity();
            }
        });


        rl_root.startAnimation(set);
    }


    private void toNextActivity(){
        //判断是否展示过引导页
        boolean isGuideShowed = PrefUtil.getBoolean(this,"isGuideShowed",false) ;

        if(!isGuideShowed){  //没有展示过
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        }else{  //已展示
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }

        finish();

    }

}
