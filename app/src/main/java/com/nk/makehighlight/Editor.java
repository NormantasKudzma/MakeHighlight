package com.nk.makehighlight;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.FFmpegSessionCompleteCallback;
import com.arthenica.ffmpegkit.LogCallback;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.Statistics;
import com.arthenica.ffmpegkit.StatisticsCallback;

public class Editor {
    interface ExecCallback {
        void onComplete(boolean success, FFmpegSession session);
    }

    final VideoView mVideoView;
    Uri mSelectedFile;

    public Editor(VideoView videoView) {
        mVideoView = videoView;

        MediaController mediaController = new MediaController(mVideoView.getContext());
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);
        mVideoView.setOnPreparedListener((mediaPlayer) -> {
            mVideoView.setBackgroundColor(0x00000000);
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        });
        mVideoView.setOnErrorListener((_1, _2, _3) -> {
            mVideoView.stopPlayback();
            return false;
        });
    }

    private FFmpegSession exec(String command, ExecCallback then) {
        return FFmpegKit.executeAsync(command, new FFmpegSessionCompleteCallback() {

            @Override
            public void apply(final FFmpegSession session) {
                then.onComplete(ReturnCode.isSuccess(session.getReturnCode()), session);
            }
        }, new LogCallback() {
            @Override
            public void apply(com.arthenica.ffmpegkit.Log log) { }
        }, new StatisticsCallback() {
            @Override
            public void apply(Statistics statistics) { }
        });
    }

    public void open(Uri file) {
        if (mVideoView.isPlaying()) { mVideoView.stopPlayback(); }

        mSelectedFile = file;
        mVideoView.setVideoURI(file);
        mVideoView.requestFocus();
        mVideoView.start();
    }

    public void pause() {
        if (mVideoView.isPlaying()) { mVideoView.pause(); }
    }

    public void play() {
        if (!mVideoView.isPlaying()) { mVideoView.resume(); }
    }
}
