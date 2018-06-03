package com.hfad.vocanoteapp.ui.vocaNoteActivityController;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.vocanoteapp.R;
import com.hfad.vocanoteapp.Utilities;
import com.hfad.vocanoteapp.viewModel.VocaNoteViewModel;

import net.gotev.speech.Speech;
import net.gotev.speech.TextToSpeechCallback;

import java.util.Locale;

/*This container fragment provides base base container
 * for the @link{VocaNoteDetailFragment}. The container
 * provides correct "flip-behaviour of fragments"
 */
public class VocaNoteContainerFragment extends Fragment {

    public static final String POSITION = "position";
    public static final String NAME_GROUP = "nameGroup";
    public static final String LANGUAGE = "LANGUAGE";
    private int position;
    private String nameGroup;
    private String language;

    public VocaNoteContainerFragment() {
    }

    public VocaNoteContainerFragment newInstance(int position, String nameGroup, String language) {
        VocaNoteContainerFragment mVocaNoteContainer = new VocaNoteContainerFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        args.putString(NAME_GROUP, nameGroup);
        args.putString(LANGUAGE, language);
        mVocaNoteContainer.setArguments(args);
        return mVocaNoteContainer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(POSITION);
        nameGroup = getArguments().getString(NAME_GROUP);
        language = getArguments().getString(LANGUAGE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_container, container, false);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container,
                        VocaNoteDetailFragment.newInstance(position, nameGroup, language, false))
                .commit();
        return rootView;
    }

    public interface VocaNoteFragmentListener {
        void onLeftArrowIconClick();

        void onRightArrowIconClick();

        void changeStudiedMark();
    }

    public static class VocaNoteDetailFragment extends Fragment {


        public static final String FLIP = "flip";
        private VocaNoteFragmentListener mListener;
        private int position;
        private String origWord;
        private String translation;
        private String nameGroup;
        private String language;
        private boolean flip;
        private VocaNoteViewModel innerVocaNoteViewModel;
        private TextView wordView;
        private Button btnStudied;
        private int studiedMark;

        public VocaNoteDetailFragment() {

        }

        static VocaNoteDetailFragment newInstance(int position, String nameGroup, String language, boolean flip) {
            VocaNoteDetailFragment mVocaNoteFragment = new VocaNoteDetailFragment();
            Bundle args = new Bundle();
            args.putInt(POSITION, position);
            args.putString(NAME_GROUP, nameGroup);
            args.putString(LANGUAGE, language);
            args.putBoolean(FLIP, flip);
            mVocaNoteFragment.setArguments(args);
            return mVocaNoteFragment;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try {
                mListener = (VocaNoteFragmentListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " mVocaNoteFragmentListener");
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            position = getArguments().getInt(POSITION);
            nameGroup = getArguments().getString(NAME_GROUP);
            language = getArguments().getString(LANGUAGE);
            flip = getArguments().getBoolean(FLIP);
            innerVocaNoteViewModel = ViewModelProviders.of(this).get(VocaNoteViewModel.class);
        }

        @Override
        public View onCreateView(@NonNull android.view.LayoutInflater inflater, android.view.ViewGroup container,
                                 Bundle savedInstanceState) {
            View detailView = inflater.inflate(R.layout.fragment_voca_note, container, false);
            wordView = detailView.findViewById(R.id.word);
            btnStudied = detailView.findViewById(R.id.button_studied);
            detailView.findViewById(R.id.rotateCard).setOnClickListener(v1 -> flipCard());
            onClickSoundButton(detailView);
            detailView.findViewById(R.id.skipLeft).setOnClickListener(v1 -> mListener.onLeftArrowIconClick());
            detailView.findViewById(R.id.skipRight).setOnClickListener(v1 -> mListener.onRightArrowIconClick());
            btnStudied.setOnClickListener(v1 -> mListener.changeStudiedMark());
            return detailView;
        }

        private void onClickSoundButton(final View detailView) {
            ImageView soundIcon = detailView.findViewById(R.id.word_sound_vc);
            soundIcon.setOnClickListener(v1 -> {
                TextToSpeechCallback mTextToSpeechCallback = new TextToSpeechCallback() {
                    @Override
                    public void onStart() {
                        soundIcon.setImageResource(R.drawable.volume_high_colored);
                    }

                    @Override
                    public void onCompleted() {
                        soundIcon.setImageResource(R.drawable.volume_high);
                    }

                    @Override
                    public void onError() {

                    }
                };
                if (flip) {
                    Speech.getInstance().setLocale(Locale.getDefault()).say(translation, mTextToSpeechCallback);
                } else {
                    Speech.getInstance().setLocale(Utilities.chooseLang(getContext(), language)).say(origWord, mTextToSpeechCallback);
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            innerVocaNoteViewModel.getByGroupVcVocaNote(nameGroup)
                    .observe(this, vocaNotes -> {
                        origWord = vocaNotes.get(position).getOriginWord();
                        translation = vocaNotes.get(position).getTranslation();
                        studiedMark = vocaNotes.get(position).getStudied();
                        if (flip) {
                            wordView.setText(translation);
                        } else {
                            wordView.setText(origWord);
                        }
                        if (studiedMark == 0) {
                            btnStudied.setText(R.string.transfer_into_studied);
                        } else {
                            btnStudied.setText(R.string.remove_from_studied);
                        }
                    });
        }

        private void flipCard() {
            if (flip) {
                flip = false;
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.card_flip_right_in,
                                R.animator.card_flip_right_out)
                        .replace(R.id.container, newInstance(this.position, nameGroup, language, flip))
                        .commit();
            } else {
                flip = true;
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_left_in,
                                R.animator.card_flip_left_out)
                        .replace(R.id.container, newInstance(this.position, nameGroup, language, flip))
                        .commit();
            }
        }
    }
}
