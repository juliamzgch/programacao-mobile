package com.example.userdb.classes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.userdb.R;

import java.util.ArrayList;
import java.util.List;

public class UserListDialogFragment extends DialogFragment {
    // the fragment initialization parameter
    private static final String ARG_USERS = "users";
    private List<User> users;
    private UserListDialogFragmentListener listener;

    /**
     * use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static UserListDialogFragment newInstance(ArrayList<User> users) {
        UserListDialogFragment fragment = new UserListDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_USERS, users);
        fragment.setArguments(args);
        return fragment;
    }
    public interface UserListDialogFragmentListener {
        public void onUserSelected(User user);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            users = getArguments().getParcelableArrayList(ARG_USERS);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.user_list)
                .setAdapter(new UserListAdapter(getActivity(), users), (dialog, which) -> {
                    listener.onUserSelected(users.get(which));
                    this.dismiss();
                }).create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // vreify that the host activity implements the callback interface
        try {
            // instantiate the UserListDialogFragmntListener so we can send events to the host
            listener = (UserListDialogFragmentListener) context;
        } catch (ClassCastException e) {
            // the activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
            + " must implement UserListDialogFragmentListener");
        }
    }
}
