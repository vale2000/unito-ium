package it.unito.ium.myreps.controllers;

import android.os.Bundle;

import it.unito.ium.myreps.R;

public final class LoginController extends BaseController {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
    }
}
