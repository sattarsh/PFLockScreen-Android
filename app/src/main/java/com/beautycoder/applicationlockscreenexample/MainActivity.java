package com.beautycoder.applicationlockscreenexample;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.security.PFResult;
import com.beautycoder.pflockscreen.security.PFSecurityManager;
import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;

public class MainActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showLockScreenFragment();
        changeStatusBarColor();
    }

    private final PFLockScreenFragment.OnPFLockScreenCodeCreateListener mCodeCreateListener =
            new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {
                @Override
                public void onCodeCreated(String encodedCode) {
                    Toast.makeText(MainActivity.this, "Code created", Toast.LENGTH_SHORT).show();
                    PreferencesSettings.saveToPref(MainActivity.this, encodedCode);
                    showMainFragment();
                }

                @Override
                public void onNewCodeValidationFailed() {
                    Toast.makeText(MainActivity.this, "Code validation error", Toast.LENGTH_SHORT).show();
                }
            };

    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {

                @Override
                public void onCodeInputSuccessful() {
                    Toast.makeText(MainActivity.this, "Code successfull", Toast.LENGTH_SHORT).show();
                    showMainFragment();
                }

                @Override
                public void onFingerprintSuccessful() {
                    Toast.makeText(MainActivity.this, "Fingerprint successfull", Toast.LENGTH_SHORT).show();
                    showMainFragment();
                }

                @Override
                public void onPinLoginFailed() {
                    Toast.makeText(MainActivity.this, "Pin failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFingerprintLoginFailed() {
                    Toast.makeText(MainActivity.this, "Fingerprint failed", Toast.LENGTH_SHORT).show();
                }
            };

    private void showLockScreenFragment() {
        new PFPinCodeViewModel().isPinCodeEncryptionKeyExist().observe(
                this,
                new Observer<PFResult<Boolean>>() {
                    @Override
                    public void onChanged(@Nullable PFResult<Boolean> result) {
                        if (result == null) {
                            return;
                        }
                        if (result.getError() != null) {
                            Toast.makeText(MainActivity.this, "Can not get pin code info", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        showLockScreenFragment(result.getResult());
                    }
                }
        );
    }

    private void showLockScreenFragment(boolean isPinExist) {
        final PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
                .setTitle(isPinExist ? "رمز عبور برنامه را وارد نمایید" : "برای محافظت از اطلاعات خود، رمز عبور دلخواه را وارد نمایید")
                .setCodeLength(4)
                .setLeftButton("رمز عبور خود را فراموش کرده‌اید؟")
                .setNewCodeValidation(true)
                .setNewCodeValidationTitle("لطفا رمز دلخواه عبور را مجددا وارد نمایید")
                .setUseFingerprint(true)
                .setAutoShowFingerprint(true);
        final PFLockScreenFragment fragment = new PFLockScreenFragment();

        fragment.setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Left button pressed", Toast.LENGTH_LONG).show();
            }
        });

        builder.setMode(isPinExist
                ? PFFLockScreenConfiguration.MODE_AUTH
                : PFFLockScreenConfiguration.MODE_CREATE);
        if (isPinExist) {
            fragment.setEncodedPinCode(PreferencesSettings.getCode(this));
            fragment.setLoginListener(mLoginListener);
        }

        fragment.setConfiguration(builder.build());
        fragment.setCodeCreateListener(mCodeCreateListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();

    }

    private void showMainFragment() {
        final MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.bg_pf));
        }
    }

}
