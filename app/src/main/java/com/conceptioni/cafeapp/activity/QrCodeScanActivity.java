package com.conceptioni.cafeapp.activity;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.TextviewRegular;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class QrCodeScanActivity extends AppCompatActivity {

    Barcode barcodeResult;
    public static final String BARCODE_KEY = "BARCODE";
    TextviewRegular scaninfotv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);
        init();
        QrCodeScanActivityPermissionsDispatcher.opencameraWithPermissionCheck(QrCodeScanActivity.this);
        if(savedInstanceState != null){
            Barcode restoredBarcode = savedInstanceState.getParcelable(BARCODE_KEY);
            if(restoredBarcode != null){
                scaninfotv.setText(restoredBarcode.rawValue);
                barcodeResult = restoredBarcode;
            }
        }
    }

    private void init() {
        scaninfotv = findViewById(R.id.scaninfotv);
    }

    private void startScan() {
        final MaterialBarcodeScanner materialBarcodeScanner;
        materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(QrCodeScanActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        barcodeResult = barcode;
                        scaninfotv.setText(barcode.rawValue);
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }
    @NeedsPermission(Manifest.permission.CAMERA)
    public void opencamera(){
        startScan();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BARCODE_KEY, barcodeResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QrCodeScanActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedcameradialogue() {
        new MakeToast(R.string.denied_camera);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskeddialogue() {
        new MakeToast(R.string.never_ask_camera);
    }
}
