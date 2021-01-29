package com.awesomevideo.activity;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awesomevideo.R;
import com.awesomevideo.activity.base.BaseActivity;
import com.awesomevideo.util.ToastUtils;
import com.awesomevideo.view.fragment.home.HomeFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    // 采样率，现在能够保证在所有设备上使用的采样率是44100Hz, 但是其他的采样率（22050, 16000, 11025）在一些设备上也可以使用。
    public static final int SAMPLE_RATE_INHZ = 16000;
    // 声道数。CHANNEL_IN_MONO and CHANNEL_IN_STEREO. 其中CHANNEL_IN_MONO是可以保证在所有设备能够使用的。
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    // 返回的音频数据的格式。 ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, and ENCODING_PCM_FLOAT.
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private HomeFragment mHomeFragment;
    private FragmentManager fm;

    private RelativeLayout mHomeLayout;
    private RelativeLayout mPondLayout;
    private RelativeLayout mMessageLayout;
    private RelativeLayout mMineLayout;
    private TextView mHomeView;
    private TextView mPondView;
    private TextView mMessageView;
    private TextView mMineView;

    private Button mBtnStartRecord,mBtnEndRecord;
    private Handler workHandler;
    private boolean isRecording;
    private AudioRecord audioRecord;
    private HandlerThread mHandlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        mHomeFragment = new HomeFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout,mHomeFragment);
        fragmentTransaction.commit();
        initView();
    }

    private void initView() {
        mHomeLayout = (RelativeLayout) findViewById(R.id.home_layout_view);
        mHomeLayout.setOnClickListener(this);
        mPondLayout = (RelativeLayout) findViewById(R.id.pond_layout_view);
        mPondLayout.setOnClickListener(this);
        mMessageLayout = (RelativeLayout) findViewById(R.id.message_layout_view);
        mMessageLayout.setOnClickListener(this);
        mMineLayout = (RelativeLayout) findViewById(R.id.mine_layout_view);
        mMineLayout.setOnClickListener(this);

        mHomeView = (TextView) findViewById(R.id.home_image_view);
        mPondView = (TextView) findViewById(R.id.fish_image_view);
        mMessageView = (TextView) findViewById(R.id.message_image_view);
        mMineView = (TextView) findViewById(R.id.mine_image_view);
        mHomeView.setBackgroundResource(R.mipmap.comui_tab_home_selected);

        mBtnStartRecord = (Button) findViewById(R.id.tv_start_record);
        mBtnEndRecord = (Button) findViewById(R.id.tv_end_record);
        // 创建
        mHandlerThread = new HandlerThread("handlerThread");
        mHandlerThread.start();
        workHandler = new Handler(mHandlerThread.getLooper());

        if (!hasPermission(Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            requestPermission(0x01,new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE});
        }

        mBtnStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(HomeActivity.this,"开始录音");
                startRecord();
            }
        });
        mBtnEndRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(HomeActivity.this,"结束录音");
                stopRecord();
            }
        });
    }

    // 开始录音
    private void startRecord() {
        // 获取最小录音缓存大小，
        final int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_INHZ,
                CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);
        // 初始化缓存
        final byte[] data = new byte[minBufferSize];
//        final File file = new File(getFilesDir()+"hello/test.pcm");
//        Log.i(TAG, "path:" + file.getAbsolutePath());
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
        // 创建音频文件
        final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + System.currentTimeMillis() + ".pcm");

        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 开始录音
        audioRecord.startRecording();
        isRecording = true;
        // 创建数据流，将缓存导入数据流
        workHandler.post(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, "文件未找到");
                }
                if (fos == null) return;
                while (isRecording) {
                    int length = audioRecord.read(data, 0, minBufferSize);
                    if (AudioRecord.ERROR_INVALID_OPERATION != length) {
                        try {
                            fos.write(data, 0, length);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    // 关闭数据流
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 停止录音
    private void stopRecord() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecord();
        // 终止
        mHandlerThread.quit();
        workHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_layout_view:
                break;
            case R.id.message_layout_view:
                break;
            case R.id.mine_layout_view:
                break;
        }
    }
}
