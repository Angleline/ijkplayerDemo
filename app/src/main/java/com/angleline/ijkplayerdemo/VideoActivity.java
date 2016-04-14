package com.angleline.ijkplayerdemo;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayerProxy;

/**
 * Created by chenhao on 16-4-14.
 */
public class VideoActivity extends AppCompatActivity {

    private MediaPlayerProxy playerProxy;
    private IjkMediaPlayer ijkMediaPlayer;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        ijkMediaPlayer = new IjkMediaPlayer();
        playerProxy = new MediaPlayerProxy(ijkMediaPlayer);
        setContentView(surfaceView = new SurfaceView(this), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            playerProxy.setDataSource(this, Uri.parse("http://202.205.93.230:280/99e8f095e48865d9cecff47cd5563763/playlist.m3u8"));
            playerProxy.setAudioStreamType(AudioManager.STREAM_MUSIC);
            playerProxy.prepareAsync();
            playerProxy.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {
                    mp.setDisplay(surfaceView.getHolder());
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        playerProxy.stop();
        playerProxy.reset();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IjkMediaPlayer.native_profileEnd();
    }
}
