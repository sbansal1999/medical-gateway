package com.example.medicalgateway;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MedicalUtils {
    /**
     * Method that checks if the device is connected to internet.
     * @return true if online else false
     */
    public static boolean isOnline(@NotNull Context context, boolean displayToast) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isOnline = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (!isOnline && displayToast) {
            Toast.makeText(context, "Connect to Internet to continue", Toast.LENGTH_SHORT)
                 .show();
        }
        return isOnline;
    }

    /**
     * Retrieves text from the {@link android.widget.EditText} in the passed {@code textInputLayout}
     *
     * @param textInputLayout the view from which the text is to be retrieved
     * @return the retrieved text
     */
    @NotNull
    public static String getTextFromTextInputLayout(@NotNull TextInputLayout textInputLayout) {
        return textInputLayout.getEditText()
                              .getText()
                              .toString();

    }

    /**
     * Sets text in the  {@link android.widget.EditText} in the passed {@code textInputLayout}
     *
     * @param textInputLayout the view in which the text to be set
     * @param string          the text to be set
     */
    public static void setTextInTextInputLayout(@NotNull TextInputLayout textInputLayout, String string) {
        if (textInputLayout.getEditText() != null) {
            textInputLayout.getEditText()
                           .setText(string);
        }
    }

}
