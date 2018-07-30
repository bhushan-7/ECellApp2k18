package nitrr.ecell.e_cell.fragments;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import nitrr.ecell.e_cell.R;
import nitrr.ecell.e_cell.bquiz.model.BQuizStatusResponse;
import nitrr.ecell.e_cell.model.AuthenticationResponse;
import nitrr.ecell.e_cell.restapi.ApiServices;
import nitrr.ecell.e_cell.restapi.AppClient;
import nitrr.ecell.e_cell.utils.AppConstants;
import nitrr.ecell.e_cell.utils.DialogFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BQuizFragment extends Fragment {

    private DialogInterface.OnClickListener clickListenerPositive = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            apiCallForBquizStatus();
        }
    };
    private DialogInterface.OnClickListener clickListenerNegative = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    public static BQuizFragment newInstance(String text) {
        BQuizFragment f = new BQuizFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bquiz_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        Typeface bebasNeue = Typeface.createFromAsset(getActivity().getAssets(), "fonts/BebasNeue.ttf");

        ImageView imageView = getView().findViewById(R.id.bqImageView);
        Glide.with(getActivity())
                .load(AppConstants.IMAGE_LOCATIONS[2])
                .into(imageView);


        TextView textView = getView().findViewById(R.id.bquiz_custom_view_text);
        textView.setText(AppConstants.HOME_TITLES[2]);
        textView.setTypeface(bebasNeue);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void apiCallForBquizStatus() {
        ApiServices apiServices = AppClient.getInstance().createService(ApiServices.class);
        Call<BQuizStatusResponse> call = apiServices.getBquizStatus();
        call.enqueue(new Callback<BQuizStatusResponse>() {
            @Override
            public void onResponse(Call<BQuizStatusResponse> call, Response<BQuizStatusResponse> response) {
                if (response.isSuccessful()) {
                    BQuizStatusResponse jsonResponse = response.body();
                    if (jsonResponse != null) {
                        if (jsonResponse.isActive()) {
                            
                        } else {
                            DialogFactory.showDialog(DialogFactory.BQUIZ_NOT_ACTIVE_ID, getContext(), clickListenerPositive, clickListenerNegative, null, getString(R.string.bquiz_dialog_title), getString(R.string.bquiz_dialog_msg), getString(R.string.bquiz_dialog_retry_btn), getString(R.string.bquiz_dialog_cancel_btn));
                        }
                    }
                }else {
                    Toast.makeText(getContext(),getString(R.string.something_went_wrong_msg),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BQuizStatusResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
