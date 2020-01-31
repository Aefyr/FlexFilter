package com.aefyr.flexfilter.applier;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

//TODO rewrite applier, this sucks

/**
 * Similar to {@link android.widget.Filter}
 *
 * @param <T>
 */
public class LiveFilterApplier<T> {

    private static final int FILTER_TOKEN = 0;
    private static final int FINISH_TOKEN = 1;

    private final Object mLock = new Object();

    private FilterApplier<T> mApplier = new FilterApplier<>();

    private Handler mRequestHandler;
    private Handler mResultHandler;

    private MutableLiveData<List<T>> mLiveData = new MutableLiveData<>(Collections.emptyList());

    public LiveFilterApplier() {
        mResultHandler = new ResultsHandler(this);
    }

    public void apply(ComplexCustomFilter<T> filter, List<T> list) {
        synchronized (mLock) {
            if (mRequestHandler == null) {
                HandlerThread thread = new HandlerThread("LiveFilterApplier", android.os.Process.THREAD_PRIORITY_BACKGROUND);
                thread.start();
                mRequestHandler = new RequestHandler(thread.getLooper());
            }

            Message message = mRequestHandler.obtainMessage(FILTER_TOKEN);

            RequestArguments args = new RequestArguments();
            args.filter = filter;
            args.listToFilter = list;
            message.obj = args;

            mRequestHandler.removeMessages(FILTER_TOKEN);
            mRequestHandler.removeMessages(FINISH_TOKEN);
            mRequestHandler.sendMessage(message);
        }
    }

    private void publishResults(List<T> filteredList) {
        mLiveData.setValue(filteredList);
    }

    public LiveData<List<T>> asLiveData() {
        return mLiveData;
    }

    private static class RequestArguments {

        ComplexCustomFilter filter;

        List listToFilter;

        List filteredList;

    }

    private class RequestHandler extends Handler {

        private RequestHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            int what = msg.what;
            Message message;
            switch (what) {
                case FILTER_TOKEN:
                    RequestArguments args = (RequestArguments) msg.obj;
                    try {
                        args.filteredList = mApplier.apply(args.filter, args.listToFilter);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        message = mResultHandler.obtainMessage(what);
                        message.obj = args;
                        message.sendToTarget();
                    }

                    synchronized (mLock) {
                        if (mRequestHandler != null) {
                            Message finishMessage = mRequestHandler.obtainMessage(FINISH_TOKEN);
                            mRequestHandler.sendMessageDelayed(finishMessage, 3000);
                        }
                    }
                    break;
                case FINISH_TOKEN:
                    synchronized (mLock) {
                        if (mRequestHandler != null) {
                            mRequestHandler.getLooper().quit();
                            mRequestHandler = null;
                        }
                    }
                    break;
            }
        }
    }

    private static class ResultsHandler extends Handler {

        private WeakReference<LiveFilterApplier> mApplier;

        private ResultsHandler(LiveFilterApplier applier) {
            mApplier = new WeakReference<>(applier);
        }

        @Override
        public void handleMessage(Message msg) {
            RequestArguments args = (RequestArguments) msg.obj;

            LiveFilterApplier applier = mApplier.get();
            if (applier != null)
                applier.publishResults(args.filteredList);
        }
    }
}
