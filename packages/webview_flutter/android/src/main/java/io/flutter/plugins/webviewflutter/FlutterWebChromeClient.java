package io.flutter.plugins.webviewflutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import io.flutter.plugins.webviewflutter.utils.ActivityUtils;
import io.flutter.plugins.webviewflutter.utils.IntentUtils;
import io.flutter.plugins.webviewflutter.utils.ResultFragment.ResultCallBack;

import static io.flutter.plugins.webviewflutter.WebViewFlutterPlugin.activity;

public class FlutterWebChromeClient extends WebChromeClient {

    private Context context;

    public FlutterWebChromeClient(Context context) {
        this.context = context;
    }

    // For Android 4.0 - 5.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        startImageChooser(uploadMsg, acceptType, capture);
    }

    // For Android > 5.0
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        startImageChooser(filePathCallback, fileChooserParams);
        return true;
    }

    //调用系统图库
    private void startImageChooser(final ValueCallback<Uri> filePathCallback, String acceptType, String capture) {
        Intent pickerIntent = IntentUtils.imagePickerIntent(context);
        if (filePathCallback == null) return;
        ActivityUtils.startActivityForResult(activity, pickerIntent, new ResultCallBack() {
            @Override
            public void onResult(int requestCode, int resultCode, Intent data) {
                if (resultCode != Activity.RESULT_OK) {
                    filePathCallback.onReceiveValue(null);
                    return;
                }
                if (data != null) {
                    Uri result = data.getData();
                    filePathCallback.onReceiveValue(result);
                }
            }
        });
    }

    //调用系统图库
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startImageChooser(final ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        Intent pickerIntent = fileChooserParams.createIntent();
        if (filePathCallback == null) return;
        ActivityUtils.startActivityForResult(activity, pickerIntent, new ResultCallBack() {
            @Override
            public void onResult(int requestCode, int resultCode, Intent data) {
                if (resultCode != Activity.RESULT_OK) {
                    filePathCallback.onReceiveValue(null);
                    return;
                }
                if (data != null) {
                    Uri result = data.getData();
                    filePathCallback.onReceiveValue(result == null ? null : new Uri[]{result});
                }
            }
        });
    }
}
