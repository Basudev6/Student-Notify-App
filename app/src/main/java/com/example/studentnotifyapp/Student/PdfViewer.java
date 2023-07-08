package com.example.studentnotifyapp.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.studentnotifyapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PdfViewer extends AppCompatActivity {

    private String url;
    private ProgressDialog pd;


    private String encodedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pd = new ProgressDialog(PdfViewer.this);
        pd.setMessage("Please wait...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        url = getIntent().getStringExtra("pdfUrl");

        WebView webView = findViewById(R.id.web);

        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);

        webSettings.setDisplayZoomControls(false);

        try {
            encodedUrl = URLEncoder.encode(url,"UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        webView.clearCache(true);
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+encodedUrl);

//        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+encodedUrl);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                pd.dismiss();
                Toast.makeText(PdfViewer.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });



    }

}