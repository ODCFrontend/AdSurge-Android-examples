package com.example.javademo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.bumptech.glide.Glide;

import com.adsurge.adn.managers.AdSurgeAdSdk;
import com.adsurge.adn.managers.AdSurgeAdSdkInitConfig;
import com.adsurge.adn.managers.OnStartListener;
import com.adsurge.adn.ads.AdSurgeAdError;
import com.adsurge.adn.ads.AdSurgeAd;
import com.adsurge.adn.ads.AdConfig;
import com.adsurge.adn.ads.AdSurgeAdSize;
import com.adsurge.adn.ads.interstitial.InterstitialAd;
import com.adsurge.adn.ads.interstitial.InterstitialAdListener;
import com.adsurge.adn.ads.rewarded.RewardItem;
import com.adsurge.adn.ads.rewarded.RewardedAd;
import com.adsurge.adn.ads.rewarded.RewardedAdListener;
import com.adsurge.adn.ads.banner.AdView;
import com.adsurge.adn.ads.banner.BannerAdListener;
import com.adsurge.adn.ads.nativead.NativeAd;
import com.adsurge.adn.ads.nativead.NativeAdListener;
import com.adsurge.adn.ads.nativead.NativeAdView;
import com.adsurge.adn.ads.nativead.MediaView;
import com.adsurge.adn.ads.appopen.AppOpenAd;
import com.adsurge.adn.ads.appopen.AppOpenAdListener;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    // AdSurge Demo Ad Unit IDs
    private static final String APP_ID = "10034";
    private static final String INTERSTITIAL_ID = "10854";
    private static final String REWARDED_ID = "10853";
    private static final String BANNER_ID = "15881";
    private static final String NATIVE_ID = "15836";
    private static final String APP_OPEN_ID = "16664";

    private TextView tvSdkStatus;
    
    // Interstitial Ad components
    private TextView tvInterstitialStatus;
    private Button btnShowInterstitial;
    private InterstitialAd mInterstitialAd;
    
    // Rewarded Ad components
    private TextView tvRewardedStatus;
    private Button btnShowRewarded;
    private RewardedAd mRewardedAd;

    // Banner Ad components
    private TextView tvBannerStatus;
    private FrameLayout flBannerContainer;
    private AdView mBannerAdView;

    // Native Ad components
    private TextView tvNativeStatus;
    private FrameLayout flNativeContainer;
    private NativeAd mNativeAd;

    // App Open Ad components
    private TextView tvAppOpenStatus;
    private Button btnShowAppOpen;
    private AppOpenAd mAppOpenAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DynamicColors.applyToActivityIfAvailable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Apply edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
        initializeAdSurgeSDK();
    }

    private void initViews() {
        tvSdkStatus = findViewById(R.id.tv_sdk_status);
        
        tvInterstitialStatus = findViewById(R.id.tv_interstitial_status);
        btnShowInterstitial = findViewById(R.id.btn_show_interstitial);
        
        tvRewardedStatus = findViewById(R.id.tv_rewarded_status);
        btnShowRewarded = findViewById(R.id.btn_show_rewarded);

        tvBannerStatus = findViewById(R.id.tv_banner_status);
        flBannerContainer = findViewById(R.id.fl_banner_container);

        tvNativeStatus = findViewById(R.id.tv_native_status);
        flNativeContainer = findViewById(R.id.fl_native_container);

        tvAppOpenStatus = findViewById(R.id.tv_app_open_status);
        btnShowAppOpen = findViewById(R.id.btn_show_app_open);
    }
    
    private void setupListeners() {
        btnShowInterstitial.setOnClickListener(v -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.showAd();
            }
        });
        
        btnShowRewarded.setOnClickListener(v -> {
            if (mRewardedAd != null) {
                mRewardedAd.showAd();
            }
        });

        btnShowAppOpen.setOnClickListener(v -> {
            if (mAppOpenAd != null && mAppOpenAd.isValid()) {
                mAppOpenAd.showAd();
            }
        });
    }

    private void initializeAdSurgeSDK() {
        tvSdkStatus.setText("Initializing SDK...");
        
        android.content.Context context = getApplicationContext();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            context = context.createAttributionContext("AdSurgeAd");
        }
        
        AdSurgeAdSdkInitConfig config = new AdSurgeAdSdkInitConfig.Builder()
                .setContext(context)
                .setAppId(APP_ID)
                .build();
        
        AdSurgeAdSdk.getInstance().init(config, new OnStartListener() {
            @Override
            public void onStartComplete() {
                Log.d(TAG, "SDK initialized successfully");
                runOnUiThread(() -> {
                    tvSdkStatus.setText("SDK Initialized Successfully");
                    loadInterstitialAd();
                    loadRewardedAd();
                    loadBannerAd();
                    loadNativeAd();
                    loadAppOpenAd();
                });
            }
            
            @Override
            public void onStartFailed(AdSurgeAdError error) {
                Log.e(TAG, "SDK initialization failed: " + error.getErrorMsg());
                runOnUiThread(() -> tvSdkStatus.setText("SDK Initialization Failed: " + error.getErrorMsg()));
            }
        });
    }

    private void loadInterstitialAd() {
        tvInterstitialStatus.setText("Loading...");
        btnShowInterstitial.setEnabled(false);
        
        mInterstitialAd = new InterstitialAd(this, INTERSTITIAL_ID);
        AdConfig config = new AdConfig.Builder().build();

        mInterstitialAd.loadAd(config, new InterstitialAdListener() {
            @Override
            public void onAdLoaded(AdSurgeAd ad) {
                Log.d(TAG, "Interstitial Ad Loaded");
                tvInterstitialStatus.setText("Ready to show");
                btnShowInterstitial.setEnabled(true);
            }

            @Override
            public void onAdFailed(AdSurgeAdError error) {
                Log.e(TAG, "Interstitial Ad Failed to Load: " + error.getErrorMsg());
                tvInterstitialStatus.setText("Load failed: " + error.getErrorMsg());
                mInterstitialAd = null;
            }
            
            @Override
            public void onAdShowFailed(AdSurgeAd ad, AdSurgeAdError error) {
                Log.e(TAG, "Interstitial Ad Failed to Show: " + error.getErrorMsg());
                tvInterstitialStatus.setText("Show failed");
            }

            @Override
            public void onAdImpression(AdSurgeAd ad) {
                Log.d(TAG, "Interstitial Ad Impression");
            }

            @Override
            public void onAdClicked(AdSurgeAd ad) {
                Log.d(TAG, "Interstitial Ad Clicked");
            }

            @Override
            public void onAdDismissed(AdSurgeAd ad) {
                Log.d(TAG, "Interstitial Ad Dismissed");
                tvInterstitialStatus.setText("Dismissed");
                btnShowInterstitial.setEnabled(false);
                mInterstitialAd = null;
            }
        });
    }
    
    private void loadRewardedAd() {
        tvRewardedStatus.setText("Loading...");
        btnShowRewarded.setEnabled(false);
        
        mRewardedAd = new RewardedAd(this, REWARDED_ID);
        AdConfig config = new AdConfig.Builder().build();
        
        mRewardedAd.loadAd(config, new RewardedAdListener() {
            @Override
            public void onAdLoaded(AdSurgeAd ad) {
                Log.d(TAG, "Rewarded Ad Loaded");
                tvRewardedStatus.setText("Ready to show");
                btnShowRewarded.setEnabled(true);
            }

            @Override
            public void onAdFailed(AdSurgeAdError error) {
                Log.e(TAG, "Rewarded Ad Failed to Load: " + error.getErrorMsg());
                tvRewardedStatus.setText("Load failed: " + error.getErrorMsg());
                mRewardedAd = null;
            }
            
            @Override
            public void onAdShowFailed(AdSurgeAd ad, AdSurgeAdError error) {
                Log.e(TAG, "Rewarded Ad Failed to Show: " + error.getErrorMsg());
                tvRewardedStatus.setText("Show failed");
            }

            @Override
            public void onAdImpression(AdSurgeAd ad) {
                Log.d(TAG, "Rewarded Ad Impression");
            }

            @Override
            public void onAdClicked(AdSurgeAd ad) {
                Log.d(TAG, "Rewarded Ad Clicked");
            }

            @Override
            public void onAdDismissed(AdSurgeAd ad) {
                Log.d(TAG, "Rewarded Ad Dismissed");
                tvRewardedStatus.setText("Dismissed");
                btnShowRewarded.setEnabled(false);
                mRewardedAd = null;
            }
            
            @Override
            public void onUserEarnedReward(AdSurgeAd ad, RewardItem rewardItem) {
                Log.d(TAG, "User Earned Reward");
                Toast.makeText(MainActivity.this, "Reward earned!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBannerAd() {
        tvBannerStatus.setText("Loading...");
        flBannerContainer.removeAllViews();
        
        mBannerAdView = new AdView(this, BANNER_ID);
        mBannerAdView.setListener(new BannerAdListener() {
            @Override
            public void onAdLoaded(AdSurgeAd ad) {
                Log.d(TAG, "Banner Ad Loaded");
                tvBannerStatus.setText("Ad loaded");
                
                if (mBannerAdView.getParent() == null) {
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    flBannerContainer.addView(mBannerAdView, layoutParams);
                }
            }

            @Override
            public void onAdFailed(AdSurgeAdError error) {
                Log.e(TAG, "Banner Ad Failed to Load: " + error.getErrorMsg());
                tvBannerStatus.setText("Load failed: " + error.getErrorMsg());
            }

            @Override
            public void onAdShowFailed(AdSurgeAd ad, AdSurgeAdError error) {
                Log.e(TAG, "Banner Ad Failed to Show: " + error.getErrorMsg());
            }

            @Override
            public void onAdImpression(AdSurgeAd ad) {
                Log.d(TAG, "Banner Ad Impression");
            }

            @Override
            public void onAdClicked(AdSurgeAd ad) {
                Log.d(TAG, "Banner Ad Clicked");
            }

            @Override
            public void onAdDismissed(AdSurgeAd ad) {
                Log.d(TAG, "Banner Ad Dismissed");
                flBannerContainer.removeAllViews();
                tvBannerStatus.setText("Dismissed");
            }
        });

        AdConfig config = new AdConfig.Builder()
                .adSize(AdSurgeAdSize.BANNER)
                .build();
        mBannerAdView.loadAd(config);
    }

    private void loadNativeAd() {
        tvNativeStatus.setText("Loading...");
        flNativeContainer.removeAllViews();

        mNativeAd = new NativeAd(this, NATIVE_ID);
        AdConfig config = new AdConfig.Builder().build();

        mNativeAd.loadAd(config, new NativeAdListener() {
            @Override
            public void onAdLoaded(AdSurgeAd ad) {
                Log.d(TAG, "Native Ad Loaded");
                tvNativeStatus.setText("Ad loaded");
                inflateNativeAd(mNativeAd, flNativeContainer);
            }

            @Override
            public void onAdFailed(AdSurgeAdError error) {
                Log.e(TAG, "Native Ad Failed to Load: " + error.getErrorMsg());
                tvNativeStatus.setText("Load failed: " + error.getErrorMsg());
                mNativeAd = null;
            }

            @Override
            public void onAdShowFailed(AdSurgeAd ad, AdSurgeAdError error) {
                Log.e(TAG, "Native Ad Failed to Show: " + error.getErrorMsg());
            }

            @Override
            public void onAdImpression(AdSurgeAd ad) {
                Log.d(TAG, "Native Ad Impression");
            }

            @Override
            public void onAdClicked(AdSurgeAd ad) {
                Log.d(TAG, "Native Ad Clicked");
            }

            @Override
            public void onAdDismissed(AdSurgeAd ad) {
                Log.d(TAG, "Native Ad Dismissed");
                flNativeContainer.removeAllViews();
                tvNativeStatus.setText("Dismissed");
                mNativeAd = null;
            }
        });
    }

    private void inflateNativeAd(NativeAd ad, FrameLayout container) {
        View adView = getLayoutInflater().inflate(R.layout.item_native_ad, container, false);

        NativeAdView mNativeAdView = adView.findViewById(R.id.native_ad_view);
        TextView mAdHeadline = adView.findViewById(R.id.tv_ad_title);
        TextView mAdBody = adView.findViewById(R.id.tv_ad_description);
        ImageView mAdIcon = adView.findViewById(R.id.iv_ad_icon);
        MediaView mAdMediaView = adView.findViewById(R.id.ad_media_view);
        Button mAdActionButton = adView.findViewById(R.id.btn_ad_action);
        FrameLayout mAdChoicesFrameLayout = adView.findViewById(R.id.fl_ad_choices);

        mAdHeadline.setText(ad.getHeadline());
        mAdBody.setText(ad.getBody());
        mAdActionButton.setText(ad.getCallToAction());
        
        com.adsurge.adn.ads.nativead.Image icon = ad.getIcon();
        if (icon != null) {
            Glide.with(this).asBitmap().load(icon.getUrl()).into(mAdIcon);
        }

        mNativeAdView.setMediaView(mAdMediaView);
        mNativeAdView.setHeadLineView(mAdHeadline);
        mNativeAdView.setBodyView(mAdBody);
        mNativeAdView.setIconView(mAdIcon);
        mNativeAdView.setCallToActionView(mAdActionButton);

        if (ad.getAdChoicesView() != null) {
            mAdChoicesFrameLayout.removeAllViews();
            mAdChoicesFrameLayout.addView(ad.getAdChoicesView());
        }

        List<View> clickViews = new ArrayList<>();
        clickViews.add(mAdHeadline);
        clickViews.add(mAdIcon);
        clickViews.add(mAdActionButton);
        ad.registerViewForInteraction(mNativeAdView, clickViews);

        container.removeAllViews();
        container.addView(adView);
    }

    private void loadAppOpenAd() {
        tvAppOpenStatus.setText("Loading...");
        btnShowAppOpen.setEnabled(false);

        mAppOpenAd = new AppOpenAd(this, APP_OPEN_ID);
        AdConfig config = new AdConfig.Builder().build();

        mAppOpenAd.loadAd(config, new AppOpenAdListener() {
            @Override
            public void onAdLoaded(AdSurgeAd ad) {
                Log.d(TAG, "App Open Ad Loaded");
                tvAppOpenStatus.setText("Ready to show");
                btnShowAppOpen.setEnabled(true);
            }

            @Override
            public void onAdFailed(AdSurgeAdError error) {
                Log.e(TAG, "App Open Ad Failed to Load: " + error.getErrorMsg());
                tvAppOpenStatus.setText("Load failed: " + error.getErrorMsg());
                mAppOpenAd = null;
            }

            @Override
            public void onAdShowFailed(AdSurgeAd ad, AdSurgeAdError error) {
                Log.e(TAG, "App Open Ad Failed to Show: " + error.getErrorMsg());
                tvAppOpenStatus.setText("Show failed");
            }

            @Override
            public void onAdImpression(AdSurgeAd ad) {
                Log.d(TAG, "App Open Ad Impression");
            }

            @Override
            public void onAdClicked(AdSurgeAd ad) {
                Log.d(TAG, "App Open Ad Clicked");
            }

            @Override
            public void onAdDismissed(AdSurgeAd ad) {
                Log.d(TAG, "App Open Ad Dismissed");
                tvAppOpenStatus.setText("Dismissed");
                btnShowAppOpen.setEnabled(false);
                mAppOpenAd = null;
            }
        });
    }
}
