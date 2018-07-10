package com.conceptioni.cafeapp.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.conceptioni.cafeapp.R;
import com.conceptioni.cafeapp.utils.Constant;
import com.conceptioni.cafeapp.utils.MakeToast;
import com.conceptioni.cafeapp.utils.SharedPrefs;
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
    String CafeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);
        init();
        QrCodeScanActivityPermissionsDispatcher.opencameraWithPermissionCheck(QrCodeScanActivity.this);
        if(savedInstanceState != null){
            Barcode restoredBarcode = savedInstanceState.getParcelable(BARCODE_KEY);
            if(restoredBarcode != null) {
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
                .withOnlyQRCodeScanning()
                .withResultListener(barcode -> {
                    barcodeResult = barcode;
                    CafeId = barcode.rawValue;
                    Log.d("+++++barcode","++++++"+CafeId);
                    SplitString(CafeId);
                    startActivity(new Intent(QrCodeScanActivity.this,CafeInfoActivity.class).putExtra("table_no",SharedPrefs.getSharedPref().getString(SharedPrefs.userSharedPrefData.table_number,Constant.notAvailable)));
                    finish();
//                    scaninfotv.setText(barcode.rawValue);
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    private void SplitString(String value){
        String[] separated = value.split("_");
        Log.d("+++++","++++"+separated[0]);
        Log.d("+++++","++++"+separated[1]);

        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.Cafe_Id, separated[0]).apply();
        SharedPrefs.getSharedPref().edit().putString(SharedPrefs.userSharedPrefData.table_number, separated[1]).apply();
//        separated[0]; // this will contain "Fruit"
//        separated[1]; // this will contain " they taste good"
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void goBack() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                QrCodeScanActivity.this);

        alertDialog.setTitle("");
        alertDialog.setMessage("Are you sure you want to exit?");

        alertDialog.setPositiveButton("YES",
                (dialog, which) -> {
                    finish();
                    System.exit(0);
                });

        alertDialog.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }
}
