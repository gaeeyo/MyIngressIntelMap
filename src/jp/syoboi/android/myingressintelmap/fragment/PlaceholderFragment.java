package jp.syoboi.android.myingressintelmap.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.Toast;

import java.io.IOException;

import jp.syoboi.android.myingressintelmap.R;
import jp.syoboi.android.myingressintelmap.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment
public class PlaceholderFragment extends WebViewFragment {

	static final String TAG = "PlaceholderFragment";

	static final int ZOOM_DEFAULT = 17;
	static final int ZOOM_LONG = 14;

	WebView mWebView;

	public PlaceholderFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mWebView = (WebView) super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(R.layout.main_fragment, container, false);
		ViewGroup webViewContainer = (ViewGroup) v.findViewById(R.id.webViewContainer);
		webViewContainer.addView(mWebView);
		return v;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@AfterViews
	void afterViews() {
		
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					Callback callback) {
				super.onGeolocationPermissionsShowPrompt(origin, callback);
				callback.invoke(origin, true, true);
			}
			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
				Log.v(TAG, "MESSAGE: " + consoleMessage.message() + " (" + consoleMessage.lineNumber() + ")");
				return super.onConsoleMessage(consoleMessage);
			} 

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) { 
					Log.d(TAG, "newProgress:" + newProgress);
					initScript();
					onClickCurrentLocation();
					onClickZoomDefault();
				} 
			}
		}); 
		
		mWebView.setWebViewClient(new WebViewClient());
		
		WebSettings ws = mWebView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setGeolocationEnabled(true);
		
		mWebView.loadUrl(getString(R.string.mapUrl));
	}
	 
	@Override
	public void onResume() {
		super.onResume();
		onClickCurrentLocation();
	}
	 
	@Click(R.id.currentLocation)
	void onClickCurrentLocation() {
		mWebView.loadUrl("javascript:_im.scrollToCurrentLocation()");
	}

	void initScript() {
		try {
			String script = Utils
					.loadAssetText(getResources(), "userscript.js");
			mWebView.loadUrl("javascript:" + script);
		} catch (IOException e) {
			Toast.makeText(getActivity(), R.string.cantLoadUserScript,
					Toast.LENGTH_SHORT).show();;
		} 
	}

	@Click(R.id.zoomDefault)
	void onClickZoomDefault() {
		setZoom(ZOOM_DEFAULT);
		setWindowZoom("1");
	}
	
	@Click(R.id.zoomLong)
	void onClickZoomLong() {
//		setZoom(ZOOM_LONG);
		setWindowZoom("0.25");
	}
	
	void setWindowZoom(String zoom) {
		mWebView.loadUrl("javascript:_im.zoom('" + zoom + "')");
		mWebView.getLayoutParams().height = mWebView.getHeight() + 1;
		mWebView.requestLayout();
		mWebView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mWebView.getLayoutParams().height = LayoutParams.MATCH_PARENT;
				mWebView.requestLayout();
			}
		}, 0);
	}

	void setZoom(int zoomLevel) {
		mWebView.loadUrl("javascript:_im.map.setZoom(" + zoomLevel + ")");
	}


	Runnable mDrawRunnable = new Runnable() {

		@Override
		public void run() {
			Bitmap bmp = Bitmap.createBitmap(1080, 2048, Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			canvas.scale(0.5f, 0.5f);
			mWebView.draw(canvas);

			mWebView.postDelayed(mDrawRunnable, 3000);
		}
	};
}