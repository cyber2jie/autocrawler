package io.jt.autocrawler.progress;

public interface ProgressEventHandler {
    void onFailed(Progress pg);

    void onComplete(Progress pg);

    void onProgress(Progress pg);
}
