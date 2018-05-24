package com.hfad.vocanoteapp.ui.groupsActivityController;


import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.hfad.vocanoteapp.R;

public class EditNameGroupAlertDialog extends DialogFragment implements LifecycleObserver {
    private static final String TAG = "CANCEL";
    EditNameListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EditNameListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditNameGroupAlertDialogListener");
        }
    }

    @NonNull
    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_edit_group, null);
        builder.setView(dialoglayout);
        builder.setMessage(R.string.change_name_of_group)
                .setPositiveButton(R.string.change, (dialog, id) ->
                        mListener.onEditDialogPositiveClick(EditNameGroupAlertDialog.this)
                )
                .setNegativeButton(R.string.cancel, (dialog, id) ->
                        mListener.onEditDialogNegativeClick(EditNameGroupAlertDialog.this));
        return builder.create();
    }

    public interface EditNameListener {
        void onEditDialogPositiveClick(DialogFragment dialog);

        default void onEditDialogNegativeClick(DialogFragment dialog) {
            Log.i(TAG, "Cancel button in EditName dialog.");
            dialog.dismiss();
        }
    }

}
