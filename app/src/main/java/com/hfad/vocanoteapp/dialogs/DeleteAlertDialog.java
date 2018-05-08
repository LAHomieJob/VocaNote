package com.hfad.vocanoteapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import com.hfad.vocanoteapp.R;


public class DeleteAlertDialog extends DialogFragment implements LifecycleObserver {

    public static final String MESSAGE = "Message";
    public static final String DISMISS_DELETE_DIALOG = "Dismiss Delete Dialog";
    DeleteAlertDialogListener mListener;

    public DeleteAlertDialog newInstance(String message) {
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        DeleteAlertDialog mDeleteAlertDialog = new DeleteAlertDialog();
        mDeleteAlertDialog.setArguments(args);
        return mDeleteAlertDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DeleteAlertDialogListener so we can send events to the host
            mListener = (DeleteAlertDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DeleteAlertDialogListener");
        }
    }

    @NonNull
    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setMessage(getArguments().getString(MESSAGE))
                .setPositiveButton(R.string.delete, (dialog, id) ->
                        mListener.onDeleteDialogPositiveClick(DeleteAlertDialog.this))
                .setNegativeButton(R.string.cancel, (dialog, id) ->
                        mListener.onDeleteDialogNegativeClick(DeleteAlertDialog.this));
        return builder.create();
    }

    public interface DeleteAlertDialogListener {
        void onDeleteDialogPositiveClick(DialogFragment dialog);

        default void onDeleteDialogNegativeClick(DialogFragment dialog) {
            Log.i(DISMISS_DELETE_DIALOG, "Pressed No Button");
            dialog.dismiss();
        }
    }
}
