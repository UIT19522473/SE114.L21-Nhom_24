package com.example.trelloreal.LoginAndSignUp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class LoginAndSignUpFragmentAdapter extends FragmentStateAdapter {
    public LoginAndSignUpFragmentAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new LoginTabFragment();
            default:
                return new SignUpTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
